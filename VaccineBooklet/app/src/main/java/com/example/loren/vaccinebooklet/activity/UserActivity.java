package com.example.loren.vaccinebooklet.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.model.Libretto;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.request.RemoteDBInteractions;
import com.example.loren.vaccinebooklet.tasks.SyncDBLocalToRemote;
import com.example.loren.vaccinebooklet.utils.DateInteractions;
import com.example.loren.vaccinebooklet.utils.InternetConnection;
import com.example.loren.vaccinebooklet.utils.Utils;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

import static android.R.attr.duration;

public class UserActivity extends AppCompatActivity implements
        View.OnClickListener {


    public static final String USER_INTERACTION = "USER INTERACTION";
    public static final String CREATE_USER = "create";
    public static final String EDIT_USER = "edit";
    protected int year, month, day;

    @Bind(R.id.input_name)
    EditText etName;
    @Bind(R.id.input_surname)
    EditText etSurname;
    @Bind(R.id.back_button)
    ImageButton backButton;
    @Bind(R.id.tick_button)
    ImageButton createButton;
    @Bind(R.id.input_birth_date)
    EditText etBirthDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ImageButton backButton;
        ImageButton tickButton;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        /* Components setup */
        etName = (EditText) findViewById(R.id.input_name);
        etSurname = (EditText) findViewById(R.id.input_surname);
        etBirthDate = (EditText) findViewById(R.id.input_birth_date);
        etBirthDate.setFocusable(true);
        etBirthDate.setClickable(true);
        etBirthDate.setOnClickListener(this);
        if(getIntent().getStringExtra(USER_INTERACTION).equals(EDIT_USER)) {
            Utente user = (Utente) getIntent().getSerializableExtra("Utente");
            etName.setText(user.getName());
            etSurname.setText(user.getSurname());
            etBirthDate.setText(DateInteractions.changeDateFormat(user.getbirthdayDate(), "yyyy-MM-dd", "dd/MM/yyyy"));
        }
        backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setOnClickListener(new cancelClickListener());
        tickButton = (ImageButton) findViewById(R.id.tick_button);
        tickButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (validate()) {
                    switch (getIntent().getStringExtra(USER_INTERACTION)) {
                        case CREATE_USER:
                            createNewUser();
                            break;
                        case EDIT_USER:
                            editUser((Utente) getIntent().getSerializableExtra("Utente"));
                            break;
                    }
                    setResult(RESULT_OK);
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.operation_successful), Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
            }
        });

    }

    private void editUser(Utente user) {
        user.setName(etName.getText().toString());
        user.setSurname(etSurname.getText().toString());
        user.setBirthdayDate(DateInteractions.changeDateFormat(etBirthDate.getText().toString(), "dd/MM/yyyy", "yyyy-MM-dd"));
        user.setStatus(VakcinoDbManager.NOT_SYNCED_WITH_SERVER);
        VakcinoDbManager dbManager = new VakcinoDbManager(getApplicationContext());
        dbManager.updateUser(user);
        if(InternetConnection.haveInternetConnection(getApplicationContext()))
            new SyncDBLocalToRemote();
    }

    private void createNewUser() {
        //creo l'untente nel db e sincronizzo
        final VakcinoDbManager dbManager = new VakcinoDbManager(getApplicationContext());
        String email = Utils.getAccount(getApplicationContext());
        final Utente newUser = new Utente(dbManager.getUsers(email).size() + 1,
                etName.getText().toString(), etSurname.getText().toString(),
                DateInteractions.changeDateFormat(etBirthDate.getText().toString(), "dd/MM/yyyy", "yyyy-MM-ddd"), "p", email, VakcinoDbManager.NOT_SYNCED_WITH_SERVER);
        dbManager.addUser(newUser);
        List<TipoVaccinazione> vt = dbManager.getVaccinationType();
        for (int i = 0; i < vt.size(); i++) {
            Libretto booklet = new Libretto(newUser.getId(), vt.get(i).getId(), VakcinoDbManager.NOT_DONE, "", VakcinoDbManager.NOT_SYNCED_WITH_SERVER);
            dbManager.addBooklet(booklet);
        }
    }

    private class cancelClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            createAlert(view.getContext());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        createAlert(this.getApplicationContext());
    }

    private void createAlert(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(R.string.undo_request)
                .setMessage(R.string.undo_description)
                .setNegativeButton(R.string.label_cancel, null)
                .setPositiveButton(R.string.label_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_CANCELED);
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.operation_cancelled), Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    }
                })
                .show();
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
