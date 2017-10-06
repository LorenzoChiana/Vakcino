package com.example.loren.vaccinebooklet.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.loren.vaccinebooklet.Item;
import com.example.loren.vaccinebooklet.R;

import java.util.ArrayList;


public class PersonFragment extends Fragment {

    private static ArrayList<Item> vaccinations;
    public PersonFragment() {
    }

    public static PersonFragment newInstance() {
        PersonFragment fragment = new PersonFragment();
        return fragment;
    }

    public static PersonFragment newInstance(ArrayList<Item> vac) {
        PersonFragment fragment = new PersonFragment();
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
        View v = inflater.inflate(R.layout.people_fragment, container, false);

        return v;
    }
}