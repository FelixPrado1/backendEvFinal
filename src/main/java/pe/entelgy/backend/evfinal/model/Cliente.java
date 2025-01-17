package pe.entelgy.backend.evfinal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(max = 50)
    private String nombres;

    @Column(nullable = false)
    @Size(max = 50)
    private String apellidoPaterno;

    @Column(nullable = false)
    @Size(max = 50)
    private String apellidoMaterno;

    @NotNull(message = "El DNI no puede estar vacío.")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos.")
    private String dni;

    @NotNull(message = "El teléfono no puede estar vacío.")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos.")
    private String telefono;
}
