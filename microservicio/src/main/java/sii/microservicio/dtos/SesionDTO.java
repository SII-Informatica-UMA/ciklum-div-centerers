package sii.microservicio.dtos;

import lombok.*;
import sii.microservicio.entities.Sesion;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SesionDTO extends SesionNuevaDTO {
    private Long id;
    public static SesionDTO fromEntity(Sesion sesion){
        SesionDTO s = new SesionDTO();
        s.setId(sesion.getId());
        s.setIdPlan(sesion.getIdPlan());
        s.setInicio(sesion.getInicio());
        s.setFin(sesion.getFin());
        s.setTrabajoRealizado(sesion.getTrabajoRealizado());
        s.setMultimedia(sesion.getMultimedia());
        s.setDescripcion(sesion.getDescripcion());
        s.setPresencial(sesion.getPresencial());
        s.setDatosSalud(sesion.getDatosSalud());
        return s;
    }

    public Sesion toEntity() {
        return Sesion.builder().id(this.getId()).idPlan(this.getIdPlan()).inicio(this.getInicio()).fin(this.getFin()).trabajoRealizado(this.getTrabajoRealizado()).multimedia(this.getMultimedia()).descripcion(this.getDescripcion()).presencial(this.getPresencial()).datosSalud(this.getDatosSalud()).build();
    }
}
