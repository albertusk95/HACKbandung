package com.sai.hackbandung;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sai.hackbandung.DatabaseClass.CitizensInfo;
import com.sai.hackbandung.DatabaseClass.GovernmentInfo;

public class UserRoleActivity extends AppCompatActivity {

    private Button buttonUserRole_CITIZENS;
    private Button buttonUserRole_GOVERNMENT;

    private String usernameFromSignUp;
    private String emailFromSignUp;
    private String passwordFromSignUp;
    private String fullNameFromSignUp;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_role);

        // initialize the database
        database = FirebaseDatabase.getInstance();

        // retrieve the username from signup
        retrieveDataFromSignup(savedInstanceState);

        // initialize views
        buttonUserRole_CITIZENS = (Button) findViewById(R.id.buttonUserRoleActivity_CITIZENS);
        buttonUserRole_GOVERNMENT = (Button) findViewById(R.id.buttonUserRoleActivity_GOVERNMENT);

        // attach event listener to button
        buttonUserRole_CITIZENS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // save user role to database based on the username from signup
                saveUserRole_CITIZENS();

                // redirect to the main page
                UserRoleActivity.this.startActivity(new Intent(UserRoleActivity.this, NavigationDrawerCitizensActivity.class));

            }

        });

        buttonUserRole_GOVERNMENT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // save user role to database based on the username from signup
                //saveUserRole_GOVERNMENT();

                // create intent with extra
                Intent intentForUserRole = new Intent(UserRoleActivity.this, GovernmentAgencyActivity.class);

                intentForUserRole.putExtra("USERNAME_FROM_USER_ROLE", usernameFromSignUp);
                intentForUserRole.putExtra("EMAIL_FROM_USER_ROLE", emailFromSignUp);
                intentForUserRole.putExtra("PASSWORD_FROM_USER_ROLE", passwordFromSignUp);
                intentForUserRole.putExtra("FULLNAME_FROM_USER_ROLE", fullNameFromSignUp);

                UserRoleActivity.this.startActivity(intentForUserRole);

            }

        });

    }

    private void retrieveDataFromSignup(Bundle savedInstanceState) {

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();

            if(extras == null) {

                usernameFromSignUp = null;
                emailFromSignUp = null;
                passwordFromSignUp = null;
                fullNameFromSignUp = null;

            } else {

                usernameFromSignUp = extras.getString("USERNAME_FROM_SIGNUP");
                emailFromSignUp = extras.getString("EMAIL_FROM_SIGNUP");
                passwordFromSignUp = extras.getString("PASSWORD_FROM_SIGNUP");
                fullNameFromSignUp = extras.getString("FULLNAME_FROM_SIGNUP");

            }

        } else {

            usernameFromSignUp = (String) savedInstanceState.getSerializable("USERNAME_FROM_SIGNUP");
            emailFromSignUp = (String) savedInstanceState.getSerializable("EMAIL_FROM_SIGNUP");
            passwordFromSignUp = (String) savedInstanceState.getSerializable("PASSWORD_FROM_SIGNUP");
            fullNameFromSignUp = (String) savedInstanceState.getSerializable("FULLNAME_FROM_SIGNUP");

        }

    }

    private void saveUserRole_CITIZENS() {

        // add username, fullname, email, and user role to Firebase realtime database
        CitizensInfo new_CI = new CitizensInfo(usernameFromSignUp, emailFromSignUp, passwordFromSignUp, fullNameFromSignUp);

        DatabaseReference myRef = database.getReference("CITIZENS/" + usernameFromSignUp);
        myRef.setValue(new_CI);

    }


    /*
    private void saveUserRole_GOVERNMENT() {

        // add username, fullname, and email to Firebase realtime database
        // set agency type equals to unknown for the first time
        GovernmentInfo new_GI = new GovernmentInfo(usernameFromSignUp, emailFromSignUp, passwordFromSignUp, fullNameFromSignUp, "unknown");

        DatabaseReference myRef = database.getReference("GOVERNMENT/" + usernameFromSignUp);
        myRef.setValue(new_GI);

    }
    */

}
