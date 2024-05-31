/*package sii.microservicio.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AuthService {

        @Value("${microservice.gestionCentros}")
        private String gestionCentrosUrl;

        @Value("${microservice.gestionEntrenadores}")
        private String gestionEntrenadoresUrl;

        @Value("${microservice.gestionClientes}")
        private String gestionClientesUrl;

        private final RestTemplate restTemplate = new RestTemplate();

        public boolean isAuthorized(String idUsuario) {
            List<Integer> centros = getCentros();

            for (Integer idCentro : centros) {
                List<String> entrenadores = getEntrenadores(idCentro);
                List<String> clientes = getClientes(idCentro);

                if (entrenadores.contains(idUsuario) || clientes.contains(idUsuario)) {
                    return true;
                }
            }

            return false;
        }

        private List<Integer> getCentros() {
            return restTemplate.getForObject(gestionCentrosUrl + "/centros", List.class);
        }

        private List<String> getEntrenadores(Integer idCentro) {
            return restTemplate.getForObject(gestionEntrenadoresUrl + "/entrenadores?centro=" + idCentro, List.class);
        }

        private List<String> getClientes(Integer idCentro) {
            return restTemplate.getForObject(gestionClientesUrl + "/clientes?centro=" + idCentro, List.class);
        }
    }

*/
