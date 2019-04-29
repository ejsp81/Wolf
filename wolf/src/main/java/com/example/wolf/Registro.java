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

import com.example.wolf.R;

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
    JSONParser jsonparser = new JSONParser();
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
                new CrearProducto().execute();

            }
        });

    }



    class CrearProducto extends AsyncTask<String, String, String> {

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
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("transaccion", "insertaUsuario"));
            params.add(new BasicNameValuePair("pNombre", pNombre));
            params.add(new BasicNameValuePair("sNombre", sNombre));
            params.add(new BasicNameValuePair("pApellido", pApellido));
            params.add(new BasicNameValuePair("sApellido", sApellido));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("pass", pass));

            json = jsonparser.makeHttpRequest(url, "POST", params);

            return mensaje;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            btnInsertar.setEnabled(true);
            String tag = "success";
            try {
                if (!json.get("faild").equals("not")){
                    if (json.get("faild").equals("23000")) {
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
