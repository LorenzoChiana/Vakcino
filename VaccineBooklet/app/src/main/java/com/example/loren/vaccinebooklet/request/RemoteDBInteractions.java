package com.example.loren.vaccinebooklet.request;

import android.content.Context;
import android.content.Intent;

import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.utils.HTTPHelper;
import com.example.loren.vaccinebooklet.utils.JSONHelper;
import com.example.loren.vaccinebooklet.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class RemoteDBInteractions {
    private static final String URL_SET_SYNC_REMOTE_USER = "http://vakcinoapp.altervista.org/setSyncRemoteUser.php";
    private static final String URL_GET_UNSYNC_USER = "http://vakcinoapp.altervista.org/getUnsyncedUsers.php";
    private static final String URL_GET_USER = "http://vakcinoapp.altervista.org/getUsers.php";
    private static final String URL_SET_USER = "http://vakcinoapp.altervista.org/setUser.php";

    public static ArrayList<Utente> getRemoteUsers(String email) {
        HashMap<String,String> hm = new HashMap<>();
        hm.put("email", email);
        return JSONHelper.parseUser(HTTPHelper.connectPost(URL_GET_USER, hm));
    }

    public static ArrayList<Utente> getRemoteUnsyncedUsers(String email) {
        HashMap<String,String> hm = new HashMap<>();
        hm.put("email", email);
        return JSONHelper.parseUser(HTTPHelper.connectPost(URL_GET_UNSYNC_USER, hm));
    }

    public static void createUsersRemoteToLocal(Context context){
        ArrayList<Utente> remoteUsers = getRemoteUsers(Utils.getAccount(context));
        VakcinoDbManager dbManager = new VakcinoDbManager(context);
        for (Utente ru: remoteUsers) {
            if(!dbManager.updateUser(ru)){
                dbManager.addUser(ru);
            }
        }
    }

    public static void createUserLocalToRemote(Utente user) {
        HashMap<String,String> hm = new HashMap<>();
        hm.put("ID", Integer.toString(user.getId()));
        hm.put("Nome", user.getName());
        hm.put("Cognome", user.getSurname());
        hm.put("DataNascita", user.getbirthdayDate());
        hm.put("Tipo", user.getType());
        hm.put("Email", user.getEmail());
        hm.put("Status", Integer.toString(user.getStatus()));
        HTTPHelper.connectPost(URL_SET_USER, hm);
    }

    public static void syncUsersRemoteToLocal(Context context) {
        ArrayList<Utente> remoteUsers = getRemoteUnsyncedUsers(Utils.getAccount(context));
        VakcinoDbManager dbManager = new VakcinoDbManager(context);
        HashMap<String,String> hm = new HashMap<>();
        for (Utente ru: remoteUsers) {
            if(!dbManager.updateUser(ru)){
                dbManager.addUser(ru);
            }
            hm.put("id", Integer.toString(ru.getId()));
            HTTPHelper.connectPost(URL_SET_SYNC_REMOTE_USER, hm);
            hm.clear();
        }
    }
}
