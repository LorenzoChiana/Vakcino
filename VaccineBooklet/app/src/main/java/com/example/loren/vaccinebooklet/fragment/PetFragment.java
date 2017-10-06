package com.example.loren.vaccinebooklet.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.loren.vaccinebooklet.Item;
import com.example.loren.vaccinebooklet.R;

import java.util.ArrayList;


public class PetFragment extends Fragment {


    private static ArrayList<Item> vaccinations;
    public PetFragment() {
    }

    public static PetFragment newInstance() {
        PetFragment fragment = new PetFragment();
        return fragment;
    }

    public static PetFragment newInstance(ArrayList<Item> vac) {
        PetFragment fragment = new PetFragment();
        vaccinations = vac;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pets_fragment, container, false);

        return v;
    }
}
