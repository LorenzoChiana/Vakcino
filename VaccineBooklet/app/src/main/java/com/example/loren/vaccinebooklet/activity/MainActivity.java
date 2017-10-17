package com.example.loren.vaccinebooklet.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.database.VakcinoDbHelper;
import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.request.RemoteDBInteractions;
import com.example.loren.vaccinebooklet.utils.HTTPHelper;
import com.example.loren.vaccinebooklet.utils.InternetConnection;
import com.example.loren.vaccinebooklet.utils.NetworkStateReceiver;
import com.example.loren.vaccinebooklet.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
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

    private ViewPager mViewPager;

    private TextView twEmailUser;
    private NavigationView navigationView;
    private boolean afterLogin;

    private BroadcastReceiver broadcastReceiver;
    private NetworkStateReceiver receiverConnectivity;
    private IntentFilter filter, intentFilter;
    private int sync;


    //private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.message = null;
        this.doubleBackToExitPressedOnce = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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

        mViewPager = (ViewPager) findViewById(R.id.container);

        receiverConnectivity = new NetworkStateReceiver();
        receiverConnectivity.setAfterLogin(afterLogin);
        filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiverConnectivity, filter);
        intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_ACTION_INT);

        //initializeNavigationUI(navigationView.getMenu(), getApplicationContext());
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
                sync = intent.getIntExtra(INTENT_EXTRA, 0);
                if(sync > 0)
                    updateNavigationUI(navigationView.getMenu(), context);
                else
                    initializeNavigationUI(navigationView.getMenu(), context);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        updateNavigationUI(navigationView.getMenu(), getApplicationContext());
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
        int menuSize = menu.size();
        if(menuSize > 1) {
            for(int i = 1; i < menuSize; i++) {
                menu.removeItem(i);
            }
            initializeNavigationUI(menu, context);
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final View view = findViewById(R.id.add_user);

                if (view != null) {
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            // Do something...

                            Toast.makeText(getApplicationContext(), "Long pressed", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }*/

    /*public static boolean connectionOK(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE);
    }*/

    private void initializeNavigationUI(Menu menu, Context context) {
        VakcinoDbManager dbManager = new VakcinoDbManager(context);
        List<Utente> users = dbManager.getUsers(email);
        int i = 0;
        for (Utente u: users) {
            menu.add(R.id.nav_users, USER_ID + i++, Menu.FIRST, u.getName() + " " + u.getSurname()).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_person));

        }
        menu.add(R.id.drawer_options, NOTICE_ID, Menu.CATEGORY_SECONDARY, getString(R.string.notice_settings)).setIcon(ContextCompat.getDrawable(context, android.R.drawable.ic_lock_idle_alarm));
        menu.add(R.id.drawer_options, LOGOUT_ID, Menu.CATEGORY_SECONDARY, getString(R.string.log_out)).setIcon(ContextCompat.getDrawable(context, android.R.drawable.ic_lock_power_off));
        /*int a = menu.size();
        CharSequence b = menu.getItem(0).getTitle();
        if(menu.size() > 2) {
            menu.getItem(0).getActionView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(MainActivity.this, message != null ? message : "Long click", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }*/
        /*MenuItem menuItem = menu.findItem(NOTICE_ID);
        for(int j = 1; j < menu.size() - 2; j++){
            View v = new View(this);
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(MainActivity.this, message != null ? message : "Long click", Toast.LENGTH_SHORT).show();
                    return false;
                }

            });
            menu.getItem(j).setActionView(v);
        }*/
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

       /* } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
