package com.example.loren.vaccinebooklet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.loren.vaccinebooklet.model.DeveFare;
import com.example.loren.vaccinebooklet.model.HaFatto;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.model.Vaccinazione;


public class VakcinoDbHelper extends SQLiteOpenHelper {

    //Dati del database
    private static final String DATABASE_NAME = "vakcino.db";
    private static final int DATABASE_VERSION = 1;

    //Riferimenti SQL alle costanti per creare tabelle di database
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String DATE_TYPE = " DATE";
    private static final String COMMA_SEP = ",";
    private static final String INTEGER_PRIMARY_KEY = INTEGER_TYPE + " PRIMARY KEY";
    private static final String TEXT_PRIMARY_KEY = TEXT_TYPE + " PRIMARY KEY";

    /**
     * Costruttore che richiama il costruttore padre, passando il riferimento al contesto, il nome del database e
     * la versione del database.
     *
     * @param context riferimento al contesto
     */
    public VakcinoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Query per la creazione delle tabelle, sfruttando i nomi delle colonne delle classi del modello.
    public static final String CREATE_TABLE_UTENTE = "CREATE TABLE IF NOT EXISTS "
            + Utente.TABLE_NAME + " (" +
            Utente.COLUMN_ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
            Utente.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
            Utente.COLUMN_SURNAME + TEXT_TYPE + COMMA_SEP +
            Utente.COLUMN_BIRTHDAY_DATE + DATE_TYPE + COMMA_SEP +
            Utente.COLUMN_TYPE + TEXT_TYPE + COMMA_SEP +
            Utente.COLUMN_EMAIL + TEXT_TYPE + COMMA_SEP +
            Utente.COLUMN_STATUS + INTEGER_TYPE + " )";

    public static final String CREATE_TABLE_VACCINAZIONE = "CREATE TABLE IF NOT EXISTS "
            + Vaccinazione.TABLE_NAME + " (" +
            Vaccinazione.COLUMN_ANTIGEN + TEXT_PRIMARY_KEY + COMMA_SEP +
            Vaccinazione.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
            Vaccinazione.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            Vaccinazione.COLUMN_GROUP + TEXT_TYPE + COMMA_SEP +
            Vaccinazione.COLUMN_STATUS + INTEGER_TYPE + " )";

    public static final String CREATE_TABLE_TIPOVACCINAZIONE = "CREATE TABLE IF NOT EXISTS "
            + TipoVaccinazione.TABLE_NAME + " (" +
            TipoVaccinazione.COLUMN_ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
            TipoVaccinazione.COLUMN_DA + TEXT_TYPE + COMMA_SEP +
            TipoVaccinazione.COLUMN_A + TEXT_TYPE + COMMA_SEP +
            TipoVaccinazione.COLUMN_TIPOIMMUNIZZAZIONE + TEXT_TYPE + COMMA_SEP +
            TipoVaccinazione.COLUMN_NUMRICHIAMO + INTEGER_TYPE + COMMA_SEP +
            TipoVaccinazione.COLUMN_ANTIGEN + TEXT_TYPE + COMMA_SEP +
            Vaccinazione.COLUMN_STATUS + INTEGER_TYPE + " )";

    public static final String CREATE_TABLE_HAFATTO = "CREATE TABLE IF NOT EXISTS "
            + HaFatto.TABLE_NAME + " (" +
            HaFatto.COLUMN_IDUTENTE + INTEGER_TYPE + COMMA_SEP +
            HaFatto.COLUMN_IDTIPOVAC + INTEGER_TYPE + COMMA_SEP +
            HaFatto.COLUMN_DATE + DATE_TYPE + COMMA_SEP +
            " CONSTRAINT PK_HAFATTO PRIMARY KEY (" +
            HaFatto.COLUMN_IDUTENTE + COMMA_SEP + HaFatto.COLUMN_IDTIPOVAC + ")"
            + " )";

    public static final String CREATE_TABLE_DEVEFARE = "CREATE TABLE IF NOT EXISTS "
            + DeveFare.TABLE_NAME + " (" +
            DeveFare.COLUMN_IDUTENTE + INTEGER_TYPE + COMMA_SEP +
            DeveFare.COLUMN_IDTIPOVAC + INTEGER_TYPE + COMMA_SEP +
            " CONSTRAINT PK_DEVEFARE PRIMARY KEY (" +
            DeveFare.COLUMN_IDUTENTE + COMMA_SEP + DeveFare.COLUMN_IDTIPOVAC + ")"
            + " )";

    /**
     * Metodo da overridare per aver esteso SQLiteOpenHelper; viene richiamato alla prima creazione del database.
     * In questo metodo, tramite il riferimento al database, vengono eseguite le query per la creazione delle tabelle
     * che comporranno il database.
     *
     * @param db riferimento al database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("DBMANAGER", CREATE_TABLE_UTENTE);
        db.execSQL(CREATE_TABLE_UTENTE);
        db.execSQL(CREATE_TABLE_TIPOVACCINAZIONE);
        db.execSQL(CREATE_TABLE_VACCINAZIONE);
        db.execSQL(CREATE_TABLE_DEVEFARE);
        db.execSQL(CREATE_TABLE_HAFATTO);
    }

    /**
     * Metodo da overridare per aver esteso SQLiteOpenHelper; viene richiamato ogni volta che viene cambiata la versione del database.
     * In questo metodo vengono effettuate le modifiche alla struttura del database, come l'aggiunta di nuove tabelle o la modifica
     * di quelle esistenti.
     *
     * @param db riferimento al database
     * @param oldVersion versione del database sul dispositivo
     * @param newVersion nuova versione del database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
