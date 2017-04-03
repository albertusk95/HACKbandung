package com.sai.hackbandung;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InProgressGovernmentDetail extends AppCompatActivity {

    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("In Progress Detail");
        setContentView(R.layout.activity_in_progress_government_detail);

        // retrieve intent data
        retrieveDataFromAdapterCompleted(savedInstanceState);

        // initialize views


        // set values to the views


    }

    private void retrieveDataFromAdapterCompleted(Bundle savedInstanceState) {

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();

            if(extras == null) {

                address = null;

            } else {

                address = extras.getString("GOVERNMENT_WIP_ADDRESS");

            }

        } else {

            address = (String) savedInstanceState.getSerializable("GOVERNMENT_WIP_ADDRESS");

        }

    }

}
