package com.example.wolf;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.List;

public class Varios {

    public static String ip="10.10.33.148";
    public static Usuario usuarioLogueado;
    public static List<Ubicacion> ubicacionList=null;
    public static List<Usuario> usuarioList=null;
    public static double latitud,longitud;
    public static NetworkInfo networkInfo;

    public static String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    public static JSONObject retornaJason(String data) {
        try {
            return new JSONObject(data);
        } catch (JSONException e) {
            System.out.println("JSON Parser Error parsing data " + e.toString());
            return null;
        }
    }

    public static Object retornaObjeto(Object objeto, String[] informacion) {
        if (informacion != null) {
            try {
                Class cla = objeto.getClass();
                Class clas = Class.forName(cla.getName());
                Field[] fields = clas.getDeclaredFields();
                int i = 0;
                for (Field f : fields) {
                    f.setAccessible(true);
                    String tipo = f.getType().getName();
                    if (tipo.equals("")) {
                        i++;
                        continue;
                    }
                    switch (tipo) {
                        case "char":
                            fields[i].setChar(objeto, informacion[i].charAt(0));
                            break;
                        case "long":
                            fields[i].setLong(objeto, Long.parseLong(informacion[i]));
                            break;
                        case "int":
                            fields[i].setInt(objeto, Integer.parseInt(informacion[i]));
                            break;
                        case "double":
                            fields[i].setDouble(objeto, Double.parseDouble(informacion[i]));
                            break;
                        default:
                            fields[i].set(objeto, informacion[i]);
                    }
                    i++;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return objeto;
        } else {
            return null;
        }
    }

    public static double calculaDistancia(LatLng ubi1, LatLng ubi2){
        Location location = new Location("localizacion 1");
        location.setLatitude(ubi1.latitude);  //latitud
        location.setLongitude(ubi1.longitude); //longitud
        Location location2 = new Location("localizacion 2");
        location2.setLatitude(ubi2.latitude);  //latitud
        location2.setLongitude(ubi2.longitude); //longitud
        return location.distanceTo(location2);
    }

    public static boolean verivicaConexion(Activity activity){
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }



}
