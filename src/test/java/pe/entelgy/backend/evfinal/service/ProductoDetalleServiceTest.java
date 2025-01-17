package pe.entelgy.backend.evfinal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import pe.entelgy.backend.evfinal.model.ProductoDetalle;
import pe.entelgy.backend.evfinal.repository.ProductoDetalleRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductoDetalleServiceTest {

    @MockBean
    private ProductoDetalleRepository productoDetalleRepository;

    @Autowired
    private ProductoDetalleService productoDetalleService;

    @Test
    public void testGetAllProductoDetalles() {
        ProductoDetalle productoDetalle1 = new ProductoDetalle();
        ProductoDetalle productoDetalle2 = new ProductoDetalle();
        when(productoDetalleRepository.findAll()).thenReturn(Arrays.asList(productoDetalle1, productoDetalle2));

        List<ProductoDetalle> result = productoDetalleService.getAllProductoDetalles();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productoDetalleRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductoDetalleById() {
        Long id = 1L;
        ProductoDetalle productoDetalle = new ProductoDetalle();
        when(productoDetalleRepository.findById(id)).thenReturn(Optional.of(productoDetalle));

        Optional<ProductoDetalle> result = productoDetalleService.getProductoDetalleById(id);

        assertNotNull(result);
        assertEquals(productoDetalle, result.get());
        verify(productoDetalleRepository, times(1)).findById(id);
    }

    @Test
    public void testGetProductoDetalleByIdNotFound() {
        Long id = 1L;
        when(productoDetalleRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ProductoDetalle> result = productoDetalleService.getProductoDetalleById(id);

        assertNotNull(result);
        assertEquals(Optional.empty(), result);
        verify(productoDetalleRepository, times(1)).findById(id);
    }

    @Test
    public void testSaveProduct() {
        ProductoDetalle productoDetalle = new ProductoDetalle();
        when(productoDetalleRepository.save(any(ProductoDetalle.class))).thenReturn(productoDetalle);

        ProductoDetalle result = productoDetalleService.saveProduct(productoDetalle);

        assertNotNull(result);
        verify(productoDetalleRepository, times(1)).save(productoDetalle);
    }

    @Test
    public void testDeleteProduct() {
        Long id = 1L;

        doNothing().when(productoDetalleRepository).deleteById(id);

        assertDoesNotThrow(() -> productoDetalleService.deleteProduct(id));

        verify(productoDetalleRepository, times(1)).deleteById(id);
    }
}