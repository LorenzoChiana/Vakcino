package com.example.loren.vaccinebooklet.listeners;

import android.view.View;

import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.activity.MainActivity;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Vaccinazione;

import java.util.List;

public class OnInfoClickListener implements View.OnClickListener {
    Vaccinazione vac;
    TipoVaccinazione vt;
    public OnInfoClickListener(Vaccinazione vac, TipoVaccinazione vt) {
        this.vac = vac;
        this.vt = vt;
    }

    @Override
    public void onClick(View v) {


        MainActivity.materialInfoStyleDialog.setHeaderDrawable(R.drawable.header_2)
            .setTitle(vac.getName())
            .setDescription(vac.getDescription() +
                "\n\nAntigene: " + vac.getAntigen() +
                "\nGruppo: " + vac.getGroup() +
                "\nNumero di richiamo: " + vt.getNumRichiamo())
            .setPositiveText("Ok");

        MainActivity.materialInfoStyleDialog.show();
    }
}
