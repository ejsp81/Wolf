package com.example.wolf;

public class Ubicacion {
    public int ubic_id;
    public double ubic_latitud;
    public double ubic_longitud;
    public String ubic_hora_conexion;
    public int ubic_estado;
    public int ubic_mote_id;

    public Ubicacion(int ubic_id, double ubic_latitud, double ubic_longitud, String ubic_hora_conexion, int ubic_estado, int ubic_mote_id) {
        this.ubic_id = ubic_id;
        this.ubic_latitud = ubic_latitud;
        this.ubic_longitud = ubic_longitud;
        this.ubic_hora_conexion = ubic_hora_conexion;
        this.ubic_estado = ubic_estado;
        this.ubic_mote_id = ubic_mote_id;
    }

    public Ubicacion() {
        this.ubic_id = 0;
        this.ubic_latitud = 0.0;
        this.ubic_longitud = 0.0;
        this.ubic_hora_conexion = "";
        this.ubic_estado = 0;
        this.ubic_mote_id = 0;
    }

    public int getUbic_id() {
        return ubic_id;
    }

    public void setUbic_id(int ubic_id) {
        this.ubic_id = ubic_id;
    }

    public double getUbic_latitud() {
        return ubic_latitud;
    }

    public void setUbic_latitud(double ubic_latitud) {
        this.ubic_latitud = ubic_latitud;
    }

    public double getUbic_longitud() {
        return ubic_longitud;
    }

    public void setUbic_longitud(double ubic_longitud) {
        this.ubic_longitud = ubic_longitud;
    }

    public String getUbic_hora_conexion() {
        return ubic_hora_conexion;
    }

    public void setUbic_hora_conexion(String ubic_hora_conexion) {
        this.ubic_hora_conexion = ubic_hora_conexion;
    }

    public int getUbic_estado() {
        return ubic_estado;
    }

    public void setUbic_estado(int ubic_estado) {
        this.ubic_estado = ubic_estado;
    }

    public int getUbic_mote_id() {
        return ubic_mote_id;
    }

    public void setUbic_mote_id(int ubic_mote_id) {
        this.ubic_mote_id = ubic_mote_id;
    }
}
