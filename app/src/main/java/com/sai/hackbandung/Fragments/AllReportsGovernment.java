package com.sai.hackbandung.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sai.hackbandung.Adapter.MyAdapterGovernment;
import com.sai.hackbandung.Constants.Constants;
import com.sai.hackbandung.DatabaseClass.ReportInfo;
import com.sai.hackbandung.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlbertusK95 on 4/1/2017.
 */

public class AllReportsGovernment extends Fragment {

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

    private EditText editTextSearch;

    private String usernameFromNAVDRAWGOVERNMENT;
    private String fullnameFromNAVDRAWGOVERNMENT;
    private String resAgencyFromNAVDRAWGOVERNMENT;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        // menambahkan layout
        v = inflater.inflate(R.layout.all_reports_government, container, false);

        // search text
        editTextSearch = (EditText) v.findViewById(R.id.search);

        // get argument that passed from activity in "USERNAME_FROM_NAVDRAWERCITIZENS" key value
        usernameFromNAVDRAWGOVERNMENT = getArguments().getString("USERNAME_FROM_NAVDRAWERGOVERNMENT");
        fullnameFromNAVDRAWGOVERNMENT = getArguments().getString("FULLNAME_FROM_NAVDRAWERGOVERNMENT");
        resAgencyFromNAVDRAWGOVERNMENT = getArguments().getString("RESAGENCY_FROM_NAVDRAWERGOVERNMENT");

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressDialog = new ProgressDialog(getActivity());
        reportInfos = new ArrayList<>();

        return v;

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("All Reports");

        // retrieve report info from database
        retrieveRelevantReportInfo();

        addTextListener();

    }


    public void addTextListener(){

        editTextSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<ReportInfo> filteredList = new ArrayList<>();

                for (int i = 0; i < reportInfos.size(); i++) {

                    final String text = reportInfos.get(i).userMessage.toLowerCase();

                    if (text.contains(query)) {

                        filteredList.add(reportInfos.get(i));

                    }

                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new MyAdapterGovernment(getActivity().getApplicationContext(), filteredList);

                ((MyAdapterGovernment)adapter).setAgency(resAgencyFromNAVDRAWGOVERNMENT);

                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();  // data set changed
            }
        });
    }


    private void retrieveRelevantReportInfo() {

        //displaying progress dialog while fetching images
        progressDialog.setMessage("Please wait...");
        //progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        //adding an event listener to fetch values
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                progressDialog.dismiss();

                reportInfos = new ArrayList<>();

                // get all data having the corresponding username
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ReportInfo RI = postSnapshot.getValue(ReportInfo.class);

                    reportInfos.add(RI);
                }

                //creating adapter
                adapter = new MyAdapterGovernment(getActivity().getApplicationContext(), reportInfos);

                ((MyAdapterGovernment)adapter).setAgency(resAgencyFromNAVDRAWGOVERNMENT);

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
