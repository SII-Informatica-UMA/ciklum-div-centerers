package sii.microservicio.controllers;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import sii.microservicio.dtos.SesionDTO;
import sii.microservicio.dtos.SesionNuevaDTO;
import sii.microservicio.entities.Sesion;
import sii.microservicio.exceptions.SesionInexistente;
import sii.microservicio.services.SesionService;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@CrossOrigin
@RequestMapping("/sesion")
public class GestionSesiones {

    private final SesionService sesionService;

    public GestionSesiones(SesionService sesionService) {
        this.sesionService = sesionService;
    }

    @GetMapping
    public List<SesionDTO> obtenerSesiones(@RequestHeader("Authorization") String jwt, @RequestParam(value = "plan") Long idPlan) {
        return this.sesionService.findByIdPlan(jwt, idPlan).stream().map(SesionDTO::fromEntity).toList();
    }

    @PostMapping
    public ResponseEntity<SesionDTO> crearSesion(@RequestHeader("Authorization") String jwt,@RequestParam(value = "plan") Long idPlan, @RequestBody SesionNuevaDTO sesionNuevaDTO, UriComponentsBuilder uriComponentsBuilder){
        Sesion s = sesionNuevaDTO.toEntity();
        s.setIdPlan(idPlan);
        Sesion aux = this.sesionService.save(jwt, s);
        URI location = uriComponentsBuilder.path("/sesion/{id}")
                .buildAndExpand(aux.getId())
                .toUri();
        return ResponseEntity.created(location).body(SesionDTO.fromEntity(aux));
    }

    @GetMapping({"/{idSesion}"})
    public ResponseEntity<SesionDTO> getSesion(@RequestHeader("Authorization") String jwt, @PathVariable Long idSesion) {
        Optional<SesionDTO> res = this.sesionService.findById(jwt, idSesion).map(SesionDTO::fromEntity);
        return ResponseEntity.of(res);
    }


    @PutMapping({"/{idSesion}"})
    public ResponseEntity<SesionDTO> actualizarSesion(@RequestHeader("Authorization") String jwt, @PathVariable Long idSesion, @RequestBody SesionDTO sesionDTO) {
        sesionDTO.setId(idSesion);
        Sesion s = this.sesionService.save(jwt, sesionDTO.toEntity());
        return ResponseEntity.ok(SesionDTO.fromEntity(s));
    }

    @DeleteMapping({"/{idSesion}"})
    public void eliminarSesion(@RequestHeader("Authorization") String jwt, @PathVariable Long idSesion) {
        this.sesionService.deleteById(jwt, idSesion);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({SesionInexistente.class})
    public void handleSesionInexistente(SesionInexistente s){
    }


}
