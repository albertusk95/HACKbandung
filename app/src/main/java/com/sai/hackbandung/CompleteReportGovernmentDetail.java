package com.sai.hackbandung;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sai.hackbandung.Constants.Constants;

public class CompleteReportGovernmentDetail extends AppCompatActivity {

    private Long imgREFFromAdapterGovCom;
    private Long imgREFF_AFTER_COMPLETEDFromAdapterGovCom;
    private String startTimeFromAdapterGovCom;
    private String finishTimeFromAdapterGovCom;

    private ImageView imageViewVerification_BEFORE;
    private ImageView imageViewVerification_AFTER;

    private TextView textViewCompletionTime_START_TIME_VALUE;
    private TextView textViewCompletionTime_FINISH_TIME_VALUE;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_report_government_detail);

        storageReference = FirebaseStorage.getInstance().getReference();

        // retrieve intent data
        retrieveIntentData(savedInstanceState);

        imageViewVerification_BEFORE = (ImageView) findViewById(R.id.imageViewVerification_BEFORE);
        imageViewVerification_AFTER = (ImageView) findViewById(R.id.imageViewVerification_AFTER);

        textViewCompletionTime_START_TIME_VALUE = (TextView) findViewById(R.id.textViewCompletionTime_START_TIME_VALUE);
        textViewCompletionTime_FINISH_TIME_VALUE = (TextView) findViewById(R.id.textViewCompletionTime_FINISH_TIME_VALUE);

        // set value to views
        setValueToViews();

    }

    private void setValueToViews() {

        textViewCompletionTime_START_TIME_VALUE.setText(startTimeFromAdapterGovCom);
        textViewCompletionTime_FINISH_TIME_VALUE.setText(finishTimeFromAdapterGovCom);

        final long ONE_MEGABYTE = 1024 * 1024;

        // image verification before
        storageReference = FirebaseStorage.getInstance().getReference().child(Constants.STORAGE_PATH_UPLOADS + imgREFFromAdapterGovCom + ".jpg");

        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                //Toast.makeText(WIPConfirmationActivity.this, "Success image verify", Toast.LENGTH_LONG).show();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.outWidth = 200;
                options.outHeight = 200;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                imageViewVerification_BEFORE.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Toast.makeText(CompleteReportGovernmentDetail.this, "Error", Toast.LENGTH_LONG).show();

            }
        });

        // image verification after
        //String childRef = Constants.STORAGE_PATH_UPLOADS + imgREFFromAdapterGovCom + ".jpg/" + Constants.STORAGE_PATH_UPLOADS + imgREFF_AFTER_COMPLETEDFromAdapterGovCom + ".jpg";
        storageReference = FirebaseStorage.getInstance().getReference().child(Constants.STORAGE_PATH_UPLOADS + imgREFF_AFTER_COMPLETEDFromAdapterGovCom + ".jpg");

        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                //Toast.makeText(WIPConfirmationActivity.this, "Success image verify", Toast.LENGTH_LONG).show();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.outWidth = 200;
                options.outHeight = 200;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                imageViewVerification_AFTER.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Toast.makeText(CompleteReportGovernmentDetail.this, "Error", Toast.LENGTH_LONG).show();

            }
        });


    }

    private void retrieveIntentData(Bundle savedInstanceState) {

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();

            if(extras == null) {

                imgREFFromAdapterGovCom = null;
                imgREFF_AFTER_COMPLETEDFromAdapterGovCom = null;
                startTimeFromAdapterGovCom = null;
                finishTimeFromAdapterGovCom = null;

            } else {

                imgREFFromAdapterGovCom = extras.getLong("COMPLETED_GOV_IMGREF");
                imgREFF_AFTER_COMPLETEDFromAdapterGovCom = extras.getLong("COMPLETED_GOV_IMGREF_AFTER_COMPLETED");
                startTimeFromAdapterGovCom = extras.getString("COMPLETED_GOV_START_TIME");
                finishTimeFromAdapterGovCom = extras.getString("COMPLETED_GOV_FINISH_TIME");

            }

        } else {

            imgREFFromAdapterGovCom = (Long) savedInstanceState.getSerializable("COMPLETED_GOV_IMGREF");
            imgREFF_AFTER_COMPLETEDFromAdapterGovCom = (Long) savedInstanceState.getSerializable("COMPLETED_GOV_IMGREF_AFTER_COMPLETED");
            startTimeFromAdapterGovCom = (String) savedInstanceState.getSerializable("COMPLETED_GOV_START_TIME");
            finishTimeFromAdapterGovCom = (String) savedInstanceState.getSerializable("COMPLETED_GOV_FINISH_TIME");

        }

    }


}
