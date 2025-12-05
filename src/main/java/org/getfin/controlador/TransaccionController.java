package org.getfin.controlador;

import org.getfin.modelos.Transaccion;
import org.getfin.modelos.enums.TipoTransaccion;
import org.getfin.servicios.GenericServiceImpl;
import org.getfin.servicios.IGenericService;
import org.getfin.util.HibernateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TransaccionController {

    private static TransaccionController instance;

    public TransaccionController(){

    }
    public void guardarTransaccion(Transaccion transaccion){
        IGenericService<Transaccion> clientes = new GenericServiceImpl<>(Transaccion.class, HibernateUtil.getSessionFactory());
        clientes.save(transaccion);
    }
    public void eliminarTransaccion(Transaccion transaccion){
        IGenericService<Transaccion> clientes = new GenericServiceImpl<>(Transaccion.class, HibernateUtil.getSessionFactory());
        clientes.delete(transaccion);
    }
    public void editarTransaccion(Transaccion cliente){
        IGenericService<Transaccion> clientes = new GenericServiceImpl<>(Transaccion.class, HibernateUtil.getSessionFactory());
        clientes.update(cliente);
    }
    public List<Transaccion> getTransacciones() {
        IGenericService<Transaccion> clienteIGenericService= new GenericServiceImpl<>(Transaccion.class, HibernateUtil.getSessionFactory());
        return clienteIGenericService.getAll();
    }

    public static TransaccionController getInstance() {
        if (instance == null) {
            instance = new TransaccionController();
        }
        return instance;
    }
    public BigDecimal totalIngresoFecha(int mes, int anio) {
        return getTransacciones().stream()
                .filter(t -> t.getTipo().equals(TipoTransaccion.INGRESO))
                .filter(t -> t.getFecha().getMonthValue() == mes
                        && t.getFecha().getYear() == anio)
                .map(Transaccion::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BigDecimal totalEgresoFecha(int mes, int anio) {
        return getTransacciones().stream()
                .filter(t -> t.getTipo().equals(TipoTransaccion.EGRESO))
                .filter(t -> t.getFecha().getMonthValue() == mes
                        && t.getFecha().getYear() == anio)
                .map(Transaccion::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    /// ///////////
    public BigDecimal PorcentajeCrecimientoIngreso(int mes, int anio) {
        BigDecimal ingresos = totalIngresoFecha(mes, anio);
        BigDecimal ingresosAnterior;

        if (mes == 1) {
            // Si es enero, el mes anterior es diciembre del año anterior
            ingresosAnterior = totalIngresoFecha(12, anio - 1);
        } else {
            // Mes normal, el mes anterior es mes - 1
            ingresosAnterior = totalIngresoFecha(mes - 1, anio);
        }
        if (ingresosAnterior.compareTo(BigDecimal.ZERO) == 0) {
            // Evitar división entre cero
            return BigDecimal.ZERO;
        }
        return ingresos.subtract(ingresosAnterior)
                .divide(ingresosAnterior, 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
    }
    //////////////////////////////////
    public BigDecimal PorcentajeCrecimientoEgreso(int mes, int anio) {
        BigDecimal egreso = totalEgresoFecha(mes, anio);
        BigDecimal egresoAnterior;

        if (mes == 1) {
            // Si es enero, el mes anterior es diciembre del año anterior
            egresoAnterior = totalEgresoFecha(12, anio - 1);
        } else {
            // Mes normal, el mes anterior es mes - 1
            egresoAnterior = totalEgresoFecha(mes - 1, anio);
        }

        if (egresoAnterior.compareTo(BigDecimal.ZERO) == 0) {
            // Evitar división entre cero
            return BigDecimal.ZERO;
        }

        return egreso.subtract(egresoAnterior)
                .divide(egresoAnterior, 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
    }

    public BigDecimal totalCapital() {
        return totalIngresos().subtract(totalEgresos());
    }

    private BigDecimal totalIngresos() {
        return getInstance().getTransacciones().stream()
                .filter(t -> t.getTipo().equals(TipoTransaccion.INGRESO))
                .map(Transaccion::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal totalEgresos() {
        return getInstance().getTransacciones().stream()
                .filter(t -> t.getTipo().equals(TipoTransaccion.EGRESO))
                .map(Transaccion::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }



}
