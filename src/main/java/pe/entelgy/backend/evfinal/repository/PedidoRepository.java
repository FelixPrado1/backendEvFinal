package pe.entelgy.backend.evfinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.entelgy.backend.evfinal.model.Pedido;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);
}
