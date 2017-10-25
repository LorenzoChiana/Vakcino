package com.example.loren.vaccinebooklet.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.model.Libretto;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.request.RemoteDBInteractions;
import com.example.loren.vaccinebooklet.utils.InternetConnection;
import com.example.loren.vaccinebooklet.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

import static android.R.attr.duration;

public class NewUserActivity extends AppCompatActivity implements
        View.OnClickListener {
    int year, month, day;

    @Bind(R.id.input_name) EditText etName;
    @Bind(R.id.input_surname) EditText etSurname;
    @Bind(R.id.new_user_button) Button createButton;
    @Bind(R.id.input_birth_date) EditText etBirthDate;
    @Bind(R.id.link_cancel) TextView cancelButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ImageButton backButton;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        /* Components setup */
        etName = (EditText) findViewById(R.id.input_name);
        etSurname = (EditText) findViewById(R.id.input_surname);
        etBirthDate = (EditText) findViewById(R.id.input_birth_date);
        etBirthDate.setFocusable(false);
        etBirthDate.setClickable(true);
        etBirthDate.setOnClickListener(this);
        backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setOnClickListener(cancelClickListener);
        cancelButton = (TextView) findViewById(R.id.link_cancel);
        cancelButton.setOnClickListener(cancelClickListener);
        createButton = (Button) findViewById(R.id.new_user_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    //creo l'untente nel db e sincronizzo
                    final VakcinoDbManager dbManager = new VakcinoDbManager(getApplicationContext());
                    String email = Utils.getAccount(getApplicationContext());
                    final Utente newUser = new Utente(dbManager.getUsers(email).size() + 1,
                            etName.getText().toString(), etSurname.getText().toString(),
                            convertToDate(etBirthDate.getText().toString()), "p", email, VakcinoDbManager.NOT_SYNCED_WITH_SERVER);
                    dbManager.addUser(newUser);
                    List<TipoVaccinazione> vt = dbManager.getVaccinationType();
                    for (int i = 0; i < vt.size(); i++) {
                        Libretto booklet = new Libretto(newUser.getId(), vt.get(i).getId(), VakcinoDbManager.NOT_DONE, "", VakcinoDbManager.NOT_SYNCED_WITH_SERVER);
                        dbManager.addBooklet(booklet);
                    }

                    setResult(RESULT_OK);
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.operation_successful), Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
            }
        });

    }

    private String convertToDate(String dateString){
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        Date originDate;
        String returnDate = "";
        try {
            originDate = dateFormat1.parse(dateString);
            returnDate = dateFormat2.format(originDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    private View.OnClickListener cancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setResult(RESULT_CANCELED);
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.operation_cancelled), Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.operation_cancelled), Toast.LENGTH_SHORT);
        toast.show();
        finish();
    }

    @Override
    public void onClick(View view) {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        etBirthDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public boolean validate() {
        boolean valid = true;

        final String name = etName.getText().toString();
        final String surname = etSurname.getText().toString();
        final String birthDate = etBirthDate.getText().toString();

        if (name.isEmpty()) {
            etName.setError(getText(R.string.empty_input));
            valid = false;
        } else {
            etName.setError(null);
        }

        if (surname.isEmpty()) {
            etSurname.setError(getText(R.string.empty_input));
            valid = false;
        } else {
            etSurname.setError(null);
        }

        if (birthDate.isEmpty()) {
            etBirthDate.setError(getText(R.string.empty_input));
            valid = false;
        } else {
            etBirthDate.setError(null);
        }

        return valid;
    }

}
