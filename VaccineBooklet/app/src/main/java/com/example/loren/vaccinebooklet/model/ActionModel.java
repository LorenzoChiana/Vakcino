package com.example.loren.vaccinebooklet.model;

import android.content.Context;
import android.util.Log;

import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.utils.HTTPHelper;
import com.example.loren.vaccinebooklet.utils.JSONHelper;
import com.example.loren.vaccinebooklet.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionModel {
    private static final String URL_GET_REMOTE_DB_VERSION = "http://vakcinoapp.altervista.org/getDBVersion.php";
    private String account;
    private Context context;
    private static final String URL_GET_USER = "http://vakcinoapp.altervista.org/getUsers.php";

    public ActionModel(Context context){
        this.context = context;
        this.account = Utils.getAccount(context);
    }

    public ArrayList<Utente> getUsers(){
        HashMap<String,String> hm = new HashMap<>();
        hm.put("email", account);
        String result = HTTPHelper.connectPost(URL_GET_USER, hm);
        //Log.d("JSON", result);
        ArrayList<Utente> users = JSONHelper.parseUser(result);
        return users;
    }

    public int getRemoteDBVersion() {
        HashMap<String,String> hm = new HashMap<>();
        hm.put("email", account);
        String result = HTTPHelper.connectPost(URL_GET_REMOTE_DB_VERSION, hm);
        Log.d("DBVersion", result);
        return JSONHelper.parseSyncVersion(result);
    }

}
