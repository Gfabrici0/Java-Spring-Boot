package med.voll.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.consulta.ConsultasRepository;
import med.voll.api.domain.consulta.DadosCadastroConsulta;

@RestController
@RequestMapping("consultas")
public class ConsultasController {

    private ConsultasRepository repository;

    /* @PostMapping
    @Transactional
    public void agendarConsulta(@RequestBody @Valid DadosCadastroConsulta dados) {
        repository.save(new Consulta(dados));
    } */
}
