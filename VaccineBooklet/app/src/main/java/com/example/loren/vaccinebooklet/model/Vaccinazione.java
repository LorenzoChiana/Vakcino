package com.example.loren.vaccinebooklet.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

public class Vaccinazione implements BaseColumns, Serializable {

    //Nome della tabella che ospiterà gli oggetti di tipo Vaccinazione
    public static final String TABLE_NAME = "vaccinazione";

    //Nomi delle colonne della relativa tabella
    public static final String COLUMN_ANTIGEN = "antigen";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_GROUP = "gruppo";
    public static final String COLUMN_STATUS = "status";


    //Variabili del modello
    private String antigen;
    private String name;
    private String description;
    private String group;
    private int status;


    //Costruttore "standard"
    public Vaccinazione(String antigen, String name, String description, String group, int status) {
        this.antigen = antigen;
        this.name = name;
        this.description = description;
        this.group = group;
        this.status = status;
    }

    /**
     * Costruttore che crea un oggetto Vaccinazione da un cursore. Un cursore è una tipologia di oggetto che conterrà il risultato di una
     * query effettuata sul database. Per recuperare i dati da un cursore si utilizzano i vari metodi di get, ai quali viene passato
     * come parametro l'indice della colonna relativa al dato che si vuole ottenere. Invece che passare l'indice numerico vero e proprio,
     * si chiede al cursore stesso di ritrovare gli indici utilizzando il nome delle colonne relative al dato stesso.
     *
     * @param cursor oggetto cursore
     */
    public Vaccinazione(Cursor cursor) {
        this.antigen = cursor.getString(cursor.getColumnIndex(Vaccinazione.COLUMN_ANTIGEN));
        this.name = cursor.getString(cursor.getColumnIndex(Vaccinazione.COLUMN_NAME));
        this.description = cursor.getString(cursor.getColumnIndex(Vaccinazione.COLUMN_DESCRIPTION));
        this.group = cursor.getString(cursor.getColumnIndex(Vaccinazione.COLUMN_GROUP));
        this.status = cursor.getInt(cursor.getColumnIndex(Vaccinazione.COLUMN_STATUS));
    }


    //Metodi di get

    public String getAntigen() {
        return antigen;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getGroup() {
        return group;
    }
    public int getStatus() {
        return status;
    }

    //Metodi di set
    public void setAntigen(String antigen) {
        this.antigen = antigen;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public void setStatus(int status) {
        this.status = status;
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
        cv.put(COLUMN_ANTIGEN, antigen);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DESCRIPTION, description);
        cv.put(COLUMN_GROUP, group);
        cv.put(COLUMN_STATUS, status);
        return cv;
    }
}
