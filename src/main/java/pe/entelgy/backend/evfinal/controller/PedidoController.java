package pe.entelgy.backend.evfinal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pe.entelgy.backend.evfinal.dto.PedidoDTO;
import pe.entelgy.backend.evfinal.dto.PedidoNewDTO;
import pe.entelgy.backend.evfinal.dto.ProductoDetalleDTO;
import pe.entelgy.backend.evfinal.enums.EstadoPedido;
import pe.entelgy.backend.evfinal.model.Pedido;
import pe.entelgy.backend.evfinal.model.Producto;
import pe.entelgy.backend.evfinal.model.ProductoDetalle;
import pe.entelgy.backend.evfinal.repository.ProductoDetalleRepository;
import pe.entelgy.backend.evfinal.service.PedidoService;
import pe.entelgy.backend.evfinal.service.ProductoDetalleService;
import pe.entelgy.backend.evfinal.service.ProductoService;
import pe.entelgy.backend.evfinal.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final UserService userService;
    private final ProductoDetalleService productoDService;

    public PedidoController(PedidoService pedidoService, ProductoService productoService, UserService userService, ProductoDetalleService productoDService){
        this.pedidoService = pedidoService;
        this.productoService = productoService;
        this.userService = userService;
        this.productoDService = productoDService;
    }

    private String getLoggedUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    private String getLoggedUserRole() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return role;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> getPedidos(){
        List<Pedido> pedidos;
        if(getLoggedUserRole() == "ROLE_CLIENT"){
            pedidos = pedidoService.getPedidosByClienteId(userService.getUser(getLoggedUsername()).get().getCliente().getId());
        } else {
            pedidos = pedidoService.getAllPedidos();
        }
        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Long id){
        Optional<Pedido> pedido = pedidoService.getPedidoById(id);
        if (pedido.isPresent()){
            if(getLoggedUserRole() == "ROLE_CLIENT"){
                if (!userService.getUser(getLoggedUsername()).get().getCliente().getId().equals(pedido.get().getCliente().getId())) {
                    throw new AccessDeniedException("No tienes permiso para obtener datos de este pedido.");
                }
            }
            return ResponseEntity.ok(pedido.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Pedido createPedido(@RequestBody PedidoDTO pedidoDTO){
        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setCliente(userService.getUser(getLoggedUsername()).get().getCliente());
        return pedidoService.createPedido(pedido, pedidoDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable Long id, @RequestBody PedidoDTO pedidoDetails){
        Optional<Pedido> pedido = pedidoService.getPedidoById(id);
        if (pedido.isPresent()){
            Pedido pedidoUpdated = pedido.get();
            if (getLoggedUserRole().equals("ROLE_CLIENT")) {
                if (!userService.getUser(getLoggedUsername()).get().getCliente().getId().equals(pedidoUpdated.getCliente().getId())) {
                    throw new AccessDeniedException("No tienes permiso para actualizar los datos de este pedido.");
                }
            }

            Pedido pedidoFinalizado = pedidoService.updatePedido(pedidoUpdated, pedidoDetails);

            return ResponseEntity.ok(pedidoFinalizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePedido(@PathVariable Long id){
        Optional<Pedido> pedido = pedidoService.getPedidoById(id);
        if (pedido.isPresent()){
            try {
                if(getLoggedUserRole() == "ROLE_CLIENT"){
                    if (!userService.getUser(getLoggedUsername()).get().getCliente().getId().equals(pedido.get().getCliente().getId())) {
                        throw new AccessDeniedException("No tienes permiso para eliminar este pedido.");
                    }
                }
                pedidoService.deletePedido(id);
                return ResponseEntity.ok("Pedido eliminado con éxito.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al eliminar el pedido: " + e.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
