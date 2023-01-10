package med.voll.api.domain.consulta;

import java.security.Timestamp;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "consultas")
@Table(name = "consultas")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private String medicoNome;
    private LocalDate dataEHora;
    private Long medicoId;
    SimpleDateFormat formatadorHora = new SimpleDateFormat("HH:mm");
    SimpleDateFormat formatadorData = new SimpleDateFormat("dd/mm/yy");

    /* public Consulta(DadosCadastroConsulta dados) {
        this.pacienteNome = dados.paciente().nome();
        this.pacienteId = dados.paciente().id();
        this.medicoNome = dados.medico().nome();
        this.medicoId = dados.medico().id();
        if(validarInformacoes(dados)) {
            this.dataEHora = dados.dataEHora();        
        }
    } */

    /* public boolean validarInformacoes(DadosCadastroConsulta dados) {
        LocalDate HoraDeInicioDeFuncionamento = after();
        LocalDate dataDaConsulta = formatadorData.parse();
       
        
        if(true) {
            return true;
        }
        return false;
    } */
    
}
