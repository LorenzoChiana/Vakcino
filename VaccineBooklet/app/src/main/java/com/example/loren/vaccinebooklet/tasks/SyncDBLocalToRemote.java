package com.example.loren.vaccinebooklet.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.model.Libretto;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.request.RemoteDBInteractions;
import com.example.loren.vaccinebooklet.utils.Utils;

import java.util.List;

public class SyncDBLocalToRemote extends AsyncTask<Context, Void, Void> {


    @Override
    protected Void doInBackground(Context... context) {
        VakcinoDbManager dbManager = new VakcinoDbManager(context[0]);
        List<Utente> unsyncedUsers = dbManager.getLocalUnsyncedUsers();
        for (Utente user : unsyncedUsers) {
            RemoteDBInteractions.syncUserLocalToRemote(user);
            user.setStatus(VakcinoDbManager.SYNCED_WITH_SERVER);
            dbManager.updateUser(user);
        }

        List<Utente> deletedUsers = dbManager.getLocalDeletedUsers();
        for (Utente user : deletedUsers) {
            RemoteDBInteractions.syncDeleteUserLocalToRemote(user);
            List<Libretto> booklet = dbManager.getAllBooklet(user);
            for (Libretto l: booklet) {
                dbManager.deleteBooklet(l);
            }
            dbManager.deleteUser(user);
        }

        for (Utente user : dbManager.getUsers(Utils.getAccount(context[0]))) {
            List<Libretto> unsyncedBooklet = dbManager.getUnsyncedToDoBooklet(user);
            for (Libretto booklet : unsyncedBooklet) {
                RemoteDBInteractions.syncBookletLocalToRemote(booklet, Utils.getAccount(context[0]));
                booklet.setStatus(VakcinoDbManager.SYNCED_WITH_SERVER);
                dbManager.updateBooklet(booklet);
            }
        }
        return null;
    }
}
