package es.informatica.uma.es;

import es.informatica.uma.es.entities.Sesion;
import es.informatica.uma.es.repositories.SesionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CorredorLineaComandos implements CommandLineRunner {
    private SesionRepository repository;
    public CorredorLineaComandos(SesionRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        /* Se implementa un caso de ejemplo en el que se almacena una nueva sesión al
        *  iniciarse el programa.
        *
        *  En caso de que en la línea de comandos se introduzca algún valor, se intentará buscar
        *  en la base de datos una sesión con ese ID.
        *
        *  (No es necesario para la entrega, simplemente se comprueba el funcionamiento de las clases
        *  y que se realiza la conexión con la BD).
        * */

        Sesion n = new Sesion("Enero", "Marzo", "Pecho", new String[10], "Descripción de sesión", Boolean.TRUE, new String[10], 1, 1, 1);
        repository.save(n);
        System.out.println("Se ha almacenado una nueva sesión con id " + n.getId());
        for (String s: args) {
            System.out.println("Id introducido: " + s);
        }

        if (args.length > 0) {
            Sesion s = repository.findById(Integer.valueOf(args[0]));
            if (s != null) {
                System.out.println(s.toString());
            } else{
                System.out.println("No existe una sesión con ese id");
            }
        }
    }

}