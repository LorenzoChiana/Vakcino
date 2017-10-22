package com.example.loren.vaccinebooklet.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.activity.MainActivity;
import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.model.DeveFare;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.model.Vaccinazione;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VaccinationsToDoAdapter extends RecyclerView.Adapter<VaccinationsToDoAdapter.MyViewHolder> {

    private final List<Vaccinazione> vaccinations;
    private final Utente user;
    private final List<DeveFare> toDoList;
    private final List<TipoVaccinazione> vacTypeList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewVaccinationName;
        TextView textViewUserName;
        TextView textViewNumRichiamo;
        TextView textViewDate;
        Button button_apply;
        ImageView imageInfo;
        VakcinoDbManager dbManager;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewVaccinationName = (TextView) itemView.findViewById(R.id.textViewVaccinationName);
            this.textViewUserName = (TextView) itemView.findViewById(R.id.textViewUserName);
            this.textViewNumRichiamo = (TextView) itemView.findViewById(R.id.numRichiamo);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            this.imageInfo = (ImageView) itemView.findViewById(R.id.imageInfo);
            this.button_apply = (Button) itemView.findViewById(R.id.button_apply);

        }
    }

    public VaccinationsToDoAdapter(List<DeveFare> toDoList, Utente user, List<Vaccinazione> vaccinations, List<TipoVaccinazione> vacTypeList) {
        this.toDoList = toDoList;
        this.user = user;
        this.vaccinations = vaccinations;
        this.vacTypeList = vacTypeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewUserName = holder.textViewUserName;
        TextView textViewVacName = holder.textViewVaccinationName;
        TextView textViewDate = holder.textViewDate;
        TextView textViewNumRichiamo = holder.textViewNumRichiamo;

        textViewUserName.setText(user.toString());
        textViewVacName.setText(vacTypeList.get(toDoList.get(listPosition).getIdTipoVac() -1).getAntigen());
        textViewNumRichiamo.setText(Integer.toString(vacTypeList.get(toDoList.get(listPosition).getIdTipoVac() -1).getNumRichiamo()));
        textViewDate.setText(translateDaIntoDate(user.getbirthdayDate(),vacTypeList.get(toDoList.get(listPosition).getIdTipoVac() -1).getDa()));
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    private String translateDaIntoDate(String birthDate, int da) {
        //String dt = "2012-01-04";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(birthDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.MONTH, da);
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        return sdf1.format(c.getTime());
    }
}
