# Adicionando a lib de jwt
JSON Web Token, ou JWT, é um padrão utilizado para a geração de tokens, que nada mais são do que Strings, representando, de maneira segura, informações que serão compartilhadas entre dois sistemas.

Nosso foco agora é fazer o retorno do token no Insomnia.

Então para fazer isso precisamos fazer uma mudança na classe `AutenticacaoController`, que é essa classe:

``` Java
package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.usuario.DadosAtutenticacao;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;
    
    @PostMapping
    @Transactional
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAtutenticacao dados) {
        var token = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var autentication = manager.authenticate(token);

        return ResponseEntity.ok().build();
    }
}
```

Então vamos tirar o `.build` do retorno e passar o token nos parênteses do `.ok()`, mas para passarmos o token precisamos adicionar uma biblioteca que lida com token.

Para isso entramos no site: http://jwt.io/, vamos em -> "Libraries", selecionamos Java na *combo box* e escolhemos qual das bibliotecas vamos usar.

Neste caso aqui usaremos a `auth0`. Então clicamos no `View Repo`, onde vamos para o repositório da biblioteca. Lá explica como adicionar a biblioteca, onde possui uma dependencia maven que podemos adicionar, neste caso usarei a seguinte dependência:

``` xml
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>4.2.1</version>
</dependency>
```

Agora vamos criar a classe responsável pela geração dos tokens:

``` Java
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Voll.med")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerar token jwt", exception);
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
```

No método `gerarToken()` estamos pegando direto do Github e adicionamos `withSubject(usuario.getLogin())`, que é para sabermos quem se logou pelo token e o método `withExpiresAt(dataExpiracao())`, que é um método para especificar quando irá expirar o token, e estamos passando um outro método, o `dataExpiracao()`, que contém a hora em que o token é gerado e adiciona +2 horas para ele expirar.

Também temos uma variável algoritmo, em que possui o tipo do algoritmo que será usado, que no caso é o `HMAC256` e a senha, ela será a `secret`, que não estamos deixando a senha visível no código.

Para saber qual a senha, colocamos ela no arquivo `application.properties`:

``` properties
api.security.token.secret=${JWT_SECRET:12345678}
```

Em produção usamos uma variável de ambiente que colocamos o nome `JWT_SECRET` porém se não for encontrada usamos `:` e a senha, pois no nosso computador não terá a variável de ambiente, mas temos que pensar que em produção seria somente `JWT_SECRET`.

Na classe `TokenService` precisamos informar para a nossa API onde olhar a senha, e isso é feito pela anotação:

``` Java
@Value("${api.security.token.secret}")
private String secret;
```

Por fim é necessário criar o DTO `DadosTokenJWT` e alterar a classe `AutenticacaoController`:

``` Java
public record DadosTokenJWT(String token) {}
```

Agora alterando a classe `AutenticacaoController`:

``` Java
@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
```

Onde alteramos o método `efetuarLogin()`.

adicionamos a variável `var TokenJWT` e o atributo privado `tokenService` do tipo `TokenService`, para podermos usar o método `gerarToken`.

``` Java
@Autowired
private TokenService tokenService;

/*código omitido*/

var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
```

Então pegamos o usuário pela variável `authentication` e passamos para a `gerarToken()` e retornamos pelo DTO `DadosTokenJWT`.