package org.getfin.modelos;

import jakarta.persistence.*;
import org.getfin.modelos.enums.CategoriaProducto;
import org.getfin.modelos.enums.EstadoProducto;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @Column(nullable = false)
    private String nombreProducto;

    private String descripcion;
    private String unidadMedida;

    @Enumerated(EnumType.STRING)
    private CategoriaProducto categoria = CategoriaProducto.INSUMOS;

    private LocalDate fechaCaducidad;

    private BigDecimal stock;
    private BigDecimal montoInicial;

    @Enumerated(EnumType.STRING)
    private EstadoProducto estado = EstadoProducto.DISPONIBLE;
    public Producto() {}

    public Producto(String nombreProducto, String descripcion, String unidadMedida, CategoriaProducto categoria, LocalDate fechaCaducidad, BigDecimal stock, BigDecimal montoInicial, EstadoProducto estado) {
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.categoria = categoria;
        this.fechaCaducidad = fechaCaducidad;
        this.stock = stock;
        this.montoInicial = montoInicial;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
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

    public EstadoProducto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }
}
