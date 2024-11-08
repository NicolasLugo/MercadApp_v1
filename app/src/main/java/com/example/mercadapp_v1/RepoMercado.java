package com.example.mercadapp_v1;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;

public class RepoMercado {
    private final DatabaseHelper dbHelper;

    public RepoMercado(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long obtenerIdMercado(String nombreMerc) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sqlCheckMercado = "SELECT idMercado FROM mercado WHERE nombre = ?";
        SQLiteStatement statement = db.compileStatement(sqlCheckMercado);
        statement.bindString(1, nombreMerc);

        try {
            return statement.simpleQueryForLong();
        } catch (SQLiteDoneException e) {
            return -1;
        }
    }

    public long insertarMercado(String nombreMerc) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sqlMercado = "INSERT INTO mercado(nombre) VALUES (?)";
        SQLiteStatement s1 = db.compileStatement(sqlMercado);
        s1.bindString(1, nombreMerc);
        return s1.executeInsert();
    }
}
