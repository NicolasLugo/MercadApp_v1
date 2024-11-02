package com.example.mercadapp_v1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class NewUserActivity extends AppCompatActivity {
    private Button btnCrear, btnCancelar;
    private Spinner list_sexo;
    private EditText txtNombre, txtApellido, fechaNacimiento, txtCelular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        btnCrear = findViewById(R.id.btnCrear);
        btnCancelar = findViewById(R.id.btnCancelar);
        list_sexo = findViewById(R.id.listSexo);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtCelular = findViewById(R.id.txtCelular);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.listSexo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list_sexo.setAdapter(adapter);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearUsuario();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void crearUsuario(){
        try
        {
            String nombre = txtNombre.getText().toString();
            String apellido = txtApellido.getText().toString();
            String fechaNacim = fechaNacimiento.getText().toString();
            String celular = txtCelular.getText().toString();
            String sexo = list_sexo.getSelectedItem().toString();

            SQLiteDatabase db = openOrCreateDatabase("MercadAppBD", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS usuario(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR, apellido VARCHAR, " +
                    "fechaNacimiento DATE, celular INTEGER, sexo VARCHAR)"); //preferiblemente que la fecha este en formato yyyy/mm/dd

            String sql = "INSERT INTO usuario(nombre, apellido, fechaNacimiento, celular, sexo) VALUES (?,?,?,?,?)";
            SQLiteStatement statement =db.compileStatement(sql);

            statement.bindString(1, nombre);
            statement.bindString(2, apellido);
            statement.bindString(3, fechaNacim);
            statement.bindString(4, celular);
            statement.bindString(5, sexo);

            statement.execute();

            Toast.makeText(this, "Usuario agregado exitosamente", Toast.LENGTH_LONG).show();

            txtNombre.setText("");
            txtApellido.setText("");
            fechaNacimiento.setText("");
            txtCelular.setText("");
            list_sexo.setSelection(0);
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Error, no se agregaron los datos, intente nuevamente", Toast.LENGTH_LONG).show();
        }
    }
}