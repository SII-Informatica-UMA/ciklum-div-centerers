package sii.microservicio.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import sii.microservicio.entities.Sesion;
import sii.microservicio.exceptions.AccesoNoAutorizado;
import sii.microservicio.exceptions.SesionInexistente;
import sii.microservicio.repositories.SesionRepo;
import sii.microservicio.security.JwtUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class SesionService {

    private static String PUERTO_CENTRO;
    private static final String URL_CENTRO = "http://localhost:" + PUERTO_CENTRO + "/centro";

    private static String PUERTO_ENTRENADOR;
    private static final String URL_ENTRENADOR = "http://localhost:" + PUERTO_ENTRENADOR + "/entrenador";

    private static String PUERTO_CLIENTE;
    private static final String URL_CLIENTE = "http://localhost:" + PUERTO_CLIENTE + "/cliente";

    private static String PUERTO_ENTRENA;
    private static final String URL_ENTRENA = "http://localhost:" + PUERTO_ENTRENA + "/entrena";



    private final SesionRepo sesionRepo;

    public SesionService(SesionRepo sesionRepo) {
        this.sesionRepo = sesionRepo;
    }

    public Sesion save(String jwt, Sesion sesion){
        //this.puedeAcceder(jwt, sesion.getIdPlan());
        return this.sesionRepo.save(sesion);
    }

    public Optional<Sesion> findById(String jwt, Long id) {
        return this.sesionRepo.findById(id);
    }

    public List<Sesion> findByIdPlan(String jwt, Long idPlan) {
        //this.puedeAcceder(jwt, idPlan);
        return this.sesionRepo.findByIdPlan(idPlan);
    }

    public void deleteById(String jwt, Long id){
        if(!this.sesionRepo.existsById(id)){
            throw new SesionInexistente(id);
        } else {
            //this.puedeAcceder(jwt, sesionRepo.findById(id).get().getIdPlan());
            this.sesionRepo.deleteById(id);
        }
    }

    /*  Método que verifique si el usuario tiene permiso para acceder al recurso
    *
    *   Verificar mediante peticiones a microservicios externos que el usuario
    *   está relacionado con el idPlan pasado como parámetro
    *
    *   Concretamente, realizar petición a "Gestión de Entrenamientos"
    *   al endpoint /entrena, pasando como parámetro idUsuario (obtenido en el JWT)
    *
    *   Habría que controlar que el rol sea entrenador o cliente para hacer la petición
    *   indicando que el parámetro es idEntrenador o idCliente (realizar peticiones a
    *   Gestión de centros, Gestión de entrenadores y Gestión de clientes)
    *
    *   De la respuesta obtenida, verificar si idPlan se encuentra entre los planes
    *   de la relación cliente-entrenador obtenida
    *
    * */

    /*
    private void puedeAcceder(String jwt, Long idPlan){
        JwtUtil jwtUtil = new JwtUtil();
        String idUsuario = jwtUtil.getUsernameFromToken(jwt);

        if (idUsuario == null) {
            throw new AccesoNoAutorizado("Id de usuario no puede ser nulo");
        }

        List<Map<String, Object>> centros = getCentros();
        for (Map<String, Object> centro : centros) {
            Long idCentro = ((Number) centro.get("idCentro")).longValue();
            List<Long> idEntrenadores = getIdEntrenadores(idCentro);
            List<Long> idClientes = getIdClientes(idCentro);

            if (idEntrenadores.contains(Long.parseLong(idUsuario)) || idClientes.contains(Long.parseLong(idUsuario))) {
                boolean esEntrenador = idEntrenadores.contains(Long.parseLong(idUsuario));
                List<Long> idPlanes = getIdPlanes(idUsuario, esEntrenador);

                if (idPlanes.contains(idPlan)) {
                    return; // Usuario autorizado
                }
            }
        }

        throw new AccesoNoAutorizado("Usuario no autorizado para este plan");

    }

    public List<Map<String, Object>> getCentros() {
        Map<String, Object>[] response = restTemplate.getForObject(URL_CENTRO, Map[].class);
        return Arrays.asList(response);
    }

    public List<Long> getIdEntrenadores(Long idCentro) {
        String url = URL_ENTRENADOR + "?centro=" + idCentro;
        Long[] response = restTemplate.getForObject(url, Long[].class);
        return Arrays.asList(response);
    }

    public List<Long> getIdClientes(Long idCentro) {
        String url = URL_CLIENTE + "?centro=" + idCentro;
        Long[] response = restTemplate.getForObject(url, Long[].class);
        return Arrays.asList(response);
    }

    public List<Long> getIdPlanes(String idUsuario, boolean esEntrenador) {
        String roleQueryParam = esEntrenador ? "entrenador=" : "cliente=";
        String url = URL_ENTRENA + "?" + roleQueryParam + idUsuario;
        Long[] response = restTemplate.getForObject(url, Long[].class);
        return Arrays.asList(response);
    }

*/

}
