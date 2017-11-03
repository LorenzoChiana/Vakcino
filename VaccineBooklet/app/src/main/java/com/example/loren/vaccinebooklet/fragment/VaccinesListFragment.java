package com.example.loren.vaccinebooklet.fragment;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ActionMenuView;
import android.widget.TextView;

import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.adapter.VaccinationsBookletAdapter;
import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.listeners.MyOnScrollListener;
import com.example.loren.vaccinebooklet.listeners.OnViewVacDoneClickListener;
import com.example.loren.vaccinebooklet.listeners.OnViewVacToDoClickListener;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.model.Vaccinazione;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

public class VaccinesListFragment extends Fragment {
    private Utente user;
    //private String title;

    public static VaccinesListFragment newInstance(Utente user/*, String appBarTitle*/) {
        VaccinesListFragment fragment = new VaccinesListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        //bundle.putString("title", appBarTitle);
        fragment.setArguments(bundle);
        return fragment;
    }

    public VaccinesListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.vaccines_list_fragment, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        VakcinoDbManager dbManager = new VakcinoDbManager(view.getContext());
        List<Vaccinazione> vaccinations = dbManager.getVaccinations();
        List<TipoVaccinazione> vacTypeList = dbManager.getVaccinationType();
        VaccinationsBookletAdapter adapterToDo = new VaccinationsBookletAdapter(dbManager.getToDoList(user), dbManager.getDoneList(user), user, vaccinations, vacTypeList, VaccinationsBookletAdapter.CHOICE_TO_DO);
        mRecyclerView.setAdapter(adapterToDo);
        final FloatingActionMenu mFab = (FloatingActionMenu) view.findViewById(R.id.material_design_android_floating_action_menu);
        mFab.getMenuIconView().setImageResource(R.drawable.ic_visibility);
        mFab.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFab.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.md_styled_slide_up_normal));
                mFab.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.md_styled_slide_down_normal));

            }
        }, 300);

        mRecyclerView.addOnScrollListener(new MyOnScrollListener(mFab));
        View toDoChoice = view.findViewById(R.id.material_design_floating_action_menu_item1);
        View doneChoice = view.findViewById(R.id.material_design_floating_action_menu_item2);
        /*TextView appBarTitle = (TextView) view.findViewById(R.id.appbar_title);
        appBarTitle.setText(R.string.bookletToDo);*/
        toDoChoice.setOnClickListener(new OnViewVacToDoClickListener(mRecyclerView, user, mFab/*, title*/));
        doneChoice.setOnClickListener(new OnViewVacDoneClickListener(mRecyclerView, user, mFab/*, title)*/));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (Utente) bundle.getSerializable("user");
            //title = bundle.getString("title");
        }
    }
}
