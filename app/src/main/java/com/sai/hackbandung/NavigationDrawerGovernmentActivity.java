package com.sai.hackbandung;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sai.hackbandung.Fragments.AllReportsGovernment;
import com.sai.hackbandung.Fragments.CompletedReportsGovernment;
import com.sai.hackbandung.Fragments.InProgressReportsGovernment;

public class NavigationDrawerGovernmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String usernameFromSignInOrGovAgency;
    private String fullnameFromSignInOrGovAgency;
    private String resAgencyFromSignInOrGovAgency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.NavigationDrawerGovernmentActivity_TITLE);
        setContentView(R.layout.activity_navigation_drawer_government);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // retrieve intent data from User Role Activity
        retrieveIntentData(savedInstanceState);

        //Toast.makeText(NavigationDrawerGovernmentActivity.this, "username from signin or agency: " + usernameFromSignInOrAgency, Toast.LENGTH_LONG).show();
        //Toast.makeText(NavigationDrawerGovernmentActivity.this, "fullname from signin or agency: " + fullnameFromSignInOrAgency, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer_government, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        // Data to be sent to Fragment:
        // - responsibleAgency

        // Use bundle to pass data
        Bundle data = new Bundle();
        data.putString("USERNAME_FROM_NAVDRAWERGOVERNMENT", usernameFromSignInOrGovAgency);
        data.putString("FULLNAME_FROM_NAVDRAWERGOVERNMENT", fullnameFromSignInOrGovAgency);
        data.putString("RESAGENCY_FROM_NAVDRAWERGOVERNMENT", resAgencyFromSignInOrGovAgency);

        switch (id){
            case R.id.nav_ALL_REPORTS:
                fragment = new AllReportsGovernment();
                fragment.setArguments(data);
                break;
            case R.id.nav_IN_PROGRESS_REPORTS:
                fragment = new InProgressReportsGovernment();
                fragment.setArguments(data);
                break;
            case R.id.nav_COMPLETED_REPORTS:
                fragment = new CompletedReportsGovernment();
                fragment.setArguments(data);
                break;
            default:
                fragment = new AllReportsGovernment();
                fragment.setArguments(data);
                break;
        }

        if (fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void retrieveIntentData(Bundle savedInstanceState) {

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();

            if(extras == null) {

                usernameFromSignInOrGovAgency = null;
                fullnameFromSignInOrGovAgency = null;
                resAgencyFromSignInOrGovAgency = null;

            } else {

                usernameFromSignInOrGovAgency = extras.getString("USERNAME_FROM_SIGNIN_OR_AGENCY");
                fullnameFromSignInOrGovAgency = extras.getString("FULLNAME_FROM_SIGNIN_OR_AGENCY");
                resAgencyFromSignInOrGovAgency = extras.getString("RESAGENCY_FROM_SIGNIN_OR_AGENCY");

            }

        } else {

            usernameFromSignInOrGovAgency = (String) savedInstanceState.getSerializable("USERNAME_FROM_SIGNIN_OR_AGENCY");
            fullnameFromSignInOrGovAgency = (String) savedInstanceState.getSerializable("FULLNAME_FROM_SIGNIN_OR_AGENCY");
            resAgencyFromSignInOrGovAgency = (String) savedInstanceState.getSerializable("RESAGENCY_FROM_SIGNIN_OR_AGENCY");

        }

    }

}
