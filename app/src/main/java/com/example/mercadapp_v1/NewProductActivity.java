package com.example.mercadapp_v1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import androidx.appcompat.app.AlertDialog;
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

        dbHelper = new DatabaseHelper(this);
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
            ingresarMercado();
        }
    });

    btnCancelar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            cancelarAccion();
        }
    });

    btnNewProduct.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            agregarProducto();
            String nombreMercado = txtNombreMerc.getText().toString();
            cargarProductosDesdeBD(nombreMercado);
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
                limpiarCampos();
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
    }
}

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("nombreMercadoEditable", nombreMercadoEditable);
    }

    public void cargarProductosDesdeBD(String nombreMercado) {
        SQLiteDatabase db = null;
        Cursor c = null;

        try{
            db = dbHelper.getReadableDatabase();
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

                        Producto producto = new Producto(nombreProducto, precio, cantidad);
                        productos.add(producto);
                    }
                } while (c.moveToNext());
            }
            actualizarRecyclerView(productos);
        } catch (Exception e) {
            Log.e("CargarProductos", "Error al cargar los productos", e);
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    private void limpiarCampos() {
        txtNombreProd.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtObservaciones.setText("");
        list_categoria.setSelection(0);
        txtNombreProd.requestFocus();
    }

    public void actualizarRecyclerView(List<Producto> productos) {
        productoAdapter.setProductos(productos);
        productoAdapter.notifyItemInserted(productos.size() - 1);
    }

    public void mostrarInicio(){
        Intent intent = new Intent(this, InicioActivity.class);
        startActivity(intent);
    }

    private void cancelarAccion() {
        if(!nombreMercadoEditable){
            AlertDialog.Builder alerta = new AlertDialog.Builder(NewProductActivity.this);
            alerta.setTitle("Confirmar cancelación");
            alerta.setMessage("¿Está seguro de cancelar la operación? Esto eliminará los datos ingresados");

            alerta.setPositiveButton("Sí",(dialog, which) -> {
                String nombreMerc = txtNombreMerc.getText().toString();
                try {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    String query = "DELETE FROM mercado WHERE nombre = ?";
                    String query2 = "DELETE FROM producto WHERE nombreMercado = ?";
                    SQLiteStatement s1 = db.compileStatement(query);
                    SQLiteStatement s2 = db.compileStatement(query2);

                    s1.bindString(1, nombreMerc);
                    s2.bindString(1, nombreMerc);
                    int filasAfectadas = s1.executeUpdateDelete();
                    int filasAfectadas2 = s2.executeUpdateDelete();

                    if(filasAfectadas >= 1 && filasAfectadas2 >= 1){
                        Toast.makeText(this,"Datos eliminados de la base de datos.",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(this, InicioActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e){
                    Toast.makeText(this,"Ocurrió un error inesperado.",Toast.LENGTH_LONG).show();
                }

                txtNombreMerc.setEnabled(true);
                txtNombreMerc.setText("");
                nombreMercadoEditable = true;
            });

            alerta.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });

            AlertDialog dialog = alerta.create();
            Log.d("CancelarAccion", "Mostrando el diálogo de confirmación");
            dialog.show();
        } else {
            Intent intent = new Intent(this, InicioActivity.class);
            startActivity(intent);
            finish();
        }
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

    private void ingresarMercado(){
        if(!nombreMercadoEditable){
            AlertDialog.Builder alerta = new AlertDialog.Builder(NewProductActivity.this);
            alerta.setTitle("Guardar mercado");
            alerta.setMessage("¿Desea guardar su mercado?");
            alerta.setPositiveButton("Sí, quiero guardar mi mercado", (dialog, which) -> {
                Toast.makeText(NewProductActivity.this, "Mercado guardado exitosamente", Toast.LENGTH_SHORT).show();
                mostrarInicio();
            });

            alerta.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
            });

            alerta.create().show();
        } else {
            Toast.makeText(NewProductActivity.this, "Agregue un producto a la lista", Toast.LENGTH_SHORT).show();
        }
    }
}
