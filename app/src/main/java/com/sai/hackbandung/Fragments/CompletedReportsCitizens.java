package com.sai.hackbandung.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sai.hackbandung.Adapter.MyAdapter;
import com.sai.hackbandung.Adapter.MyAdapterCompleted;
import com.sai.hackbandung.Constants.Constants;
import com.sai.hackbandung.DatabaseClass.ReportInfo;
import com.sai.hackbandung.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlbertusK95 on 4/1/2017.
 */

public class CompletedReportsCitizens extends Fragment {

    View v;
    WebView myWeb;

    //recyclerview object
    private RecyclerView recyclerView;

    //adapter object
    private RecyclerView.Adapter adapterCompleted;

    //database reference
    private DatabaseReference mDatabase;

    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<ReportInfo> reportInfos;

    private String usernameFromNAVDRAWCITIZENS;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        // menambahkan layout
        v = inflater.inflate(R.layout.completed_reports_citizens, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewCompleted);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressDialog = new ProgressDialog(getActivity());

        // get argument that passed from activity in "USERNAME_FROM_NAVDRAWERCITIZENS" key value
        usernameFromNAVDRAWCITIZENS = getArguments().getString("USERNAME_FROM_NAVDRAWERCITIZENS");

        return v;

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Completed Reports");

        // retrieve report info from database
        retrieveRelevantReportInfo();
    }

    private void retrieveRelevantReportInfo() {

        //displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        //mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //adding an event listener to fetch values
        mDatabase.child(Constants.DATABASE_PATH_UPLOADS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                progressDialog.dismiss();

                reportInfos = new ArrayList<>();

                // get all data having the corresponding username and status COMPLETED
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ReportInfo RI = postSnapshot.getValue(ReportInfo.class);

                    if (RI.username.equals(usernameFromNAVDRAWCITIZENS) && RI.status.equals("completed")) {
                        reportInfos.add(RI);
                    }
                }

                //creating adapter
                adapterCompleted = new MyAdapterCompleted(getActivity().getApplicationContext(), reportInfos);

                //adding adapter to recyclerview
                recyclerView.setAdapter(adapterCompleted);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }

}
