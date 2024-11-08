package com.example.mercadapp_v1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NewProductActivity extends AppCompatActivity {
    private RepoMercado repoMercado;
    private RepoProducto repoProducto;
    private Button btnIngresar, btnCancelar;
    private EditText txtNombreProd, txtNombreMerc, txtPrecio, txtCantidad, txtObservaciones;
    private Spinner list_categoria;
    private FloatingActionButton btnNewProduct;
    private RecyclerView listaMercado;
    List<Producto> productos = new ArrayList<>();
    private boolean nombreMercadoEditable = true;
    ProductoAdapter productoAdapter = new ProductoAdapter(productos);
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        //Identificar todos los elementos del xml
        txtNombreProd = findViewById(R.id.txtNombreProducto);
        txtNombreMerc = findViewById(R.id.txtNombreMercado);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtCantidad = findViewById(R.id.txtCantidad);
        txtObservaciones = findViewById(R.id.txtObservaciones);
        listaMercado = findViewById(R.id.recyclerProductos);
        btnNewProduct = findViewById(R.id.btnNewProduct);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnCancelar = findViewById(R.id.btnCancelar);
        list_categoria = findViewById(R.id.listCategoria);

        repoProducto = new RepoProducto(dbHelper);
        repoMercado = new RepoMercado(dbHelper);

        //Instanciar el adapter para mostrar la lista de productos agregados
        listaMercado.setAdapter(productoAdapter);
        listaMercado.setLayoutManager(new LinearLayoutManager(this));

        //Parametrizar el spinner con los tipos de compras
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.listCategoria, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        list_categoria.setAdapter(spinnerAdapter);

        //Restaurar el estado de edición del txtNombreMerc al recrear la actividad
        if (savedInstanceState != null) {
            nombreMercadoEditable = savedInstanceState.getBoolean("nombreMercadoEditable", true);
            txtNombreMerc.setEnabled(nombreMercadoEditable);
        }

    btnIngresar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mostrarHistorico();
        }
    });

    btnCancelar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            cancelarAccion();
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

    public void agregarProducto(){
        //Validaciones varias
        if (!validarCampo(txtNombreMerc, "El nombre del mercado es obligatorio") ||
        !validarCampo(txtNombreProd, "El nombre del producto es obligatorio") ||
        !validarCampo(txtPrecio, "El precio es obligatorio") ||
        !validarCampo(txtCantidad,"La cantidad es obligatoria")) {
            return;
        }

        if (list_categoria.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Seleccione un tipo de producto", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().toString());
            double precio = Double.parseDouble(txtPrecio.getText().toString());

            if (precio <= 0) {
                txtPrecio.setError("El precio debe ser mayor que 0");
                return;
            }

            if (cantidad <= 0) {
                txtCantidad.setError("La cantidad debe ser mayor que 0");
                return;
            }
        }
        catch (NumberFormatException e) {
            txtPrecio.setError("El precio debe ser un número válido");
            txtCantidad.setError("La cantidad debe ser un número válido");
            return;
        }

        /*Se valida si ya se agregó al menos un producto a la lista, esto asegura que el usuario no cambie
        el nombre del mercado y se agregue a mercados diferentes*/
        if (nombreMercadoEditable) {
            txtNombreMerc.setEnabled(false); // Deshabilitar el campo
            nombreMercadoEditable = false;
        }

        //Lógica de base de datos
        try {
            String nombreMerc = txtNombreMerc.getText().toString();
            String nombreProd = txtNombreProd.getText().toString();
            double precio = Double.parseDouble(txtPrecio.getText().toString().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().toString().trim());
            String observaciones = txtObservaciones.getText().toString();
            String tipoProd = list_categoria.getSelectedItem().toString();

            repoMercado = new RepoMercado(dbHelper);
            repoProducto = new RepoProducto(dbHelper);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.setForeignKeyConstraintsEnabled(true);

            db.beginTransaction();
            try {
                long mercadoID = repoMercado.obtenerIdMercado(nombreMerc);

                if(mercadoID == -1){
                    mercadoID = repoMercado.insertarMercado(nombreMerc);
                }

                repoProducto.insertarProducto(nombreProd, precio, cantidad, observaciones, tipoProd, nombreMerc, mercadoID);

                db.setTransactionSuccessful();
                Toast.makeText(this, "Producto agregado correctamente.", Toast.LENGTH_SHORT).show();
                cargarProductosDesdeBD(nombreMerc);
        }
        catch(Exception e){
            Log.e("DBTransaction", "Error durante la transacción", e);
        }
        finally{
            db.endTransaction();
        }
    }
    catch(Exception e) {
        Toast.makeText(this, "Hubo un problema al agregar el producto. Intente nuevamente.", Toast.LENGTH_LONG).show();
        Log.e("AgregarProducto", "Error al insertar en la base de datos", e);
    }
}

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("nombreMercadoEditable", nombreMercadoEditable);
    }

    public void cargarProductosDesdeBD(String nombreMercado) {
        SQLiteDatabase db = openOrCreateDatabase("MercadAppBD", Context.MODE_PRIVATE, null);

        Cursor c = null;
        try {
            // Realizamos la consulta filtrando por el nombre del mercado
            String query = "SELECT p.nombreProducto, p.precio, p.cantidad " +
                    "FROM producto p " +
                    "JOIN mercado m ON p.nombreMercado = m.nombre " +
                    "WHERE p.nombreMercado = ?";

            c = db.rawQuery(query, new String[]{nombreMercado});

            List<Producto> productos = new ArrayList<>();

            if (c != null && c.moveToFirst()) {
                do {
                    // Recuperamos los datos de cada producto
                    int indexNombreProducto = c.getColumnIndex("nombreProducto");
                    int indexPrecio = c.getColumnIndex("precio");
                    int indexCantidad = c.getColumnIndex("cantidad");

                    if (indexNombreProducto != -1 && indexPrecio != -1 && indexCantidad != -1) {
                        String nombreProducto = c.getString(indexNombreProducto);
                        double precio = c.getDouble(indexPrecio);
                        int cantidad = c.getInt(indexCantidad);

                        // Creamos el objeto Producto y lo agregamos a la lista
                        Producto producto = new Producto(nombreProducto, precio, cantidad);
                        productos.add(producto);
                    }
                } while (c.moveToNext());
            }
            actualizarRecyclerView(productos);
            limpiarCampos();

        }
        catch (Exception e) {
            Log.e("CargarProductos", "Error al cargar los productos", e);
        }
        finally {
            if (c != null) {
                c.close();
            }
            db.close();
        }
    }

    private void limpiarCampos() {
        txtNombreProd.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtObservaciones.setText("");
        txtNombreProd.requestFocus();
    }

    public void actualizarRecyclerView(List<Producto> productos) {
        productoAdapter.setProductos(productos);
        productoAdapter.notifyDataSetChanged();
    }

    public void mostrarHistorico(){
        Intent intent = new Intent(this, HistoricoActivity.class);
        startActivity(intent);
    }

    private void cancelarAccion() {
        txtNombreMerc.setEnabled(true);
        txtNombreMerc.setText("");
        nombreMercadoEditable = true;
    }

    private boolean validarCampo(EditText campo, String mensajeError){
        if(campo.getText().toString().trim().isEmpty()){
            campo.setError(mensajeError);
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

}
