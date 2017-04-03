package com.sai.hackbandung;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class InProgressGovernmentDetail extends AppCompatActivity {

    private String address;

    private ImageView imageViewMap;
    private TextView textViewLocation_VALUE;
    private Button buttonGetDirection;

    private int zoom = 14;
    private int mapWidth = 640;
    private int mapHeight = 640;
    private String maptype = "roadmap";
    public int scale = 1;

    private int selectedMethodForHowToGetThere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("In Progress Detail");
        setContentView(R.layout.activity_in_progress_government_detail);

        // retrieve intent data
        retrieveDataFromAdapterCompleted(savedInstanceState);

        // initialize views
        imageViewMap = (ImageView) findViewById(R.id.imageViewMap);
        textViewLocation_VALUE = (TextView) findViewById(R.id.textViewLocation_VALUE);
        buttonGetDirection = (Button) findViewById(R.id.buttonGetDirection);

        textViewLocation_VALUE.setText(address);

        /*
        String url_source = "https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=640x480&maptype=roadmap";
        url_source = url_source + "&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318";
        url_source = url_source + "&markers=color:red%7Clabel:C%7C40.718217,-73.998284";
        url_source = url_source + "&key=AIzaSyDVoExFkxRS_AmMmhZFzcMhMzfx9twd0C4";
        */

        String url_source = "https://maps.googleapis.com/maps/api/staticmap?";
        url_source = url_source + "&zoom=" + zoom + "&size=" + mapWidth + "x" + mapHeight + "&scale=" + scale;
        url_source = url_source + "&markers=color:green%7Clabel:S%7C" + address;
        url_source = url_source + "&maptype=" + maptype + "&key=AIzaSyDVoExFkxRS_AmMmhZFzcMhMzfx9twd0C4";


        // show The Image in a ImageView
        new DownloadImageTask(imageViewMap).execute(url_source);


        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioHowToGetThere);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // checkedId is the RadioButton selected
                selectedMethodForHowToGetThere = checkedId;

            }

        });


        // attach event listener to button
        buttonGetDirection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String mode;

                if (selectedMethodForHowToGetThere == 0) {
                    mode = "d";
                } else if (selectedMethodForHowToGetThere == 1) {
                    mode = "w";
                } else {
                    mode = "b";
                }

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + address + "&mode=" + mode);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }

        });

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


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}

