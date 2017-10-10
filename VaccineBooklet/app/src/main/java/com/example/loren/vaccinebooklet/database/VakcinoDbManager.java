package com.example.loren.vaccinebooklet.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.loren.vaccinebooklet.model.DeveFare;
import com.example.loren.vaccinebooklet.model.HaFatto;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.model.Vaccinazione;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che si occupa delle interazioni con il database locale. Si tendono a centralizzare queste operazioni in modo da poterle
 * richiamare in diverse parti del codice.
 */
public class VakcinoDbManager {
    //Riferimento alla classe di helper.
    private final VakcinoDbHelper dbHelper;


    //Costruttore
    public VakcinoDbManager(Context context) {
        dbHelper = new VakcinoDbHelper(context);
    }

    /*
    *
    * --------- UTENTE ---------
    *
    * */
    /**
     * Metodo di aggiunta di un utente nel database; l'oggetto viene passato come parametro.
     * Viene poi innanzitutto recuperato il riferimento al database (modalità scrittura),
     * dopodiché viene richamato il metodo insert, a cui viene passato il nome della tabella in cui effettuare
     * l'inserimento, valore null in corrispondenza di nullColumnHack, e il ContentValues dell'oggetto che si
     * vuole inserire.
     *
     * @param user riferimento all'oggetto che si vuole inserire nel database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean addUser(Utente user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long row = db.insert(Utente.TABLE_NAME, null, user.getContentValues());
        return row > 1;
    }

    /**
     * Metodo di modifica di un utente nel database; l'oggetto viene passato come parametro.
     * Viene poi innanzitutto recuperato il riferimento al database (modalità scrittura),
     * dopodiché viene richamato il metodo update, a cui viene passato il nome della tabella in cui effettuare
     * la modifica, il ContentValues dell'oggetto che si vuole modificare, e come ultimi due parametri, la condizione
     * da verificare per effettuare la modifica, e il valore che sarà oggetto del confronto (in questo caso viene verificato
     * che gli id delle righe prese in considerazione siano uguali a quello dell'oggetto passato).
     *
     * @param user riferimento all'oggetto che si vuole modificare nel database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean updateUser(Utente user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.update(Utente.TABLE_NAME, user.getContentValues(),
                Utente.COLUMN_ID + " = ? ", new String[]{Integer.toString(user.getId())});
        return row > 0;
    }

    /**
     * Metodo di rimozione di un utente dal database; l'oggetto viene passato come parametro.
     * Viene poi innanzitutto recuperato il riferimento al database (modalità scrittura),
     * dopodiché viene richamato il metodo delete, a cui viene passato il nome della tabella in cui effettuare
     * la rimozione, la condizione da verificare per effettuare la rimozione, e il valore che sarà oggetto del confronto
     * (in questo caso viene verificato che gli id delle righe prese in considerazione siano uguali a quello dell'oggetto passato).
     *
     * @param user riferimento all'oggetto che si vuole rimuovere dal database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean deleteUser(Utente user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.delete(Utente.TABLE_NAME, Utente.COLUMN_ID + " = ?", new String[]{Integer.toString(user.getId())});
        return row > 0;
    }

    /**
     * Metodo di lettura di tutti gli utenti di un certo account dal database.
     * Viene poi innanzitutto recuperato il riferimento al database (modalità lettura); viene poi predisposto un oggetto di tipo Cursor
     * che osipterà il risultato della query e una lista che conterrà le persone lette dal database.
     * In una stringa viene composta la query e tramite il riferimento al database viene eseguita, ponendo il risultato all'interno
     * del cursore. Il cursore, tramite il metodo moveToNext() viene ciclato e per ogni "riga" di risultato viene creato un oggetto di tipo
     * Person che viene poi aggiunto alla lista.
     *
     * @param email l'email dell'account del quale si vuole visualizzare gli utenti
     * @return lista contenente gli utenti di un certo account letti dal database
     */
    public List<Utente> getUsers(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Utente> users = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Utente.TABLE_NAME +
                    " WHERE email = " + email +
                    " ORDER BY " + Utente.COLUMN_NAME + " ASC";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Utente user = new Utente(cursor);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return users;
    }

    /*
    *
    * --------- VACCINAZIONE ---------
    *
    * */
    /**
     * Metodo di aggiunta di una vaccinazione nel database; l'oggetto viene passato come parametro.
     * Viene poi innanzitutto recuperato il riferimento al database (modalità scrittura),
     * dopodiché viene richamato il metodo insert, a cui viene passato il nome della tabella in cui effettuare
     * l'inserimento, valore null in corrispondenza di nullColumnHack, e il ContentValues dell'oggetto che si
     * vuole inserire.
     *
     * @param vaccination riferimento all'oggetto che si vuole inserire nel database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean addVaccination(Vaccinazione vaccination) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long row = db.insert(Vaccinazione.TABLE_NAME, null, vaccination.getContentValues());
        return row > 1;
    }

    /**
     * Metodo di lettura di tutti i vaccini dal database.
     * Viene poi innanzitutto recuperato il riferimento al database (modalità lettura); viene poi predisposto un oggetto di tipo Cursor
     * che osipterà il risultato della query e una lista che conterrà le persone lette dal database.
     * In una stringa viene composta la query e tramite il riferimento al database viene eseguita, ponendo il risultato all'interno
     * del cursore. Il cursore, tramite il metodo moveToNext() viene ciclato e per ogni "riga" di risultato viene creato un oggetto di tipo
     * Person che viene poi aggiunto alla lista.
     *
     * @return lista contenente i vaccini letti dal database
     */
    public List<Vaccinazione> getVaccinations() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Vaccinazione> vaccinations = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Vaccinazione.TABLE_NAME +
                    " ORDER BY " + Vaccinazione.COLUMN_ANTIGEN + " ASC";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Vaccinazione vaccination = new Vaccinazione(cursor);
                vaccinations.add(vaccination);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return vaccinations;
    }

        /*
    *
    * --------- TIPO VACCINAZIONE ---------
    *
    * */
    /**
     * Metodo di aggiunta di una vaccinazione nel database; l'oggetto viene passato come parametro.
     * Viene poi innanzitutto recuperato il riferimento al database (modalità scrittura),
     * dopodiché viene richamato il metodo insert, a cui viene passato il nome della tabella in cui effettuare
     * l'inserimento, valore null in corrispondenza di nullColumnHack, e il ContentValues dell'oggetto che si
     * vuole inserire.
     *
     * @param typeVaccination riferimento all'oggetto che si vuole inserire nel database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean addTypeVaccination(TipoVaccinazione typeVaccination) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long row = db.insert(TipoVaccinazione.TABLE_NAME, null, typeVaccination.getContentValues());
        return row > 1;
    }

    /**
     * Metodo di lettura di tutti i vaccini dal database.
     * Viene poi innanzitutto recuperato il riferimento al database (modalità lettura); viene poi predisposto un oggetto di tipo Cursor
     * che osipterà il risultato della query e una lista che conterrà le persone lette dal database.
     * In una stringa viene composta la query e tramite il riferimento al database viene eseguita, ponendo il risultato all'interno
     * del cursore. Il cursore, tramite il metodo moveToNext() viene ciclato e per ogni "riga" di risultato viene creato un oggetto di tipo
     * Person che viene poi aggiunto alla lista.
     *
     * @return lista contenente i vaccini letti dal database
     */
    public List<TipoVaccinazione> getTypeVaccinations() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<TipoVaccinazione> typeVaccinations = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TipoVaccinazione.TABLE_NAME +
                    " ORDER BY " + TipoVaccinazione.COLUMN_DA + " ASC";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                TipoVaccinazione typeVaccination = new TipoVaccinazione(cursor);
                typeVaccinations.add(typeVaccination);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return typeVaccinations;
    }

    /*
    *
    * --------- DEVE FARE ---------
    *
    * */
    /**
     * @param toDo riferimento all'oggetto che si vuole inserire nel database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean addToDo(DeveFare toDo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long row = db.insert(DeveFare.TABLE_NAME, null, toDo.getContentValues());
        return row > 1;
    }

    /**
     * @param toDo riferimento all'oggetto che si vuole modificare nel database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean updateToDO(DeveFare toDo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.update(DeveFare.TABLE_NAME, toDo.getContentValues(),
                DeveFare.COLUMN_IDUTENTE + " = ? AND " + DeveFare.COLUMN_IDTIPOVAC + " = ?",
                new String[]{Integer.toString(toDo.getIdUtente()), Integer.toString(toDo.getIdTipoVac())});
        return row > 0;
    }

    /**
     * @param toDo riferimento all'oggetto che si vuole rimuovere dal database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean deleteToDo(DeveFare toDo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.delete(Utente.TABLE_NAME,
                DeveFare.COLUMN_IDUTENTE + " = ? AND " + DeveFare.COLUMN_IDTIPOVAC + " = ?",
                new String[]{Integer.toString(toDo.getIdUtente()), Integer.toString(toDo.getIdTipoVac())});
        return row > 0;
    }

    /*
    *
    * --------- HA FATTO ---------
    *
    * */
    /**
     * @param done riferimento all'oggetto che si vuole inserire nel database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean addDone(HaFatto done) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long row = db.insert(HaFatto.TABLE_NAME, null, done.getContentValues());
        return row > 1;
    }

    /**
     * @param done riferimento all'oggetto che si vuole modificare nel database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean updateDone(HaFatto done) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.update(HaFatto.TABLE_NAME, done.getContentValues(),
                HaFatto.COLUMN_IDUTENTE + " = ? AND " + HaFatto.COLUMN_IDTIPOVAC + " = ?",
                new String[]{Integer.toString(done.getIdUtente()), Integer.toString(done.getIdTipoVac())});
        return row > 0;
    }

    /**     
     * @param done riferimento all'oggetto che si vuole rimuovere dal database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean deleteDone(HaFatto done) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.delete(HaFatto.TABLE_NAME,
                HaFatto.COLUMN_IDUTENTE + " = ? AND " + HaFatto.COLUMN_IDTIPOVAC + " = ?",
                new String[]{Integer.toString(done.getIdUtente()), Integer.toString(done.getIdTipoVac())});
        return row > 0;
    }

    public void deleteTable(String tableName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(tableName, null, null);
    }
}
