package sii.microservicio.exceptions;

public class AccesoNoAutorizado extends RuntimeException{

    public AccesoNoAutorizado(String str) {
        super(str);
    }
    public AccesoNoAutorizado() {
        super();
    };
}
