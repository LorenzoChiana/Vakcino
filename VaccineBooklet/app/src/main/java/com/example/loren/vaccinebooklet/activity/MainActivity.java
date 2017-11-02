package com.example.loren.vaccinebooklet.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.loren.vaccinebooklet.utils.DateInteractions;
import com.example.loren.vaccinebooklet.utils.NetworkStateReceiver;
import com.example.loren.vaccinebooklet.utils.Utils;
import com.github.clans.fab.FloatingActionMenu;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int LOGOUT_ID = 1;
    private static final int CALENDAR_ID = 2;
    private static final int USER_ID = 3;

    public static final String INTENT_EXTRA = "finish";
    public static final String INTENT_ACTION_INT = "com.example.loren.vaccinebooklet.intent.action.TEST.int";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 123;
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
        for (Utente u : users) {
            menu.removeItem(i);
            menu.add(R.id.nav_users, i, Menu.FIRST, u.getName() + " " + u.getSurname()).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_person)).setCheckable(true);
            i++;
        }
    }

    private void initializeNavigationUI(Menu menu, Context context) {
       /* VakcinoDbManager dbManager = new VakcinoDbManager(context);
        List<Utente> users = dbManager.getUsers(email);*/
        //menu.add(R.id.nav_users, 0, Menu.FIRST, "aggiungi").setIcon(ContextCompat.getDrawable(context, android.R.drawable.ic_input_add));
        menu.getItem(0).setCheckable(false);

        menu.add(R.id.drawer_options, CALENDAR_ID, Menu.CATEGORY_SECONDARY, getString(R.string.notice_settings)).setIcon(ContextCompat.getDrawable(context, android.R.drawable.ic_lock_idle_alarm));
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
        } else if (id == CALENDAR_ID) {
            final List<Utente> selectedUsers = new ArrayList<>();
            new MaterialDialog.Builder(this)
                    .title("Titolo")
                    .items(users)
                    .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            for (Integer i: which) {
                                selectedUsers.add(users.get(i));
                            }
                            /*AsyncTask<Void, Void, Boolean> po = new AsyncTask<Void, Void, Boolean>(){
                                ProgressDialog progressDialog = null;
                                Context context = getApplication();
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    progressDialog = new ProgressDialog(context,
                                            R.style.AppTheme_Dark_Dialog);
                                    progressDialog.setIndeterminate(true);
                                    progressDialog.setMessage(context.getString(R.string.waiting));
                                    progressDialog.show();
                                }

                                @Override
                                protected void onPostExecute(final Boolean success) {
                                    progressDialog.dismiss();
                                }

                                @Override
                                protected Boolean doInBackground(Void... params) {
                                    VakcinoDbManager dbManager = new VakcinoDbManager(context);
                                    List<TipoVaccinazione> listVacType = dbManager.getVaccinationType();
                                    List<Vaccinazione> listVac = dbManager.getVaccinations();
                                    int cont = 0;
                                    for (Utente user : selectedUsers) {
                                        List<Libretto> toDoList = dbManager.getToDoList(user);
                                        for (Libretto page : toDoList) {
                                            TipoVaccinazione vacType = listVacType.get(page.getIdTipoVac());
                                            int i = 0;
                                            while (listVac.iterator().hasNext() && !listVac.get(i).getAntigen().equals(listVacType.get(toDoList.get(cont).getIdTipoVac() - 1).getAntigen())) {
                                                listVac.iterator().next();
                                                i++;
                                            }
                                            Vaccinazione vac = listVac.get(i);
                                            Calendar beginTime = Calendar.getInstance();
                                            String dateDa = DateInteractions.translateDate(user.getbirthdayDate(), vacType.getDa());
                                            beginTime.set(DateInteractions.getYear(dateDa),
                                                    DateInteractions.getMonth(dateDa),
                                                    DateInteractions.getDay(dateDa),
                                                    0, 0);
                                            Calendar endTime = Calendar.getInstance();
                                            String dateA = DateInteractions.translateDate(user.getbirthdayDate(), vacType.getA());
                                            endTime.set(DateInteractions.getYear(dateA),
                                                    DateInteractions.getMonth(dateA),
                                                    DateInteractions.getDay(dateA),
                                                    0, 0);
                                            addCalendarEvent(beginTime, endTime, vac.getName(), vac.getDescription());
                                            cont++;
                                        }
                                    }
                                    return null;
                                }
                            };*/
                            exportVacIntoCalendar(selectedUsers);
                            return true;
                        }
                    })
                    .positiveText("scegli")
                    .show();
        } else {
            VakcinoDbManager dbManager = new VakcinoDbManager(getApplicationContext());
            Utente user = users.get(id - USER_ID);
            item.setChecked(true);
            adapterToDo = new VaccinationsBookletAdapter(dbManager.getAllBooklet(user), user, vaccinations, vacTypeList, VaccinationsBookletAdapter.CHOICE_TO_DO);
            mRecyclerView.setAdapter(adapterToDo);
            mFab.getMenuIconView().setImageResource(R.drawable.ic_visibility);
            mFab.setVisibility(View.VISIBLE);
            View toDoChoice = findViewById(R.id.material_design_floating_action_menu_item1);
            View doneChoice = findViewById(R.id.material_design_floating_action_menu_item2);
            TextView appBarTitle = (TextView) findViewById(R.id.appbar_title);
            appBarTitle.setText(R.string.bookletToDo);
            toDoChoice.setOnClickListener(new OnViewVacToDoClickListener(mRecyclerView, user, mFab, appBarTitle));
            doneChoice.setOnClickListener(new OnViewVacDoneClickListener(mRecyclerView, user, mFab, appBarTitle));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void exportVacIntoCalendar(List<Utente> selectedUsers) {
        VakcinoDbManager dbManager = new VakcinoDbManager(this);
        List<TipoVaccinazione> listVacType = dbManager.getVaccinationType();
        List<Vaccinazione> listVac = dbManager.getVaccinations();
        int cont = 0;
        for (Utente user : selectedUsers) {
            List<Libretto> toDoList = dbManager.getToDoList(user);
            for (Libretto page : toDoList) {
                int i = 0;
                TipoVaccinazione vacType = listVacType.get(toDoList.get(cont).getIdTipoVac() - 1);
                //i = 0;
                while (listVac.iterator().hasNext() && !listVac.get(i).getAntigen().equals(listVacType.get(toDoList.get(cont).getIdTipoVac() - 1).getAntigen())) {
                    listVac.iterator().next();
                    i++;
                }
                Vaccinazione vac = listVac.get(i);
                Calendar beginTime = Calendar.getInstance(Locale.ITALIAN);
                String dateDa = DateInteractions.translateDate(user.getbirthdayDate(), vacType.getDa());
                beginTime.set(DateInteractions.getYear(dateDa),
                        DateInteractions.getMonth(dateDa) - 1,
                        DateInteractions.getDay(dateDa),
                        0, 0);
                Calendar endTime = Calendar.getInstance(Locale.ITALIAN);
                String dateA = DateInteractions.translateDate(user.getbirthdayDate(), vacType.getA());
                endTime.set(DateInteractions.getYear(dateA),
                        DateInteractions.getMonth(dateA) - 1,
                        DateInteractions.getDay(dateA),
                        0, 0);
                addCalendarEvent(beginTime, endTime, vac.getName(), vac.getDescription());
                cont++;
            }
        }
    }

    public void addCalendarEvent(Calendar beginTime, Calendar endTime, String title, String description) {
        long calID = 1;
        long startMillis = 0;
        long endMillis = 0;
        /*Calendar beginTime = Calendar.getInstance();
        beginTime.set(2017, 11, 9, 7, 30);*/
        startMillis = beginTime.getTimeInMillis();
        /*Calendar endTime = Calendar.getInstance();
        endTime.set(2017, 11, 9, 8, 45);*/
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/New_York");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_CALENDAR)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CALENDAR},
                        MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);

                // MY_PERMISSIONS_REQUEST_READ_CALENDAR is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        }

    }

}
