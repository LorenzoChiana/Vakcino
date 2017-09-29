package com.example.loren.vaccinebooklet.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

public class Paese implements BaseColumns, Serializable {

    //Nome della tabella che ospiterà gli oggetti di tipo Paese
    public static final String TABLE_NAME = "paese";

    //Nomi delle colonne della relativa tabella
    public static final String COLUMN_COD_STATE = "codState";
    public static final String COLUMN_NAME = "name";


    //Variabili del modello
    private String codState;
    private String name;


    //Costruttore "standard"
    public Paese(String codState, String name) {
        this.name = name;
        this.codState = codState;
    }

    /**
     * Costruttore che crea un oggetto Paese da un cursore. Un cursore è una tipologia di oggetto che conterrà il risultato di una
     * query effettuata sul database. Per recuperare i dati da un cursore si utilizzano i vari metodi di get, ai quali viene passato
     * come parametro l'indice della colonna relativa al dato che si vuole ottenere. Invece che passare l'indice numerico vero e proprio,
     * si chiede al cursore stesso di ritrovare gli indici utilizzando il nome delle colonne relative al dato stesso.
     *
     * @param cursor oggetto cursore
     */
    public Paese(Cursor cursor) {
        this.codState = cursor.getString(cursor.getColumnIndex(Paese.COLUMN_COD_STATE));
        this.name = cursor.getString(cursor.getColumnIndex(Paese.COLUMN_NAME));
    }


    //Metodi di get
    public String getName() {
        return name;
    }

    public String getCodState() {
        return codState;
    }

    //Metodi di set
    public void setName(String name) {
        this.name = name;
    }

    public void setCodState(String codState) {
        this.codState = codState;
    }


    /**
     * Metodo che ritorna il content values dell'oggetto di classe Paese.
     * Un oggetto ContentValues è un oggetto che contiene tutti i dati dell'oggetto Paese, codificati
     * secondo la logica chiave-valore, dove la chiave è il nome della colonna in cui vogliamo inserire
     * il dato nella rispettiva tabella di database.
     *
     * @return l'oggetto ContentValues
     */
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_COD_STATE, codState);
        return cv;
    }
}
