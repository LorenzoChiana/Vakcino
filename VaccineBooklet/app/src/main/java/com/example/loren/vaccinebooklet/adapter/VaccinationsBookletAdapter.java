package com.example.loren.vaccinebooklet.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.listeners.OnInfoClickListener;
import com.example.loren.vaccinebooklet.model.Libretto;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.model.Vaccinazione;
import com.example.loren.vaccinebooklet.tasks.SyncDBLocalToRemote;
import com.example.loren.vaccinebooklet.utils.DateInteractions;
import com.example.loren.vaccinebooklet.utils.InternetConnection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VaccinationsBookletAdapter extends RecyclerView.Adapter<VaccinationsBookletAdapter.MyViewHolder> {

    public static final int CHOICE_TO_DO = 0;
    public static final int CHOICE_DONE = 1;
    private final List<Vaccinazione> vaccinations;
    private final Utente user;
    private List<Libretto> bookletToDo;
    private List<Libretto> bookletDone;
    private final List<TipoVaccinazione> vacTypeList;
    private int imageID = 0;
    private int choice;

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewVaccinationName;
        TextView textImunizationType;
        TextView textViewNumRichiamo;
        TextView textViewDate;
        Button button_apply;
        ImageView imageInfo;
        ImageView imageTime;
        CardView cardView;
        TextView bigText;
        TextView smallText;
        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewVaccinationName = (TextView) itemView.findViewById(R.id.textViewVaccinationName);
            this.textImunizationType = (TextView) itemView.findViewById(R.id.textViewUserName);
            this.textViewNumRichiamo = (TextView) itemView.findViewById(R.id.numRichiamo);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            this.button_apply = (Button) itemView.findViewById(R.id.button_apply);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.imageInfo = (ImageView) itemView.findViewById(R.id.imageInfo);
            this.imageTime = (ImageView) itemView.findViewById(R.id.imageTemp);
            this.bigText = (TextView) itemView.findViewById(R.id.big_text);
            this.smallText = (TextView) itemView.findViewById(R.id.small_text);
            this.context = itemView.getContext();
        }
    }

    public VaccinationsBookletAdapter(List<Libretto> bookletToDo, List<Libretto> bookletDone, Utente user, List<Vaccinazione> vaccinations, List<TipoVaccinazione> vacTypeList, int choice) {
        this.choice = choice;
        this.bookletToDo = bookletToDo;
        this.bookletDone = bookletDone;
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
        if (choice == CHOICE_TO_DO) {
            initializeUIToDoBooklet(holder, listPosition);
        } else {
            initializeUIDoneBooklet(holder, listPosition);
        }
    }

    private void initializeUIDoneBooklet(MyViewHolder holder, final int listPosition) {
        TextView textViewVacName = holder.textViewVaccinationName;
        TextView textViewDate = holder.textViewDate;
        TextView textViewNumRichiamo = holder.textViewNumRichiamo;
        TextView textViewImmunizzazione = holder.textImunizationType;
        Context context = holder.context;


        ImageView imageTime = holder.imageTime;
        Button removeButton = holder.button_apply;
        removeButton.setText(R.string.remove_button);
        final ImageView imageInfo = holder.imageInfo;
        int i = 0;
        while (vaccinations.iterator().hasNext() && !vaccinations.get(i).getAntigen().equals(vacTypeList.get(bookletDone.get(listPosition).getIdTipoVac() - 1).getAntigen())) {
            vaccinations.iterator().next();
            i++;
        }
        Vaccinazione currentVac = vaccinations.get(i);
        textViewImmunizzazione.setText(context.getString(R.string.immunization_type) + ": " + vacTypeList.get(i).getTipoImmunizzazione());
        textViewVacName.setText(currentVac.getName());
        TipoVaccinazione currentVt = vacTypeList.get(bookletDone.get(listPosition).getIdTipoVac() - 1);
        textViewNumRichiamo.setText(Integer.toString(currentVt.getNumRichiamo()));
        String dateDone = DateInteractions.changeDateFormat(bookletDone.get(listPosition).getDate(), "yyyy-MM-dd", "dd/MM/yyyy");
        textViewDate.setText(dateDone);
        imageTime.setImageDrawable(ContextCompat.getDrawable(imageTime.getContext(), R.drawable.ic_calendar_done));
        imageInfo.setId(imageID);
        imageInfo.setOnClickListener(new OnInfoClickListener(currentVac, currentVt));

        /*if(bookletDone.size() == 0) {
            bigText.setText(R.string.noVac);
            smallText.setText(R.string.noVac_text);
        }*/
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Libretto page = bookletDone.get(listPosition);
                page.setDate("");
                page.setDone(VakcinoDbManager.NOT_DONE);
                page.setStatus(VakcinoDbManager.NOT_SYNCED_WITH_SERVER);
                VakcinoDbManager dbManager = new VakcinoDbManager(v.getContext());
                dbManager.updateBooklet(page);
                new SyncDBLocalToRemote().execute(v.getContext());
                bookletDone.remove(listPosition);
                notifyItemRemoved(imageID);
                notifyItemRangeChanged(imageID, imageID);
                notifyDataSetChanged();

                if(InternetConnection.haveInternetConnection(v.getContext())){
                    new SyncDBLocalToRemote().execute(v.getContext());
                }
                /*if(bookletDone.size() == 0) {
                    TextView bigText = (TextView) v.findViewById(R.id.big_text);
                    TextView smallText = (TextView) v.findViewById(R.id.small_text);
                    bigText.setText(R.string.noVac);
                    smallText.setText(R.string.noVac_text);
                }*/

            }
        });
        imageID++;
    }

    private void initializeUIToDoBooklet(final MyViewHolder holder, final int listPosition) {
        TextView textViewVacName = holder.textViewVaccinationName;
        TextView textViewDate = holder.textViewDate;
        TextView textViewNumRichiamo = holder.textViewNumRichiamo;
        TextView textViewImmunizzazione = holder.textImunizationType;
        Context context = holder.context;

        ImageView imageTime = holder.imageTime;
        Button applyButton = holder.button_apply;
        final ImageView imageInfo = holder.imageInfo;
        int i = 0;
        while (vaccinations.iterator().hasNext() && !vaccinations.get(i).getAntigen().equals(vacTypeList.get(bookletToDo.get(listPosition).getIdTipoVac() - 1).getAntigen())) {
            vaccinations.iterator().next();
            i++;
        }
        Vaccinazione currentVac = vaccinations.get(i);
        textViewImmunizzazione.setText(context.getString(R.string.immunization_type) + ": " + vacTypeList.get(i).getTipoImmunizzazione());
        textViewVacName.setText(currentVac.getName());
        TipoVaccinazione currentVt = vacTypeList.get(bookletToDo.get(listPosition).getIdTipoVac() - 1);
        textViewNumRichiamo.setText(Integer.toString(currentVt.getNumRichiamo()));
        String dateDa = DateInteractions.translateDate(user.getbirthdayDate(), vacTypeList.get(bookletToDo.get(listPosition).getIdTipoVac() - 1).getDa());
        String dateA = DateInteractions.translateDate(user.getbirthdayDate(), vacTypeList.get(bookletToDo.get(listPosition).getIdTipoVac() - 1).getA());
        if (dateA.equals(dateDa))
            textViewDate.setText(dateA);
        else
            textViewDate.setText(dateDa + " - " + dateA);
        if (DateInteractions.isLateThan(dateA)) {
            imageTime.setColorFilter(Color.RED);
            imageTime.setImageDrawable(ContextCompat.getDrawable(imageTime.getContext(), R.drawable.ic_calendar_late));
            textViewDate.setTextColor(Color.RED);
        } else {
            imageTime.clearColorFilter();
            imageTime.setImageDrawable(ContextCompat.getDrawable(imageTime.getContext(), R.drawable.ic_action_date));
            textViewDate.setTextColor(Color.DKGRAY);
        }
        imageInfo.setId(imageID);
        imageInfo.setOnClickListener(new OnInfoClickListener(currentVac, currentVt));
        /*if(bookletToDo.size() == 0) {
            bigText.setText(R.string.congrats);
            smallText.setText(R.string.congrats_text);
        }*/
        applyButton.setOnClickListener(new View.OnClickListener() {
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
                                        Libretto page = bookletToDo.get(listPosition);
                                        page.setDate(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                        page.setDone(VakcinoDbManager.DONE);
                                        page.setStatus(VakcinoDbManager.NOT_SYNCED_WITH_SERVER);
                                        VakcinoDbManager dbManager = new VakcinoDbManager(view.getContext());
                                        dbManager.updateBooklet(page);
                                        new SyncDBLocalToRemote().execute(view.getContext());
                                        bookletToDo.remove(listPosition);

                                        notifyItemRemoved(imageID);
                                        notifyItemRangeChanged(imageID, imageID);
                                        notifyDataSetChanged();
                                        if(InternetConnection.haveInternetConnection(view.getContext())){
                                            new SyncDBLocalToRemote().execute(view.getContext());
                                        }
                                        /*if(bookletToDo.size() == 0) {
                                            TextView bigText = (TextView) view.findViewById(R.id.big_text);
                                            TextView smallText = (TextView) view.findViewById(R.id.small_text);
                                            bigText.setText(R.string.congrats);
                                            smallText.setText(R.string.congrats_text);
                                        }*/

                                    }
                                }, year, month, day);
                        datePickerDialog.setTitle(R.string.date_vaccination_question);
                        datePickerDialog.show();
                    }
                });
        imageID++;
    }
    @Override
    public int getItemCount() {
        if(choice == CHOICE_DONE)
            return bookletDone.size();
        else
            return bookletToDo.size();
    }

}
