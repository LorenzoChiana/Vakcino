package com.example.loren.vaccinebooklet.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

public class Libretto implements BaseColumns, Serializable {

    //Nome della tabella che ospiterà gli oggetti di tipo Libretto
    public static final String TABLE_NAME = "libretto";

    //Nomi delle colonne della relativa tabella
    public static final String COLUMN_IDUTENTE = "idUtente";
    public static final String COLUMN_IDTIPOVAC = "idTipoVac";
    public static final String COLUMN_DONE = "fatto";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_STATUS = "status";

    //Variabili del modello
    private int idUtente;
    private int idTipoVac;
    private int done;
    private String date;
    private int status;

    //Costruttore "standard"
    public Libretto(int idUtente, int idTipoVac, int done, String date, int status) {
        this.idUtente = idUtente;
        this.idTipoVac = idTipoVac;
        this.done = done;
        this.date = date;
        this.status = status;
    }

    /**
     * Costruttore che crea un oggetto Libretto da un cursore. Un cursore è una tipologia di oggetto che conterrà il risultato di una
     * query effettuata sul database. Per recuperare i dati da un cursore si utilizzano i vari metodi di get, ai quali viene passato
     * come parametro l'indice della colonna relativa al dato che si vuole ottenere. Invece che passare l'indice numerico vero e proprio,
     * si chiede al cursore stesso di ritrovare gli indici utilizzando il nome delle colonne relative al dato stesso.
     *
     * @param cursor oggetto cursore
     */
    public Libretto(Cursor cursor) {
        this.idUtente = cursor.getInt(cursor.getColumnIndex(Libretto.COLUMN_IDUTENTE));
        this.idTipoVac = cursor.getInt(cursor.getColumnIndex(Libretto.COLUMN_IDTIPOVAC));
        this.done = cursor.getInt(cursor.getColumnIndex(Libretto.COLUMN_DONE));
        this.date = cursor.getString(cursor.getColumnIndex(Libretto.COLUMN_DATE));
        this.status = cursor.getInt(cursor.getColumnIndex(Libretto.COLUMN_STATUS));
    }


    //Metodi di get
    public int getIdUtente() {
        return idUtente;
    }
    public int getIdTipoVac() {
        return idTipoVac;
    }
    public int getDone() {
        return done;
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
    public void setDone(int done) {
        this.done = done;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setStatus(int status) {
        this.status = status;
    }


    /**
     * Metodo che ritorna il content values dell'oggetto di classe Libretto.
     * Un oggetto ContentValues è un oggetto che contiene tutti i dati dell'oggetto Libretto, codificati
     * secondo la logica chiave-valore, dove la chiave è il nome della colonna in cui vogliamo inserire
     * il dato nella rispettiva tabella di database.
     *
     * @return l'oggetto ContentValues
     */
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_IDUTENTE, idUtente);
        cv.put(COLUMN_IDTIPOVAC, idTipoVac);
        cv.put(COLUMN_DONE, done);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_STATUS, status);
        return cv;
    }
}
