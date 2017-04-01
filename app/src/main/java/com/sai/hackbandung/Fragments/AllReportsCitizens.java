package com.sai.hackbandung.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.sai.hackbandung.R;

/**
 * Created by AlbertusK95 on 4/1/2017.
 */

public class AllReportsCitizens extends Fragment {

    View v;
    WebView myWeb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        // menambahkan layout
        v = inflater.inflate(R.layout.all_reports_citizens, container, false);

        return v;

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("All Reports");
    }

}
