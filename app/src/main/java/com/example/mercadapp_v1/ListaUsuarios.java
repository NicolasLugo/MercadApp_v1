package com.example.mercadapp_v1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaUsuarios extends AppCompatActivity {
    private ListView lista;
    private ArrayList<String> array = new ArrayList<String>();
    private ArrayAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        try {
            SQLiteDatabase db = openOrCreateDatabase("BD_EJEMPLO",Context.MODE_PRIVATE,null);
            lista = findViewById(R.id.listaUsuarios);
            final Cursor c =db.rawQuery("SELECT * FROM usuario", null);
            int id = c.getColumnIndex("id");
            int nombre = c.getColumnIndex("nombre");
            int apellido = c.getColumnIndex("apellido");
            int fechaNacim = c.getColumnIndex("fechaNacimiento");
            int celular = c.getColumnIndex("celular");
            int sexo = c.getColumnIndex("sexo");
            array.clear();

            adaptador =new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, array);
            lista.setAdapter(adaptador);
            final ArrayList<Usuario> list = new ArrayList<Usuario>();

            if(c.moveToFirst()){
                do{
                    Usuario usuario = new Usuario();
                    usuario.id = c.getString(id);
                    usuario.nombre = c.getString(nombre);
                    usuario.apellido = c.getString(apellido);
                    usuario.fechaNacimiento = c.getString(fechaNacim);
                    usuario.celular = c.getString(celular);
                    usuario.sexo = c.getString(sexo);
                    list.add(usuario);

                    array.add(c.getString(id) + " \t " +
                            c.getString(nombre) + " \t " +
                            c.getString(apellido) + " \t " +
                            c.getString(fechaNacim) + " \t " +
                            c.getString(celular) + " \t " +
                            c.getString(sexo));
                } while(c.moveToNext());
                adaptador.notifyDataSetChanged();
                lista.invalidateViews();
            }

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Usuario usuario = list.get(position);
                    Intent i = new Intent(getApplicationContext(), EditarUsuario.class);
                    i.putExtra("id",usuario.id);
                    i.putExtra("nombre",usuario.nombre);
                    i.putExtra("apellido",usuario.apellido);
                    i.putExtra("fechaNacim",usuario.fechaNacimiento);
                    i.putExtra("celular",usuario.celular);
                    i.putExtra("sexo",usuario.sexo);
                    startActivity(i);
                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(this, "Ha ocurrido un error, intente nuevamente", Toast.LENGTH_SHORT).show();
        }
    }
}