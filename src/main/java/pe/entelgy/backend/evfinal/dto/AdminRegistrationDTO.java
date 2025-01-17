package pe.entelgy.backend.evfinal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminRegistrationDTO {
    @NotBlank
    @Size(max = 50)
    private String nombres;

    @NotBlank
    @Size(max = 50)
    private String apellidoPaterno;

    @NotBlank
    @Size(max = 50)
    private String apellidoMaterno;

    @NotBlank
    @Size(min = 4, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;
}
