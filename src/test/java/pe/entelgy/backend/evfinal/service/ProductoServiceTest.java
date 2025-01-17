package pe.entelgy.backend.evfinal.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pe.entelgy.backend.evfinal.model.Producto;
import pe.entelgy.backend.evfinal.repository.ProductoRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductoServiceTest {

    @MockBean
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoService productoService;

    @Test
    public void testGetAllProductos() {
        Producto producto1 = new Producto();
        producto1.setId(1L);
        Producto producto2 = new Producto();
        producto2.setId(2L);
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto1, producto2));

        List<Producto> result = productoService.getAllProductos();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetProductoByIdSuccess() {
        Long id = 1L;
        Producto producto = new Producto();
        producto.setId(id);
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        Optional<Producto> result = productoService.getProductoById(id);

        assertNotNull(result);
        assertEquals(id, result.get().getId());
    }

    @Test
    public void testGetProductoByIdNotFound() {
        Long id = 1L;
        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Producto> result = productoService.getProductoById(id);

        assertNotNull(result);
        assertThrows(NoSuchElementException.class, () -> result.get());
    }

    @Test
    public void testSaveProduct() {
        Producto producto = new Producto();
        producto.setId(1L);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.saveProduct(producto);

        assertNotNull(result);
        assertEquals(producto.getId(), result.getId());
    }

    @Test
    public void testDeleteProduct() {

        Long id = 1L;
        doNothing().when(productoRepository).deleteById(id);

        assertDoesNotThrow(() -> productoService.deleteProduct(id));
        verify(productoRepository, times(1)).deleteById(id);
    }
}