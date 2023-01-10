package med.voll.api.domain.paciente;

public record DadosPacienteConsulta(Long id, String nome) {
    
        public DadosPacienteConsulta(Paciente paciente) {
            this(paciente.getId(), paciente.getNome());
        }
}