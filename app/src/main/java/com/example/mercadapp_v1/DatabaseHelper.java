package com.example.mercadapp_v1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "MercadAppBD";
    private static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS mercado(" +
                "idMercado INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS producto(" +
                "idProducto INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombreProducto VARCHAR," +
                "precio DECIMAL, " +
                "cantidad INTEGER, " +
                "observaciones TEXT, " +
                "tipoProducto VARCHAR, " +
                "nombreMercado VARCHAR," +
                "idMercado INTEGER," +
                "FOREIGN KEY (idMercado) REFERENCES mercado (idMercado) ON DELETE CASCADE ON UPDATE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {  // Supongamos que estás actualizando a la versión 2
            // Crear tabla temporal con la nueva estructura
            db.execSQL("CREATE TABLE producto_temp (" +
                    "idProducto INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombreProducto VARCHAR NOT NULL, " +
                    "precio REAL NOT NULL, " +
                    "cantidad INTEGER NOT NULL, " +
                    "observaciones VARCHAR, " +
                    "tipoProducto VARCHAR NOT NULL, " +
                    "nombreMercado VARCHAR, " +
                    "idMercado INTEGER NOT NULL, " +
                    "FOREIGN KEY (idMercado) REFERENCES mercado (idMercado) " +
                    "ON DELETE CASCADE)");

            // Eliminar la tabla antigua
            db.execSQL("DROP TABLE producto");

            // Renombrar la tabla temporal con el nombre de la tabla original
            db.execSQL("ALTER TABLE producto_temp RENAME TO producto");
        }
    }
}