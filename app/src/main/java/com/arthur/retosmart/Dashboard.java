package com.arthur.retosmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Dashboard extends AppCompatActivity {

    // VARIABLES DE FIREBASE
    private FirebaseAuth mAuth;
    private DatabaseReference mDatadatabase;

    // VARIABLES DE INTERFAZ
    private TextView nombre;
    private TextView correo;
    private  TextView nuevosCasos;
    private TextView totalCasos;
    private TextView nuevosCurados;
    private TextView totalCurados;
    private TextView nuevasMuertes;
    private TextView totalMuertes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.dashboard_activity);
        // METODOS ADICIONALES
        mAuth = FirebaseAuth.getInstance(); // OBTIENE LA REFERENCIA DE LA BASE DE DATOS EN FIREBASE
        mDatadatabase= FirebaseDatabase.getInstance().getReference();
        nombre = (TextView) findViewById(R.id.tv_NombreUsuario);
        correo = (TextView) findViewById(R.id.tv_CorreoUsuario);
        inicializarVariables();
        informacionDeUsuario();
        obtenerDatosDeApi();

    }

    public void cerrarSesion(View view){


        startActivity(new Intent(this, MainActivity.class));
        mAuth.signOut(); // CIERRA LA SESION DEL USUARIO
        finish();

    }

    public void informacionDeUsuario(){
        String id = mAuth.getCurrentUser().getUid().toString();
        mDatadatabase.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    nombre.setText(dataSnapshot.child("Nombre").getValue().toString()); // OBTIENE EL NOMBRE DE USUARIO
                    correo.setText(dataSnapshot.child("Correo").getValue().toString()); // OBTIENE EL CORREO DEL USUARIO
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void obtenerDatosDeApi(){
        String sql = "https://api.covid19api.com/summary";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection con;
        try {
            url=new URL(sql);
            con =( HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLIne;
            StringBuffer response = new StringBuffer();
            String json = "";
            while ((inputLIne = in.readLine()) !=null){
                response.append(inputLIne);
            }

            json = response.toString();
            System.out.println(json);
            JSONObject jsonObject = new JSONObject(json);
            String global = jsonObject.getString("Global");
            jsonObject = new JSONObject(global);


            // LLENAR TEXTVIEW DE INFERFAZ

            // CASOS
            asignarValorDeApi(nuevosCasos,jsonObject.getString("NewConfirmed"));
            asignarValorDeApi(totalCasos,jsonObject.getString("TotalConfirmed"));

            // CURADOS
            asignarValorDeApi(nuevosCurados, jsonObject.getString("NewRecovered"));
            asignarValorDeApi(totalCurados, jsonObject.getString("TotalRecovered"));

            // MUERTES
            asignarValorDeApi(nuevasMuertes, jsonObject.getString("NewDeaths"));
            asignarValorDeApi(totalMuertes, jsonObject.getString("TotalDeaths"));
          //  System.out.println("INFORMACION RECOPILADA: " + jsonObject.getString("NewConfirmed"));
            /*JSONArray jsonar=null;

            jsonar = new JSONArray(json);

            for(int i = 0 ; i < jsonar.length(); i ++){
                JSONObject jsonObject = jsonar.getJSONObject(i);
                Log.d("SLD", "obtenerDatosDeApi: " +jsonObject.optString("Global"));
            }*/


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void asignarValorDeApi(TextView x, String texto){
        x.setText(x.getText().toString() +" "+ texto);
    }

    public void inicializarVariables(){
        nuevosCasos = (TextView) findViewById(R.id.Nuevos_Casos);
        totalCasos = (TextView) findViewById(R.id.Total_Casos);
        nuevosCurados = (TextView) findViewById(R.id.Nuevos_Curados);
        totalCurados = (TextView) findViewById(R.id.Total_Curados);
        nuevasMuertes = (TextView) findViewById(R.id.Nuevas_Muertes);
        totalMuertes = (TextView) findViewById(R.id.Total_Muertes);
    }
}
