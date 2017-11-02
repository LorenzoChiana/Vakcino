package com.example.loren.vaccinebooklet.tasks;

import android.os.AsyncTask;

import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.model.Libretto;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.model.Vaccinazione;
import com.example.loren.vaccinebooklet.utils.DateInteractions;

import java.util.Calendar;
import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ExportVacIntoCalendar extends AsyncTask<Utente, Void, Boolean>{
    private MaterialProgressBar progressiveBar;

    @Override
    protected void onPreExecute() {
        //progressiveBar = new MaterialProgressBar();
    }

    @Override
    protected void onPostExecute(final Boolean success) {


    }

    @Override
    protected Boolean doInBackground(Utente... selectedUsers) {
       /* VakcinoDbManager dbManager = new VakcinoDbManager(get);
        List<TipoVaccinazione> listVacType = dbManager.getVaccinationType();
        List<Vaccinazione> listVac = dbManager.getVaccinations();
        int cont = 0;
        for (Utente user : selectedUsers) {
            List<Libretto> toDoList = dbManager.getToDoList(user);
            for (Libretto page : toDoList) {
                TipoVaccinazione vacType = listVacType.get(page.getIdTipoVac());
                int i = 0;
                while (listVac.iterator().hasNext() && !listVac.get(i).getAntigen().equals(listVacType.get(toDoList.get(cont).getIdTipoVac() - 1).getAntigen())) {
                    listVac.iterator().next();
                    i++;
                }
                Vaccinazione vac = listVac.get(i);
                Calendar beginTime = Calendar.getInstance();
                String dateDa = DateInteractions.translateDate(user.getbirthdayDate(), vacType.getDa());
                beginTime.set(DateInteractions.getYear(dateDa),
                        DateInteractions.getMonth(dateDa),
                        DateInteractions.getDay(dateDa),
                        0, 0);
                Calendar endTime = Calendar.getInstance();
                String dateA = DateInteractions.translateDate(user.getbirthdayDate(), vacType.getA());
                endTime.set(DateInteractions.getYear(dateA),
                        DateInteractions.getMonth(dateA),
                        DateInteractions.getDay(dateA),
                        0, 0);
                addCalendarEvent(beginTime, endTime, vac.getName(), vac.getDescription());
                cont++;
            }
        }*/
        return null;
    }

}
