package med.voll.api.domain.consulta;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.medico.DadosMedicoConsulta;
import med.voll.api.domain.paciente.DadosPacienteConsulta;

public record DadosCadastroConsulta(    
    DadosPacienteConsulta paciente, 
    DadosMedicoConsulta medico, 

    @NotNull
    LocalDate dataEHora) {
}
