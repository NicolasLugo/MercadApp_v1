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

public class EditarUsuario extends AppCompatActivity {

    private EditText txtId, txtNombre, txtApellido, txtCelular, fechaNacimiento;
    private Button btnEditar, btnEliminar, btnVolver;
    private Spinner list_sexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        txtId = findViewById(R.id.txtId);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtCelular = findViewById(R.id.txtCelular);
        fechaNacimiento = findViewById(R.id.fechaNacimiento);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolver = findViewById(R.id.btnVolver);
        list_sexo = findViewById(R.id.listSexo);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.listSexo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list_sexo.setAdapter(adapter);

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");
        String nombre = intent.getStringExtra("nombre");
        String apellido = intent.getStringExtra("apellido");
        String fechaNacim = intent.getStringExtra("fechaNacim");
        String celular = intent.getStringExtra("celular");
        String sexo = intent.getStringExtra("sexo");

        txtId.setText(id);
        txtNombre.setText(nombre);
        txtApellido.setText(apellido);
        txtCelular.setText(celular);
        fechaNacimiento.setText(fechaNacim);
        if(sexo.equals("Masculino")){
            list_sexo.setSelection(2);
        } else {
            list_sexo.setSelection(1);
        }

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editarUsuario();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarUsuario();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditarUsuario.this, DatePickerActivity.class);
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String fechaSeleccionada = data.getStringExtra("fechaSeleccionada");
            fechaNacimiento.setText(fechaSeleccionada);
        }
    }

    public void editarUsuario(){
        try {
            String id = txtId.getText().toString();
            String nombre = txtNombre.getText().toString();
            String apellido = txtApellido.getText().toString();
            String fechaNacim = fechaNacimiento.getText().toString();
            String celular = txtCelular.getText().toString();
            String sexo = list_sexo.getSelectedItem().toString();

            SQLiteDatabase db = openOrCreateDatabase("MercadAppBD", Context.MODE_PRIVATE, null);
            String sql = "UPDATE usuario set nombre = ?, apellido = ?, fechaNacimiento = ?, celular = ?, sexo = ? WHERE id = ?";

            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindString(1, nombre);
            statement.bindString(2, apellido);
            statement.bindString(3, fechaNacim);
            statement.bindString(4, celular);

            if(sexo.equals("Seleccione una opción")){
                Toast.makeText(this, "Por favor, seleccione una opción válida", Toast.LENGTH_SHORT).show();
            }else{
                statement.bindString(5, sexo);
                statement.bindString(6, id);

                statement.execute();

                Toast.makeText(this, "Usuario Actualizado exitosamente", Toast.LENGTH_LONG).show();

                txtId.setText("");
                txtNombre.setText("");
                txtApellido.setText("");
                fechaNacimiento.setText("");
                txtCelular.setText("");
                list_sexo.setSelection(0);

                Intent intent = new Intent(this, ListaUsuarios.class);
                startActivity(intent);
                finish();
            }

        }
        catch (Exception e){
            Toast.makeText(this,"Ocurrió un error al intentar actualizar los datos",Toast.LENGTH_LONG).show();
        }
    }

    public void eliminarUsuario(){
        try{
            String id = txtId.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("MercadAppBD", Context.MODE_PRIVATE, null);
            String sql = "DELETE FROM usuario WHERE id = ?";
            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindString(1, id);
            statement.execute();

            Toast.makeText(this,"Datos eliminados de la base de datos.",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, ListaUsuarios.class);
            startActivity(intent);
            finish();
        }
        catch(Exception e)
        {
            Toast.makeText(this,"Ocurrió un error inesperado.",Toast.LENGTH_LONG).show();
        }
    }
}