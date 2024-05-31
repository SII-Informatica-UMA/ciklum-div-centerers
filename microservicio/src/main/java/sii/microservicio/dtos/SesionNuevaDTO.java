package sii.microservicio.dtos;

import lombok.*;
import sii.microservicio.entities.Sesion;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SesionNuevaDTO {
    private Long idPlan;
    private Date inicio;
    private Date fin;
    private String trabajoRealizado;
    private List<String> multimedia;
    private String descripcion;
    private Boolean presencial;
    private List<String> datosSalud;

    public Sesion toEntity() {
        return Sesion.builder().idPlan(this.getIdPlan()).inicio(this.getInicio()).fin(this.getFin()).trabajoRealizado(this.getTrabajoRealizado()).multimedia(this.getMultimedia()).descripcion(this.getDescripcion()).presencial(this.getPresencial()).datosSalud(this.getDatosSalud()).build();
    }
}
