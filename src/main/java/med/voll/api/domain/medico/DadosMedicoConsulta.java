package med.voll.api.domain.medico;

public record DadosMedicoConsulta(Long id, String nome) {
    
    public DadosMedicoConsulta(Medico medico) {
        this(medico.getId(), medico.getNome());
    }
}