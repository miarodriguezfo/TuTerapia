package com.miarodriguezfo.tuterapia;

/**
 * Created by Miguel Rodriguez on 27/11/2016.
 */

public class EjercicioAsignado {
    String uid;
    String nombre;
    String peso;
    String repeticiones;
    String duracion;
    String indicaciones;
    String estado;

    public EjercicioAsignado(String uid, String nombre, String peso, String repeticiones, String duracion, String indicaciones) {
        this.uid = uid;
        this.nombre=nombre;
        this.peso = peso;
        this.repeticiones = repeticiones;
        this.duracion = duracion;
        this.indicaciones = indicaciones;
        this.estado="Programado";
    }

    public EjercicioAsignado(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(String repeticiones) {
        this.repeticiones = repeticiones;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


}
