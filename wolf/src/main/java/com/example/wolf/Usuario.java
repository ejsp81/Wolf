package com.example.wolf;

public class Usuario {

    public int usua_id;
    public String usua_email;
    public String usua_primer_nombre;
    public String usua_segundo_nombre;
    public String usua_primer_apellido;
    public String usua_segundo_apellido ;
    public String usua_password;

    public Usuario(int usua_id, String usua_email, String usua_primer_nombre, String usua_segundo_nombre, String usua_primer_apellido, String usua_segundo_apellido, String usua_password) {
        this.usua_id = usua_id;
        this.usua_email = usua_email;
        this.usua_primer_nombre = usua_primer_nombre;
        this.usua_segundo_nombre = usua_segundo_nombre;
        this.usua_primer_apellido = usua_primer_apellido;
        this.usua_segundo_apellido = usua_segundo_apellido;
        this.usua_password = usua_password;
    }

    public Usuario() {
        this.usua_id = 0;
        this.usua_email = "";
        this.usua_primer_nombre = "";
        this.usua_segundo_nombre = "";
        this.usua_primer_apellido = "";
        this.usua_segundo_apellido = "";
        this.usua_password = "";
    }

    public int getUsua_id() {
        return usua_id;
    }

    public void setUsua_id(int usua_id) {
        this.usua_id = usua_id;
    }

    public String getUsua_email() {
        return usua_email;
    }

    public void setUsua_email(String usua_email) {
        this.usua_email = usua_email;
    }

    public String getUsua_primer_nombre() {
        return usua_primer_nombre;
    }

    public void setUsua_primer_nombre(String usua_primer_nombre) {
        this.usua_primer_nombre = usua_primer_nombre;
    }

    public String getUsua_segundo_nombre() {
        return usua_segundo_nombre;
    }

    public void setUsua_segundo_nombre(String usua_segundo_nombre) {
        this.usua_segundo_nombre = usua_segundo_nombre;
    }

    public String getUsua_primer_apellido() {
        return usua_primer_apellido;
    }

    public void setUsua_primer_apellido(String usua_primer_apellido) {
        this.usua_primer_apellido = usua_primer_apellido;
    }

    public String getUsua_segundo_apellido() {
        return usua_segundo_apellido;
    }

    public void setUsua_segundo_apellido(String usua_segundo_apellido) {
        this.usua_segundo_apellido = usua_segundo_apellido;
    }

    public String getUsua_password() {
        return usua_password;
    }

    public void setUsua_password(String usua_password) {
        this.usua_password = usua_password;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "usua_id=" + usua_id +
                ", usua_email='" + usua_email + '\'' +
                ", usua_primer_nombre='" + usua_primer_nombre + '\'' +
                ", usua_segundo_nombre='" + usua_segundo_nombre + '\'' +
                ", usua_primer_apellido='" + usua_primer_apellido + '\'' +
                ", usua_segundo_apellido='" + usua_segundo_apellido + '\'' +
                ", usua_password='" + usua_password + '\'' +
                '}';
    }
}
