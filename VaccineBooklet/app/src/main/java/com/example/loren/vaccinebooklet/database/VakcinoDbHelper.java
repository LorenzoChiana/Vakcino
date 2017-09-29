package com.example.loren.vaccinebooklet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class VakcinoDbHelper extends SQLiteOpenHelper {

    //Dati del database
    private static final String DATABASE_NAME = "vakcino.db";
    private static final int DATABASE_VERSION = 1;

    //Riferimenti SQL alle costanti per creare tabelle di database
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String INTEGER_PRIMARY_KEY = INTEGER_TYPE + " PRIMARY KEY";

    public VakcinoDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
