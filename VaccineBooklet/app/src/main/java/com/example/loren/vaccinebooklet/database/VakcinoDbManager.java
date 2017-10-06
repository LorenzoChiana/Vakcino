package com.example.loren.vaccinebooklet.database;

import android.content.Context;

/**
 * Classe che si occupa delle interazioni con il database locale e remoto. Si tendono a centralizzare queste operazioni in modo da poterle
 * richiamare in diverse parti del codice.
 */
public class VakcinoDbManager {
    //Riferimento alla classe di helper.
    private final VakcinoDbHelper dbHelper;


    //Costruttore
    public VakcinoDbManager(Context context) {
        dbHelper = new VakcinoDbHelper(context);
    }

    public void getRemoteUsersInfo(String email) {

    }
}
