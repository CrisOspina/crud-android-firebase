package com.example.app_licorera_firebase;

public class Model {
    //Modelo
    private String codProduct;
    private String nombre;
    private String descripcion;
    private String precio;


    public Model(){

    }

    public Model(String codProduct, String nombre, String descripcion, String precio) {
        this.codProduct = codProduct;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getCodProduct() {
        return codProduct;
    }

    public void setCodProduct(String codProduct) {
        this.codProduct = codProduct;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
