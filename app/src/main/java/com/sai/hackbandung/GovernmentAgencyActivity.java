package com.sai.hackbandung;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sai.hackbandung.DatabaseClass.GovernmentInfo;

public class GovernmentAgencyActivity extends AppCompatActivity {
    GridView grid;
    String[] web = {
            "PD.Kebersihan",
            "Disdik",
            "Diskominfo",
            "PDAM",
            "dispora",
            "DISBUDPAR",
            "distarcip",
            "dbmp",
            "SatpolPP",
            "PPHD PolPP",
            "bappeda",
            "PJU DBMP"
    } ;
    int[] imageId = {
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_camera
    };

    private String usernameFromUserRole;
    private String emailFromUserRole;
    private String passwordFromUserRole;
    private String fullNameFromUserRole;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.GovernmentAgencyActivity_TITLE);
        setContentView(R.layout.activity_government_agency);

        // initialize the database
        database = FirebaseDatabase.getInstance();

        // retrieve the username from signup
        retrieveDataFromSignup(savedInstanceState);

        CustomGrid adapter = new CustomGrid(GovernmentAgencyActivity.this, web, imageId);
        grid = (GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(GovernmentAgencyActivity.this, "You Clicked at " + web[+ position], Toast.LENGTH_SHORT).show();

                // store the agency type in the DB
                storeAgencyType(web[+ position]);

                // redirect to the main page
                GovernmentAgencyActivity.this.startActivity(new Intent(GovernmentAgencyActivity.this, NavigationDrawerGovernmentActivity.class));

            }
        });

    }

    private void retrieveDataFromSignup(Bundle savedInstanceState) {

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();

            if(extras == null) {

                usernameFromUserRole = null;
                emailFromUserRole = null;
                passwordFromUserRole = null;
                fullNameFromUserRole = null;

            } else {

                usernameFromUserRole = extras.getString("USERNAME_FROM_USER_ROLE");
                emailFromUserRole = extras.getString("EMAIL_FROM_USER_ROLE");
                passwordFromUserRole = extras.getString("PASSWORD_FROM_USER_ROLE");
                fullNameFromUserRole = extras.getString("FULLNAME_FROM_USER_ROLE");

            }

        } else {

            usernameFromUserRole = (String) savedInstanceState.getSerializable("USERNAME_FROM_USER_ROLE");
            emailFromUserRole = (String) savedInstanceState.getSerializable("EMAIL_FROM_USER_ROLE");
            passwordFromUserRole = (String) savedInstanceState.getSerializable("PASSWORD_FROM_USER_ROLE");
            fullNameFromUserRole = (String) savedInstanceState.getSerializable("FULLNAME_FROM_USER_ROLE");

        }

    }

    private void storeAgencyType(String agencyType) {

        GovernmentInfo new_GI = new GovernmentInfo(usernameFromUserRole, emailFromUserRole, passwordFromUserRole, fullNameFromUserRole, agencyType);

        DatabaseReference myRef = database.getReference("GOVERNMENT/" + usernameFromUserRole);
        myRef.setValue(new_GI);

    }

}