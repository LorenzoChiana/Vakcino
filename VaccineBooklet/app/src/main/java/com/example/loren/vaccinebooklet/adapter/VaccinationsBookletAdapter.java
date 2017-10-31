package com.example.loren.vaccinebooklet.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        TextView textViewUserName;
        TextView textViewNumRichiamo;
        TextView textViewDate;
        Button button_apply;
        ImageView imageInfo;
        ImageView imageTime;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewVaccinationName = (TextView) itemView.findViewById(R.id.textViewVaccinationName);
            this.textViewUserName = (TextView) itemView.findViewById(R.id.textViewUserName);
            this.textViewNumRichiamo = (TextView) itemView.findViewById(R.id.numRichiamo);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            this.button_apply = (Button) itemView.findViewById(R.id.button_apply);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.imageInfo = (ImageView) itemView.findViewById(R.id.imageInfo);
            this.imageTime = (ImageView) itemView.findViewById(R.id.imageTemp);
        }
    }

    public VaccinationsBookletAdapter(List<Libretto> booklet, Utente user, List<Vaccinazione> vaccinations, List<TipoVaccinazione> vacTypeList, int choice) {
        this.choice = choice;
        this.bookletToDo = new ArrayList<>();
        this.bookletDone = new ArrayList<>();
        for (Libretto l: booklet) {
            if(l.getDone() == VakcinoDbManager.DONE) {
                this.bookletDone.add(l);
            } else {
                this.bookletToDo.add(l);
            }
        }
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

        textViewUserName.setText(user.toString());

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
        textViewVacName.setText(currentVac.getName());
        textViewNumRichiamo.setText(Integer.toString(vacTypeList.get(bookletDone.get(listPosition).getIdTipoVac() - 1).getNumRichiamo()));
        String dateDone = changeDateFormat(bookletDone.get(listPosition).getDate(), "yyyy-MM-dd", "dd/MM/yyyy");
        textViewDate.setText(dateDone);
        imageTime.setImageDrawable(ContextCompat.getDrawable(imageTime.getContext(), R.drawable.ic_calendar_done));
        imageInfo.setId(imageID);
        imageInfo.setOnClickListener(new OnInfoClickListener(currentVac));
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
            }
        });
        imageID++;
    }

    private void initializeUIToDoBooklet(final MyViewHolder holder, final int listPosition) {
        TextView textViewVacName = holder.textViewVaccinationName;
        TextView textViewDate = holder.textViewDate;
        TextView textViewNumRichiamo = holder.textViewNumRichiamo;
        ImageView imageTime = holder.imageTime;
        Button applyButton = holder.button_apply;
        final CardView cardView = holder.cardView;
        final ImageView imageInfo = holder.imageInfo;
        int i = 0;
        while (vaccinations.iterator().hasNext() && !vaccinations.get(i).getAntigen().equals(vacTypeList.get(bookletToDo.get(listPosition).getIdTipoVac() - 1).getAntigen())) {
            vaccinations.iterator().next();
            i++;
        }
        Vaccinazione currentVac = vaccinations.get(i);
        textViewVacName.setText(currentVac.getName());
        textViewNumRichiamo.setText(Integer.toString(vacTypeList.get(bookletToDo.get(listPosition).getIdTipoVac() - 1).getNumRichiamo()));
        String dateDa = translateDate(user.getbirthdayDate(), vacTypeList.get(bookletToDo.get(listPosition).getIdTipoVac() - 1).getDa());
        String dateA = translateDate(user.getbirthdayDate(), vacTypeList.get(bookletToDo.get(listPosition).getIdTipoVac() - 1).getA());
        if (dateA.equals(dateDa))
            textViewDate.setText(dateA);
        else
            textViewDate.setText(dateDa + " - " + dateA);
        if (isLateThan(dateA)) {
            imageTime.setColorFilter(Color.RED);
            imageTime.setImageDrawable(ContextCompat.getDrawable(imageTime.getContext(), R.drawable.ic_calendar_late));
            textViewDate.setTextColor(Color.RED);
        } else {
            imageTime.clearColorFilter();
            imageTime.setImageDrawable(ContextCompat.getDrawable(imageTime.getContext(), R.drawable.ic_action_date));
            textViewDate.setTextColor(Color.DKGRAY);
        }
        imageInfo.setId(imageID);
        imageInfo.setOnClickListener(new OnInfoClickListener(currentVac));
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

    private boolean isLateThan(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        Date today = new Date();
        String currentDateString = sdf.format(today);
        try {
            strDate = sdf.parse(date);
            today = sdf.parse(currentDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return today.after(strDate);

    }

    private String changeDateFormat(String dateIn, String inputPattern, String outputPattern) {
        /*String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd/MM/yyyy";*/
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateIn);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
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
