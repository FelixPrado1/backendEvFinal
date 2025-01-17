package pe.entelgy.backend.evfinal.dto;

import lombok.Getter;
import lombok.Setter;
import pe.entelgy.backend.evfinal.enums.EstadoPedido;
import pe.entelgy.backend.evfinal.model.Producto;

import java.util.List;

@Getter
@Setter
public class PedidoDTO {
    private List<ProductoDetalleDTO> productos;
    private EstadoPedido estado;
}