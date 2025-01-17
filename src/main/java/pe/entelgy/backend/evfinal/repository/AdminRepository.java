package pe.entelgy.backend.evfinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.entelgy.backend.evfinal.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}