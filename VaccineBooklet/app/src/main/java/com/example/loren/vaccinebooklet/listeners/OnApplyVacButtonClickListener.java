package com.example.loren.vaccinebooklet.listeners;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.activity.MainActivity;
import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.model.Libretto;
import com.example.loren.vaccinebooklet.tasks.SyncDBLocalToRemote;

import java.util.Calendar;

public class OnApplyVacButtonClickListener implements View.OnClickListener{

    private Libretto page;

    public OnApplyVacButtonClickListener(Libretto page) {
        this.page = page;
    }
    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        page.setDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        page.setDone(VakcinoDbManager.DONE);
                        page.setStatus(VakcinoDbManager.NOT_SYNCED_WITH_SERVER);
                        VakcinoDbManager dbManager = new VakcinoDbManager(view.getContext());
                        dbManager.updateBooklet(page);
                        new SyncDBLocalToRemote().execute(view.getContext());
                    }
                }, year, month, day);
        datePickerDialog.setTitle(R.string.date_vaccination_question);
        datePickerDialog.show();
    }
}
