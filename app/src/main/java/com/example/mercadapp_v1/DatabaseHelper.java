package com.example.mercadapp_v1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "MercadAppBD";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS mercado(idMercado INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS producto(" +
                "idProducto INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreProducto VARCHAR," +
                "precio DECIMAL, " +
                "cantidad INTEGER, " +
                "observaciones TEXT, " +
                "tipoProducto VARCHAR, " +
                "idMercado INTEGER," +
                "FOREIGN KEY (idMercado) REFERENCES mercado (idMercado) ON DELETE CASCADE ON UPDATE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS producto");
        db.execSQL("DROP TABLE IF EXISTS mercado");
        onCreate(db);
    }
}
