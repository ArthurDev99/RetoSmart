package com.arthur.retosmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // VARIABLES DE FIREBASE
    private  DatabaseReference mDataBase; // OBTIENE LA REFERENCIA DE LA BASE DE DATOS
    private FirebaseAuth mAuth;

    // VARIABLES PARA OBTENCION DE DATOS DE LA INTERFAZ
    private EditText user;
    private EditText password;

    // VARIABLES PARA DATOS
    private String email = "";
    private String contrasena= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        user = (EditText) findViewById(R.id.et_User);
        password = (EditText) findViewById(R.id.et_Password);
        mAuth = FirebaseAuth.getInstance();

    }

    public void iniciarSesion(View view){
        email = user.getText().toString();
        contrasena = password.getText().toString();
        if(!email.isEmpty() && !contrasena.isEmpty()){
            login();
        }else{
            Toast.makeText(this, "Por favor ingrese los datos correspondientes.",Toast.LENGTH_SHORT).show();
        }
    }

    public void login(){
        mAuth.signInWithEmailAndPassword(email,contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
                    Toast.makeText(getApplicationContext(), "Bienvenido: " + email, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "No se pudo iniciar sesión.: " + email, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void register(View v){
        Intent register = new Intent(this, Register.class);
        startActivity(register);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!=null){ // VALIDA QUE SI EL USUARIO ESTÁ LOGGEADO ENTONCES QUE LO MANDE DIRECTAMENTE AL DASHBOARD
            startActivity(new Intent(this,Dashboard.class));
            finish();
        }
    }
}
