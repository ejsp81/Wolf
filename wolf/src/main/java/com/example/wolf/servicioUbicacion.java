package com.example.wolf;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class servicioUbicacion extends Service {
    private static final String TAG = "ProyectoFinal";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 3000;
    private static final float LOCATION_DISTANCE = 10f;
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private class LocationListener implements android.location.LocationListener{
        Location mLastLocation;
        String ubicacion="";
        private Context mContext;
        //Preferencias preff;
        int id_usuario;

        public LocationListener (String provider){
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            //aqui se registra
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG,"onProviderChanged"+ provider);
        }
        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG,"onProviderEnabled"+ provider);
        }
        @Override
        public void onProviderDisabled(String provider) {
            if(provider.equals("gps")){
                Log.e(TAG,"onProviderDisabled"+ provider);
            }
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        Log.e(TAG,"onStartComand");
        super.onStartCommand(intent,flags,startId);
        return START_STICKY;
    }
    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        //preff = new Preferencias(getApplicationContext());
        //id_usuario; = preff.misPreferencias.getInt(idUsuario,0);
        initializeLocationManager();
        try{
            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,LOCATION_INTERVAL,LOCATION_DISTANCE,mLocationListeners[0]);
            }else{
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,LOCATION_INTERVAL,LOCATION_DISTANCE,mLocationListeners[1]);
            }
        }catch (java.lang.SecurityException ex){
            Log.i(TAG,"Fallo la obtencion de la ubicacion, ",ex);
        }catch (IllegalArgumentException ex){
            Log.d(TAG,"Provedor de red no existe, "+ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "Fallo en remover los datos de ubicacion , ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager(){
        Log.e(TAG,"inicia la localizacion");
        if(mLocationManager==null){
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }}