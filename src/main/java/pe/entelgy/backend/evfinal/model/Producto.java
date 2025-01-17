package pe.entelgy.backend.evfinal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pe.entelgy.backend.evfinal.enums.Categoria;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private BigDecimal precio;

    @Lob
    @NotEmpty(message = "La descripción no puede estar vacía.")
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres.")
    private String descripcion;

    @Column(nullable = false)
    @Min(value = 0, message = "El stock no puede ser negativo.")
    private Integer stock;
}