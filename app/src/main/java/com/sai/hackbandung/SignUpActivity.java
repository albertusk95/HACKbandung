package com.sai.hackbandung;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import static android.content.ContentValues.TAG;

import com.sai.hackbandung.DatabaseClass.CitizensInfo;
import com.sai.hackbandung.DatabaseClass.GovernmentInfo;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextFullName;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    private Button buttonSignup;
    private TextView textViewLogin;
    private ProgressDialog progressDialog;

    private FirebaseDatabase database;

    private String username;
    private String email;
    private String full_name;
    private String password;
    private String confirmPassword;

    private int isUsernameExist;
    private int isEmailExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.SignUpActivity_TITLE);
        setContentView(R.layout.activity_sign_up);

        // initialize username and email existence status
        isUsernameExist = 0;
        isEmailExist = 0;

        // initialize firebase realtime database
        database = FirebaseDatabase.getInstance();

        // initialize views
        editTextFullName = (EditText) findViewById(R.id.editTextSignUpActivity_FULL_NAME);
        editTextUsername = (EditText) findViewById(R.id.editTextSignUpActivity_USERNAME);
        editTextEmail = (EditText) findViewById(R.id.editTextSignUpActivity_EMAIL);
        editTextPassword = (EditText) findViewById(R.id.editTextSignUpActivity_PASSWORD);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextSignUpActivity_CONFIRM_PASSWORD);

        buttonSignup = (Button) findViewById(R.id.buttonSignUpActivity_SIGNUP);

        textViewLogin =  (TextView) findViewById(R.id.textViewSignUpActivity_HAVE_AN_ACCOUNT);

        progressDialog = new ProgressDialog(this);

        // attach event listener to button
        buttonSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // call register method when user clicks sign up button
                registerUser();

            }

        });

        // attach event listener to login text view
        textViewLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SignUpActivity.this.startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }

        });

    }

    private void registerUser(){

        // get email and password from edit texts
        username = editTextUsername.getText().toString().trim();
        full_name = editTextFullName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password  = editTextPassword.getText().toString().trim();
        confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // check if email and passwords are empty
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this,"Email is required",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Password is required",Toast.LENGTH_LONG).show();
            return;
        }

        // check whether the password and its confirmation are same
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this,"Confirmation password is not valid", Toast.LENGTH_LONG).show();
            return;
        }


        // display a progress dialog if the email and pass are not empty
        progressDialog.setMessage("Registering your account...");
        progressDialog.show();


        // check whether the username is already exist
        validateUsernameAndEmailExistence();

    }

    private void validateUsernameAndEmailExistence() {

        DatabaseReference myRef = database.getReference();

        // CHECK FOR CITIZENS
        myRef.child("CITIZENS").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int usernameExistenceStatus = 0;
                int emailExistenceStatus = 0;

                Toast.makeText(SignUpActivity.this, "On Data Change Citizens", Toast.LENGTH_LONG).show();

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                    CitizensInfo ci = noteDataSnapshot.getValue(CitizensInfo.class);

                    if (ci.username.equals(username)) {
                        usernameExistenceStatus = 1;
                    }

                    if (ci.email.equals(email)) {
                        emailExistenceStatus = 1;
                    }

                    if (usernameExistenceStatus == 1 || emailExistenceStatus == 1) {
                        break;
                    }

                }

                isUsernameExist = usernameExistenceStatus;
                isEmailExist = emailExistenceStatus;


                if (isUsernameExist == 0 && isEmailExist == 0) {


                } else {

                    if (isUsernameExist == 1) {
                        // username is already exist
                        Toast.makeText(SignUpActivity.this, "Username citizens already exists: " + username, Toast.LENGTH_LONG).show();
                    }

                    if (isEmailExist == 1) {
                        // email is already exist
                        Toast.makeText(SignUpActivity.this, "Email citizens already exists: " + email, Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled validateUsernameAndEmailExistence Citizens", databaseError.toException());
            }

        });

        // CHECK FOR GOVERNMENT
        if (isUsernameExist == 0 && isEmailExist == 0) {

            myRef.child("GOVERNMENT").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int usernameExistenceStatus = 0;
                    int emailExistenceStatus = 0;

                    Toast.makeText(SignUpActivity.this, "On Data Change Government", Toast.LENGTH_LONG).show();

                    for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

                        GovernmentInfo gi = noteDataSnapshot.getValue(GovernmentInfo.class);

                        if (gi.username.equals(username)) {
                            usernameExistenceStatus = 1;
                        }

                        if (gi.email.equals(email)) {
                            emailExistenceStatus = 1;
                        }

                        if (usernameExistenceStatus == 1 || emailExistenceStatus == 1) {
                            break;
                        }

                    }

                    isUsernameExist = usernameExistenceStatus;
                    isEmailExist = emailExistenceStatus;


                    if (isUsernameExist == 0 && isEmailExist == 0) {

                        // add username, fullname, and email to Firebase realtime database
                        // set the user role to "unknown" for the first time
                        //UserInfo new_ui = new UserInfo(username, email, password, full_name, "unknown");

                        //DatabaseReference myRef = database.getReference("CITIZENS/" + username);
                        //myRef.setValue(new_ui);

                        //Toast.makeText(SignUpActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();

                        // redirect to main page
                        Intent intentForUserRole = new Intent(SignUpActivity.this, UserRoleActivity.class);

                        intentForUserRole.putExtra("USERNAME_FROM_SIGNUP", username);
                        intentForUserRole.putExtra("EMAIL_FROM_SIGNUP", email);
                        intentForUserRole.putExtra("PASSWORD_FROM_SIGNUP", password);
                        intentForUserRole.putExtra("FULLNAME_FROM_SIGNUP", full_name);

                        Toast.makeText(SignUpActivity.this, "Regstration success", Toast.LENGTH_LONG).show();

                        SignUpActivity.this.startActivity(intentForUserRole);

                    } else {

                        if (isUsernameExist == 1) {
                            // username is already exist
                            Toast.makeText(SignUpActivity.this, "Username government already exists", Toast.LENGTH_LONG).show();
                        }

                        if (isEmailExist == 1) {
                            // email is already exist
                            Toast.makeText(SignUpActivity.this, "Email government already exists", Toast.LENGTH_LONG).show();
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled validateUsernameAndEmailExistence Government", databaseError.toException());
                }

            });

        } else {

            Toast.makeText(SignUpActivity.this, "Username or Email already exist", Toast.LENGTH_LONG).show();

        }

        Log.d("AndroidBash", "validate username and email for registration complete");

    }

}
