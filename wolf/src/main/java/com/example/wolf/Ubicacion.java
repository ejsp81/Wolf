package com.example.wolf;

public class Ubicacion {
    public int ubic_id;
    public double ubic_longitud;
    public double ubic_latitud;
    public int ubic_estado;
    public int ubic_usua_id;

    public Ubicacion(int ubic_id, double ubic_longitud, double ubic_latitud, int ubic_estado, int ubic_usua_id) {
        this.ubic_id = ubic_id;
        this.ubic_longitud = ubic_longitud;
        this.ubic_latitud = ubic_latitud;
        this.ubic_estado = ubic_estado;
        this.ubic_usua_id = ubic_usua_id;
    }

    public Ubicacion() {
        this.ubic_id = 0;
        this.ubic_longitud = 0;
        this.ubic_latitud = 0;
        this.ubic_estado = 0;
        this.ubic_usua_id = 0;
    }

    public int getUbic_id() {
        return ubic_id;
    }

    public void setUbic_id(int ubic_id) {
        this.ubic_id = ubic_id;
    }

    public double getUbic_longitud() {
        return ubic_longitud;
    }

    public void setUbic_longitud(double ubic_longitud) {
        this.ubic_longitud = ubic_longitud;
    }

    public double getUbic_latitud() {
        return ubic_latitud;
    }

    public void setUbic_latitud(double ubic_latitud) {
        this.ubic_latitud = ubic_latitud;
    }

    public int getUbic_estado() {
        return ubic_estado;
    }

    public void setUbic_estado(int ubic_estado) {
        this.ubic_estado = ubic_estado;
    }

    public int getUbic_usua_id() {
        return ubic_usua_id;
    }

    public void setUbic_usua_id(int ubic_usua_id) {
        this.ubic_usua_id = ubic_usua_id;
    }

    @Override
    public String toString() {
        return "Ubicacion{" +
                "ubic_id=" + ubic_id +
                ", ubic_longitud=" + ubic_longitud +
                ", ubic_latitud=" + ubic_latitud +
                ", ubic_estado=" + ubic_estado +
                ", ubic_usua_id=" + ubic_usua_id +
                '}';
    }
}
