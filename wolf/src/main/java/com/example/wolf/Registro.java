package com.example.wolf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Registro extends AppCompatActivity {

    private AutoCompleteTextView txtPrimerNombre,txtSegundoNombre,txtPrimerApellido,txtSegundoApellido,txtEmail;
    private EditText txtPassword,txtConfirmarPassword;
    ProgressDialog dialogo;
    Button btnInsertar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        txtPrimerNombre=(AutoCompleteTextView) findViewById(R.id.txtPrimerNombre);
        txtSegundoNombre=(AutoCompleteTextView) findViewById(R.id.txtSegundoNombre);
        txtPrimerApellido=(AutoCompleteTextView) findViewById(R.id.txtPrimerApellido);
        txtSegundoApellido=(AutoCompleteTextView) findViewById(R.id.txtSegundoApellido);
        txtEmail=(AutoCompleteTextView) findViewById(R.id.txtEmail);

        txtPassword=(EditText) findViewById(R.id.txtPassword);
        txtConfirmarPassword=(EditText) findViewById(R.id.txtConfirmarPassword);

        btnInsertar = (Button) findViewById(R.id.btnInsertar);
        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                new CrearUsuario().execute();

            }
        });

    }



    class CrearUsuario extends AsyncTask<String, String, String> {
        TransBD tb = new TransBD();
        boolean conexion=true;

        JSONObject json=null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialogo = new ProgressDialog(Registro.this);
            dialogo.setMessage("Guardando...");
            dialogo.setIndeterminate(false);
            dialogo.setCancelable(true);
            dialogo.show();
            btnInsertar.setEnabled(false);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String mensaje="";
            // TODO Auto-generated method stub
            String url="http://"+Varios.ip+"/TransBD/transBDUsuarios.php";
            String pNombre = txtPrimerNombre.getText().toString();
            String sNombre = txtSegundoNombre.getText().toString();
            String pApellido = txtPrimerApellido.getText().toString();
            String sApellido = txtSegundoApellido.getText().toString();
            String email = txtEmail.getText().toString();
            String pass = txtPassword.getText().toString();

            tb.llenaTreeMap("transaccion", "insertaUsuario");
            tb.llenaTreeMap("pNombre", pNombre);
            tb.llenaTreeMap("sNombre", sNombre);
            tb.llenaTreeMap("pApellido", pApellido);
            tb.llenaTreeMap("sApellido", sApellido);
            tb.llenaTreeMap("email", email);
            tb.llenaTreeMap("pass", pass);

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
                Toast.makeText(Registro.this,
                        "No se encuentra la ruta en el servidor", Toast.LENGTH_LONG)
                        .show();
                dialogo.dismiss();
                btnInsertar.setEnabled(true);
                return;
            }
            if (result.trim().equals("faild")) {
                Toast.makeText(Registro.this,
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
                        Toast.makeText(Registro.this, "El correo digitado ya se encuentra en la base de datos", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Registro.this, json.get(tag).toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registro.this, MenuPrincincipal.class);
                    intent.putExtra("pNombre",txtPrimerNombre.getText().toString());
                    intent.putExtra("pApellido",txtPrimerApellido.getText().toString());
                    startActivity(intent);
                }
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                Toast.makeText(Registro.this, e1.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("ERROR:::::::: " + e1.getMessage());
                e1.printStackTrace();
            }
            dialogo.dismiss();
        }

    }

}
