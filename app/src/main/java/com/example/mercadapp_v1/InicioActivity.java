package com.example.mercadapp_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InicioActivity extends AppCompatActivity {
private Button btnInformes;
private Button btnHistorico;
private FloatingActionButton newProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        btnInformes = findViewById(R.id.btnInformes);
        btnHistorico = findViewById(R.id.btnHistorico);
        newProduct = findViewById(R.id.floatingActionButton);

        btnInformes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mostrarInformes();
            }
        });
        btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarHistorico();
            }
        });
        newProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevoProducto();
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
        Intent intent = new Intent(this, InformesActivity.class);
        startActivity(intent);
    }

    public void mostrarHistorico(){
        Intent intent = new Intent(this, HistoricoActivity.class);
        startActivity(intent);
    }

    public void nuevoProducto(){
        Intent intent = new Intent(this, NewProductActivity.class);
        startActivity(intent);
    }
}