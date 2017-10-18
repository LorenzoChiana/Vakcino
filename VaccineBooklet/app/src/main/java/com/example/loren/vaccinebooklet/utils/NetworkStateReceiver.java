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
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.request.RemoteDBInteractions;

import java.util.List;

public class NetworkStateReceiver extends BroadcastReceiver {
    private boolean isAfterLogin;
    int i = 0;

    public boolean isAfterLogin() {
        return isAfterLogin;
    }

    public void setAfterLogin(boolean afterLogin) {
        isAfterLogin = afterLogin;
    }

    public void onReceive(final Context context, Intent intent) {
        Log.d("app", "Network connectivity change");
        if (intent.getExtras() != null) {
            //NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED) {
                Log.i("app", "Network " + activeNetwork.getTypeName() + " connected");
                Toast toast = Toast.makeText(context, "Network " + activeNetwork.getTypeName() + " connected", Toast.LENGTH_SHORT);
                toast.show();
                final ProgressDialog progressDialog = new ProgressDialog(context,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(context.getString(R.string.waiting));
                progressDialog.show();
                AsyncTask<Void, Void, Boolean> sync = new AsyncTask<Void, Void, Boolean>() {
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
                            //get unsync users per ogni users
                            VakcinoDbManager dbManager = new VakcinoDbManager(context);
                            List<Utente> unsyncedUsers = dbManager.getUnsyncedUsers(Utils.getAccount(context));
                            for (Utente user: unsyncedUsers) {
                                RemoteDBInteractions.syncUserLocalToRemote(user);
                                user.setStatus(VakcinoDbManager.SYNCED_WITH_SERVER);
                                dbManager.updateUser(user);
                            }

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
                        progressDialog.dismiss();
                    }
                }.execute();
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d("app", "There's no network connectivity");
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(MainActivity.INTENT_ACTION_INT);
                broadcastIntent.putExtra(MainActivity.INTENT_EXTRA, i);
                i++;
                context.sendBroadcast(broadcastIntent);
                Toast toast = Toast.makeText(context, "There's no network connectivity", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}