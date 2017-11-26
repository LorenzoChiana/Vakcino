package com.example.loren.vaccinebooklet.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.loren.vaccinebooklet.model.Libretto;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.model.Vaccinazione;
import com.example.loren.vaccinebooklet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che si occupa delle interazioni con il database locale. Si tendono a centralizzare queste operazioni in modo da poterle
 * richiamare in diverse parti del codice.
 */
public class VakcinoDbManager {
    //Riferimento alla classe di helper.
    private final VakcinoDbHelper dbHelper;

    public static final int SYNCED_WITH_SERVER = 0;
    public static final int NOT_SYNCED_WITH_SERVER = 1;

    public static final int DONE = 0;
    public static final int NOT_DONE = 1;
    public static final int DELETED = 2;


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
        Log.d("DBMANAGER", Long.toString(row));
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
                Utente.COLUMN_ID + " = ?",
                new String[]{Integer.toString(user.getId())});
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
                    " WHERE email = '" + email +
                    "' AND STATUS <> " + DELETED +
                    " ORDER BY " + Utente.COLUMN_NAME + ", " + Utente.COLUMN_SURNAME + " ASC";
            Log.d("DBMANAGER", query);
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

    public List<Utente> getAllUsers(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Utente> users = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Utente.TABLE_NAME +
                    " WHERE email = '" + email +
                    "' ORDER BY " + Utente.COLUMN_NAME + ", " + Utente.COLUMN_SURNAME + " ASC";
            Log.d("DBMANAGER", query);
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

    public List<Utente> getLocalUnsyncedUsers() {
        return getLocalUsers(NOT_SYNCED_WITH_SERVER);
    }

    public List<Utente> getLocalDeletedUsers() {
       return getLocalUsers(DELETED);
    }

    private List<Utente> getLocalUsers(int flag) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Utente> users = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Utente.TABLE_NAME +
                    " WHERE " + Utente.COLUMN_STATUS + " = " + flag;
            Log.d("DBMANAGER", query);
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
     * Metodo di modifica di una vaccinazione nel database; l'oggetto viene passato come parametro.
     * Viene poi innanzitutto recuperato il riferimento al database (modalità scrittura),
     * dopodiché viene richamato il metodo update, a cui viene passato il nome della tabella in cui effettuare
     * la modifica, il ContentValues dell'oggetto che si vuole modificare, e come ultimi due parametri, la condizione
     * da verificare per effettuare la modifica, e il valore che sarà oggetto del confronto (in questo caso viene verificato
     * che gli id delle righe prese in considerazione siano uguali a quello dell'oggetto passato).
     *
     * @param vac riferimento all'oggetto che si vuole modificare nel database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean updateVaccination(Vaccinazione vac) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.update(Vaccinazione.TABLE_NAME, vac.getContentValues(),
                Vaccinazione.COLUMN_ANTIGEN + " = ? ", new String[]{vac.getAntigen()});
        return row > 0;
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
    public boolean addVaccinationType(TipoVaccinazione typeVaccination) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long row = db.insert(TipoVaccinazione.TABLE_NAME, null, typeVaccination.getContentValues());
        return row > 1;
    }

    /**
     * Metodo di modifica di un tipo di vaccinazione nel database; l'oggetto viene passato come parametro.
     * Viene poi innanzitutto recuperato il riferimento al database (modalità scrittura),
     * dopodiché viene richamato il metodo update, a cui viene passato il nome della tabella in cui effettuare
     * la modifica, il ContentValues dell'oggetto che si vuole modificare, e come ultimi due parametri, la condizione
     * da verificare per effettuare la modifica, e il valore che sarà oggetto del confronto (in questo caso viene verificato
     * che gli id delle righe prese in considerazione siano uguali a quello dell'oggetto passato).
     *
     * @param vt riferimento all'oggetto che si vuole modificare nel database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean updateVaccinationType(TipoVaccinazione vt) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.update(TipoVaccinazione.TABLE_NAME, vt.getContentValues(),
                TipoVaccinazione.COLUMN_ID + " = ? ", new String[]{Integer.toString(vt.getId())});
        return row > 0;
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
    public List<TipoVaccinazione> getVaccinationType() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<TipoVaccinazione> typeVaccinations = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TipoVaccinazione.TABLE_NAME +
                    " ORDER BY cast(" + TipoVaccinazione.COLUMN_DA + " as INTEGER) ASC";
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
    * --------- LIBRETTO ---------
    *
    * */
    /**
     * @param booklet riferimento all'oggetto che si vuole inserire nel database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean addBooklet(Libretto booklet) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long row = db.insert(Libretto.TABLE_NAME, null, booklet.getContentValues());
        return row > 1;
    }

    /**
     * @param booklet riferimento all'oggetto che si vuole modificare nel database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean updateBooklet(Libretto booklet) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.update(
                Libretto.TABLE_NAME,
                booklet.getContentValues(),
                Libretto.COLUMN_IDUTENTE + " = ? AND " +
                Libretto.COLUMN_IDTIPOVAC + " = ?",
                new String[]{
                        Integer.toString(booklet.getIdUtente()),
                        Integer.toString(booklet.getIdTipoVac())
                }
        );
        return row > 0;
    }

    /**
     * @param booklet riferimento all'oggetto che si vuole rimuovere dal database
     * @return booleano che indica se l'operazione ha avuto o meno esito positivo
     */
    public boolean deleteBooklet(Libretto booklet) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int row = db.delete(Libretto.TABLE_NAME,
                Libretto.COLUMN_IDUTENTE + " = ? AND " + Libretto.COLUMN_IDTIPOVAC + " = ?",
                new String[]{Integer.toString(booklet.getIdUtente()), Integer.toString(booklet.getIdTipoVac())});
        return row > 0;
    }

    public List<Libretto> getAllBooklet(Utente user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Libretto> bookletList = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Libretto.TABLE_NAME +
                    " WHERE " + Libretto.COLUMN_IDUTENTE + " = " + user.getId();
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Libretto booklet = new Libretto(cursor);
                bookletList.add(booklet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return bookletList;
    }

    public List<Libretto> getToDoList(Utente user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Libretto> bookletList = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Libretto.TABLE_NAME +
                    " WHERE " + Libretto.COLUMN_IDUTENTE + " = " + user.getId() +
                    " AND " + Libretto.COLUMN_DONE + " = " + NOT_DONE;
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Libretto booklet = new Libretto(cursor);
                bookletList.add(booklet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return bookletList;
    }

    public List<Libretto> getDoneList(Utente user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Libretto> bookletList = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Libretto.TABLE_NAME +
                    " WHERE " + Libretto.COLUMN_IDUTENTE + " = " + user.getId() +
                    " AND " + Libretto.COLUMN_DONE + " = " + DONE;
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Libretto booklet = new Libretto(cursor);
                bookletList.add(booklet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return bookletList;
    }

    public List<Libretto> getUnsyncedBooklet(Utente user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Libretto> bookletList = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Libretto.TABLE_NAME +
                    " WHERE " + Libretto.COLUMN_IDUTENTE + " = " + user.getId() +
                    " AND " + Libretto.COLUMN_STATUS + " = " + NOT_SYNCED_WITH_SERVER;
            Log.d("DBMANAGER", query);
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Libretto booklet = new Libretto(cursor);
                bookletList.add(booklet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return bookletList;
    }

    public List<Libretto> getUnsyncedToDoBooklet(Utente user) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Libretto> bookletList = new ArrayList<>();

        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + Libretto.TABLE_NAME +
                    " WHERE " + Libretto.COLUMN_IDUTENTE + " = " + user.getId() +
                    " AND " + Libretto.COLUMN_DONE + " = " + NOT_DONE +
                    " AND " + Libretto.COLUMN_STATUS + " = " + NOT_SYNCED_WITH_SERVER;
            Log.d("DBMANAGER", query);
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                Libretto booklet = new Libretto(cursor);
                bookletList.add(booklet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return bookletList;
    }

    public void deleteTable(String tableName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(tableName, null, null);
    }
}
