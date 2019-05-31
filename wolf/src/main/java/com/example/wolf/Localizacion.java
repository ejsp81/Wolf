package com.example.wolf;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class Localizacion  implements LocationListener, Serializable {

    MenuPrincincipal gps;
    double latitud,longitud;
    String direccion,ciudad;
    Location location;
    Activity activity;
    Context context;


    public Localizacion(Activity activity) {
        this.activity=activity;
        context=activity.getApplicationContext();
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public MenuPrincincipal getGps() {
        return gps;
    }

    public void setGps(MenuPrincincipal gps) {
        this.gps = gps;
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("#################################################--2");
        latitud=location.getLatitude();
        longitud=location.getLongitude();
        this.location=location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(context,
                "El GPS se encuentra activo ", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(context,
                "El GPS no se encuentra activo ", Toast.LENGTH_LONG)
                .show();
    }

    /**
    public void setLocation(){
        if (location.getLongitude()!=0.0 && location.getLongitude()!=0.0){
            latitud=location.getLatitude();
            longitud=location.getLongitude();
            try{
                Geocoder geocoder=new Geocoder(activity, Locale.getDefault());
                List<Address> direcciones=geocoder.getFromLocation(latitud,longitud,1);
                if (!direcciones.isEmpty()){
                    Address dir=direcciones.get(0);
                    direccion=dir.getAddressLine(0)+" Ciudad "+dir.getLocality();
                    ciudad=dir.getLocality()+" "+direccion;
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}
