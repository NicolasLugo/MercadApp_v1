package com.example.mercadapp_v1;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FirebaseHelper {
    private final DatabaseReference db;
    private final Context context;

    public FirebaseHelper(Context context){
        this.db = FirebaseDatabase.getInstance().getReference();
        this.context = context;
    }

    public void guardarDatos(String mercado, List<Producto> productos){
        DatabaseReference mercadoRef = db.child("Mercados:").child(mercado).child("Productos:");

        for(Producto prod : productos){
            mercadoRef.push().setValue(prod)
                    .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Producto agregado", Toast.LENGTH_SHORT).show();
            })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error al guardar el producto", Toast.LENGTH_SHORT).show();
                    });
        }
        Toast.makeText(context, "Todos los productos se han agregado exitosamente", Toast.LENGTH_SHORT).show();
    }
}