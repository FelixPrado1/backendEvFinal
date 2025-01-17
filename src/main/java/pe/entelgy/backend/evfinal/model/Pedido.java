package pe.entelgy.backend.evfinal.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pe.entelgy.backend.evfinal.enums.EstadoPedido;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "pedido_id")
    private List<ProductoDetalle> productos = new ArrayList<>();

    @Column(nullable = false)
    @NotNull(message = "El costo de envío no puede ser nulo.")
    @Min(value = 0, message = "El costo de envío no puede ser negativo.")
    private BigDecimal envio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    @Column(nullable = false)
    private BigDecimal costoTotal;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    public void addProducto(ProductoDetalle producto) {
        if (producto != null) {
            this.productos.add(producto);
        }
    }
}