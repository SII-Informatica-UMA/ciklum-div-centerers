package sii.microservicio.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    @Column(name = "Inicio")
    private Date inicio;
    @Column(name = "Fin")
    private Date fin;
    @Column(name = "TrabajoRealizado")
    private String trabajoRealizado;
    @Column(name = "Multimedia")
    @ElementCollection
    private List<String> multimedia;
    @Column(name = "Descripcion")
    private String descripcion;
    @Column(name = "Presencial")
    private Boolean presencial;
    @Column(name = "DatosSalud")
    @ElementCollection
    private List<String> datosSalud;
    @Column(name = "ID_Plan")
    private Long idPlan;

}
