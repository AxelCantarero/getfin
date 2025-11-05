package org.getfin.modelos;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "visitas")
public class Visita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVisita;

    private String nombreResponsable;
    private Integer total;
    private Integer hombres;
    private Integer mujeres;
    private String carrera;
    private LocalDate fecha;

    public Visita() {
    }

    public Visita(String nombreResponsable, Integer total, Integer hombres, Integer mujeres, String carrera, LocalDate fecha) {
        this.nombreResponsable = nombreResponsable;
        this.total = total;
        this.hombres = hombres;
        this.mujeres = mujeres;
        this.carrera = carrera;
        this.fecha = fecha;
    }

    public Long getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(Long idVisita) {
        this.idVisita = idVisita;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getHombres() {
        return hombres;
    }

    public void setHombres(Integer hombres) {
        this.hombres = hombres;
    }

    public Integer getMujeres() {
        return mujeres;
    }

    public void setMujeres(Integer mujeres) {
        this.mujeres = mujeres;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
