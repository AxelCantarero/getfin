package org.getfin.modelos;

import jakarta.persistence.*;
import org.getfin.modelos.enums.TipoTransaccion;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transacciones")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaccion;

    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipo;

    private String descripcion;
    private String nombreCliente;
    private LocalDate fecha;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal iva;
    private BigDecimal retencion;
    private BigDecimal total;
    private String numeroFactura;
    private String categoria;

    @ManyToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "idCultivo")
    private Cultivo cultivo;

    @ManyToOne
    @JoinColumn(name = "idAnimal")
    private Animal animal;

    public Transaccion() {
    }

    public Transaccion(TipoTransaccion tipo, String descripcion, String nombreCliente, LocalDate fecha, BigDecimal cantidad, BigDecimal precioUnitario, BigDecimal iva, BigDecimal retencion, BigDecimal total, String numeroFactura, Producto producto) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.nombreCliente = nombreCliente;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.iva = iva;
        this.retencion = retencion;
        this.total = total;
        this.numeroFactura = numeroFactura;
        this.producto = producto;
    }

    public Transaccion(TipoTransaccion tipo, String descripcion, String nombreCliente, Cultivo cultivo, String numeroFactura, BigDecimal total, BigDecimal retencion, BigDecimal cantidad, LocalDate fecha, BigDecimal precioUnitario, BigDecimal iva) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.nombreCliente = nombreCliente;
        this.cultivo = cultivo;
        this.numeroFactura = numeroFactura;
        this.total = total;
        this.retencion = retencion;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.precioUnitario = precioUnitario;
        this.iva = iva;
    }

    public Transaccion(Animal animal, String numeroFactura, BigDecimal total, BigDecimal retencion, BigDecimal precioUnitario, BigDecimal iva, BigDecimal cantidad, LocalDate fecha, String nombreCliente, TipoTransaccion tipo, String descripcion) {
        this.animal = animal;
        this.numeroFactura = numeroFactura;
        this.total = total;
        this.retencion = retencion;
        this.precioUnitario = precioUnitario;
        this.iva = iva;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.nombreCliente = nombreCliente;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }
    // Constructor especial para ventas de lácteos
    public Transaccion(String nombreCliente, BigDecimal cantidad, BigDecimal precio,
                       BigDecimal retencion, LocalDate fecha, String descripcion) {
        this.nombreCliente = nombreCliente;
        this.cantidad = cantidad;
        this.precioUnitario = precio;
        this.retencion = retencion;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tipo = TipoTransaccion.INGRESO; // siempre ingreso
        this.categoria = "LÁCTEO";           // predeterminado
        this.iva = BigDecimal.ZERO;          // sin IVA
        this.total = precio.multiply(cantidad).subtract(retencion); // calcula total básico
    }


    public Long getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Long idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public TipoTransaccion getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransaccion tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getRetencion() {
        return retencion;
    }

    public void setRetencion(BigDecimal retencion) {
        this.retencion = retencion;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Cultivo getCultivo() {
        return cultivo;
    }

    public void setCultivo(Cultivo cultivo) {
        this.cultivo = cultivo;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
