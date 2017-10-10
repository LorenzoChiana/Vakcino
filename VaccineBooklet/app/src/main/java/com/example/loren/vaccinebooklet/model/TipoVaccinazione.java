package com.example.loren.vaccinebooklet.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by loren on 10/10/2017.
 */

public class TipoVaccinazione implements BaseColumns, Serializable {

    //Nome della tabella che ospiterà gli oggetti di tipo Vaccinazione
    public static final String TABLE_NAME = "tipovaccinazione";

    //Nomi delle colonne della relativa tabella
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DA = "da";
    public static final String COLUMN_A = "a";
    public static final String COLUMN_TIPOIMMUNIZZAZIONE = "tipoimmunizzazione";
    public static final String COLUMN_NUMRICHIAMO = "numrichiamo";
    public static final String COLUMN_ANTIGEN = "antigene";

    //Variabili del modello
    private int id;
    private String da;
    private String a;
    private String tipoImmunizzazione;
    private int numRichiamo;
    private String antigen;


    //Costruttore "standard"
    public TipoVaccinazione(int ID, String da, String a, String tipoImmunizzazione, int numRichiamo, String antigen) {
        this.id = ID;
        this.da = da;
        this.a = a;
        this.tipoImmunizzazione = tipoImmunizzazione;
        this.numRichiamo = numRichiamo;
        this.antigen = antigen;
    }

    /**
     * Costruttore che crea un oggetto Vaccinazione da un cursore. Un cursore è una tipologia di oggetto che conterrà il risultato di una
     * query effettuata sul database. Per recuperare i dati da un cursore si utilizzano i vari metodi di get, ai quali viene passato
     * come parametro l'indice della colonna relativa al dato che si vuole ottenere. Invece che passare l'indice numerico vero e proprio,
     * si chiede al cursore stesso di ritrovare gli indici utilizzando il nome delle colonne relative al dato stesso.
     *
     * @param cursor oggetto cursore
     */
    public TipoVaccinazione(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(Utente.COLUMN_ID));
        this.da = cursor.getString(cursor.getColumnIndex(TipoVaccinazione.COLUMN_DA));
        this.a = cursor.getString(cursor.getColumnIndex(TipoVaccinazione.COLUMN_A));
        this.tipoImmunizzazione = cursor.getString(cursor.getColumnIndex(TipoVaccinazione.COLUMN_TIPOIMMUNIZZAZIONE));
        this.numRichiamo = cursor.getInt(cursor.getColumnIndex(TipoVaccinazione.COLUMN_NUMRICHIAMO));
        this.antigen = cursor.getString(cursor.getColumnIndex(TipoVaccinazione.COLUMN_ANTIGEN));
    }


    // Getter
    public int getId() {
        return id;
    }
    public String getDa() {
        return da;
    }
    public String getA() {
        return a;
    }
    public String getTipoImmunizzazione() {
        return tipoImmunizzazione;
    }
    public int getNumRichiamo() {
        return numRichiamo;
    }
    public String getAntigen() {
        return antigen;
    }

    // Setter
    public  void setId(int id) {
        this.id = id;
    }public void setDa(String da) {
        this.da = da;
    }public void setA(String a) {
        this.a = a;
    }public void setTipoImmunizzazione(String tipoImmunizzazione) {
        this.tipoImmunizzazione = tipoImmunizzazione;
    }public void setNumRichiamo(int numRichiamo) {
        this.numRichiamo = numRichiamo;
    }public void setAntigen(String antigen) {
        this.antigen = antigen;
    }

    /**
     * Metodo che ritorna il content values dell'oggetto di classe Vaccinazione.
     * Un oggetto ContentValues è un oggetto che contiene tutti i dati dell'oggetto Vaccinazione, codificati
     * secondo la logica chiave-valore, dove la chiave è il nome della colonna in cui vogliamo inserire
     * il dato nella rispettiva tabella di database.
     *
     * @return l'oggetto ContentValues
     */
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, id);
        cv.put(COLUMN_DA, da);
        cv.put(COLUMN_A, a);
        cv.put(COLUMN_TIPOIMMUNIZZAZIONE, tipoImmunizzazione);
        cv.put(COLUMN_NUMRICHIAMO, numRichiamo);
        cv.put(COLUMN_ANTIGEN, antigen);
        return cv;
    }
}