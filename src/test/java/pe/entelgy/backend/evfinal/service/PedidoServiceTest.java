package pe.entelgy.backend.evfinal.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pe.entelgy.backend.evfinal.repository.PedidoRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PedidoServiceTest {

    @MockBean
    private PedidoRepository pedidoRepository;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private PedidoService pedidoService;

    @Test
    public void testDeletePedido() {
        Long pedidoId = 1L;
        pedidoService.deletePedido(pedidoId);

        verify(pedidoRepository, times(1)).deleteById(pedidoId);
    }
}