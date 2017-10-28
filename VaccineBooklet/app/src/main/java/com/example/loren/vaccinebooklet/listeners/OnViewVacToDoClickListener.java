package com.example.loren.vaccinebooklet.listeners;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.activity.MainActivity;
import com.example.loren.vaccinebooklet.adapter.VaccinationsBookletAdapter;
import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.model.Vaccinazione;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

public class OnViewVacToDoClickListener implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private Utente currentUser;
    private FloatingActionMenu mFab;
    private TextView appBarTitle;

    public OnViewVacToDoClickListener(RecyclerView mRecyclerView, Utente user, FloatingActionMenu mFab, TextView appBarTitle) {
        this.mRecyclerView = mRecyclerView;
        this.currentUser = user;
        this.mFab = mFab;
        this.appBarTitle = appBarTitle;
    }

    @Override
    public void onClick(View v) {
        mFab.close(true);
        VakcinoDbManager dbManager = new VakcinoDbManager(v.getContext());
        List<Vaccinazione> vaccinations = dbManager.getVaccinations();
        List<TipoVaccinazione> vacTypeList = dbManager.getVaccinationType();
        mRecyclerView.setAdapter(new VaccinationsBookletAdapter(dbManager.getAllBooklet(currentUser), currentUser, vaccinations, vacTypeList, VaccinationsBookletAdapter.CHOICE_TO_DO));
        appBarTitle.setText(R.string.bookletToDo);
    }
}