package com.example.loren.vaccinebooklet.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.request.RegisterRequest;
import com.example.loren.vaccinebooklet.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final int MIN_PW_LENGTH = 8;

    @Bind(R.id.input_email) EditText emailText;
    @Bind(R.id.input_confirm_email) EditText confirmEmailText;
    @Bind(R.id.input_password) EditText passwordText;
    @Bind(R.id.input_confirm_password) EditText confirmPasswordText;
    @Bind(R.id.btn_signup) Button registrationButton;
    @Bind(R.id.link_login) TextView loginLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void registration() {
        Log.d(TAG, String.valueOf(R.string.title_activity_register));

        if (!validate()) {
            onSignupFailed();
            return;
        }

        registrationButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.registration_creating_account));
        progressDialog.show();

        /*String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String confirmEmail = confirmEmailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();*/
        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();

        // TODO: Implement your own signup logic here.
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        // On complete call either onSignupSuccess or onSignupFailed
                                        // depending on success
                                        onSignupSuccess();
                                        // onSignupFailed();
                                        progressDialog.dismiss();
                                    }
                                }, 3000);
                        Utils.setLogged(RegisterActivity.this, true);
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        progressDialog.dismiss();
                        onSignupFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(email, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);



    }


    public void onSignupSuccess() {
        registrationButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.registration_failed), Toast.LENGTH_LONG).show();

        registrationButton.setEnabled(true);
    }

    private boolean isEmailValid(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    private boolean isPasswordValid(String password) {
        Pattern VALID_PASSWORD_REGEX = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})");
        Matcher matcher = VALID_PASSWORD_REGEX .matcher(password);
        return matcher.find();
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String confirmEmail = confirmEmailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();



        if (email.isEmpty() || !isEmailValid(email) /*!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()*/) {
            emailText.setError(getString(R.string.wrong_email));
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (confirmEmail.isEmpty() || !(confirmEmail.equals(email))) {
            confirmEmailText.setError(getString(R.string.error_email_no_match));
            valid = false;
        } else {
            confirmPasswordText.setError(null);
        }

        if (password.isEmpty() || !isPasswordValid(password)/*password.length() < MIN_PW_LENGTH*/) {
            passwordText.setError(getString(R.string.password_info));
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (confirmPassword.isEmpty() || !(confirmPassword.equals(password))) {
            confirmPasswordText.setError(getString(R.string.error_pw_no_match));
            valid = false;
        } else {
            confirmPasswordText.setError(null);
        }

        return valid;
    }
}