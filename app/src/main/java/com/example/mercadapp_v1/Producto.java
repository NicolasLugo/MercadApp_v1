package com.example.mercadapp_v1;

public class Producto {
    private int idProd;
    private int idMercado;
    private String nombreProducto;
    private double precio;
    private int cantidad;
    private String tipoProducto;
    private String observaciones;

    //este constructor es para la creación de las tablas del recyclerView
    public Producto(String nombreProducto, double precio, int cantidad){
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public Producto(String nombreProducto, double precio, int cantidad, String tipoProducto, String observaciones) {
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.cantidad = cantidad;
        this.tipoProducto = tipoProducto;
        this.observaciones = observaciones;
    }

    public int getIdProd() {
        return idProd;
    }

    public void setIdProd(int idProd) {
        this.idProd = idProd;
    }

    public int getIdMercado() {
        return idMercado;
    }

    public void setIdMercado(int idMercado) {
        this.idMercado = idMercado;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}