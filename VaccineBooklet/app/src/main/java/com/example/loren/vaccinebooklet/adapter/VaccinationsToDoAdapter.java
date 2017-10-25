package com.example.loren.vaccinebooklet.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.activity.MainActivity;
import com.example.loren.vaccinebooklet.model.Libretto;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.model.Vaccinazione;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.loren.vaccinebooklet.R.drawable.ic_menu;

public class VaccinationsToDoAdapter extends RecyclerView.Adapter<VaccinationsToDoAdapter.MyViewHolder> {

    private final List<Vaccinazione> vaccinations;
    private final Utente user;
    private final List<Libretto> booklet;
    private final List<TipoVaccinazione> vacTypeList;
    private int imageID = 0;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewVaccinationName;
        TextView textViewUserName;
        TextView textViewNumRichiamo;
        TextView textViewDate;
        Button button_apply;
        ImageView imageInfo;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewVaccinationName = (TextView) itemView.findViewById(R.id.textViewVaccinationName);
            this.textViewUserName = (TextView) itemView.findViewById(R.id.textViewUserName);
            this.textViewNumRichiamo = (TextView) itemView.findViewById(R.id.numRichiamo);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            this.imageInfo = (ImageView) itemView.findViewById(R.id.imageInfo);
            this.button_apply = (Button) itemView.findViewById(R.id.button_apply);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.imageInfo = (ImageView) itemView.findViewById(R.id.imageInfo);

        }
    }

    public VaccinationsToDoAdapter(List<Libretto> booklet, Utente user, List<Vaccinazione> vaccinations, List<TipoVaccinazione> vacTypeList) {
        this.booklet = booklet;
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
        final CardView cardView = holder.cardView;
        final ImageView imageInfo = holder.imageInfo;

        textViewUserName.setText(user.toString());
        for (Vaccinazione v: vaccinations) {
            v.getAntigen().equals(vacTypeList.get(booklet.get(listPosition).getIdTipoVac() -1).getAntigen());
        }
        int i = 0;

        while (vaccinations.iterator().hasNext() && !vaccinations.get(i).getAntigen().equals(vacTypeList.get(booklet.get(listPosition).getIdTipoVac() -1).getAntigen())) {
            vaccinations.iterator().next();
            i++;
        }
        textViewVacName.setText(vaccinations.get(i).getName());
        textViewNumRichiamo.setText(Integer.toString(vacTypeList.get(booklet.get(listPosition).getIdTipoVac() -1).getNumRichiamo()));
        String dateDa = translateDate(user.getbirthdayDate(),vacTypeList.get(booklet.get(listPosition).getIdTipoVac() -1).getDa());
        String dateA = translateDate(user.getbirthdayDate(),vacTypeList.get(booklet.get(listPosition).getIdTipoVac() -1).getA());
        if(dateA.equals(dateDa))
            textViewDate.setText(dateA);
        else
            textViewDate.setText(dateDa + " - " + dateA);
        if (isLateThan(dateA)) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.getContext(), R.color.fab_material_red_500));
        }
        imageInfo.setId(imageID);
        imageID++;
        View.OnClickListener onInfoClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;

                while (vaccinations.iterator().hasNext() && !vaccinations.get(i).getAntigen().equals(vacTypeList.get(booklet.get(imageInfo.getId()).getIdTipoVac() -1).getAntigen())) {
                    vaccinations.iterator().next();
                    i++;
                }

                MainActivity.dialogInfoVac.setHeaderDrawable(R.drawable.header_2)
                        .setTitle(vaccinations.get(i).getName())
                        .setDescription(vaccinations.get(i).getDescription() +
                                "\n\nAntigene: " + vaccinations.get(i).getAntigen() +
                                "\nGruppo: " + vaccinations.get(i).getGroup())
                        .setPositiveText("Ok");

                MainActivity.dialogInfoVac.show();
            }
        };
        imageInfo.setOnClickListener(onInfoClick);

    }
    @Override
    public int getItemCount() {
        return booklet.size();
    }

    private boolean isLateThan(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (new Date().after(strDate)) {
            return true;
        }
        else{
            return false;
        }
    }

    private String translateDate(String birthDate, int da) {
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
