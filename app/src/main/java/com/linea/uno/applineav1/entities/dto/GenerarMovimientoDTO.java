package com.linea.uno.applineav1.entities.dto;

import com.linea.uno.applineav1.entities.Cliente;
import com.linea.uno.applineav1.entities.Detalle;
import com.linea.uno.applineav1.entities.Movimiento;

import java.util.ArrayList;

public class GenerarMovimientoDTO {

    private Movimiento movimiento;

    private ArrayList<Detalle> detalle;

    private Cliente cliente;

    public Movimiento getMovimiento(){
        return movimiento;
    }


    public GenerarMovimientoDTO(Movimiento movimiento, ArrayList<Detalle> detalle, Cliente cliente) {
        this.movimiento = movimiento;
        this.detalle = detalle;
        this.cliente = cliente;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public ArrayList<Detalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(ArrayList<Detalle> detalle) {
        this.detalle = detalle;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}