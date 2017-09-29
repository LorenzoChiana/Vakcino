package com.example.loren.vaccinebooklet.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

public class Dove implements BaseColumns, Serializable {

    //Nome della tabella che ospiterà gli oggetti di tipo Dove
    public static final String TABLE_NAME = "dove";

    //Nomi delle colonne della relativa tabella
    public static final String COLUMN_COD_STATE = "codState";
    public static final String COLUMN_ANTIGEN = "antigen";
    public static final String COLUMN_ENTIRE_COUNTRY = "entireCountry";


    //Variabili del modello
    private String codState;
    private String antigen;
    private String entireCountry;

    //Costruttore "standard"
    public Dove(String codState, String antigen, String entireCountry) {
        this.codState = codState;
        this.antigen = antigen;
        this.entireCountry = entireCountry;
    }

    /**
     * Costruttore che crea un oggetto Dove da un cursore. Un cursore è una tipologia di oggetto che conterrà il risultato di una
     * query effettuata sul database. Per recuperare i dati da un cursore si utilizzano i vari metodi di get, ai quali viene passato
     * come parametro l'indice della colonna relativa al dato che si vuole ottenere. Invece che passare l'indice numerico vero e proprio,
     * si chiede al cursore stesso di ritrovare gli indici utilizzando il nome delle colonne relative al dato stesso.
     *
     * @param cursor oggetto cursore
     */
    public Dove(Cursor cursor) {
        this.codState = cursor.getString(cursor.getColumnIndex(Dove.COLUMN_COD_STATE));
        this.antigen = cursor.getString(cursor.getColumnIndex(Dove.COLUMN_ANTIGEN));
        this.entireCountry = cursor.getString(cursor.getColumnIndex(Dove.COLUMN_ENTIRE_COUNTRY));
    }


    //Metodi di get
    public String getCodState() {
        return codState;
    }

    public String getAntigen() {
        return antigen;
    }

    public String getEntireCountry() {
        return entireCountry;
    }

    //Metodi di set
    public void setCodState(String codState) {
        this.codState = codState;
    }

    public void setAntigen(String antigen) {
        this.antigen = antigen;
    }

    public void setEntireCountry(String entireCountry) {
        this.entireCountry = entireCountry;
    }


    /**
     * Metodo che ritorna il content values dell'oggetto di classe Dove.
     * Un oggetto ContentValues è un oggetto che contiene tutti i dati dell'oggetto Dove, codificati
     * secondo la logica chiave-valore, dove la chiave è il nome della colonna in cui vogliamo inserire
     * il dato nella rispettiva tabella di database.
     *
     * @return l'oggetto ContentValues
     */
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_COD_STATE, codState);
        cv.put(COLUMN_ANTIGEN, antigen);
        cv.put(COLUMN_ENTIRE_COUNTRY, entireCountry);
        return cv;
    }
}
