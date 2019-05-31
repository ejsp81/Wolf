package com.example.wolf;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MenuPrincincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtNombre;

    static final int MY_PERMISSIONS_REQUEST_ACCES_FINE_LOCATION=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_princincipal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //con esto generamos el usuario en el header del menu-------------------------------
        View hView = navigationView.getHeaderView(0);
        txtNombre=(TextView) hView.findViewById(R.id.textView);
        String nombre=getIntent().getStringExtra("pNombre")+" "+getIntent().getStringExtra("pApellido");
        System.out.println(nombre);

        txtNombre.setText(nombre);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MenuPrincincipal.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCES_FINE_LOCATION);
            return;
        }
    }
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_princincipal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Creamos un nuevo Bundle
        //Una vez haz creado tu instancia de TestFragment y colocado el Bundle entre sus argumentos, usas el FragmentManager para iniciarla desde tu segunda actividad.
        //FragmentManager fm = getFragmentManager();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

            //fm.beginTransaction().replace(R.id.escenario,new MapsFragment()).commit();
            //local=new Localizacion(MenuPrincincipal.this);
            //System.out.println("¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡"+local.getDireccion());



            //Bundle args = new Bundle();
            //args.putSerializable("local",local);

            // Colocamos el String
            //args.putString("latitud", Varios.latitud+"");
            //args.putString("longitud", Varios.longitud+"");
            //args.putString("ciudad", ciudad);
            Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.escenario);
            if (mFragment instanceof MapsFragment) {
            }else{
                FragmentManager fm = getSupportFragmentManager();

                // Supongamos que tu Fragment se llama TestFragment. Colocamos este nuevo Bundle como argumento en el fragmento.
                MapsFragment newFragment = new MapsFragment();
                //newFragment.setArguments(args);
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.escenario, newFragment);
                fragmentTransaction.commit();
            }

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setLocation(Location loc){
        if (loc.getLongitude()!=0.0 && loc.getLongitude()!=0.0){
            Varios.latitud=loc.getLatitude();
            Varios.longitud=loc.getLongitude();
            try{
                Geocoder geocoder=new Geocoder(this, Locale.getDefault());
                List<Address> direcciones=geocoder.getFromLocation(Varios.latitud,Varios.longitud,1);
                if (!direcciones.isEmpty()){
                    Address dir=direcciones.get(0);
                    //direccion=dir.getAddressLine(0)+" Ciudad "+dir.getLocality();
                    //ciudad=dir.getLocality()+" "+direccion;
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }


}
