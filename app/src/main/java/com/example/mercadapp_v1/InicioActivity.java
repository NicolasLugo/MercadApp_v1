package com.example.mercadapp_v1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InicioActivity extends AppCompatActivity {
private Button btnInformes, btnHistorico, btnUsuarios;
private FloatingActionButton newProduct;
private TextView tvUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        btnInformes = findViewById(R.id.btnInformes);
        btnHistorico = findViewById(R.id.btnHistorico);
        btnUsuarios = findViewById(R.id.btnUsuarios);
        newProduct = findViewById(R.id.floatingActionButton);
        tvUser = findViewById(R.id.tvUser);

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

        btnUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarUsuarios();
            }
        });

        Intent i = getIntent();
        String userName = i.getStringExtra("nombreUsuario");
        if(userName != null && !userName.isEmpty()){
            tvUser.setText("Bienvenido, " + userName);
        } else {
           tvUser.setText("Bienvenido");
        }

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

    public void mostrarUsuarios(){
        Intent intent = new Intent(this, ListaUsuarios.class);
        startActivity(intent);
    }
}