package com.example.loren.vaccinebooklet.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.model.DeveFare;

import java.util.ArrayList;
import java.util.List;

public class VaccinationsToDoAdapter extends RecyclerView.Adapter<VaccinationsToDoAdapter.MyViewHolder> {

    private List<DeveFare> toDoList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewVaccinationName;
        TextView textViewUserName;
        TextView textViewNumRichiamo;
        TextView textViewDate;
        TextView button_apply;
        ImageView imageInfo;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewVaccinationName = (TextView) itemView.findViewById(R.id.textViewVaccinationName);
            this.textViewUserName = (TextView) itemView.findViewById(R.id.textViewUserName);
            this.textViewNumRichiamo = (TextView) itemView.findViewById(R.id.numRichiamo);
            this.textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
            this.imageInfo = (ImageView) itemView.findViewById(R.id.imageInfo);
            this.button_apply = (TextView) itemView.findViewById(R.id.button_apply);
        }
    }

    public VaccinationsToDoAdapter(List<DeveFare> toDoList) {
        this.toDoList = toDoList;
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

        /*TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(toDoList.get(listPosition).getName());
        textViewVersion.setText(toDoList.get(listPosition).getVersion());
        imageView.setImageResource(toDoList.get(listPosition).getImage());*/
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }
}
