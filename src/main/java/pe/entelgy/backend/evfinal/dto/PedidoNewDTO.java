package pe.entelgy.backend.evfinal.dto;

import lombok.Data;
import pe.entelgy.backend.evfinal.enums.EstadoPedido;
import pe.entelgy.backend.evfinal.model.Cliente;

import java.util.List;

@Data
public class PedidoNewDTO {
    private List<ProductoDetalleDTO> productos;

    private Cliente cliente;

    private EstadoPedido estado;

}
