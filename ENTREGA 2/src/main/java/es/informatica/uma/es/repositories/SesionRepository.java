package es.informatica.uma.es.repositories;

import es.informatica.uma.es.entities.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SesionRepository extends JpaRepository<Sesion,Long>{
    Sesion findById(Integer id);
    List<Sesion> findByIdPlan(Integer id);
    List<Sesion> findByIdCliente(Integer id);
    List<Sesion> findByIdEntrenador(Integer id);
}