package com.example.loren.vaccinebooklet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.adapter.VaccinationsBookletAdapter;

import com.example.loren.vaccinebooklet.database.VakcinoDbHelper;
import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.listeners.MyOnScrollListener;
import com.example.loren.vaccinebooklet.listeners.OnViewVacDoneClickListener;
import com.example.loren.vaccinebooklet.listeners.OnViewVacToDoClickListener;
import com.example.loren.vaccinebooklet.model.Libretto;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.model.Vaccinazione;
import com.example.loren.vaccinebooklet.request.RemoteDBInteractions;
import com.example.loren.vaccinebooklet.utils.NetworkStateReceiver;
import com.example.loren.vaccinebooklet.utils.Utils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int LOGOUT_ID = 1;
    private static final int NOTICE_ID = 2;
    private static final int USER_ID = 3;

    public static final String INTENT_EXTRA   = "finish";
    public static final String INTENT_ACTION_INT = "com.example.loren.vaccinebooklet.intent.action.TEST.int";
    private boolean doubleBackToExitPressedOnce;
    private String message;
    private String email;

    //private ViewPager mViewPager;
    public RecyclerView mRecyclerView;
    private TextView twEmailUser;
    private NavigationView navigationView;
    private NetworkStateReceiver receiverConnectivity;
    private IntentFilter filter, intentFilter;
    private static RecyclerView.Adapter adapterToDo;

    private RecyclerView.LayoutManager layoutManager;
    private List<Utente> users;
    private List<Vaccinazione> vaccinations;
    private List<TipoVaccinazione> vacTypeList;
    public static MaterialStyledDialog.Builder dialogInfoVac;

    private FloatingActionMenu mFab;

    private boolean afterLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VakcinoDbManager dbManager = new VakcinoDbManager(getApplicationContext());
        users = dbManager.getUsers(Utils.getAccount(getApplicationContext()));
        vaccinations = dbManager.getVaccinations();
        vacTypeList = dbManager.getVaccinationType();


        this.message = null;
        this.doubleBackToExitPressedOnce = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        dialogInfoVac = new MaterialStyledDialog.Builder(MainActivity.this);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        twEmailUser = (TextView) headerView.findViewById(R.id.tw_email);
        /*final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.show();*/
        /*
            Se ho appena fatto il login/sign up
            allora setto la sharedpreference con l'email dell'account
            altrimenti prendo l'email direttamente dal sharedpreference
         */
        /*setAfterOpenApp(true);
        setAfterLogin(false);*/

        final String extraEmail;
        try {
            extraEmail = getIntent().getExtras().getString("email");
            Utils.setAccount(MainActivity.this, extraEmail);
            // significa inoltre che si è appena loggato quindi devo scaricarmi localmente il db remoto
            afterLogin = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        email = Utils.getAccount(MainActivity.this);
        twEmailUser.setText(email);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        receiverConnectivity = new NetworkStateReceiver();
        receiverConnectivity.setAfterLogin(afterLogin);
        filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiverConnectivity, filter);
        intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_ACTION_INT);
        initializeNavigationUI(navigationView.getMenu(), getApplicationContext());

        mFab = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        mFab.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFab.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.md_styled_slide_up_normal));
                mFab.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.md_styled_slide_down_normal));

            }
        }, 300);

        mRecyclerView.addOnScrollListener(new MyOnScrollListener(mFab));
    }



    private BroadcastReceiver receiverSync = new BroadcastReceiver() {

        /**
         * "onReceive" è il metodo principale di un broadcast receiver, che va obbligatoriamente implementato.
         * Questo metodo viene richiamato quando un particolare broadcast receiver riceve un messaggio dalla
         * "fonte" di cui è in ascolto.
         * All'interno di questo metodo viene posto il corpo delle funzionalità da eseguire.
         *
         * @param context riferimento al contesto
         * @param intent intent allegato al messaggio inviato in broadcast
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(INTENT_ACTION_INT)) {
                //sync = intent.getIntExtra(INTENT_EXTRA, 0);
                updateUsersList();
                updateVacList();
                updateVacTypeList();
                //if(sync > 0)
                    updateNavigationUI(navigationView.getMenu(), context);
                //else

                VakcinoDbManager dbManager = new VakcinoDbManager(context);
                users = dbManager.getUsers(Utils.getAccount(context));
                vaccinations = dbManager.getVaccinations();
                vacTypeList = dbManager.getVaccinationType();

            }
        }
    };

    private void updateUsersList() {
        VakcinoDbManager dbManager = new VakcinoDbManager(getApplicationContext());
        users = dbManager.getUsers(email);
    }

    private void updateVacList() {
        VakcinoDbManager dbManager = new VakcinoDbManager(getApplicationContext());
        vaccinations = dbManager.getVaccinations();
    }

    private void updateVacTypeList() {
        VakcinoDbManager dbManager = new VakcinoDbManager(getApplicationContext());
        vacTypeList = dbManager.getVaccinationType();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
           AsyncTask<Void, Void, Boolean> sync = new AsyncTask<Void, Void, Boolean>() {

                @Override
                protected Boolean doInBackground(Void... params) {
                    VakcinoDbManager dbManager = new VakcinoDbManager(getApplicationContext());
                    List<Utente> unsyncedUsers = dbManager.getUnsyncedUsers(Utils.getAccount(getApplicationContext()));

                    for (Utente user : unsyncedUsers) {
                        RemoteDBInteractions.syncUserLocalToRemote(user);
                        user.setStatus(VakcinoDbManager.SYNCED_WITH_SERVER);
                        dbManager.updateUser(user);
                    }

                    for (Utente user : dbManager.getUsers(Utils.getAccount(getApplicationContext()))) {
                        List<Libretto> unsyncedBooklet = dbManager.getUnsyncedBooklet(user);
                        for (Libretto booklet : unsyncedBooklet) {
                            RemoteDBInteractions.syncBookletLocalToRemote(booklet, Utils.getAccount(getApplicationContext()));
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    updateNavigationUI(navigationView.getMenu(), getApplicationContext());
                }
            };

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateNavigationUI(navigationView.getMenu(), getApplicationContext());
        registerReceiver(receiverConnectivity, filter);
        registerReceiver(receiverSync, intentFilter);
    }

    @Override
    public void onPause() {
        unregisterReceiver(receiverConnectivity);
        unregisterReceiver(receiverSync);
        super.onPause();
    }

    public void updateNavigationUI(Menu menu, Context context) {
        int i = USER_ID;
        for (Utente u: users) {
            try {
                menu.removeItem(i);
                menu.add(R.id.nav_users, i, Menu.FIRST, u.getName() + " " + u.getSurname()).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_person)).setCheckable(true);

            } catch (Exception e) {
                e.printStackTrace();
                menu.add(R.id.nav_users, i, Menu.FIRST, u.getName() + " " + u.getSurname()).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_person)).setCheckable(true);
            }

            i++;

        }
    }

    private void initializeNavigationUI(Menu menu, Context context) {
       /* VakcinoDbManager dbManager = new VakcinoDbManager(context);
        List<Utente> users = dbManager.getUsers(email);*/
        //menu.add(R.id.nav_users, 0, Menu.FIRST, "aggiungi").setIcon(ContextCompat.getDrawable(context, android.R.drawable.ic_input_add));
        menu.getItem(0).setCheckable(false);

        menu.add(R.id.drawer_options, NOTICE_ID, Menu.CATEGORY_SECONDARY, getString(R.string.notice_settings)).setIcon(ContextCompat.getDrawable(context, android.R.drawable.ic_lock_idle_alarm));
        menu.add(R.id.drawer_options, LOGOUT_ID, Menu.CATEGORY_SECONDARY, getString(R.string.log_out)).setIcon(ContextCompat.getDrawable(context, android.R.drawable.ic_lock_power_off));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // se è aperto il drawer lo chiudo
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //altrimenti esco dopo il secondo click
            if (doubleBackToExitPressedOnce) {
                //super.onBackPressed();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            this.doubleBackToExitPressedOnce = true;

            Toast.makeText(MainActivity.this, message != null ? message : getString(R.string.double_back_exit), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.d("ID_DEBUG", Integer.toString(id));

        // Sezione aggiunta utente
        if (id == R.id.add_user) {
            Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
            startActivity(intent);
        } // sezione logout
        else if (id == LOGOUT_ID) {
            Utils.setLogged(MainActivity.this, false);
            Utils.setAccount(MainActivity.this, "");
            VakcinoDbHelper dbHelper = new VakcinoDbHelper(getApplicationContext());
            this.deleteDatabase(dbHelper.getDatabaseName());
            Intent returnToLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(returnToLogin);
            Toast.makeText(MainActivity.this, message != null ? message : getString(R.string.logout_successfully), Toast.LENGTH_SHORT).show();
        }
        else if (id == NOTICE_ID) {

        }
        else {
            VakcinoDbManager dbManager = new VakcinoDbManager(getApplicationContext());
            Utente user = users.get(id-USER_ID);
            item.setChecked(true);
            TextView textViewUserName = (TextView) findViewById(R.id.user_name_title);
            textViewUserName.setText(user.toString());
            adapterToDo = new VaccinationsBookletAdapter(dbManager.getAllBooklet(user), user, vaccinations, vacTypeList, VaccinationsBookletAdapter.CHOICE_TO_DO);
            mRecyclerView.setAdapter(adapterToDo);
            //mFab = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
            mFab.setVisibility(View.VISIBLE);
            View toDoChoice = findViewById(R.id.material_design_floating_action_menu_item1);
            View doneChoice = findViewById(R.id.material_design_floating_action_menu_item2);
            toDoChoice.setOnClickListener(new OnViewVacToDoClickListener(mRecyclerView, user, mFab));
            doneChoice.setOnClickListener(new OnViewVacDoneClickListener(mRecyclerView, user, mFab));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
