package com.example.mercadapp_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView btnNewUser;
    private EditText txtUsuario;
    private EditText txtContrasena;
    private Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnNewUser = findViewById(R.id.btnNewUser);

        btnIngresar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                validarLogin();
            }
        });
        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevoUsuario();
            }
        });
    }

    public void validarLogin(){
        //Se obtienen los valores ingresados por el usuario
        String user = txtUsuario.getText().toString();
        String passw = txtContrasena.getText().toString();

        //Registro de inicio de sesión de prueba
        String validUser = "admin";
        String validPassw = "1234";

        //Se comparan ambos valores
        if(user.equals(validUser) && passw.equals(validPassw)){
            //Se inicia la nueva actividad si los valores son iguales
            Intent intent = new Intent(this, InicioActivity.class);

            /*usaría esta línea cuando tuviera varios usuarios y así tener un inicio de sesión
            para cada uno, usando los datos de ingreso para darles la bienvenida
            intent.putExtra("nombre", user);
            */

            startActivity(intent);
        }else{
            //Se muestra un mensaje de error
            Toast.makeText(this,"Datos incorrectos, intente nuevamente", Toast.LENGTH_SHORT).show();
        }

    }
    public void nuevoUsuario(){
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }
}