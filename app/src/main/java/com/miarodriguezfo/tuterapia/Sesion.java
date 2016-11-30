package com.miarodriguezfo.tuterapia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Miguel Rodriguez on 31/10/2015.
 */
public class Sesion {
    String numero;
    Date fecha;
    String comentarios;
    List<EjercicioAsignado> ejercicios;
    String estado;

    public Sesion(String numero, Date fecha, String comentarios, String estado) {
        this.numero = numero;
        this.fecha = fecha;
        this.comentarios = comentarios;
        this.estado = estado;
        this.ejercicios= new ArrayList<>();
    }

    public void addEjercicio(EjercicioAsignado ejercicioAsignado){
        ejercicios.add(ejercicioAsignado);
    }
}
