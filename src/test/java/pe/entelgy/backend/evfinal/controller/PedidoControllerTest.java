package pe.entelgy.backend.evfinal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pe.entelgy.backend.evfinal.dto.PedidoDTO;
import pe.entelgy.backend.evfinal.dto.ProductoDetalleDTO;
import pe.entelgy.backend.evfinal.enums.EstadoPedido;
import pe.entelgy.backend.evfinal.enums.Role;
import pe.entelgy.backend.evfinal.model.Cliente;
import pe.entelgy.backend.evfinal.model.Pedido;
import pe.entelgy.backend.evfinal.model.Producto;
import pe.entelgy.backend.evfinal.model.User;
import pe.entelgy.backend.evfinal.service.PedidoService;
import pe.entelgy.backend.evfinal.service.ProductoDetalleService;
import pe.entelgy.backend.evfinal.service.ProductoService;
import pe.entelgy.backend.evfinal.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PedidoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;
    @MockBean
    private ProductoService productoService;
    @MockBean
    private UserService userService;
    @MockBean
    private ProductoDetalleService productoDService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testGetPedidosAsAdmin() throws Exception {
        Pedido pedido1 = new Pedido();
        pedido1.setId(1L);
        pedido1.setEstado(EstadoPedido.PENDIENTE);

        Pedido pedido2 = new Pedido();
        pedido2.setId(2L);
        pedido2.setEstado(EstadoPedido.PENDIENTE);

        List<Pedido> pedidos = Arrays.asList(pedido1, pedido2);

        when(pedidoService.getAllPedidos()).thenReturn(pedidos);

        mockMvc.perform(get("/api/pedido")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @WithMockUser(username = "cliente", roles = "CLIENT")
    public void testCreatePedido() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombres("Juan Pérez");

        User usuario = new User();
        usuario.setUsername("cliente");
        usuario.setPassword("password");
        usuario.setRole(Role.CLIENT);
        usuario.setCliente(cliente);

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setEstado(EstadoPedido.PENDIENTE);
        ProductoDetalleDTO producto = new ProductoDetalleDTO();
        producto.setProducto(1L);
        producto.setCantidad(2);
        pedidoDTO.setProductos(Arrays.asList(producto));

        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setCliente(cliente);

        when(userService.getUser("cliente")).thenReturn(Optional.of(usuario));
        when(pedidoService.createPedido(any(Pedido.class), any(PedidoDTO.class))).thenReturn(pedido);

        mockMvc.perform(post("/api/pedido")
                        .contentType("application/json")
                        .content("{\"productos\": [{\"producto\": 1, \"cantidad\": 2}], \"estado\": \"PENDIENTE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cliente.id").value(cliente.getId()));
    }


}
