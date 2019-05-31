package com.example.wolf;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSActivyty extends AppCompatActivity implements View.OnClickListener {

    String ubicacion,direccion,ciudad;
    double latitud,longitud;
    TextView txtubicacion;
    Button verMapa;

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    LocationManager locationManager;
    Localizacion local;
    static final int MY_PERMISSIONS_REQUEST_ACCES_FINE_LOCATION=1;
    @RequiresApi(api= Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        txtubicacion=(TextView)findViewById(R.id.txtubicacion);
        verMapa =(Button)findViewById(R.id.verMapa);
        verMapa.setOnClickListener(this);
        lanzarLocalizacion();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(GPSActivyty.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCES_FINE_LOCATION);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,local);
        txtubicacion.setText("Ubicacion agregada");

    }

    public void setLocation(Location loc){
        if (loc.getLongitude()!=0.0 && loc.getLongitude()!=0.0){
            latitud=loc.getLatitude();
            longitud=loc.getLongitude();
            try{
                Geocoder geocoder=new Geocoder(this, Locale.getDefault());
                List<Address> direcciones=geocoder.getFromLocation(latitud,longitud,1);
                if (!direcciones.isEmpty()){
                    Address dir=direcciones.get(0);
                    direccion=dir.getAddressLine(0)+" Ciudad "+dir.getLocality();
                    ciudad=dir.getLocality();
                    txtubicacion.setText(direccion);
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent i =new Intent(this,Navegacion.class);
        i.putExtra("longitud",String.valueOf(longitud));
        i.putExtra("latitud",String.valueOf(latitud));
        i.putExtra("ciudad",ciudad);
        startActivity(i);
    }

    public void lanzarLocalizacion(){
        locationManager=(LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        //local.setGps(this);
    }
}

