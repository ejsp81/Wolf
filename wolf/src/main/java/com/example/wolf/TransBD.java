package com.example.wolf;

import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class TransBD {
    Uri.Builder builder;
    TreeMap<Object, Object> treeMap;
    HttpURLConnection conn;
    InputStream is = null;

    public TransBD(Uri.Builder builder, TreeMap<Object, Object> treeMap) {
        this.builder = builder;
        this.treeMap = treeMap;
    }

    public TransBD(Uri.Builder builder, TreeMap<Object, Object> treeMap, HttpURLConnection conn) {
        this.builder = builder;
        this.treeMap = treeMap;
        this.conn = conn;
    }

    public TransBD() {
        this.builder = new Uri.Builder();
        this.treeMap = new TreeMap<Object, Object>();
        this.conn = null;
    }

    public Uri.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(Uri.Builder builder) {
        this.builder = builder;
    }

    public TreeMap<Object, Object> getTreeMap() {
        return treeMap;
    }

    public void setTreeMap(TreeMap<Object, Object> treeMap) {
        this.treeMap = treeMap;
    }


    public void connectionURL(String urlBD, String method) {
        try {
            URL url = new URL(urlBD);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //conn.connect();
            //int responseCode = conn.getResponseCode();
            //return responseCode==HttpURLConnection.HTTP_OK?true:false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean buildQueryURL(String url,String method) {
        try {
            connectionURL(url,method);
            Iterator iterator = treeMap.keySet().iterator();//funcion del treemap para recorrerlo
            iterator = treeMap.keySet().iterator();
            while (iterator.hasNext()) {
                Object clave = iterator.next();
                builder.appendQueryParameter(clave.toString(), treeMap.get(clave).toString());
            }
            String query = builder.build().getEncodedQuery();
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK ? true : false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String resultadoBDURL() {
        String data="" ;
        try {
            is = conn.getInputStream();
            data = Varios.convertStreamToString(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public void llenaTreeMap(Object clave, Object valor) {
        treeMap.put(clave, valor);
    }

    public List<NameValuePair> parametrosBD() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Iterator iterator = treeMap.keySet().iterator();//funcion del treemap para recorrerlo
        iterator = treeMap.keySet().iterator();
        while (iterator.hasNext()) {
            Object clave = iterator.next();
            params.add(new BasicNameValuePair(clave.toString(), treeMap.get(clave).toString()));
        }
        return params;
    }

    public boolean buildHTTP(String url, String method){
        HttpResponse httpResponse=null;
        try {
            // check for request method
            if(method == "POST"){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(parametrosBD()));
                httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }else if(method == "GET"){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(parametrosBD(), "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
            return httpResponse.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK ? true : false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String resultadoBDHTTP() {
        JSONObject jObj=null;
        String data="" ;
        try {
            data = Varios.convertStreamToString(is);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return data;
    }



}
