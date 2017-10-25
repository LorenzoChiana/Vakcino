package com.example.loren.vaccinebooklet.utils;

import com.example.loren.vaccinebooklet.model.Libretto;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.model.Vaccinazione;

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
                int status = object.getInt("Status");
                //Log.d("JSON", Integer.toString(status));
                users.add(new Utente(id, name, surname, birthdayDate, type, email, status));
                //Log.d("JSON", users.toString());
            }
        } catch (JSONException e) {
            return null;
        } finally {
            return users;
        }
    }

    /**
     * Method that parse a {@link Vaccinazione}
     * @param result {@link String} of the response from the server
     * @return the object of the result user
     */
    public static ArrayList<Vaccinazione> parseVaccination(String result){
        ArrayList<Vaccinazione> vaccinations = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(result);
            for(int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String antigen = object.getString("Antigene");
                String name = object.getString("Nome");
                String description = object.getString("Descrizione");
                String group = object.getString("Gruppo");
                int status = object.getInt("Status");
                vaccinations.add(new Vaccinazione(antigen, name, description, group, status));
            }
        } catch (JSONException e) {
            return null;
        } finally {
            return vaccinations;
        }
    }


    /**
     * Method that parse a {@link TipoVaccinazione}
     * @param result {@link String} of the response from the server
     * @return the object of the result user
     */
    public static ArrayList<TipoVaccinazione> parseVaccinationType(String result){
        ArrayList<TipoVaccinazione> vaccinationsType = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(result);
            for(int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                int id = object.getInt("ID");
                int da = object.getInt("Da");
                int a = object.getInt("A");
                String ti = object.getString("TipoImmunizzazione");
                int numRichiamo = object.getInt("NumRichiamo");
                String antigene = object.getString("Antigene");
                int status = object.getInt("Status");
                vaccinationsType.add(new TipoVaccinazione(id, da, a, ti, numRichiamo, antigene, status));
            }
        } catch (JSONException e) {
            return null;
        } finally {
            return vaccinationsType;
        }
    }

    /**
     * Method that parse a {@link Libretto}
     * @param result {@link String} of the response from the server
     * @return the object of the result user
     */
    public static ArrayList<Libretto> parseBooklet(String result){
        ArrayList<Libretto> bookletList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(result);
            for(int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                int idUser = object.getInt("ID");
                int idVac = object.getInt("ID_VAC");
                int done = object.getInt("Fatto");
                String date = object.getString("InData");
                int status = object.getInt("Status");
                bookletList.add(new Libretto(idUser, idVac, done, date, status));
            }
        } catch (JSONException e) {
            return null;
        } finally {
            return bookletList;
        }
    }
}
