package com.example.wolf;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.TreeMap;

public class Varios {

    public static String ip="10.10.33.148";

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


}
