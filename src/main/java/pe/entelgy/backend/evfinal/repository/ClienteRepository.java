package pe.entelgy.backend.evfinal.repository;

import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.entelgy.backend.evfinal.model.Cliente;

import java.util.List;

public interface ClienteRepository extends JpaRepository <Cliente, Long> {
    boolean existsByDni(@Pattern(regexp = "\\d{8}") String dni);
    boolean existsByTelefono(@Pattern(regexp = "\\d{9}") String telefono);
}
