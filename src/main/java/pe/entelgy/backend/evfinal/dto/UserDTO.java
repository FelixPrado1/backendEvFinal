package pe.entelgy.backend.evfinal.dto;

import lombok.Data;
import pe.entelgy.backend.evfinal.enums.Role;

@Data
public class UserDTO {
    private Integer id;
    private String username;
    private Role role;  // Enum Role
    private ClienteDTO cliente;
    private AdminDTO admin;
}
