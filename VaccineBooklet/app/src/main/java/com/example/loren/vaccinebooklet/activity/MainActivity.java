package com.example.loren.vaccinebooklet.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.loren.vaccinebooklet.R;

import com.example.loren.vaccinebooklet.database.VakcinoDbHelper;
import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.fragment.HomePageFragment;
import com.example.loren.vaccinebooklet.fragment.VaccinesListFragment;
import com.example.loren.vaccinebooklet.model.Libretto;
import com.example.loren.vaccinebooklet.model.TipoVaccinazione;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.model.Vaccinazione;
import com.example.loren.vaccinebooklet.request.RemoteDBInteractions;
import com.example.loren.vaccinebooklet.tasks.SyncDBLocalToRemote;
import com.example.loren.vaccinebooklet.utils.DateInteractions;
import com.example.loren.vaccinebooklet.utils.InternetConnection;
import com.example.loren.vaccinebooklet.utils.NetworkStateReceiver;
import com.example.loren.vaccinebooklet.utils.Utils;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.heinrichreimersoftware.materialdrawer.DrawerView;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerHeaderItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    private static final int LOGOUT_ID = 2;
    private static final int CALENDAR_ID = 1;
    private static final int ADD_USER = 0;
    //private static final int USER_ID = 3;

    public static final String INTENT_EXTRA = "finish";
    public static final String INTENT_ACTION_INT = "com.example.loren.vaccinebooklet.intent.action.TEST.int";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 123;
    private static final int VIEW_VAC = 0;
    private static final int EDIT_USER = 0;
    private static final int DELETE_USER = 1;
    private boolean doubleBackToExitPressedOnce;
    private String message;
    private String email;

    //private ViewPager mViewPager;
    //public RecyclerView mRecyclerView;
    private TextView twEmailUser;
    private NavigationView navigationView;
    private NetworkStateReceiver receiverConnectivity;
    private IntentFilter filter, intentFilter;
    private static RecyclerView.Adapter adapterToDo;

    private RecyclerView.LayoutManager layoutManager;
    private List<Utente> users;
    private List<Vaccinazione> vaccinations;
    private List<TipoVaccinazione> vacTypeList;
    public static MaterialStyledDialog.Builder materialStyleDialog, materialInfoStyleDialog;

    private DrawerView drawer;
    private DrawerLayout drawerLayout;

    private ActionBarDrawerToggle drawerToggle;

    private boolean afterLogin = false, afterDelete = false;
    private int idSelectedUser = 0, flagProfile = 0;
    private static boolean govar = true;

    public static boolean canIGo(){
        return govar;
    }

    public static void stop(){
        govar = false;
    }

    public static void go(){
        govar = true;
    }

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

        materialStyleDialog = new MaterialStyledDialog.Builder(MainActivity.this);
        materialInfoStyleDialog = new MaterialStyledDialog.Builder(MainActivity.this);

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

        receiverConnectivity = new NetworkStateReceiver();
        receiverConnectivity.setAfterLogin(afterLogin);
        filter = new IntentFilter();
        filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiverConnectivity, filter);
        intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_ACTION_INT);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        HomePageFragment fragment = HomePageFragment.newInstance();
        transaction.add(R.id.activity_main, fragment);
        transaction.commit();

        drawerInitialize();
    }

    private void drawerInitialize() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer = (DrawerView) findViewById(R.id.drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        drawerLayout.addDrawerListener(drawerToggle);
        drawerLayout.closeDrawer(drawer);

        updateUsersDrawer();

        drawer.addFixedItem(new DrawerItem()
                .setRoundedImage((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.ic_add_person), DrawerItem.SMALL_AVATAR)
                .setTextPrimary(getString(R.string.add_user))
        );

        drawer.addFixedItem(new DrawerItem()
                .setImage(ContextCompat.getDrawable(this, R.drawable.ic_export))
                .setTextPrimary(getString(R.string.export))
        );

        drawer.addFixedItem(new DrawerItem()
                .setImage(ContextCompat.getDrawable(this, R.drawable.ic_log_out))
                .setTextPrimary(getString(R.string.log_out))
        );

        drawer.setOnFixedItemClickListener(new DrawerItem.OnItemClickListener() {
            @Override
            public void onClick(DrawerItem item, long id, int position) {
                //drawer.selectFixedItem(position);
                switch (position) {
                    case ADD_USER:
                        Intent intent = new Intent(MainActivity.this, UserActivity.class);
                        intent.putExtra(UserActivity.USER_INTERACTION, UserActivity.CREATE_USER);
                        startActivity(intent);
                        break;
                    case LOGOUT_ID:
                        logOut();
                        break;
                    case CALENDAR_ID:
                        final List<Utente> selectedUsers = new ArrayList<>();
                        new MaterialDialog.Builder(MainActivity.this)
                                .title(R.string.export_calendar_question)
                                .items(users)
                                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                        for (Integer i : which) {
                                            selectedUsers.add(users.get(i));
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                exportVacIntoCalendar(selectedUsers);
                                            }
                                        });
                                        return true;
                                    }
                                })
                                .positiveText(R.string.choose)
                                .show();
                        break;
                }
            }
        });

        for (Utente u : users) {
            addUserToDrawerView(drawer, u.getId(), u.toString(), DateInteractions.changeDateFormat(u.getbirthdayDate(), "yyyy-MM-dd", "dd/MM/yyyy"));
        }

        drawer.setOnProfileSwitchListener(new DrawerProfile.OnProfileSwitchListener() {
            @Override
            public void onSwitch(DrawerProfile oldProfile, long oldId, DrawerProfile newProfile, long newId) {
                drawerLayout.closeDrawer(GravityCompat.START);
                //Toast.makeText(MainActivity.this, "Switched from profile *" + oldId + " to profile *" + newId, Toast.LENGTH_SHORT).show();
                idSelectedUser = (int) newId - 1;
                viewVac(idSelectedUser);
            }
        });
    }

    private void viewVac(int userId) {
        Utente user = users.get(userId);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();


        VaccinesListFragment fragment = VaccinesListFragment.newInstance(user);
        transaction.replace(R.id.activity_main, fragment);
        transaction.commit();
    }

    private void deleteUser(Utente user) {
        VakcinoDbManager vakcinoDbManager = new VakcinoDbManager(this);
        user.setStatus(VakcinoDbManager.DELETED);
        vakcinoDbManager.updateUser(user);
        afterDelete = true;
    }

    private void addUserToDrawerView(DrawerView drawer, long idUser, String name, String description) {
        drawer.addProfile(new DrawerProfile()
                .setId(idUser)
                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.ic_person))
                .setBackground(ContextCompat.getDrawable(this, R.drawable.header_1))
                .setName(name)
                .setDescription(description)
        );
    }

    private void logOut() {
        Utils.setLogged(MainActivity.this, false);
        Utils.setAccount(MainActivity.this, "");
        VakcinoDbHelper dbHelper = new VakcinoDbHelper(getApplicationContext());
        this.deleteDatabase(dbHelper.getDatabaseName());
        Intent returnToLogin = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(returnToLogin);
        Toast.makeText(MainActivity.this, message != null ? message : getString(R.string.logout_successfully), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        /*switch (item.getItemId()) {

        }*/

        return super.onOptionsItemSelected(item);
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
                updateAmbient();
            }
        }
    };

    private void updateAmbient() {
        updateUsersList();
        updateVacList();
        updateVacTypeList();
        if(InternetConnection.haveInternetConnection(getApplicationContext())){
            new SyncDBLocalToRemote().execute(getApplicationContext());
        }

        drawer.clearProfiles();
        int i = 1;
        for (Utente u : users) {
            addUserToDrawerView(drawer, i, u.toString(), DateInteractions.changeDateFormat(u.getbirthdayDate(), "yyyy-MM-dd", "dd/MM/yyyy"));
            i++;
        }
        /*if(afterDelete){
            flagProfile = 0;
            afterDelete = false;
        }*/
        updateUsersDrawer();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private void updateUsersDrawer() {
        if (users.size() > 0) {
            if (drawer.findItemById(EDIT_USER) == null && drawer.findItemById(DELETE_USER) == null) {
                drawer.addItem(new DrawerItem()
                        .setId(EDIT_USER)
                        .setImage(ContextCompat.getDrawable(this, R.drawable.ic_edit))
                        .setTextPrimary(getString(R.string.menu_edit))
                );
                drawer.addItem(new DrawerItem()
                        .setId(DELETE_USER)
                        .setImage(ContextCompat.getDrawable(this, R.drawable.ic_delete))
                        .setTextPrimary(getString(R.string.menu_delete))
                );
            }
        } else if (flagProfile == 0) {
            drawer.addProfile(newEmptyProfile());
            flagProfile++;
        } else if (afterDelete && users.size()==0) {
            drawer.clearItems();
            /*int size = drawer.getProfiles().size();
            for (int i = 0; i < size; i++){
                DrawerProfile profile = drawer.getProfiles().get(i);
                profile.removeName();
                profile.removeDescription();
            }
            drawer.clearProfiles();
            drawer.addProfile(newEmptyProfile());*/
            drawer.clearProfiles();
            afterDelete = false;
            drawerLayout.closeDrawer(drawer);;
            HomePageFragment fragmentHome = HomePageFragment.newInstance();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.activity_main, fragmentHome);
            transaction.commit();
            //drawer.clearProfiles();

           // drawer.addProfile(newEmptyProfile());
            //drawerInitialize();
        }

        drawer.setOnItemClickListener(new DrawerItem.OnItemClickListener() {
            @Override
            public void onClick(DrawerItem item, long id, int position) {
                //drawer.selectItem(position);
                //Toast.makeText(MainActivity.this, "Clicked item #" + position, Toast.LENGTH_SHORT).show();
                switch (position) {
                    case EDIT_USER:
                        Intent intent = new Intent(MainActivity.this, UserActivity.class);
                        intent.putExtra(UserActivity.USER_INTERACTION, UserActivity.EDIT_USER);
                        intent.putExtra("Utente", users.get(idSelectedUser));
                        startActivity(intent);
                        break;
                    case DELETE_USER:
                        materialStyleDialog.setHeaderDrawable(R.drawable.header_2_waring)
                                .setTitle(R.string.warning)
                                .setDescription(R.string.delete_question)
                                .setNegativeText(R.string.label_cancel)
                                .setPositiveText(R.string.label_positive)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        deleteUser(users.get(idSelectedUser));
                                        updateAmbient();
                                    }
                                })
                                .show();
                        break;
                }
            }
        });
    }

    private DrawerProfile newEmptyProfile() {
        return new DrawerProfile()
                .setId(1)
                .setBackground(ContextCompat.getDrawable(this, R.drawable.header_1))
                .setName(getString(R.string.app_name))
                .setDescription(email)
                .setRoundedAvatar((BitmapDrawable) ContextCompat.getDrawable(this, R.mipmap.ic_launcher));
    }

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
            new AsyncTask<Void, Void, Boolean>() {

                @Override
                protected Boolean doInBackground(Void... params) {
                    VakcinoDbManager dbManager = new VakcinoDbManager(getApplicationContext());
                    List<Utente> unsyncedUsers = dbManager.getLocalUnsyncedUsers();

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
                    //updateNavigationUI(navigationView.getMenu(), getApplicationContext());
                }
            };
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiverConnectivity, filter);
        registerReceiver(receiverSync, intentFilter);
        //updateNavigationUI(navigationView.getMenu(), getApplicationContext());
    }

    @Override
    public void onPause() {
        unregisterReceiver(receiverConnectivity);
        unregisterReceiver(receiverSync);
        super.onPause();
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

    private void exportVacIntoCalendar(List<Utente> selectedUsers) {
        VakcinoDbManager dbManager = new VakcinoDbManager(this);
        List<TipoVaccinazione> listVacType = dbManager.getVaccinationType();
        List<Vaccinazione> listVac = dbManager.getVaccinations();

        for (Utente user : selectedUsers) {
            int cont = 0;
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
                addCalendarEvent(beginTime, endTime, user.toString() + " - " + vac.getName(), vac.getDescription());
                cont++;
            }
        }
    }

    public void addCalendarEvent(Calendar beginTime, Calendar endTime, String title, String description) {
        long calID = 1;
        long startMillis = 0;
        long endMillis = 0;
        startMillis = beginTime.getTimeInMillis();
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.HAS_ALARM, 0);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/New_York");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_CALENDAR)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CALENDAR},
                        MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
            }
        } else {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        }
    }
}