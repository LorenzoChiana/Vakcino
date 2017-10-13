package com.example.loren.vaccinebooklet.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loren.vaccinebooklet.Item;
import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.database.VakcinoDbManager;
import com.example.loren.vaccinebooklet.fragment.PersonFragment;
import com.example.loren.vaccinebooklet.fragment.PetFragment;
import com.example.loren.vaccinebooklet.model.ActionModel;
import com.example.loren.vaccinebooklet.model.Utente;
import com.example.loren.vaccinebooklet.utils.HTTPHelper;
import com.example.loren.vaccinebooklet.utils.JSONHelper;
import com.example.loren.vaccinebooklet.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_TYPE = "type";
    public static final String URL_GET_USER = "http://vakcinoapp.altervista.org/getUsers.php";
    private static final int LOGOUT_ID = 1;
    private static final int NOTICE_ID = 2;
    private static final String URL_GET_UNSYNC_USER = "http://vakcinoapp.altervista.org/getUnsyncedUsers.php";
    private boolean doubleBackToExitPressedOnce;
    private String message;
    private String email;

    private ViewPager mViewPager;

    private TextView twEmailUser;
    private ActionModel actionModel;


    //private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.message = null;
        this.doubleBackToExitPressedOnce = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionModel = new ActionModel(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeNavigationUI(navigationView.getMenu());


        View headerView = navigationView.getHeaderView(0);
        twEmailUser = (TextView) headerView.findViewById(R.id.tw_email);
        /*
            Se ho appena fatto il login/sign up
            allora setto la sharedpreference con l'email dell'account
            altrimenti prendo l'email direttamente dal sharedpreference
         */
        final String extraEmail;
        try {
            extraEmail = getIntent().getExtras().getString("email");
            Utils.setAccount(MainActivity.this, extraEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        email = Utils.getAccount(MainActivity.this);
        twEmailUser.setText(email);

        /* Setting up the three main Pages (Fragment) of the MainActivity */
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        //mViewPager.setAdapter(mSectionsPagerAdapter);

        /*TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);*/

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.waiting));
        progressDialog.show();

        new AsyncTask<String, Void, String>(){
            @Override
            protected String doInBackground(String... strings) {

                if(connectionOK(getApplicationContext())) {
                    HashMap<String,String> hm = new HashMap<>();
                    hm.put("email", email);
                    return HTTPHelper.connectPost(URL_GET_UNSYNC_USER, hm);

                    /*List<Utente> prova = dbManager.getUsers(email);
                    if(prova.isEmpty())
                        Log.d("DBMANAGER", "vuoto");
                    Log.d("DBMANAGER", prova.toString());
                    return prova.toString();*/
                }

                return "";
            }
            @Override
            protected void onPostExecute(String result) {
                ArrayList<Utente> remoteUsers = JSONHelper.parseUser(result);
                VakcinoDbManager dbManager = new VakcinoDbManager(getApplicationContext());
                for (Utente ru: remoteUsers) {
                    if(!dbManager.updateUser(ru)){
                        dbManager.addUser(ru);
                    }
                }

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }
        }.execute();
    }

    private boolean connectionOK(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    private void initializeNavigationUI(Menu menu) {
        menu.add(R.id.drawer_options, NOTICE_ID, Menu.CATEGORY_SECONDARY, getString(R.string.notice_settings)).setIcon(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.ic_lock_idle_alarm));
        menu.add(R.id.drawer_options, LOGOUT_ID, Menu.CATEGORY_SECONDARY, getString(R.string.log_out)).setIcon(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.ic_lock_power_off));
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
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            Menu menu = navigationView.getMenu();
            menu.add(R.id.nav_users, 2, Menu.FIRST + 1, "aggiunto").setIcon(R.drawable.ic_person);
            Intent intent = new Intent(MainActivity.this, NewUserActivity.class);
            startActivity(intent);
        } // sezione logout
        else if (id == LOGOUT_ID) {
            Utils.setLogged(MainActivity.this, false);
            Utils.setAccount(MainActivity.this, "");
            Utils.setDBVersion(MainActivity.this, -1);
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
