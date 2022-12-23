package med.voll.api.model.paciente;

import jakarta.validation.constraints.NotNull;
import med.voll.api.model.endereco.DadosEndereco;

public record DadosAtualizacaoPaciente(
    
    @NotNull
    Long id, 
    String nome, 
    String telefone, 
    DadosEndereco endereco) {

}
