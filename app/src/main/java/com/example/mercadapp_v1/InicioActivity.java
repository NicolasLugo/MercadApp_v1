package com.example.mercadapp_v1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InicioActivity extends AppCompatActivity {
private Button btnInformes;
private Button btnHistorico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        btnInformes = findViewById(R.id.btnInformes);
        btnHistorico = findViewById(R.id.btnHistorico);

        btnInformes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mostrarInformes();
            }
        });

        /* esta función la usaría en caso tal de tener diferentes tipos de usuario y poder
        darles una bienvenida más única, para eso, tendría que tener un textview en blanco en el que pueda
        colocar el mensaje de bienvenida
        Intent intent = getIntent();
        String nombre = intent.getExtras().getString("nombre");
        -TextView-.setText("Bienvenido, " + nombre);
        */
    }

    public void mostrarInformes(){
    }
}