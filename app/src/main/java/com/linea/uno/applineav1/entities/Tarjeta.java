package com.linea.uno.applineav1.entities;

public class Tarjeta {
    private Integer id_tarjeta;
    private TipoTarjeta tipo_tarjeta;
    private String codigo;
    private Double saldo;
    private String anio_compra;
    private String anio_vencimiento;
    private Boolean estado;
    private Cliente cliente;

    public Integer getId_tarjeta() {
        return id_tarjeta;
    }

    public void setId_tarjeta(Integer id_tarjeta) {
        this.id_tarjeta = id_tarjeta;
    }

    public TipoTarjeta getTipo_tarjeta() {
        return tipo_tarjeta;
    }

    public void setTipo_tarjeta(TipoTarjeta tipo_tarjeta) {
        this.tipo_tarjeta = tipo_tarjeta;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getAnio_compra() {
        return anio_compra;
    }

    public void setAnio_compra(String anio_compra) {
        this.anio_compra = anio_compra;
    }

    public String getAnio_vencimiento() {
        return anio_vencimiento;
    }

    public void setAnio_vencimiento(String anio_vencimiento) {
        this.anio_vencimiento = anio_vencimiento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
