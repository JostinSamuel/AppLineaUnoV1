package com.linea.uno.applineav1.entities.dto;

import com.linea.uno.applineav1.entities.Cliente;
import com.linea.uno.applineav1.entities.Movimiento;

public class GenerarMovimientoDTO {

    private Movimiento movimiento;

    private Cliente cliente;

    public Movimiento getMovimiento(){
        return movimiento;
    }

    public GenerarMovimientoDTO() {
        this.movimiento = new Movimiento();
        this.cliente = new Cliente();
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}