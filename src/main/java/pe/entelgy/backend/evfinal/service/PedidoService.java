package pe.entelgy.backend.evfinal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.entelgy.backend.evfinal.dto.PedidoDTO;
import pe.entelgy.backend.evfinal.dto.ProductoDetalleDTO;
import pe.entelgy.backend.evfinal.model.Pedido;
import pe.entelgy.backend.evfinal.model.Producto;
import pe.entelgy.backend.evfinal.model.ProductoDetalle;
import pe.entelgy.backend.evfinal.repository.PedidoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProductoService productoService;
    private final ProductoDetalleService productoDService;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, ProductoService productoService, ProductoDetalleService productoDService){
        this.pedidoRepository = pedidoRepository;
        this.productoService = productoService;
        this.productoDService = productoDService;
    }

    private BigDecimal calculateEnvio(List<ProductoDetalle> productos) {
        int cantidadProductos = productos.size();

        if (cantidadProductos <= 10) {
            return BigDecimal.valueOf(15);
        } else {
            return BigDecimal.ZERO;
        }
    }

    @Transactional(readOnly = true)
    public List<Pedido> getPedidosByClienteId(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public List<Pedido> getAllPedidos(){
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> getPedidoById(Long id){
        return pedidoRepository.findById(id);
    }

    @Transactional
    public Pedido createPedido(Pedido pedidoUpdated, PedidoDTO pedidoDetails) {
        if (pedidoDetails.getProductos() != null) {
            pedidoUpdated.getProductos().clear();
            for (ProductoDetalleDTO productoId : pedidoDetails.getProductos()) {
                Optional<Producto> productoOptional = productoService.getProductoById(productoId.getProducto());
                if (productoOptional.isPresent()) {
                    Producto producto = productoOptional.get();
                    if (producto.getStock() - productoId.getCantidad() >= 0) {
                        for (int i = 0; i < productoId.getCantidad(); i++) {
                            ProductoDetalle productoDetalle = new ProductoDetalle();
                            productoDetalle.setProducto(producto);
                            productoDService.saveProduct(productoDetalle);
                            pedidoUpdated.addProducto(productoDetalle);
                            producto.setStock(producto.getStock() - 1);
                        }
                        productoService.saveProduct(producto);
                    } else {
                        throw new IllegalArgumentException("Error: No hay suficiente stock.");
                    }
                } else {
                    throw new IllegalArgumentException("Error: Producto inexistente.");
                }
            }
        }

        if (pedidoUpdated.getProductos().size() < 3) {
            throw new IllegalArgumentException("El pedido debe tener al menos 3 productos.");
        }

        BigDecimal envio = calculateEnvio(pedidoUpdated.getProductos());
        pedidoUpdated.setEnvio(envio);
        BigDecimal costoTotal = pedidoUpdated.getProductos().stream()
                .map(productoDetalle -> productoDetalle.getProducto().getPrecio())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(envio);
        pedidoUpdated.setCostoTotal(costoTotal);

        return pedidoRepository.save(pedidoUpdated);
    }

    @Transactional
    public Pedido updatePedido(Pedido pedidoUpdated, PedidoDTO pedidoDetails) {
        if (pedidoDetails.getProductos() != null) {
            pedidoUpdated.getProductos().clear();
            for (ProductoDetalleDTO productoId : pedidoDetails.getProductos()) {
                Optional<Producto> productoOptional = productoService.getProductoById(productoId.getProducto());
                if (productoOptional.isPresent()) {
                    Producto producto = productoOptional.get();
                    if (producto.getStock() - productoId.getCantidad() >= 0) {
                        for (int i = 0; i < productoId.getCantidad(); i++) {
                            ProductoDetalle productoDetalle = new ProductoDetalle();
                            productoDetalle.setProducto(producto);
                            productoDService.saveProduct(productoDetalle);
                            pedidoUpdated.addProducto(productoDetalle);
                            producto.setStock(producto.getStock() - 1);
                        }
                        productoService.saveProduct(producto);
                    } else {
                        throw new IllegalArgumentException("Error: No hay suficiente stock.");
                    }
                } else {
                    throw new IllegalArgumentException("Error: Producto inexistente.");
                }
            }
        }

        if (pedidoDetails.getEstado() != null) {
            pedidoUpdated.setEstado(pedidoDetails.getEstado());
        }

        if (pedidoUpdated.getProductos().size() < 3) {
            throw new IllegalArgumentException("El pedido debe tener al menos 3 productos.");
        }

        BigDecimal envio = calculateEnvio(pedidoUpdated.getProductos());
        pedidoUpdated.setEnvio(envio);
        BigDecimal costoTotal = pedidoUpdated.getProductos().stream()
                .map(productoDetalle -> productoDetalle.getProducto().getPrecio())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(envio);
        pedidoUpdated.setCostoTotal(costoTotal);

        return pedidoRepository.save(pedidoUpdated);
    }

    public void deletePedido(Long id){
        pedidoRepository.deleteById(id);
    }

}
