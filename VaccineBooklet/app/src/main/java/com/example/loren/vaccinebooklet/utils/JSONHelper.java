package com.example.loren.vaccinebooklet.utils;

import android.util.Log;

import com.example.loren.vaccinebooklet.model.Utente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

public class JSONHelper {

    /**
     * Method that parse a {@link Utente}
     * @param result {@link String} of the response from the server
     * @return the object of the result user
     */
    public static ArrayList<Utente> parseUser(String result){
        ArrayList<Utente> users = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(result);
            for(int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                int id = object.getInt("ID");
                //Log.d("JSON", Integer.toString(id));
                String name = object.getString("Nome");
                //Log.d("JSON", name);
                String surname = object.getString("Cognome");
                //Log.d("JSON", surname);
                String birthdayDate = object.getString("DataNascita");
                //Log.d("JSON", birthdayDate);
                String type = object.getString("Tipo");
                //Log.d("JSON", type);
                String email = object.getString("Email");
                //Log.d("JSON", email);
                users.add(new Utente(id, name, surname, birthdayDate, type, email));
                //Log.d("JSON", users.toString());
            }
        } catch (JSONException e) {
            return null;
        } finally {
            return users;
        }
    }

    /**
     * @param result {@link String} of the response from the server
     * @return the synchronization version of remote db
     */
    public static int parseSyncVersion(String result){
        int version = -1;
        try {
            JSONArray array = new JSONArray(result);
            for(int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                version = object.getInt("Version");
            }
        } catch (JSONException e) {
            return -1;
        } finally {
            return version;
        }
    }
}
