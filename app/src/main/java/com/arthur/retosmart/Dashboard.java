package com.arthur.retosmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    // VARIABLES DE FIREBASE
    private FirebaseAuth mAuth;
    private DatabaseReference mDatadatabase;

    // VARIABLES DE INTERFAZ
    private TextView nombre;
    private TextView correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.dashboard_activity);
        mAuth = FirebaseAuth.getInstance();
        mDatadatabase= FirebaseDatabase.getInstance().getReference();
        nombre = (TextView) findViewById(R.id.tv_NombreUsuario);
        correo = (TextView) findViewById(R.id.tv_CorreoUsuario);
        informacionDeUsuario();
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
}
