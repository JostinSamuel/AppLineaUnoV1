package com.linea.uno.applineav1.entities.dto;

import com.linea.uno.applineav1.entities.Cliente;
import com.linea.uno.applineav1.entities.Detalle;
import com.linea.uno.applineav1.entities.Movimiento;

import java.util.ArrayList;

public class GenerarMovimientoDTO {

    private Movimiento movimiento;

    private Detalle detalle;

    private Cliente cliente;

    public Movimiento getMovimiento(){
        return movimiento;
    }

    public GenerarMovimientoDTO() {
        this.movimiento = new Movimiento();
        this.detalle = new Detalle();
        this.cliente = new Cliente();
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public Detalle getDetalle() {
        return detalle;
    }

    public void setDetalle(Detalle detalle) {
        this.detalle = detalle;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}