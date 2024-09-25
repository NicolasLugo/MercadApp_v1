package com.example.mercadapp_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class NewProductActivity extends AppCompatActivity {
    private Button btnCrear;
    private Button btnCancelar;
    private Spinner list_categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        btnCrear = findViewById(R.id.btnCrear);
        btnCancelar = findViewById(R.id.btnCancelar);
        list_categoria = findViewById(R.id.listCategoria);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.listCategoria, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list_categoria.setAdapter(adapter);

    btnCrear.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mostrarHistorico();
        }
    });
    btnCancelar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });
    }

    public void mostrarHistorico(){
        Intent intent = new Intent(this, HistoricoActivity.class);
        startActivity(intent);
    }
}