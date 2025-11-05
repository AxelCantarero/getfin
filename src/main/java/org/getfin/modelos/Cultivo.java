package org.getfin.modelos;

import jakarta.persistence.*;
import org.getfin.modelos.enums.CategoriaCultivo;
import org.getfin.modelos.enums.EstadoCultivo;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cultivos")
public class Cultivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCultivo;

    private String nombreCultivo;
    private LocalDate fechaSiembra;
    private LocalDate fechaCosecha;
    private BigDecimal areaSembrada;

    @Enumerated(EnumType.STRING)
    private CategoriaCultivo categoria = CategoriaCultivo.GRANOS_BASICOS;

    @Enumerated(EnumType.STRING)
    private EstadoCultivo estado = EstadoCultivo.CRECIMIENTO;

    private BigDecimal stock;
    private BigDecimal montoInicial;

    public Cultivo() {
    }

    public Cultivo(String nombreCultivo, LocalDate fechaSiembra, LocalDate fechaCosecha, BigDecimal areaSembrada, CategoriaCultivo categoria, EstadoCultivo estado, BigDecimal stock, BigDecimal montoInicial) {
        this.nombreCultivo = nombreCultivo;
        this.fechaSiembra = fechaSiembra;
        this.fechaCosecha = fechaCosecha;
        this.areaSembrada = areaSembrada;
        this.categoria = categoria;
        this.estado = estado;
        this.stock = stock;
        this.montoInicial = montoInicial;
    }

    public Long getIdCultivo() {
        return idCultivo;
    }

    public void setIdCultivo(Long idCultivo) {
        this.idCultivo = idCultivo;
    }

    public String getNombreCultivo() {
        return nombreCultivo;
    }

    public void setNombreCultivo(String nombreCultivo) {
        this.nombreCultivo = nombreCultivo;
    }

    public LocalDate getFechaSiembra() {
        return fechaSiembra;
    }

    public void setFechaSiembra(LocalDate fechaSiembra) {
        this.fechaSiembra = fechaSiembra;
    }

    public LocalDate getFechaCosecha() {
        return fechaCosecha;
    }

    public void setFechaCosecha(LocalDate fechaCosecha) {
        this.fechaCosecha = fechaCosecha;
    }

    public BigDecimal getAreaSembrada() {
        return areaSembrada;
    }

    public void setAreaSembrada(BigDecimal areaSembrada) {
        this.areaSembrada = areaSembrada;
    }

    public CategoriaCultivo getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaCultivo categoria) {
        this.categoria = categoria;
    }

    public EstadoCultivo getEstado() {
        return estado;
    }

    public void setEstado(EstadoCultivo estado) {
        this.estado = estado;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public BigDecimal getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(BigDecimal montoInicial) {
        this.montoInicial = montoInicial;
    }
}
