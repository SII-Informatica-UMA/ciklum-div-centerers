package sii.microservicio;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import sii.microservicio.dtos.SesionDTO;
import sii.microservicio.entities.Sesion;
import sii.microservicio.repositories.SesionRepo;

import java.net.URI;
import java.util.*;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el microservicio de gestión de sesiones")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SesionesApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value = "${local.server.port}")
	private int port;

	@Autowired
	private SesionRepo sesionRepository;

	private static String jwtToken;

	@BeforeEach
	public void initializeDatabase() {
		jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE4MTYyMzkwMjJ9.Sva_CCk3SXxN2-GrjJtEuRnORDqgI22BGoG6aSZJJMg";
		sesionRepository.deleteAll();
	}

	private URI uri(String scheme, String host, int port, String... paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path : paths) {
			ub = ub.path(path);
		}
		return ub.build();
	}

	private static String creaURL(String scheme, String host, int port, String path) {
		return UriComponentsBuilder.newInstance()
				.scheme(scheme)
				.host(host)
				.port(port)
				.path(path)
				.build()
				.toUriString();
	}


	@Nested
	@DisplayName("cuando no hay sesiones")
	public class SesionesVacio {

		@Test
		@DisplayName("devuelve la lista de sesiones vacía")
		public void devuelveSesiones() {
			var peticion = creaURL("http", "localhost", port, "/sesion?plan=1");

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + jwtToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);

			var respuesta = restTemplate.exchange(peticion, HttpMethod.GET, entity,
					new ParameterizedTypeReference<Set<Sesion>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isEmpty();
		}

		@Test
		@DisplayName("se crea correctamente una nueva sesión")
		public void creaSesion() {
			assertThat(sesionRepository.findByIdPlan(1L)).isEmpty();
			Sesion ses = new Sesion();

			var peticion = creaURL("http", "localhost", port, "/sesion?plan=1");


			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + jwtToken);
			HttpEntity<?> entity = new HttpEntity<Object>(ses, headers);

			var respuesta = restTemplate.exchange(peticion, HttpMethod.POST, entity,
					new ParameterizedTypeReference<Sesion>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(entity.getBody()).isNotNull();
			assertThat(sesionRepository.findAll()).isNotEmpty();
			assertThat(sesionRepository.findByIdPlan(1L)).isNotEmpty();
		}

		@Test
		@DisplayName("devuelve error al buscar una sesión por su ID")
		public void devuelveSesionPorID() {
			long id = 1L;
			var peticion = creaURL("http", "localhost", port, "/sesion/" + id);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + jwtToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);

			var respuesta = restTemplate.exchange(peticion, HttpMethod.GET, entity,
					new ParameterizedTypeReference<Sesion>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			assertThat(sesionRepository.findById(id)).isEmpty();
		}

		@Test
		@DisplayName("devuelve error al intentar borrar una sesión por su ID")
		public void eliminaSesionPorID() {
			long id = 1L;
			var peticion = creaURL("http", "localhost", port, "/sesion/" + id);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + jwtToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);

			var respuesta = restTemplate.exchange(peticion, HttpMethod.DELETE, entity,
					new ParameterizedTypeReference<Sesion>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			assertThat(sesionRepository.findById(id)).isEmpty();
		}

		@Test
		@DisplayName("devuelve error si no se pasa un idPlan al intentar crear una sesión")
		public void creaSesionErronea() {
			assertThat(sesionRepository.findByIdPlan(1L)).isEmpty();
			Sesion ses = new Sesion();
			ses.setIdPlan(null);

			var peticion = creaURL("http", "localhost", port, "/sesion?plan=");

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + jwtToken);
			HttpEntity<?> entity = new HttpEntity<Object>(ses, headers);

			var respuesta = restTemplate.exchange(peticion, HttpMethod.POST, entity,
					new ParameterizedTypeReference<Sesion>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
			assertThat(sesionRepository.findAll()).isEmpty();
		}

	}

	@Nested
	@DisplayName("cuando hay sesiones almacenadas")
	public class SesionesConDatos {

		Long idSesionTest;

		@BeforeEach
		public void inicializaBaseDatos() {
			Sesion s1 = SesionDTO.builder().idPlan(5L).descripcion("Descripcion de ejemplo").build().toEntity();
			Sesion s2 = SesionDTO.builder().idPlan(10L).descripcion("Entrenamiento hoy").build().toEntity();
			Sesion s3 = SesionDTO.builder().idPlan(1L).descripcion("Sesión visible inicialmente").build().toEntity();

			sesionRepository.save(s1);
			sesionRepository.save(s2);
			sesionRepository.save(s3);

			idSesionTest = sesionRepository.findByIdPlan(1L).get(0).getId();

			assertThat(sesionRepository.findAll()).size().isEqualTo(3);
		}

		@Test
		@DisplayName("devuelve la lista de sesiones")
		public void devuelveSesionesConDatos() {
			var peticion = creaURL("http", "localhost", port, "/sesion?plan=1");

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + jwtToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);

			var respuesta = restTemplate.exchange(peticion, HttpMethod.GET, entity,
					new ParameterizedTypeReference<Set<Sesion>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(sesionRepository.findByIdPlan(1L)).isNotEmpty();
		}


		@Test
		@DisplayName("devuelve una sesión al buscarla por su ID")
		public void devuelveSesionPorIDConDatos() {
			String s = String.format("/sesion/%d", idSesionTest);
			assertThat(s.endsWith(idSesionTest.toString()));
			var peticion = creaURL("http", "localhost", port, s);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + jwtToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);

			var respuesta = restTemplate.exchange(peticion, HttpMethod.GET, entity,
					new ParameterizedTypeReference<Sesion>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(sesionRepository.findById(idSesionTest)).isNotEmpty();
			assertThat(respuesta.getBody()).isNotNull();
		}

		@Test
		@DisplayName("actualiza una sesión dado su ID")
		public void actualizaSesionPorId() {
			String s = String.format("/sesion/%d", idSesionTest);
			assertThat(s.endsWith(idSesionTest.toString()));
			var peticion = creaURL("http", "localhost", port, s);

			String descInit = sesionRepository.findById(idSesionTest).get().getDescripcion();


			Sesion ses = SesionDTO.builder().descripcion("Descripcion nueva").idPlan(1L).build().toEntity();

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + jwtToken);
			HttpEntity<?> entity = new HttpEntity<>(ses, headers);

			var respuesta = restTemplate.exchange(peticion, HttpMethod.PUT, entity,
					new ParameterizedTypeReference<Sesion>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isNotNull();

			assertThat(sesionRepository.findById(idSesionTest)).isNotEmpty();
			assertThat(sesionRepository.findById(idSesionTest)).isNotEqualTo(descInit);
			assertThat(sesionRepository.findById(idSesionTest).get().getDescripcion()).isEqualTo("Descripcion nueva");

		}

		@Test
		@DisplayName("elimina una sesión dado su ID")
		public void eliminaSesionPorId() {
			String s = String.format("/sesion/%d", idSesionTest);
			assertThat(s.endsWith(idSesionTest.toString()));
			var peticion = creaURL("http", "localhost", port, s);

			assertThat(sesionRepository.findById(idSesionTest)).isNotNull();
			assertThat(sesionRepository.findAll().size()).isEqualTo(3);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + jwtToken);
			HttpEntity<?> entity = new HttpEntity<>(headers);

			var respuesta = restTemplate.exchange(peticion, HttpMethod.DELETE, entity,
					new ParameterizedTypeReference<Sesion>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(sesionRepository.findAll().size()).isEqualTo(2);

		}

		@Test
		@DisplayName("se crea correctamente una nueva sesión")
		public void creaSesion() {
			assertThat(sesionRepository.findByIdPlan(33L)).isEmpty();
			Sesion ses = new Sesion();

			var peticion = creaURL("http", "localhost", port, "/sesion?plan=33");


			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + jwtToken);
			HttpEntity<?> entity = new HttpEntity<Object>(ses, headers);

			var respuesta = restTemplate.exchange(peticion, HttpMethod.POST, entity,
					new ParameterizedTypeReference<Sesion>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(entity.getBody()).isNotNull();
			assertThat(sesionRepository.findByIdPlan(33L)).isNotEmpty();
			assertThat(sesionRepository.findAll().size()).isEqualTo(4);
		}
	}
}

