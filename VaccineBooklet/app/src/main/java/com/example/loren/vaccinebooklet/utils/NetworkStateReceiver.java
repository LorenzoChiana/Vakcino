package com.example.loren.vaccinebooklet.utils;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.activity.MainActivity;
import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.model.Libretto;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.request.RemoteDBInteractions;
import com.example.loren.vaccinebooklet.tasks.SyncDBLocalToRemote;
import com.example.loren.vaccinebooklet.tasks.SyncDBRemoteToLocal;

import java.security.PublicKey;
import java.util.List;

public class NetworkStateReceiver extends BroadcastReceiver {
    private boolean isAfterLogin;
    private int iteration = 0;

    public boolean isAfterLogin() {
        return isAfterLogin;
    }

    public void setAfterLogin(boolean afterLogin) {
        isAfterLogin = afterLogin;
    }

    public int getIteration() {
        return this.iteration;
    }

    public void nextIteration() {
        this.iteration++;
    }

    public void onReceive(final Context context, Intent intent) {
        if (intent.getExtras() != null) {
            //NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED) {

                new SyncDBRemoteToLocal(isAfterLogin(), context).execute();

            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(MainActivity.INTENT_ACTION_INT);
                broadcastIntent.putExtra(MainActivity.INTENT_EXTRA, iteration);
                iteration++;
                context.sendBroadcast(broadcastIntent);
            }
        }
    }

    /*private class SyncRemoteToLocal extends AsyncTask<Void, Void, Boolean> {
        protected Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(context.getString(R.string.waiting));
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... args) {

            if (InternetConnection.haveInternetConnection(context)) {
                if (isAfterLogin()) {
                    RemoteDBInteractions.createUsersRemoteToLocal(context);
                    RemoteDBInteractions.createVaccinationsRemoteToLocal(context);
                    RemoteDBInteractions.createVaccinationTypeRemoteToLocal(context);
                }
                RemoteDBInteractions.syncUsersRemoteToLocal(context);
                RemoteDBInteractions.syncVaccinationsRemoteToLocal(context);
                RemoteDBInteractions.syncVaccinationTypeRemoteToLocal(context);

            }
            return true;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            //MainActivity.initializeNavigationUI(navigationView.getMenu(), context);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(MainActivity.INTENT_ACTION_INT);
            broadcastIntent.putExtra(MainActivity.INTENT_EXTRA, i);
            i++;
            context.sendBroadcast(broadcastIntent);
            //progressDialog.dismiss();
            //new SyncLocalToRemote().execute(context);
        }
    }

    private class SyncLocalToRemote extends AsyncTask<Context, Void, Void> {

        @Override
        protected Void doInBackground(Context... args) {

            if (InternetConnection.haveInternetConnection(args[0])) {
                //get unsync users per ogni users
                VakcinoDbManager dbManager = new VakcinoDbManager(args[0]);
                List<Utente> unsyncedUsers = dbManager.getUnsyncedUsers(Utils.getAccount(args[0]));
                for (Utente user : unsyncedUsers) {
                    RemoteDBInteractions.syncUserLocalToRemote(user);
                    user.setStatus(VakcinoDbManager.SYNCED_WITH_SERVER);
                    dbManager.updateUser(user);
                }

                for (Utente user : dbManager.getUsers(Utils.getAccount(args[0]))) {
                    List<DeveFare> unsyncedToDo = dbManager.getUnsyncedToDo(user);
                    for (DeveFare toDo : unsyncedToDo) {
                        RemoteDBInteractions.syncToDoLocalToRemote(toDo);
                    }
                }


            }
            return null;
        }
    }*/
}