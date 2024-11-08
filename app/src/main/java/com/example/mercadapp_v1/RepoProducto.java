package com.example.mercadapp_v1;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class RepoProducto {
    private final DatabaseHelper dbHelper;

    public RepoProducto(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void insertarProducto(String nombreProd, double precio, int cantidad, String observaciones,
                                 String tipoProd, String nombreMerc, long mercadoID) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sqlProducto = "INSERT INTO producto (nombreProducto, precio, cantidad, observaciones," +
                "tipoProducto, nombreMercado, idMercado) VALUES(?,?,?,?,?,?,?)";
        SQLiteStatement s2 = db.compileStatement(sqlProducto);
        s2.bindString(1, nombreProd);
        s2.bindString(2, String.valueOf(precio));
        s2.bindString(3, String.valueOf(cantidad));
        s2.bindString(4, observaciones);
        s2.bindString(5, tipoProd);
        s2.bindString(6, nombreMerc);
        s2.bindLong(7, mercadoID);
        s2.execute();
    }
}
