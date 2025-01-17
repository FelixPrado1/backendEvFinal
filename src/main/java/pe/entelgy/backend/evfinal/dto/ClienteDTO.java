package pe.entelgy.backend.evfinal.dto;

import lombok.Data;

@Data
public class ClienteDTO {
    private Long id;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String telefono;
    private String dni;
}
