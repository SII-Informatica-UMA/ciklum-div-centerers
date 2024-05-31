package sii.microservicio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sii.microservicio.entities.Sesion;

import java.util.List;

public interface SesionRepo extends JpaRepository<Sesion, Long>{
    List<Sesion> findByIdPlan(Long idPlan);
}
