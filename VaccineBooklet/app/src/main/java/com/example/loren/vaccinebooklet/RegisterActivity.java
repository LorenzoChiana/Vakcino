package com.example.loren.vaccinebooklet;

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

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final int MIN_PW_LENGTH = 8;

    @Bind(R.id.input_name) EditText nameText;
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

        // TODO: Implement your own signup logic here.

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

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String confirmEmail = confirmEmailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError(getString(R.string.length_min_name));
            valid = false;
        } else {
            nameText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError(getString(R.string.wrong_email));
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (confirmEmail.isEmpty() || !(confirmEmail.equals(password))) {
            confirmEmailText.setError(getString(R.string.error_email_no_match));
            valid = false;
        } else {
            confirmPasswordText.setError(null);
        }

        if (password.isEmpty() || password.length() < MIN_PW_LENGTH) {
            passwordText.setError(getString(R.string.length_min_pw));
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (confirmPassword.isEmpty() || confirmPassword.length() < MIN_PW_LENGTH || !(confirmPassword.equals(password))) {
            confirmPasswordText.setError(getString(R.string.error_pw_no_match));
            valid = false;
        } else {
            confirmPasswordText.setError(null);
        }

        return valid;
    }
}