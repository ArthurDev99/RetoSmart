package com.arthur.retosmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
//import com.google.firebase.FirebaseApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {


    // VARIABLES PARA LA OBTENCION DE DATOS DESDE LA INTERFAZ4

    private EditText nombre; // Guarda el nombre del usuario
    private EditText apellidos; // Guarda los apellidos del usuario
    private EditText correo; // Guarda el email del usuario
    private EditText contrasena; // guarda la contraseña del usuario
    private EditText confirmContrasena; // Valida que la contrasena sea igual a la ingresada por el usuario


    // VARIABLES DE FIREBASE
    private DatabaseReference mDataBase; // OBTIENE LA REFERENCIA DE LA BASE DE DATOS DE FIREBASE
    private FirebaseAuth mAuth; // VARIABLE PARA ACCEDER AL REGISTRO DE USUARIOS CON AUTENTICACIÓN

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();// Oculta la barra de notificaciones.
        inicializarVariables();
    }

    public void registrarUsuario(View view){
        try {
            // OBTENCIÓN DE DATOS
            final Usuarios user = new Usuarios();
            user.setNombre(nombre.getText().toString());
            user.setApellidos(apellidos.getText().toString());
            user.setCorreo(correo.getText().toString());
            user.setContrasena(contrasena.getText().toString());

            // CREACIÓN DE USUARIO
            mAuth.createUserWithEmailAndPassword(user.getCorreo(),user.getContrasena()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Map<String, Object> map = new HashMap<>();
                        String idUser = mAuth.getCurrentUser().getUid(); // OBTIENE LA ID ASIGANADA POR FIREBASE PARA EL NUEVO USUARIO

                        map.put("Nombre", user.getNombre());
                        map.put("Apellidos",user.getApellidos());
                        map.put("Correo",user.getCorreo());
                        map.put("Contrasena",user.getContrasena());

                        mDataBase.child("Usuarios").child(idUser).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task2) {
                                if(task2.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Registro exitoso.", Toast.LENGTH_SHORT).show();
                                    Intent login = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(login);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Problema al registrar el usuario.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }else{
                        Toast.makeText(getApplicationContext(), "No se pudo realizar el registro.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            // COMPLEMENTOS
            reiniciar();
        }catch (Exception e){
            System.out.println("Error en: " + e);
        Toast.makeText(this,"Algo salió mal",Toast.LENGTH_SHORT).show();
        }
    }

    public void inicializarVariables(){
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        nombre = (EditText) findViewById(R.id.et_Name);
        apellidos = (EditText) findViewById(R.id.et_LastName);
        correo = (EditText) findViewById(R.id.et_Email);
        contrasena = (EditText) findViewById(R.id.et_Password);
        confirmContrasena = (EditText) findViewById(R.id.et_ConfirmPassword);
    }

    public void reiniciar(){
        nombre.setText("");
        apellidos.setText("");
        correo.setText("");
        contrasena.setText("");
        confirmContrasena.setText("");
    }
}
