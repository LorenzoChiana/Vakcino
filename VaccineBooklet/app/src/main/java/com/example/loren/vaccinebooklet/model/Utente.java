package com.example.loren.vaccinebooklet.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.io.Serializable;

public class Utente implements BaseColumns, Serializable {

    //Nome della tabella che ospiterà gli oggetti di tipo Utente
    public static final String TABLE_NAME = "utente";

    //Nomi delle colonne della relativa tabella
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_BIRTHDAY_DATE = "birthdayDate";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_EMAIL = "email";


    //Variabili del modello
    private int id;
    private String name;
    private String surname;
    private String birthdayDate;
    private String type;
    private String email;


    //Costruttore "standard"
    public Utente(int ID, String name, String surname, String birthdayDate, String type, String email) {
        this.id = ID;
        this.name = name;
        this.surname = surname;
        this.birthdayDate = birthdayDate;
        this.type = type;
        this.email = email;
    }

    /**
     * Costruttore che crea un oggetto Utente da un cursore. Un cursore è una tipologia di oggetto che conterrà il risultato di una
     * query effettuata sul database. Per recuperare i dati da un cursore si utilizzano i vari metodi di get, ai quali viene passato
     * come parametro l'indice della colonna relativa al dato che si vuole ottenere. Invece che passare l'indice numerico vero e proprio,
     * si chiede al cursore stesso di ritrovare gli indici utilizzando il nome delle colonne relative al dato stesso.
     *
     * @param cursor oggetto cursore
     */
    public Utente(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(Utente.COLUMN_ID));
        this.name = cursor.getString(cursor.getColumnIndex(Utente.COLUMN_NAME));
        this.surname = cursor.getString(cursor.getColumnIndex(Utente.COLUMN_SURNAME));
        this.birthdayDate = cursor.getString(cursor.getColumnIndex(Utente.COLUMN_BIRTHDAY_DATE));
        this.type = cursor.getString(cursor.getColumnIndex(Utente.COLUMN_TYPE));
        this.email = cursor.getString(cursor.getColumnIndex(Utente.COLUMN_EMAIL));
    }


    //Metodi di get
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getbirthdayDate() {
        return birthdayDate;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }


    //Metodi di set
    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthdayDate(String birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public void setType(String type) { this.type = type; }

    public void setEmail(String email) {
        this.email = email;
    }


    //Override del metodo toString per rappresnetare i valori di "name" e "surname" di Utente in una sola stringa.
    @Override
    public String toString() {
        return surname + " " + name;
    }


    /**
     * Metodo che ritorna il content values dell'oggetto di classe Utente.
     * Un oggetto ContentValues è un oggetto che contiene tutti i dati dell'oggetto Utente, codificati
     * secondo la logica chiave-valore, dove la chiave è il nome della colonna in cui vogliamo inserire
     * il dato nella rispettiva tabella di database.
     *
     * @return l'oggetto ContentValues
     */
    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_SURNAME, surname);
        cv.put(COLUMN_BIRTHDAY_DATE, birthdayDate);
        cv.put(COLUMN_TYPE, type);
        cv.put(COLUMN_EMAIL, email);
        return cv;
    }
}
