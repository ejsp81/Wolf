package com.example.wolf;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.SQLOutput;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import static android.content.Context.NOTIFICATION_SERVICE;


public class MapsFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String LOGTAG = "android-localizacion";

    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;

    private GoogleMap mMap;

    private static final String TAG = MapsFragment.class.getName();
    double latitud = 0.0, longitud = 0.0;
    public LatLng ubicacion;
    String direccion, ciudad, ciudadn;
    TreeMap<Integer, Marker> marcadores = new TreeMap<Integer, Marker>();
    TreeMap<Integer, String> marcadoresAuxiliar = new TreeMap<Integer, String>();
    TreeMap<String, String> marcadoresTelefono = new TreeMap<String, String>();

    private static final int RC_LOCATION_PERMISION = 100;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationCallback locationCallback;
    private static int INTERVAL = 5000;
    private static int FAST_INTERVAL = 5000;
    private FusedLocationProviderClient mFusedLocationClient;
    private ToggleButton btnActualizar;

    private boolean mRequestingLocationUpdates = false;
    private Location mCurrentLocation;
    private boolean mAnimacamara = false;

    MarkerOptions markerOptions = new MarkerOptions();
    LatLng miubicacion;
    String miUbicacionF = "No medida";
    boolean rodando = false;
    public static final String CHANNEL_ID = "com.chikeandroid.tutsplustalerts.ANDROID";


    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        new IngresaMapa().execute();
        View view = inflater.inflate(R.layout.fragment_maps, null, false);

        //Solicitar permisos si es necesario (Android 6.0+)
        requestPermissionIfNeedIt();
        btnActualizar = (ToggleButton) view.findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLocationUpdates(btnActualizar.isChecked());
            }
        });
        miubicacion = new LatLng(latitud, longitud);


        //Inicializar el GoogleAPIClient y armar la Petición de Ubicación
        initGoogleAPIClient();


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        createLocationRequest();

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    public void iniciaLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        latitud = location.getLatitude();
                        longitud = location.getLongitude();
                        miubicacion=new LatLng(latitud,longitud);
                        if (longitud != 0.0 && !mAnimacamara) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 15.0f));
                            mAnimacamara = true;
                        }
                        System.out.println();
                        System.out.println(String.format(Locale.US, "%s -- %s", location.getLatitude(), location.getLongitude()));
                    }
                }
            }
        };

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void toggleLocationUpdates(boolean enable) {
        if (enable) {
            enableLocationUpdates();
            rodando = true;
            System.out.println("----------------------------------------Comenze a rodar");

            // Create an explicit intent for an Activity in your app
            Intent intent = new Intent(getActivity(), Registro.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_email)
                    .setContentTitle("A rodar")
                    .setContentText("Ahora estas participando de la rodada...")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Ahora estas participando de la rodadas..."))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[] {100, 250, 100, 500})
                    .setAutoCancel(false);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

// notificationId is a unique int for each notification that you must define
            notificationManager.notify(1, builder.build());
        } else {
            disableLocationUpdates();
            rodando = false;
        }
    }

    private void disableLocationUpdates() {
        if (mFusedLocationClient != null) {
            try {
                final Task<Void> voidTask = mFusedLocationClient.removeLocationUpdates(locationCallback);
                if (voidTask.isSuccessful()) {
                    Log.d(TAG, "StopLocation updates successful! ");
                } else {
                    Log.d(TAG, "StopLocation updates unsuccessful! " + voidTask.toString());
                }
            } catch (SecurityException exp) {
                Log.d(TAG, " Security exception while removeLocationUpdates");
            }
        }
        //mFusedLocationClient.removeLocationUpdates(locationCallback);


    }

    private void enableLocationUpdates() {
        createLocationRequest();
        LocationSettingsRequest locSettingsRequest =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(mLocationRequest)
                        .build();


        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient, locSettingsRequest);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                System.out.println("+++++++++++++++++++++++++++++++++" + status.getStatusCode());
                System.out.println("+++++++++++++++++++++++++++++++++" + status.getStatusMessage());
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.i(LOGTAG, "Configuración correcta");
                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            Log.i(LOGTAG, "Se requiere actuación del usuario");
                            status.startResolutionForResult(getActivity(), PETICION_CONFIG_UBICACION);

                            System.out.println("+++++++++++++++++++++++++Hola estoy esperando configuracion");
                        } catch (IntentSender.SendIntentException e) {
                            btnActualizar.setChecked(false);
                            Log.i(LOGTAG, "Error al intentar solucionar configuración de ubicación");
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(LOGTAG, "No se puede cumplir la configuración de ubicación necesaria");
                        btnActualizar.setChecked(false);
                        break;
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("*********************************" + resultCode);
        System.out.println("**********************" + Activity.RESULT_OK + "," + Activity.RESULT_CANCELED + "," + Activity.RESULT_FIRST_USER);
        switch (requestCode) {
            case PETICION_CONFIG_UBICACION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        System.out.println("¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨eche nojoda casi que no");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "El usuario no ha realizado los cambios de configuración necesarios");
                        btnActualizar.setChecked(false);
                        break;
                }
                break;
        }
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
    /**
     * @Override public void onStart() {
     * super.onStart();
     * if (mGoogleApiClient != null) {
     * if (mGoogleApiClient.isConnected())
     * startLocationUpdates();
     * else
     * mGoogleApiClient.connect();
     * }
     * }
     */


    @Override
    public void onStop() {
        super.onStop();
        System.out.println("se ha suspendido la aplicacion");
        stopLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Instanciar GoogleApiClient
     */
    private void initGoogleAPIClient() {
        System.out.println("***********************************Hola etoy inciando googleapiclietn");

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)//perminte saber si nos conectamos con el api
                    .addOnConnectionFailedListener(this)//permite saber si el intento de conexion ha fallado
                    .addApi(LocationServices.API)
                    .enableAutoManage(getActivity(), this)
                    .build();

            //Creamos una peticion de ubicacion con el objeto LocationRequest

        }
    }

    /**
     * Instanciar LocationRequest
     */
    protected void createLocationRequest() {
        System.out.println("***********************************Crree la location requie");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FAST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                // LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates();
                iniciaLocationCallback();
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null);
                mRequestingLocationUpdates = true;
                rodando = true;
            }
        }
    }

    public LatLng ubicacion() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            // LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates();
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%entre a buscar la aplicacion");
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations, this can be null.
                            if (location != null) {
                                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%Se pudo conectar");
                                latitud = location.getLatitude();
                                longitud = location.getLongitude();
                                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + location.getLatitude());
                                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + location.getLongitude());
                            } else {
                                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%nooooo se pudo conectar");
                            }
                        }
                    });
        }
        return new LatLng(latitud, longitud);
    }

    private void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            System.out.println("°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°tambien se suspendio");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
            mGoogleApiClient.disconnect();
            mRequestingLocationUpdates = false;
        }

    }

    private void requestPermissionIfNeedIt() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RC_LOCATION_PERMISION);
        }
    }

    private void refreshUI() {
        if (mCurrentLocation != null) {
            System.out.println("---------------Latitud: " + String.valueOf(mCurrentLocation.getLatitude()));
            System.out.println("---------------Longitud: " + String.valueOf(mCurrentLocation.getLongitude()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permiso concedido
                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                startLocationUpdates();
                updateUI(lastLocation);
            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.
                requestPermissionIfNeedIt();
                Log.e(LOGTAG, "Permiso denegado");
            }
        }
        /**
         if (requestCode == RC_LOCATION_PERMISION) {
         if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
         startLocationUpdates();
         } else {
         requestPermissionIfNeedIt();
         }
         }*/
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "***********************************onConnected");
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            updateUI(lastLocation);
        }
        if (!mRequestingLocationUpdates) {
            //startLocationUpdates();
        }

    }

    private void updateUI(Location loc) {
        if (loc != null) {
            latitud=loc.getLatitude();
            longitud=loc.getLongitude();
            System.out.println("Latitud: " + String.valueOf(loc.getLatitude()));
            System.out.println("Longitud: " + String.valueOf(loc.getLongitude()));
        } else {
            System.out.println("Latitud: (desconocida)");
            System.out.println("Longitud: (desconocida)");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
        Log.e(TAG, "***********************************onConnectionSuspended");
        Toast.makeText(getContext(), getString(R.string.app_name), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOGTAG, "Error grave al conectar con Google Play Services");
        Log.e(TAG, "***********************************onConnectionFailed" + connectionResult.getErrorMessage());

    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Acualizado -------------" + String.valueOf(location.getLatitude()));
        System.out.println("Acualizado -------------" + String.valueOf(location.getLongitude()));
        refreshUI();

        Log.i(LOGTAG, "Recibida nueva ubicación!");

        //Mostramos la nueva ubicación recibida
        updateUI(location);
        //latitud=arg0.getLatitude();
        //longitud=arg0.getLongitude();
        //LatLng miubicacion = new LatLng(arg0.getLatitude(), arg0.getLongitude());
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(miubicacion, 15.0f));

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getContext(),
                "El GPS se encuentra activo ", Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getContext(),
                "El GPS no se encuentra activo ", Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("====================================06");

        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        //mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);


        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
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
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(miubicacion));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(miubicacion, 15.0f));
        System.out.println("====================================07");

        Log.i("Esta es la latitud", latitud + "y la  longiud" + longitud + "--------------------------------");
        int cont = 0;
        // first create a maker array
        //List<Marker> markers = new ArrayList<Marker>();

        // inside doInBackground() add the markers into the list from JSON
        //Marker markerl = mMap.addMarker(new MarkerOptions().position(new LatLng(latitud, longitud)));
        //markers.add(markerl);

        System.out.println("{{{{{{{{{{{{{{{{{{{{" + Varios.ubicacionList);

        LatLng ubic = null;
        if (Varios.ubicacionList != null) {
            for (Ubicacion ubicacion : Varios.ubicacionList) {
                ubic = new LatLng(ubicacion.ubic_latitud, ubicacion.ubic_longitud);
                markerOptions.title(Varios.usuarioList.get(cont).getUsua_primer_nombre() + " " + Varios.usuarioList.get(cont).getUsua_primer_apellido());

                markerOptions.position(ubic);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_moto));
                markerOptions.snippet(muestraDistanci(ubic));
                marcadores.put(ubicacion.getUbic_usua_id(), mMap.addMarker(markerOptions));
                marcadoresTelefono.put(marcadores.get(ubicacion.getUbic_usua_id()).getId(), Varios.usuarioList.get(cont).getUsua_id() + "");
                marcadoresAuxiliar.put(ubicacion.getUbic_usua_id(), Varios.usuarioList.get(cont).getUsua_primer_nombre() + " " + Varios.usuarioList.get(cont).getUsua_primer_apellido());
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + ubicacion.getUbic_usua_id());
                //mMap.addMarker(new MarkerOptions().position(miubicacion)
                //        .title(Varios.usuarioList.get(cont).usua_primer_nombre)
                //        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)).snippet("Esta vaina que es"));
                //Log.i("Contact Details", contact.ubic_latitud + "-" + contact.ubic_longitud + "-" );
                //System.out.println("------------------------------------------------------------------------------------------");
                //LatLng miubicacion = new LatLng(contact.ubic_latitud, contact.ubic_longitud);
                // mMap.addMarker(new MarkerOptions().position(miubicacion).title("Santiago de Cali"));
                cont++;
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubic, 15.0f));
            new ActualizaUbicacion().execute();
        } else {
            Toast.makeText(getContext(),
                    "No se encuentra ningun participante en la rodada", Toast.LENGTH_LONG)
                    .show();
        }
        cont = 0;
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent i = new Intent(Intent.ACTION_CALL);
                //Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:3166034338"));
                startActivity(i);
                Toast.makeText(
                        getActivity(),
                        "Marcador pulsado:\n" +
                                marker.getTitle() + "\n" +
                                marker.getId() + "\n" +
                                marcadoresTelefono.get(marker.getId()),
                        Toast.LENGTH_SHORT).show();

            }
        });


    }


    @Override
    public void onMapClick(LatLng latLng) {
        /**
         Toast.makeText(getContext(),"Nueva ubicacion",Toast.LENGTH_LONG).show();
         try{
         Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());
         List<Address> direcciones=geocoder.getFromLocation(latitud,longitud,1);
         if (!direcciones.isEmpty()){
         Address dir=direcciones.get(0);
         direccion=dir.getAddressLine(0)+" Ciudad "+dir.getLocality();
         ciudad=dir.getLocality();
         //TextView txtubicacion=(TextView) getView().findViewById(R.id.txtubicacion);
         //txtubicacion.setText(direccion);
         }
         }catch(IOException e) {
         e.printStackTrace();
         }
         mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).
         title(ciudadn).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));*/
    }

    public String muestraDistanci(LatLng dos) {
        if (rodando) {
           return "Distancia: " + Varios.calculaDistancia(miubicacion, dos);
        } else {
           return "Distancia: " + miUbicacionF;
        }
    }


    class ActualizaUbicacion extends AsyncTask<String, String, String> {

        private boolean continuar;
        TransBD tb;
        boolean conexion = true;
        String data = "";
        String url;
        int cont = 1;

        public ActualizaUbicacion() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tb = new TransBD();
            Varios.ubicacionList = null;
            Varios.usuarioList = null;
            url = "http://" + Varios.ip + "/TransBD/transBDUsuarios.php";
            continuar = true;
        }

        @Override
        protected String doInBackground(String... strings) {

            while (continuar) {
                try {

                    Thread.sleep(5000);
                    tb.llenaTreeMap("transaccion", "ingresaMapa");
                    int usua=0;
                    if (rodando){
                        usua=Varios.usuarioLogueado.getUsua_id();
                        tb.llenaTreeMap("ubic_latitud", latitud);
                        tb.llenaTreeMap("ubic_longitud", longitud);
                        tb.llenaTreeMap("ubic_estado", 1);
                    }
                    System.out.println("xxxxxxxxxxxxxxxxxxxxxxxEstoy enviando el "+usua);
                    tb.llenaTreeMap("ubic_usua_id", usua);
                    if (!tb.buildQueryURL(url, "POST")) {
                        System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;fallo la conexion maps");
                        conexion = false;
                    } else {
                        data = tb.resultadoBDURL();
                        if (!data.equalsIgnoreCase("")) {
                            try {
                                String d = data.trim();
                                if (!d.equals("no found")) {
                                    //JSONObject json = Varios.retornaJason(result);
                                    System.out.println("¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿"+Varios.retornaJason(data).optJSONArray("nuevo"));
                                    JSONArray jsonArray = Varios.retornaJason(data).optJSONArray("ubicacion");
                                    JSONObject jsonArrayChild = jsonArray.getJSONObject(0);
                                    Gson gson = new Gson();
                                    Type typeUbi = new TypeToken<List<Ubicacion>>() {
                                    }.getType();
                                    Type typeUsu = new TypeToken<List<Usuario>>() {
                                    }.getType();
                                    Varios.ubicacionList = gson.fromJson(jsonArray.toString(), typeUbi);
                                    Varios.usuarioList = gson.fromJson(Varios.retornaJason(data).optJSONArray("usuario").toString(), typeUsu);

                                } else {
                                    Varios.ubicacionList = null;
                                    Varios.usuarioList = null;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    publishProgress();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            int cont = 0;
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++Voy a actualizar");
            marcadoresAuxiliar = new TreeMap<Integer, String>();
            if (Varios.ubicacionList != null) {
                for (Ubicacion ubicacion : Varios.ubicacionList) {
                    LatLng ubic = new LatLng(ubicacion.ubic_latitud, ubicacion.ubic_longitud);
                    marcadores.get(ubicacion.getUbic_usua_id()).setVisible(true);
                    marcadoresAuxiliar.put(ubicacion.getUbic_usua_id(), Varios.usuarioList.get(cont).getUsua_primer_nombre() + " " + Varios.usuarioList.get(cont).getUsua_primer_apellido());
                    System.out.println("-------------- " + ubicacion.getUbic_usua_id());
                    System.out.println("°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°" + marcadores.get(ubicacion.getUbic_usua_id()));
                    if (marcadores.get(ubicacion.getUbic_usua_id()) == null) {
                        System.out.println("############################################Encontré uno nuevo y lo voy a agregar");
                        markerOptions.title(Varios.usuarioList.get(cont).getUsua_primer_nombre() + " " + Varios.usuarioList.get(cont).getUsua_primer_apellido());
                        markerOptions.snippet(muestraDistanci(ubic));
                        markerOptions.position(ubic);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_moto));
                        marcadores.put(ubicacion.getUbic_usua_id(), mMap.addMarker(markerOptions));
                    } else {
                        marcadores.get(ubicacion.getUbic_usua_id()).setPosition(ubic);
                        marcadores.get(ubicacion.getUbic_usua_id()).setSnippet(muestraDistanci(ubic));
                        System.out.println(":::::::::::::::::::::::::::::::::::::::::::.Movi el mapa de: " + Varios.usuarioList.get(cont).getUsua_primer_nombre() + " " + Varios.usuarioList.get(cont).getUsua_primer_apellido());
                    }
                    System.out.println("************************************  " + cont);
                    cont++;
                }
                cont = 0;

                Iterator iterator = marcadores.keySet().iterator();//funcion del treemap para recorrerlo
                Marker mk = null;
                System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////////");
                while (iterator.hasNext()) {
                    Object clave = iterator.next();
                    System.out.println("(((((((((" + clave + "===>" + marcadoresAuxiliar.get(clave));
                    if (marcadoresAuxiliar.get(clave) == null || marcadoresAuxiliar.get(clave).equals("")) {
                        System.out.println("===================================> " + marcadores.get(clave).getTitle() + " salio de la rodada");
                        marcadores.get(clave).setVisible(false);
                        //marcadores.remove(clave);
                    }
                }
                System.out.println("///////////////////////////////////////////////////////////////////////////////////////////////////////");
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++Ya actualice");
            } else {
                System.out.println("No Hay participantes");
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            continuar = false;
        }
    }

    class IngresaMapa extends AsyncTask<String, String, String> {
        TransBD tb;
        boolean conexion = true;
        String data = "";
        String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tb = new TransBD();
            Varios.ubicacionList = null;
            Varios.usuarioList = null;
            url = "http://" + Varios.ip + "/TransBD/transBDUsuarios.php";
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                //local.getGps().lanzarLocalizacion();
                tb.llenaTreeMap("transaccion", "ingresaMapa");
                int usua=0;
                if (rodando){
                    usua=Varios.usuarioLogueado.getUsua_id();
                    tb.llenaTreeMap("ubic_latitud", latitud);
                    tb.llenaTreeMap("ubic_longitud", longitud);
                }
                tb.llenaTreeMap("ubic_usua_id", usua);
                if (!tb.buildQueryURL(url, "POST")) {
                    System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;fallo la conexion maps");
                    conexion = false;
                } else {
                    data = tb.resultadoBDURL();
                    if (!data.equalsIgnoreCase("")) {
                        try {
                            String d = data.trim();
                            if (!d.equals("no found")) {
                                //JSONObject json = Varios.retornaJason(result);
                                JSONArray jsonArray = Varios.retornaJason(data).optJSONArray("ubicacion");
                                JSONObject jsonArrayChild = jsonArray.getJSONObject(0);
                                Gson gson = new Gson();
                                Type typeUbi = new TypeToken<List<Ubicacion>>() {
                                }.getType();
                                Type typeUsu = new TypeToken<List<Usuario>>() {
                                }.getType();
                                Varios.ubicacionList = gson.fromJson(jsonArray.toString(), typeUbi);
                                Varios.usuarioList = gson.fromJson(Varios.retornaJason(data).optJSONArray("usuario").toString(), typeUsu);
                                for (Ubicacion ubicacion : Varios.ubicacionList) {
                                    Log.i("Ubicacion", ubicacion.ubic_latitud + "-" + ubicacion.ubic_longitud + "-");
                                    System.out.println("------------------------------------------------------------------------------------------");
                                    //LatLng miubicacion = new LatLng(contact.ubic_latitud, contact.ubic_longitud);
                                    // mMap.addMarker(new MarkerOptions().position(miubicacion).title("Santiago de Cali"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (Exception e) {
                conexion = false;
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }


}
