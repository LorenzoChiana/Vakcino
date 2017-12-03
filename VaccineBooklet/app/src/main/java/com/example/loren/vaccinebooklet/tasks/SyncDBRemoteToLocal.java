package com.example.loren.vaccinebooklet.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.activity.MainActivity;
import com.example.loren.vaccinebooklet.request.RemoteDBInteractions;
import com.example.loren.vaccinebooklet.utils.InternetConnection;
import com.example.loren.vaccinebooklet.utils.NetworkStateReceiver;

public class SyncDBRemoteToLocal extends AsyncTask<Void, Void, Boolean> {
    private final Context context;
    private boolean isAfterLogin;

    ProgressDialog progressDialog = null;

    public SyncDBRemoteToLocal(boolean isAfterLogin, Context context) {
        this.isAfterLogin = isAfterLogin;
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(context.getString(R.string.waiting));
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... args) {
            if (InternetConnection.haveInternetConnection(context)) {
                if (isAfterLogin) {
                    RemoteDBInteractions.createUsersRemoteToLocal(context);
                    RemoteDBInteractions.createVaccinationsRemoteToLocal(context);
                    RemoteDBInteractions.createVaccinationTypeRemoteToLocal(context);
                    RemoteDBInteractions.createBookletRemoteToLocal(context);
                }
                //RemoteDBInteractions.createBookletRemoteToLocal(context);
                RemoteDBInteractions.syncUsersRemoteToLocal(context);
                RemoteDBInteractions.syncVaccinationsRemoteToLocal(context);
                RemoteDBInteractions.syncVaccinationTypeRemoteToLocal(context);
                //RemoteDBInteractions.syncToDoRemoteToLocal(context);
                //get unsync users per ogni users
                new SyncDBLocalToRemote().execute(context);

            }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        //MainActivity.initializeNavigationUI(navigationView.getMenu(), context);
        NetworkStateReceiver nsr = new NetworkStateReceiver();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.INTENT_ACTION_INT);
        broadcastIntent.putExtra(MainActivity.INTENT_EXTRA, nsr.getIteration());
        nsr.nextIteration();
        context.sendBroadcast(broadcastIntent);
        progressDialog.dismiss();
    }
}
