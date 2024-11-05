package com.example.mercadapp_v1;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NewProductActivity extends AppCompatActivity {

    private Button btnCrear, btnCancelar;
    private EditText txtNombreProd, txtNombreMerc, txtPrecio, txtCantidad, txtObservaciones;
    private Spinner list_categoria;
    private FloatingActionButton btnNewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        txtNombreProd = findViewById(R.id.txtNombreProducto);
        txtNombreMerc = findViewById(R.id.txtNombreMercado);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtCantidad = findViewById(R.id.txtCantidad);
        txtObservaciones = findViewById(R.id.txtObservaciones);
        btnNewProduct = findViewById(R.id.btnNewProduct);
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

    btnNewProduct.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            agregarProducto();
        }
        });
    }

    public void mostrarHistorico(){
        Intent intent = new Intent(this, HistoricoActivity.class);
        startActivity(intent);
    }

    public void agregarProducto(){
        try {
            String nombreMerc = txtNombreMerc.getText().toString();
            String nombreProd = txtNombreProd.getText().toString();
            String precio = txtPrecio.getText().toString();
            String cantidad = txtCantidad.getText().toString();
            String observaciones = txtObservaciones.getText().toString();
            String tipoProd = list_categoria.getSelectedItem().toString();

            SQLiteDatabase db = openOrCreateDatabase("MercadAppBD", Context.MODE_PRIVATE, null);
            db.setForeignKeyConstraintsEnabled(true);

            db.execSQL("CREATE TABLE IF NOT EXISTS mercado(idMercado INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre VARCHAR);");

            db.execSQL("CREATE TABLE IF NOT EXISTS producto(idProducto INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombreProducto VARCHAR," +
                    "precio DECIMAL, " +
                    "cantidad INTEGER, " +
                    "observaciones TEXT, " +
                    "tipoProducto VARCHAR, " +
                    "nombreMercado VARCHAR, " +
                    "FOREIGN KEY (idMercado) REFERENCES mercado (idMercado) ON DELETE CASCADE ON UPDATE CASCADE)");

            String sqlMercado = "INSERT INTO mercado(nombre)" +
                    "VALUES (?)";
            SQLiteStatement s1 = db.compileStatement(sqlMercado);
            s1.bindString(1, nombreMerc);

            String sqlProducto = "INSERT INTO producto (nombreProducto, precio, cantidad, observaciones, tipoProducto, nombreMercado)" +
                    "VALUES(?,?,?,?,?,?)";
            SQLiteStatement s2 = db.compileStatement(sqlProducto);
            s2.bindString(1, nombreProd);
            s2.bindString(2, precio);
            s2.bindString(3, cantidad);
            s2.bindString(4, observaciones);
            s2.bindString(5, tipoProd);
            s2.bindString(5, nombreMerc);

            s1.execute();
            s2.execute();



        } catch(Exception e) {

        }





    }
}