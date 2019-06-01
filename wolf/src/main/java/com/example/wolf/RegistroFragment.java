package com.example.wolf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RegistroFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = "RegistroFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText txtNombres,txtApellidos,txtCelu,txtPassword,txtConfirmarPassword;
    AutoCompleteTextView txtEmail;
    Button btnInsertar;
    Spinner spinner;
    ProgressDialog dialogo;


    public static RegistroFragment newInstance() {
        RegistroFragment fragment = new RegistroFragment();
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroFragment newInstance(String param1, String param2) {
        RegistroFragment fragment = new RegistroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro, null, false);
        txtNombres =(EditText) view.findViewById(R.id.txtNombres);
        txtApellidos =(EditText) view.findViewById(R.id.txtApellidos);
        txtCelu =(EditText) view.findViewById(R.id.txtCelu);
        txtPassword =(EditText) view.findViewById(R.id.txtPassword);
        txtConfirmarPassword =(EditText) view.findViewById(R.id.txtConfirmarPassword);
        txtEmail=(AutoCompleteTextView) view.findViewById(R.id.txtEmail);
        btnInsertar=(Button) view.findViewById(R.id.btnInsertar);
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                new CrearMotero().execute();

            }
        });

        addItemsOnSpinner2(view);
        return  view;
        //return inflater.inflate(R.layout.fragment_registro, container, false);
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner2(View v) {

        spinner = (Spinner) v.findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("SI");
        list.add("NO");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }


    //El fragment se ha adjuntado al Activity
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    //El Fragment ha sido creado
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //La vista de layout ha sido creada y ya está disponible
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //La vista ha sido creada y cualquier configuración guardada está cargada
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    //El Activity que contiene el Fragment ha terminado su creación
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**El Fragment ha sido quitado de su Activity y ya no está disponible
    @Override
    public void onDetach() {
        super.onDetach();
    }*/

    class CrearMotero extends AsyncTask<String, String, String> {
        TransBD tb = new TransBD();
        boolean conexion=true;

        JSONObject json=null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialogo = new ProgressDialog(getContext());
            dialogo.setMessage("Guardando...");
            dialogo.setIndeterminate(false);
            dialogo.setCancelable(true);
            dialogo.show();
            btnInsertar.setEnabled(false);
            super.onPreExecute();
        }



        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String mensaje="";
            // TODO Auto-generated method stub
            String url="http://"+Varios.ip+"/TransBD/transBDUsuarios.php";
            String nombres = txtNombres.getText().toString();
            String apellidos = txtApellidos.getText().toString();
            String email = txtEmail.getText().toString();
            String pass = txtPassword.getText().toString();
            String passC = txtConfirmarPassword.getText().toString();
            String celular=txtCelu.getText().toString();
            String lider=spinner.getSelectedItem().toString();

            tb.llenaTreeMap("transaccion", "insertaMotero");
            tb.llenaTreeMap("nombres", nombres);
            tb.llenaTreeMap("apellidos", apellidos);
            tb.llenaTreeMap("email", email);
            tb.llenaTreeMap("pass", pass);
            tb.llenaTreeMap("passC", passC);
            tb.llenaTreeMap("celular", celular);
            tb.llenaTreeMap("lider", lider);

            if (!tb.buildHTTP(url, "POST")){
                conexion=false;
            }else{
                mensaje=tb.resultadoBDHTTP();
                json=Varios.retornaJason(mensaje);
            }
            return mensaje;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (!conexion) {
                Toast.makeText(getContext(),
                        "No se encuentra la ruta en el servidor", Toast.LENGTH_LONG)
                        .show();
                dialogo.dismiss();
                btnInsertar.setEnabled(true);
                return;
            }
            if (result.trim().equals("faild")) {
                Toast.makeText(getContext(),
                        "No fue posible la conexion con la base de datos", Toast.LENGTH_LONG)
                        .show();
                dialogo.dismiss();
                btnInsertar.setEnabled(true);
                return;
            }
            btnInsertar.setEnabled(true);
            String tag = "success";
            try {
                if (!json.get("faild").equals("not")){
                    if (json.get("faild").equals("23000")) {//valida si hay doble registro
                        Toast.makeText(getContext(), "El correo digitado ya se encuentra en la base de datos", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), json.get(tag).toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                Toast.makeText(getContext(), e1.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("ERROR:::::::: " + e1.getMessage());
                e1.printStackTrace();
            }
            dialogo.dismiss();
        }

    }













}
