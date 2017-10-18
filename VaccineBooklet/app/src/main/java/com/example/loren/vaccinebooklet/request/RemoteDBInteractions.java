package com.example.loren.vaccinebooklet.request;

import android.content.Context;
import android.content.Intent;

import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.model.Vaccinazione;
import com.example.loren.vaccinebooklet.utils.HTTPHelper;
import com.example.loren.vaccinebooklet.utils.JSONHelper;
import com.example.loren.vaccinebooklet.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class RemoteDBInteractions {
    private static final String URL_SET_SYNC_REMOTE_USER = "http://vakcinoapp.altervista.org/setSyncRemoteUser.php";
    private static final String URL_GET_UNSYNC_USER = "http://vakcinoapp.altervista.org/getUnsyncedUsers.php";
    private static final String URL_GET_UNSYNC_VACCINATION = "http://vakcinoapp.altervista.org/getUnsyncedVaccinations.php";
    private static final String URL_GET_UNSYNC_VACCINATIONTYPE = "http://vakcinoapp.altervista.org/getUnsyncedVaccinationType.php";
    private static final String URL_GET_USER = "http://vakcinoapp.altervista.org/getUsers.php";
    private static final String URL_SET_USER = "http://vakcinoapp.altervista.org/setUser.php";
    private static final String URL_GET_VACCINATIONS = "http://vakcinoapp.altervista.org/getVaccinations.php";
    private static final java.lang.String URL_SET_SYNC_REMOTE_VACCINATION = "http://vakcinoapp.altervista.org/setSyncRemoteVaccination.php";;
    private static final java.lang.String URL_GET_VACCINATIONTYPE = "http://vakcinoapp.altervista.org/getVaccinationType.php";
    private static final java.lang.String URL_SET_SYNC_REMOTE_VACCINATIONTYPE = "http://vakcinoapp.altervista.org/setSyncRemoteVaccinationType.php";

    /*
    * --- UTENTE ---
    * */
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
            ru.setStatus(VakcinoDbManager.SYNCED_WITH_SERVER);
            if(!dbManager.updateUser(ru)){
                dbManager.addUser(ru);
            }
        }
    }

    public static void syncUserLocalToRemote(Utente user) {
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
            ru.setStatus(VakcinoDbManager.SYNCED_WITH_SERVER);
            if(!dbManager.updateUser(ru)){
                dbManager.addUser(ru);
            }
            hm.put("id", Integer.toString(ru.getId()));
            HTTPHelper.connectPost(URL_SET_SYNC_REMOTE_USER, hm);
            hm.clear();
        }
    }

    /*
    * -- VACCINAZIONE
    * */

    public static ArrayList<Vaccinazione> getRemoteVaccinations() {
        return JSONHelper.parseVaccination(HTTPHelper.connectPost(URL_GET_VACCINATIONS));
    }

    public static void createVaccinationsRemoteToLocal(Context context){
        ArrayList<Vaccinazione> remoteVaccinations = getRemoteVaccinations();
        VakcinoDbManager dbManager = new VakcinoDbManager(context);
        for (Vaccinazione rv: remoteVaccinations) {
            if(!dbManager.updateVaccination(rv)){
                dbManager.addVaccination(rv);
            }
        }
    }

    public static ArrayList<Vaccinazione> getRemoteUnsyncedVaccinations() {
        return JSONHelper.parseVaccination(HTTPHelper.connectPost(URL_GET_UNSYNC_VACCINATION));
    }

    public static void syncVaccinationsRemoteToLocal(Context context) {
        ArrayList<Vaccinazione> remoteVaccinations = getRemoteUnsyncedVaccinations();
        VakcinoDbManager dbManager = new VakcinoDbManager(context);
        HashMap<String,String> hm = new HashMap<>();
        for (Vaccinazione rv: remoteVaccinations) {
            if(!dbManager.updateVaccination(rv)){
                dbManager.addVaccination(rv);
            }
            hm.put("Antigene", rv.getAntigen());
            HTTPHelper.connectPost(URL_SET_SYNC_REMOTE_VACCINATION, hm);
            hm.clear();
        }
    }

    /*
    * -- TIPO VACCINAZIONE
    * */

    public static ArrayList<TipoVaccinazione> getRemoteVaccinationType() {
        return JSONHelper.parseVaccinationType(HTTPHelper.connectPost(URL_GET_VACCINATIONTYPE));
    }

    public static void createVaccinationTypeRemoteToLocal(Context context){
        ArrayList<TipoVaccinazione> remoteVaccinationType = getRemoteVaccinationType();
        VakcinoDbManager dbManager = new VakcinoDbManager(context);
        for (TipoVaccinazione tv: remoteVaccinationType) {
            if(!dbManager.updateVaccinationType(tv)){
                dbManager.addVaccinationType(tv);
            }
        }
    }

    public static ArrayList<TipoVaccinazione> getRemoteUnsyncedVaccinationType() {
        return JSONHelper.parseVaccinationType(HTTPHelper.connectPost(URL_GET_UNSYNC_VACCINATIONTYPE));
    }

    public static void syncVaccinationTypeRemoteToLocal(Context context) {
        ArrayList<TipoVaccinazione> remoteVaccinationType = getRemoteUnsyncedVaccinationType();
        VakcinoDbManager dbManager = new VakcinoDbManager(context);
        HashMap<String,String> hm = new HashMap<>();
        for (TipoVaccinazione tv: remoteVaccinationType) {
            if(!dbManager.updateVaccinationType(tv)){
                dbManager.addVaccinationType(tv);
            }
            hm.put("id", Integer.toString(tv.getId()));
            HTTPHelper.connectPost(URL_SET_SYNC_REMOTE_VACCINATIONTYPE, hm);
            hm.clear();
        }
    }
}
