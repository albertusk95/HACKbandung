package com.sai.hackbandung;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sai.hackbandung.DatabaseClass.CitizensInfo;

import static android.content.ContentValues.TAG;

public class SignInActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;

    private TextView textViewSignIn;

    private Button buttonSignin;

    private String email;
    private String password;

    private String userRole;

    private int isUserAuthenticated = 0;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // initialize firebase realtime database
        database = FirebaseDatabase.getInstance();

        // initialize views
        editTextEmail = (EditText) findViewById(R.id.editTextSignInActivity_EMAIL);
        editTextPassword = (EditText) findViewById(R.id.editTextSignInActivity_PASSWORD);

        textViewSignIn = (TextView) findViewById(R.id.textViewSignInActivity_REGISTER_AN_ACCOUNT);

        buttonSignin = (Button) findViewById(R.id.buttonSignInActivity_SIGNIN);

        buttonSignin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();

                // check if email and passwords are empty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Email is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Password is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // user authentication
                authenticateUser();

                if (isUserAuthenticated == 1) {

                    // valid access
                    Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();

                    // redirect to the main page
                    if (userRole.equals("citizens")) {

                        SignInActivity.this.startActivity(new Intent(SignInActivity.this, NavigationDrawerCitizensActivity.class));

                    } else {

                        SignInActivity.this.startActivity(new Intent(SignInActivity.this, NavigationDrawerGovernmentActivity.class));

                    }

                } else {

                    // invalid access
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                    return;

                }

            }

        });

        // attach event listener to login text view
        textViewSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SignInActivity.this.startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }

        });

    }

    private void authenticateUser() {

        DatabaseReference myRef = database.getReference();
        myRef.child("CITIZENS").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int userAuthenticationStatus = 0;

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                    CitizensInfo ci = noteDataSnapshot.getValue(CitizensInfo.class);

                    if (ci.getEmail().equals(email) && ci.getPassword().equals(password)) {

                        // set the authentication status
                        userAuthenticationStatus = 1;

                        // set the user role
                        userRole = ci.getUserRole();

                        break;
                    }

                }

                isUserAuthenticated = userAuthenticationStatus;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled userAuthentication login", databaseError.toException());
            }

        });

        Log.d("AndroidBash", "authenticate user for login complete");


    }

}
