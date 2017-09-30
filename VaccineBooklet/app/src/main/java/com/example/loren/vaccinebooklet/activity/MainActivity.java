package com.example.loren.vaccinebooklet.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loren.vaccinebooklet.Item;
import com.example.loren.vaccinebooklet.R;
import com.example.loren.vaccinebooklet.fragment.PersonFragment;
import com.example.loren.vaccinebooklet.fragment.PetFragment;
import com.example.loren.vaccinebooklet.utils.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean doubleBackToExitPressedOnce;
    private String message;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    TextView twEmailUser;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        twEmailUser.setText(Utils.getAccount(MainActivity.this));

        /* Setting up the three main Pages (Fragment) of the MainActivity */
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        //mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);

        /*
        setContentView(R.layout.nav_header_main);
        twEmailUser = (TextView) findViewById(R.id.tw_email);
        //final Bundle extras = getIntent().getExtras();
        String prova = twEmailUser.getText().toString();
        twEmailUser.setText(extras.getString("email"));
        //twEmailUser.getText();*/

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

        if (id == R.id.add_user) {

            final Item[] items;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                items = new Item[]{
                        new Item(getString(R.string.type_user1),
                                getResources().getDrawable(R.drawable.ic_person, null)),
                        new Item(getString(R.string.type_user2),
                                getResources().getDrawable(R.drawable.ic_pet_print, null))
                };
            } else {
                items = new Item[]{
                        new Item(getString(R.string.type_user1),
                                getResources().getDrawable(R.drawable.ic_person)),
                        new Item(getString(R.string.type_user2),
                                getResources().getDrawable(R.drawable.ic_pet_print))
                };
            }
            ListAdapter adapter = new ArrayAdapter<Item>(
                    this,
                    android.R.layout.select_dialog_item,
                    android.R.id.text1,
                    items){
                public View getView(int position, View convertView, ViewGroup parent) {
                    //Use super class to create the View
                    View myView = super.getView(position, convertView, parent);
                    TextView myTextView = (TextView)myView.findViewById(android.R.id.text1);

                    myTextView.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, null, null, null);

                    //Add margin between image and text (support various screen densities)
                    int dp5 = (int) (10 * getResources().getDisplayMetrics().density + 0.5f);
                    myTextView.setCompoundDrawablePadding(dp5);

                    return myView;
                }
            };


            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.choice_person_pet))
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            //...
                        }
                    }).show();
        } else if (id == R.id.log_out) {
            // logout
            Utils.setLogged(MainActivity.this, false);
            Utils.setAccount(MainActivity.this, "");
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

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private static final int MAX_TAB = 2;
        Fragment[] mainViewFragments;
        FragmentManager fm;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;

            mainViewFragments = new Fragment[MAX_TAB];

            mainViewFragments[0] = PersonFragment.newInstance(/*dataModel.getLoggedUser().getFavorites()*/);
            mainViewFragments[1] = PetFragment.newInstance();

        /*if (dataModel.hasOpenOrder()){
            mainViewFragments[1] = OrderFragment.newInstance(dataModel.getOrder());
        } else {
            mainViewFragments[1] = NewOrderFragment.newInstance(dataModel.getRestaurants());
        }

        if (dataModel.hasOpenOrder()){
            mainViewFragments[2] = CompletedFragment.newInstance(dataModel.getOrder());
        } else {
            mainViewFragments[2] = CompletedFragment.newInstance();
        }*/
        }

        @Override
        public Fragment getItem(int position) {
            return mainViewFragments[position];
        }

        /* Override that permits to refresh the fragments on change */
        @Override
        public int getItemPosition(Object object) {
            if (object instanceof PersonFragment || object instanceof PetFragment) {
                return POSITION_NONE;
            } else {
                return POSITION_UNCHANGED;
            }
        }

        @Override
        public int getCount() {
            return mainViewFragments.length;
        }

        @Override
        public String getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_fragment_people);
                case 1:
                    return getString(R.string.title_fragment_pets);
            }
            return null;
        }

        /**
         * Method tha can replace one fragment inside the Adapter and can refresh the data
         *
         * @param i index of the fragment.
         * @param f new {@link Fragment} that is inserted inside the adapter.
         */
        public void replaceFragment(int i, Fragment f) {
            mainViewFragments[i] = f;
            notifyDataSetChanged();
        }
    }
}
