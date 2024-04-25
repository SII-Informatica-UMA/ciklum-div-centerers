package es.informatica.uma.es.entities;

import java.util.Arrays;
import java.util.Objects;

import jakarta.persistence.*;

//Se ha modificado la anotación de Id ya que buscamos que se genere automáticamente su valor
@Entity
@Table(name = "SESION")
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "Inicio")
    private String inicio;
    @Column(name = "Fin")
    private String fin;
    @Column(name = "TrabajoRealizado")
    private String trabajoRealizado;
    @Column(name = "Multimedia")
    private String[] multimedia;
    @Column(name = "Descripcion")
    private String descripcion;
    @Column(name = "Presencial")
    private Boolean presencial;
    @Column(name = "DatosSalud")
    private String[] datosSalud;
    @Column(name = "ID_Plan", nullable = false)
    private Integer idPlan;
    @Column(name = "ID_Entrenador", nullable = false)
    private Integer idEntrenador;
    @Column(name = "ID_Cliente", nullable = false)
    private Integer idCliente;

    // Añadidos los constructores de Sesion
    public Sesion(){}
    public Sesion(String inicio, String fin, String trabR, String[] multim, String desc, Boolean pres, String[] ds, Integer plan, Integer idEnt, Integer idCli){
        setInicio(inicio);
        setFin(fin);
        setTrabajoRealizado(trabR);
        setMultimedia(multim);
        setDescripcion(desc);
        setPresencial(pres);
        setDatosSalud(ds);
        setIdPlan(plan);
        setIdEntrenador(idEnt);
        setIdCliente(idCli);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInicio() {
        return this.inicio;
    }

    public void setInicio(String ini) {
        this.inicio = ini;
    }

    public String getFin() {
        return this.fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public String getTrabajoRealizado() {
        return this.trabajoRealizado;
    }

    public void setTrabajoRealizado(String tr) {
        this.trabajoRealizado = tr;
    }

    public String getMultimedia() {
        return Arrays.toString(this.multimedia);
    }

    public void setMultimedia(String[] mlt) {
        this.multimedia = mlt;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String desc) {
        this.descripcion = desc;
    }

    public Boolean getPresencial() {
        return this.presencial;
    }

    public void setPresencial(Boolean pres) {
        this.presencial = pres;
    }

    public String getDatosSalud() {
        return Arrays.toString(this.datosSalud);
    }

    public void setDatosSalud(String[] ds) {
        this.datosSalud = ds;
    }

    public Integer getIdPlan() {
        return this.idPlan;
    }

    public void setIdPlan(Integer idPlan) {
        this.idPlan = idPlan;
    }

    public Integer getIdEntrenador() { return idEntrenador; }

    public void setIdEntrenador(Integer idEntrenador) { this.idEntrenador = idEntrenador; }

    public Integer getIdCliente() { return idCliente; }

    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    @Override
    public String toString() {
        return "Sesion [Id: " + this.id + ", IdPlan: " + this.idPlan + ", Inicio: " + this.inicio + ", Fin: " + this.fin + ", Trabajo Realizado: "
                + this.trabajoRealizado + ", Multimedia: " + Arrays.toString(this.multimedia) + ", Descripción: " + this.descripcion
                + ", Presencial: " + this.presencial + ", Datos Salud: " + Arrays.toString(this.datosSalud) + " , IdEntrenador: " + this.idEntrenador + " , IdCliente: " + this.idCliente + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sesion sesion = (Sesion) o;
        return Objects.equals(this.id, sesion.id) && Objects.equals(this.idPlan, sesion.idPlan)
                && Objects.equals(this.inicio, sesion.inicio) && Objects.equals(this.fin, sesion.fin)
                && Objects.equals(this.trabajoRealizado, sesion.trabajoRealizado) && Arrays.equals(this.multimedia, sesion.multimedia)
                && Objects.equals(this.descripcion, sesion.descripcion) && Objects.equals(this.presencial, sesion.presencial)
                && Arrays.equals(this.datosSalud, sesion.datosSalud)
                && Objects.equals(this.idEntrenador, sesion.idEntrenador)
                && Objects.equals(this.idCliente, sesion.idCliente)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.idPlan, this.inicio, this.fin, this.trabajoRealizado, Arrays.hashCode(this.multimedia),
                this.descripcion, this.presencial, Arrays.hashCode(this.datosSalud), this.idEntrenador, this.idCliente);
    }
}
