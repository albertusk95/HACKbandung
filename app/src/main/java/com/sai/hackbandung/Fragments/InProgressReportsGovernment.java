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
import android.webkit.WebView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sai.hackbandung.Adapter.MyAdapter;
import com.sai.hackbandung.Adapter.MyAdapterGovernment_WIP;
import com.sai.hackbandung.Constants.Constants;
import com.sai.hackbandung.DatabaseClass.ReportInfo;
import com.sai.hackbandung.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlbertusK95 on 4/1/2017.
 */

public class InProgressReportsGovernment extends Fragment {

    View v;
    WebView myWeb;

    //recyclerview object
    private RecyclerView recyclerView;

    //adapter object
    private RecyclerView.Adapter adapter;

    //database reference
    private DatabaseReference mDatabase;

    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<ReportInfo> reportInfos;

    private String usernameFromNAVDRAWERGOVERNMENT;
    private String fullnameFromNAVDRAWERGOVERNMENT;
    private String responsibleAgencyFromNAVDRAWERGOVERNMENT;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        // menambahkan layout
        v = inflater.inflate(R.layout.in_progress_reports_government, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewWIP_GOVERNMENT);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressDialog = new ProgressDialog(getActivity());

        reportInfos = new ArrayList<>();

        // get argument that passed from activity in "USERNAME_FROM_NAVDRAWERCITIZENS" key value
        usernameFromNAVDRAWERGOVERNMENT = getArguments().getString("USERNAME_FROM_NAVDRAWERGOVERNMENT");
        fullnameFromNAVDRAWERGOVERNMENT = getArguments().getString("FULLNAME_FROM_NAVDRAWERGOVERNMENT");
        responsibleAgencyFromNAVDRAWERGOVERNMENT = getArguments().getString("RESAGENCY_FROM_NAVDRAWERGOVERNMENT");

        return v;

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("In Progress Reports");

        // retrieve report info from database
        retrieveRelevantReportInfo();
    }

    private void retrieveRelevantReportInfo() {

        //displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        //adding an event listener to fetch values
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                progressDialog.dismiss();

                reportInfos = new ArrayList<>();

                // get all data having the corresponding username and status WIP
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ReportInfo RI = postSnapshot.getValue(ReportInfo.class);

                    if (RI.responsibleAgency.equals(responsibleAgencyFromNAVDRAWERGOVERNMENT) && RI.status.equals("wip")) {
                        reportInfos.add(RI);
                    }
                }

                //creating adapter
                adapter = new MyAdapterGovernment_WIP(getActivity().getApplicationContext(), reportInfos);

                //adding adapter to recyclerview
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

    }

}
