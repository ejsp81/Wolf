package com.example.wolf;

public class Motero {
    public int mot_id;
    public String mote_email;
    public String mote_password;
    public String mote_nombre;
    public String mote_apellido;
    public String mote_celular ;
    public String mote_lider;

    public Motero(int mot_id, String mote_email, String mote_password, String mote_nombre, String mote_apellido, String mote_celular,String mote_lider){

        this.mot_id = mot_id;
        this.mote_email  = mote_email;
        this.mote_password = mote_password;
        this.mote_nombre = mote_nombre;
        this.mote_apellido = mote_apellido;
        this.mote_celular= mote_celular;
        this.mote_lider = mote_lider;

    }

    public Motero(){

        this.mot_id =0;
        this.mote_email  = "";
        this.mote_password = "";
        this.mote_nombre = "";
        this.mote_apellido = "";
        this.mote_celular= "";
        this.mote_lider = "";
    }

    public int getMot_id() {
        return mot_id;
    }

    public void setMot_id(int mot_id) {
        this.mot_id = mot_id;
    }

    public String getMote_email() {
        return mote_email;
    }

    public void setMote_email(String mote_email) {
        this.mote_email = mote_email;
    }

    public String getMote_password() {
        return mote_password;
    }

    public void setMote_password(String mote_password) {
        this.mote_password = mote_password;
    }

    public String getMote_nombre() {
        return mote_nombre;
    }

    public void setMote_nombre(String mote_nombre) {
        this.mote_nombre = mote_nombre;
    }

    public String getMote_apellido() {
        return mote_apellido;
    }

    public void setMote_apellido(String mote_apellido) {
        this.mote_apellido = mote_apellido;
    }

    public String getMote_celular() {
        return mote_celular;
    }

    public void setMote_celular(String mote_celular) {
        this.mote_celular = mote_celular;
    }

    public String getMote_lider() {
        return mote_lider;
    }

    public void setMote_lider(String mote_lider) {
        this.mote_lider = mote_lider;
    }
}
