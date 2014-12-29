package com.devmerz.minescrapp;

/**
 * Created by @devmerz on 23/12/14.
 */
public class Elements {

    public String ingredientes;
    public String imagen;
    public String descripcion;
    public String nombre;

    public Elements(){}

    public Elements(String ingredientes, String imagen, String descripcion, String nombre) {
        this.ingredientes = ingredientes;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }


    public String getIngredientes() {
        return ingredientes;
    }

    public String getImagen() {
        return imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
