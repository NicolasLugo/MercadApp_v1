package com.example.mercadapp_v1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InformesActivity extends AppCompatActivity {
    private ListView lista;
    private TextView vacio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informes);

        lista = findViewById(R.id.lista_informe);
        vacio = findViewById(R.id.vacio);
        lista.setEmptyView(vacio);
    }
}