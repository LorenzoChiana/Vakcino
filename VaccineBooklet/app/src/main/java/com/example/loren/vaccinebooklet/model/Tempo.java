package com.example.loren.vaccinebooklet.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;


public class Tempo implements BaseColumns, Serializable {

    //Nome della tabella che ospiterà gli oggetti di tipo Tempo
    public static final String TABLE_NAME = "tempo";

    //Nomi delle colonne della relativa tabella
    public static final String COLUMN_YEARS = "years";
    public static final String COLUMN_ANTIGEN = "antigen";


    //Variabili del modello
    private int id;
    private String years;
    private String antigen;


    //Costruttore "standard"
    public Tempo(String years, String antigen) {
        this.years = years;
        this.antigen = antigen;
    }

    /**
     * Costruttore che crea un oggetto Tempo da un cursore. Un cursore è una tipologia di oggetto che conterrà il risultato di una
     * query effettuata sul database. Per recuperare i dati da un cursore si utilizzano i vari metodi di get, ai quali viene passato
     * come parametro l'indice della colonna relativa al dato che si vuole ottenere. Invece che passare l'indice numerico vero e proprio,
     * si chiede al cursore stesso di ritrovare gli indici utilizzando il nome delle colonne relative al dato stesso.
     *
     * @param cursor oggetto cursore
     */
    public Tempo(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(Tempo._ID));
        this.years = cursor.getString(cursor.getColumnIndex(Tempo.COLUMN_YEARS));
        this.antigen = cursor.getString(cursor.getColumnIndex(Tempo.COLUMN_ANTIGEN));
    }


    //Metodi di get
    public int getId() {
        return id;
    }

    public String getYears() {
        return years;
    }

    public String getAntigen() {
        return antigen;
    }


    //Metodi di set
    public void setYears(String years) {
        this.years = years;
    }

    public void getAntigen(String antigen) {
        this.antigen = antigen;
    }


    /**
     * Metodo che ritorna il content values dell'oggetto di classe Tempo.
     * Un oggetto ContentValues è un oggetto che contiene tutti i dati dell'oggetto Tempo, codificati
     * secondo la logica chiave-valore, dove la chiave è il nome della colonna in cui vogliamo inserire
     * il dato nella rispettiva tabella di database.
     *
     * @return l'oggetto ContentValues
     */
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_YEARS, years);
        cv.put(COLUMN_ANTIGEN, antigen);
        return cv;
    }
}
