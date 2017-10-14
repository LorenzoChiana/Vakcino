package com.example.loren.vaccinebooklet.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
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
import com.example.loren.vaccinebooklet.model.Utente;

import java.util.Calendar;
import java.util.List;

import static android.R.attr.duration;

public class NewUserActivity extends AppCompatActivity implements
        View.OnClickListener {
    private EditText etBirthDate;
    int year, month, day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        EditText etName;
        EditText etSurname;
        Button createButton;
        TextView cancelButton;
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
        createButton = (Button) findViewById(R.id.new_order_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creo l'untente nel db e sincronizzo
                setResult(RESULT_OK);
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.operation_successful), Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        });

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

}
