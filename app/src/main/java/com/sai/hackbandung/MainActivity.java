package com.sai.hackbandung;

import android.content.Intent;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button buttonGoToSignUp;
    private TextView textViewGoToSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize views
        buttonGoToSignUp = (Button) findViewById(R.id.buttonMainActivity_SIGNUP);
        textViewGoToSignIn = (TextView) findViewById(R.id.textViewMainActivity_HAVE_AN_ACCOUNT);

        // attach event listener to button
        buttonGoToSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // redirect to the sign up page
                MainActivity.this.startActivity(new Intent(MainActivity.this, SignUpActivity.class));

            }

        });

        // attach event listener to button
        textViewGoToSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // redirect to the login page
                MainActivity.this.startActivity(new Intent(MainActivity.this, SignInActivity.class));

            }

        });

        // set properties
        //buttonGoToSignUp.setBackgroundColor(0xff852b);

    }
}
