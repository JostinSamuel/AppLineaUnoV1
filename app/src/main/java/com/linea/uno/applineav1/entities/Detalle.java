package com.linea.uno.applineav1.entities;

import java.io.Serializable;
import java.sql.Date;

public class Detalle implements Serializable {

    private Integer id_detalle;
    private Tarjeta id_tarjeta;
    private Movimiento id_movimiento;
    private Double monto_total;
    private Date fecha;

    public Integer getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(Integer id_detalle) {
        this.id_detalle = id_detalle;
    }

    public Tarjeta getId_tarjeta() {
        return id_tarjeta;
    }

    public void setId_tarjeta(Tarjeta id_tarjeta) {
        this.id_tarjeta = id_tarjeta;
    }

    public Movimiento getId_movimiento() {
        return id_movimiento;
    }

    public void setId_movimiento(Movimiento id_movimiento) {
        this.id_movimiento = id_movimiento;
    }

    public Double getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(Double monto_total) {
        this.monto_total = monto_total;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}