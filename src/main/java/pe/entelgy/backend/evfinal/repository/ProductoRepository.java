package pe.entelgy.backend.evfinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.entelgy.backend.evfinal.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
