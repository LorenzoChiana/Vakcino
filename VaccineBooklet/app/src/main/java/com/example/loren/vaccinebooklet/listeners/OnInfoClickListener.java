package com.example.loren.vaccinebooklet.listeners;

import android.view.View;

import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.activity.MainActivity;
import com.example.loren.vaccinebooklet.model.Vaccinazione;

import java.util.List;

public class OnInfoClickListener implements View.OnClickListener {
    Vaccinazione vac;
    public OnInfoClickListener(Vaccinazione vac) {
        this.vac = vac;
    }

    @Override
    public void onClick(View v) {


        MainActivity.materialStyleDialog.setHeaderDrawable(R.drawable.header_2)
            .setTitle(vac.getName())
            .setDescription(vac.getDescription() +
            "\n\nAntigene: " + vac.getAntigen() +
            "\nGruppo: " + vac.getGroup())
            .setPositiveText("Ok");

        MainActivity.materialStyleDialog.show();
    }
}
