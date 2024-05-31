package sii.microservicio.exceptions;

import sii.microservicio.entities.Sesion;

public class SesionInexistente extends RuntimeException{
    /*public SesionInexistente(Sesion sesion) {
        this(sesion.getId());
    }*/

    public SesionInexistente(Long id) {
        super("La sesión con ID " +id + " no existe");
    }

    public SesionInexistente() {
        super();
    };

}
