package com.example.loren.vaccinebooklet.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by loren on 29/09/2017.
 */

public class DeveFare implements BaseColumns, Serializable {

    //Nome della tabella che ospiterà gli oggetti di tipo DeveFare
    public static final String TABLE_NAME = "devefare";

    //Nomi delle colonne della relativa tabella
    public static final String COLUMN_IDUTENTE = "idUtente";
    public static final String COLUMN_ANTIGEN = "antigen";


    //Variabili del modello
    private int idUtente;
    private String antigene;


    //Costruttore "standard"
    public DeveFare(int idUtente, String antigene) {
        this.idUtente = idUtente;
        this.antigene = antigene;
    }

    /**
     * Costruttore che crea un oggetto DeveFare da un cursore. Un cursore è una tipologia di oggetto che conterrà il risultato di una
     * query effettuata sul database. Per recuperare i dati da un cursore si utilizzano i vari metodi di get, ai quali viene passato
     * come parametro l'indice della colonna relativa al dato che si vuole ottenere. Invece che passare l'indice numerico vero e proprio,
     * si chiede al cursore stesso di ritrovare gli indici utilizzando il nome delle colonne relative al dato stesso.
     *
     * @param cursor oggetto cursore
     */
    public DeveFare(Cursor cursor) {
        this.idUtente = cursor.getInt(cursor.getColumnIndex(DeveFare.COLUMN_IDUTENTE));
        this.antigene = cursor.getString(cursor.getColumnIndex(DeveFare.COLUMN_ANTIGEN));
    }


    //Metodi di get
    public int getIdUtente() {
        return idUtente;
    }

    public String getAntigene() {
        return antigene;
    }

    //Metodi di set
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public void setAntigene(String antigene) {
        this.antigene = antigene;
    }


    /**
     * Metodo che ritorna il content values dell'oggetto di classe DeveFare.
     * Un oggetto ContentValues è un oggetto che contiene tutti i dati dell'oggetto DeveFare, codificati
     * secondo la logica chiave-valore, dove la chiave è il nome della colonna in cui vogliamo inserire
     * il dato nella rispettiva tabella di database.
     *
     * @return l'oggetto ContentValues
     */
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_IDUTENTE, idUtente);
        cv.put(COLUMN_ANTIGEN, antigene);
        return cv;
    }
}