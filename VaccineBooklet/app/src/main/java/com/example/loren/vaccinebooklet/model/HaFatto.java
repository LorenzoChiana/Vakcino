package com.example.loren.vaccinebooklet.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

public class HaFatto implements BaseColumns, Serializable {

    //Nome della tabella che ospiterà gli oggetti di tipo HaFatto
    public static final String TABLE_NAME = "hafatto";

    //Nomi delle colonne della relativa tabella
    public static final String COLUMN_IDUTENTE = "idUtente";
    public static final String COLUMN_IDTIPOVAC = "idTipoVac";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_STATUS = "status";

    //Variabili del modello
    private int idUtente;
    private int idTipoVac;
    private String date;
    private int status;

    //Costruttore "standard"
    public HaFatto(int idUtente, int idTipoVac, String date, int status) {
        this.idUtente = idUtente;
        this.idTipoVac = idTipoVac;
        this.date = date;
        this.status = status;
    }

    /**
     * Costruttore che crea un oggetto HaFatto da un cursore. Un cursore è una tipologia di oggetto che conterrà il risultato di una
     * query effettuata sul database. Per recuperare i dati da un cursore si utilizzano i vari metodi di get, ai quali viene passato
     * come parametro l'indice della colonna relativa al dato che si vuole ottenere. Invece che passare l'indice numerico vero e proprio,
     * si chiede al cursore stesso di ritrovare gli indici utilizzando il nome delle colonne relative al dato stesso.
     *
     * @param cursor oggetto cursore
     */
    public HaFatto(Cursor cursor) {
        this.idUtente = cursor.getInt(cursor.getColumnIndex(HaFatto.COLUMN_IDUTENTE));
        this.idTipoVac = cursor.getInt(cursor.getColumnIndex(HaFatto.COLUMN_IDTIPOVAC));
        this.date = cursor.getString(cursor.getColumnIndex(HaFatto.COLUMN_DATE));
        this.status = cursor.getInt(cursor.getColumnIndex(HaFatto.COLUMN_STATUS));
    }


    //Metodi di get
    public int getIdUtente() {
        return idUtente;
    }
    public int getIdTipoVac() {
        return idTipoVac;
    }
    public String getDate() { return date; }
    public int getStatus() {
        return status;
    }

    //Metodi di set
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }
    public void setIdTipoVac(int idTipoVac) {
        this.idTipoVac = idTipoVac;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setStatus(int status) {
        this.status = status;
    }


    /**
     * Metodo che ritorna il content values dell'oggetto di classe HaFatto.
     * Un oggetto ContentValues è un oggetto che contiene tutti i dati dell'oggetto HaFatto, codificati
     * secondo la logica chiave-valore, dove la chiave è il nome della colonna in cui vogliamo inserire
     * il dato nella rispettiva tabella di database.
     *
     * @return l'oggetto ContentValues
     */
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_IDUTENTE, idUtente);
        cv.put(COLUMN_IDTIPOVAC, idTipoVac);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_STATUS, status);
        return cv;
    }
}
