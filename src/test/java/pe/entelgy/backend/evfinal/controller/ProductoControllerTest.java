package pe.entelgy.backend.evfinal.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import pe.entelgy.backend.evfinal.enums.Categoria;
import pe.entelgy.backend.evfinal.model.Producto;
import pe.entelgy.backend.evfinal.service.ProductoService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    public void setUp() {
        producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Laptop");
        producto1.setCategoria(Categoria.LAPTOPS);
        producto1.setPrecio(new BigDecimal("1000.00"));
        producto1.setDescripcion("Una laptop potente");
        producto1.setStock(10);

        producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Smartphone");
        producto2.setCategoria(Categoria.SMARTPHONES);
        producto2.setPrecio(new BigDecimal("500.00"));
        producto2.setDescripcion("Un smartphone rápido");
        producto2.setStock(15);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testGetProductos() throws Exception {
        when(productoService.getAllProductos()).thenReturn(Arrays.asList(producto1, producto2));

        mockMvc.perform(get("/api/producto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testGetProductosNoContent() throws Exception {
        when(productoService.getAllProductos()).thenReturn(List.of());

        mockMvc.perform(get("/api/producto"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testGetProductoById() throws Exception {
        when(productoService.getProductoById(1L)).thenReturn(Optional.of(producto1));

        mockMvc.perform(get("/api/producto/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testGetProductoByIdNotFound() throws Exception {
        when(productoService.getProductoById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/producto/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateProducto() throws Exception {
        when(productoService.saveProduct(any(Producto.class))).thenReturn(producto1);

        mockMvc.perform(post("/api/producto")
                        .contentType("application/json")
                        .content("{ \"nombre\": \"Laptop\", \"categoria\": \"LAPTOPS\", \"precio\": 1000.00, \"descripcion\": \"Una laptop potente\", \"stock\": 10 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUpdateProducto() throws Exception {
        when(productoService.getProductoById(1L)).thenReturn(Optional.of(producto1));
        when(productoService.saveProduct(any(Producto.class))).thenReturn(producto1);

        mockMvc.perform(patch("/api/producto/{id}", 1L)
                        .contentType("application/json")
                        .content("{ \"nombre\": \"Laptop Actualizada\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop Actualizada"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUpdateProductoNotFound() throws Exception {
        when(productoService.getProductoById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/producto/{id}", 1L)
                        .contentType("application/json")
                        .content("{ \"nombre\": \"Laptop Actualizada\" }"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateProductoDescripcionVacia() throws Exception {
        mockMvc.perform(post("/api/producto")
                        .contentType("application/json")
                        .content("{ \"nombre\": \"Laptop\", \"categoria\": \"LAPTOPS\", \"precio\": 1000.00, \"descripcion\": \"\", \"stock\": 10 }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateProductoDescripcionExcedida() throws Exception {
        mockMvc.perform(post("/api/producto")
                        .contentType("application/json")
                        .content("{ \"nombre\": \"Laptop\", \"categoria\": \"LAPTOPS\", \"precio\": 1000.00, \"descripcion\": \"" + "A".repeat(505) + "\", \"stock\": 10 }"))
                .andExpect(status().isBadRequest());
    }

}
