package com.example.loren.vaccinebooklet;



import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.loren.vaccinebooklet.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private ProgressBar pgbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        pgbLoading = (ProgressBar) findViewById(R.id.pgb_loading);

        if (Utils.getLogged(this)) {
            Intent openList = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(openList);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                if (!username.equals("") && !password.equals("")) {
                    new LoginAsyncTask().execute(username, password);
                } else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(R.string.dialog_title_attention)
                            .setMessage(R.string.error_empty_form)
                            .setPositiveButton(R.string.dialog_response_ok, null)
                            .setCancelable(false)
                            .show();
                }
            }
        });
    }


    /**
     * Classe che estende AsyncTask per la gestione della chiamata al server.
     * Per dettagli sull'AsyncTask vedere: async-example.
     */
    private class LoginAsyncTask extends AsyncTask<String, Void, Boolean> {

        private static final String CONN_USERNAME = "username";
        private static final String CONN_PASSWORD = "password";

        //Nome dei parametri del json di risposta
        private static final String JSON_MESSAGE = "message";
        private static final String JSON_SUCCESS = "success";

        //URL su cui effettuare la chiamata
        private static final String URL = "http://demoweb.labinfo.net/univ/auth.php";

        private String message;


        @Override
        protected void onPreExecute() {
            pgbLoading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            Log.d("LoginAsyncTask", "urlToConnect: " + URL);

            /**
             * Creazione delle strutture Java per una chiamata al server.
             */
            String username = "";
            String password = "";
            if (params.length > 0) {
                username = params[0];
                if (params.length > 1) {
                    password = params[1];
                }
            }

            BufferedReader bufferedReader = null;
            HttpURLConnection connection = null;
            try {
                //Impostazioni della connessione Http
                java.net.URL url = new URL(URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(60 * 1000);
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(60 * 1000);
                connection.setDoInput(true);
                connection.setUseCaches(false);

                Map<String, String> connParams = new HashMap<>();
                connParams.put(CONN_USERNAME, username);
                connParams.put(CONN_PASSWORD, password);
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(connParams));
                writer.flush();
                writer.close();
                os.close();

                //Gestione del codice di risposta del server
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    StringBuilder response = new StringBuilder();
                    InputStream inputStream = connection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }

                    JSONObject responseJson = new JSONObject(response.toString());
                    message = responseJson.optString(JSON_MESSAGE);
                    return responseJson.has(JSON_SUCCESS) && responseJson.getBoolean(JSON_SUCCESS);
                }
                //Gestione delle eccezioni
            } catch (MalformedURLException e) {
                Log.e("LoginAsyncTask", "L'url non è formattato correttamente", e);
            } catch (IOException e) {
                Log.e("LoginAsyncTask", "Errore durante la connessione con il server", e);
            } catch (JSONException e) {
                Log.e("LoginAsyncTask", "Errore durante la deserializzazioen della risposta", e);
            } finally {
                //Chiusura della connessione e del BufferedReader
                if (connection != null)
                    connection.disconnect();
                try {
                    if (bufferedReader != null)
                        bufferedReader.close();
                } catch (Exception ignored) {
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pgbLoading.setVisibility(View.INVISIBLE);

           /* if (!result) {
                Log.d("LoginAsyncTask", "Errore nel login");

                new AlertDialog.Builder(LoginActivity.this)
                        .setCancelable(false)
                        .setTitle(R.string.dialog_title_attention)
                        .setMessage(message != null ? message : getString(R.string.login_autentication_error))
                        .setPositiveButton(R.string.dialog_response_ok, null)
                        .show();

            } else {*/
            Log.d("LoginAsyncTask", "Login effettuato correttamente");

            Toast.makeText(LoginActivity.this, message != null ? message : getString(R.string.login_successfully), Toast.LENGTH_SHORT).show();

            Utils.setLogged(LoginActivity.this, true);
            Intent openList = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(openList);
            //}
        }


        /**
         * Metodo per la composizione dei parametri da inserire poi all'interno della chiamata
         */
        private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first) first = false;
                else result.append("&");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            return result.toString();
        }
    }
}
