package com.example.wolf;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Navegacion extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    double latitud = 0, longitud = 0;
    String direccion, ciudad, ciudadn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacion);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Bundle extras = getIntent().getExtras();
        System.out.println("hooooooooooooooooooooooooooooooooooooooooooooooola");
        try {
            latitud = Double.parseDouble(extras.getString("latitud"));
            longitud = Double.parseDouble(extras.getString("longitud"));
            ciudad = extras.getString("ciudad");
            System.out.println("****************************" + ciudad);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng miubicacion = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions().position(miubicacion).title(ciudad));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(miubicacion));
    }


    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(this,"Nueva ubicacion",Toast.LENGTH_LONG).show();
        try{
            Geocoder geocoder=new Geocoder(this, Locale.getDefault());
            List<Address> direcciones=geocoder.getFromLocation(latitud,longitud,1);
            if (!direcciones.isEmpty()){
                Address dir=direcciones.get(0);
                direccion=dir.getAddressLine(0)+" Ciudad "+dir.getLocality();
                ciudad=dir.getLocality();
                TextView txtubicacion=(TextView) findViewById(R.id.txtubicacion);
                txtubicacion.setText(direccion);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).title(ciudadn).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }
}
