package org.getfin.modelos;

import jakarta.persistence.*;
import org.getfin.modelos.enums.EstadoAnimal;
import org.getfin.modelos.enums.TipoAnimal;

import java.math.BigDecimal;

@Entity
@Table(name = "animales")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAnimal;

    private String nombre;

    @Column(unique = true)
    private String identificador;

    @Enumerated(EnumType.STRING)
    private TipoAnimal tipo = TipoAnimal.BOVINOS;

    private Integer cantidad;
    private BigDecimal pesoPromedio;
    private String descripcion;
    @Enumerated(EnumType.STRING)
    private EstadoAnimal estado = EstadoAnimal.DISPONIBLE;

    public Animal() {
    }

    public Animal(String nombre, String identificador, TipoAnimal tipo, Integer cantidad, BigDecimal pesoPromedio, String descripcion, EstadoAnimal estado) {
        this.nombre = nombre;
        this.identificador = identificador;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.pesoPromedio = pesoPromedio;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Long getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(Long idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public TipoAnimal getTipo() {
        return tipo;
    }

    public void setTipo(TipoAnimal tipo) {
        this.tipo = tipo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPesoPromedio() {
        return pesoPromedio;
    }

    public void setPesoPromedio(BigDecimal pesoPromedio) {
        this.pesoPromedio = pesoPromedio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoAnimal getEstado() {
        return estado;
    }

    public void setEstado(EstadoAnimal estado) {
        this.estado = estado;
    }
}

