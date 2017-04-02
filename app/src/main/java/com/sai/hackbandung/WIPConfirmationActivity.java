package com.sai.hackbandung;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sai.hackbandung.Constants.Constants;
import com.sai.hackbandung.DatabaseClass.ReportInfo;

public class WIPConfirmationActivity extends AppCompatActivity {

    private ImageView imageViewCitizensProfpic;
    private TextView textViewCitizensFullname;
    private TextView textViewCitizensPostingDate;
    private TextView textViewCitizensTopic;
    private TextView textViewCitizensMessage;
    private ImageView imageViewCitizensVerification;
    private TextView buttonConfirm;

    private String REPORT_ID;
    private Long imgREF;
    private Long imgREF_AFTER_COMPLETED;
    private String topic;
    private String postingDate;
    private String responsibleAgency;
    private String address;
    private String userRole;
    private String username;
    private String fullname;
    private String status;
    private String userMessage;

    // Reference to an image file in Firebase Storage
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("W.I.P Confirmation");
        setContentView(R.layout.activity_wipconfirmation);

        imageViewCitizensProfpic = (ImageView) findViewById(R.id.imageViewCitizensProfpic);
        textViewCitizensFullname = (TextView) findViewById(R.id.textViewCitizensFullname);
        textViewCitizensPostingDate = (TextView) findViewById(R.id.textViewCitizensPostingDate);
        textViewCitizensTopic = (TextView) findViewById(R.id.textViewCitizensTopic);
        textViewCitizensMessage = (TextView) findViewById(R.id.textViewCitizensMessage);
        imageViewCitizensVerification = (ImageView) findViewById(R.id.imageViewCitizensVerification);
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);

        // retrieve intent data
        retrieveIntentData(savedInstanceState);


        Toast.makeText(this, "data imgREF: " + imgREF, Toast.LENGTH_LONG).show();


        // set views with the intent data
        setViewWithIntentData();

        // HANDLER for button W.I.P
        buttonConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // store in the database
                storeIntoDatabase();

            }

        });

    }

    private void storeIntoDatabase() {

        String newStatus = "wip";

        ReportInfo new_RI = new ReportInfo(REPORT_ID, imgREF, imgREF_AFTER_COMPLETED, topic, postingDate,
                                            responsibleAgency, address, userRole, username, fullname, newStatus, userMessage);

        //mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
        //mDatabase.child(REPORT_ID).setValue(new_RI);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(Constants.DATABASE_PATH_UPLOADS + "/" + REPORT_ID).setValue(new_RI);

        /*
        Toast.makeText(WIPConfirmationActivity.this, "Confirmed", Toast.LENGTH_LONG).show();

        mDatabase.child(REPORT_ID).setValue(new_RI, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    //System.out.println("Data could not be saved " + databaseError.getMessage());
                    Toast.makeText(WIPConfirmationActivity.this, "Data could not be saved " + databaseError.getMessage(), Toast.LENGTH_LONG).show();

                } else {
                    //System.out.println("Data saved successfully.");
                    Toast.makeText(WIPConfirmationActivity.this, "Data saved successfully.", Toast.LENGTH_LONG).show();
                }
            }
        });
        */

    }

    private void setViewWithIntentData() {

        // image verification
        storageReference = FirebaseStorage.getInstance().getReference().child(Constants.STORAGE_PATH_UPLOADS + imgREF + ".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Toast.makeText(WIPConfirmationActivity.this, "Success image verify", Toast.LENGTH_LONG).show();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.outWidth = 100;
                options.outHeight = 100;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                imageViewCitizensVerification.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Toast.makeText(WIPConfirmationActivity.this, "Error", Toast.LENGTH_LONG).show();

            }
        });

        //imageViewCitizensProfpic = (ImageView) findViewById(R.id.imageViewCitizensProfpic);

        textViewCitizensFullname.setText(fullname);
        textViewCitizensPostingDate.setText(postingDate);
        textViewCitizensTopic.setText(topic);
        textViewCitizensMessage.setText(userMessage);

    }

    private void retrieveIntentData(Bundle savedInstanceState) {

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();

            if(extras == null) {

                REPORT_ID = null;
                imgREF = null;
                imgREF_AFTER_COMPLETED = null;
                topic = null;
                postingDate = null;
                responsibleAgency = null;
                address = null;
                userRole = null;
                username = null;
                fullname = null;
                status = null;
                userMessage = null;

            } else {

                REPORT_ID = extras.getString("CONFIRM_WIP_REPORT_ID");
                imgREF = extras.getLong("CONFIRM_WIP_IMG_REF");
                imgREF_AFTER_COMPLETED = extras.getLong("CONFIRM_WIP_IMG_REF_AFTER_COMPLETED");
                topic = extras.getString("CONFIRM_WIP_TOPIC");
                postingDate = extras.getString("CONFIRM_WIP_POSTING_DATE");
                responsibleAgency = extras.getString("CONFIRM_WIP_RESPONSIBLE_AGENCY");
                address = extras.getString("CONFIRM_WIP_ADDRESS");
                userRole = extras.getString("CONFIRM_WIP_USER_ROLE");
                username = extras.getString("CONFIRM_WIP_USERNAME");
                fullname = extras.getString("CONFIRM_WIP_FULLNAME");
                status = extras.getString("CONFIRM_WIP_STATUS");
                userMessage = extras.getString("CONFIRM_WIP_USER_MESSAGE");

            }

        } else {

            REPORT_ID = (String) savedInstanceState.getSerializable("CONFIRM_WIP_REPORT_ID");
            imgREF = (Long) savedInstanceState.getSerializable("CONFIRM_WIP_IMG_REF");
            imgREF_AFTER_COMPLETED = (Long) savedInstanceState.getSerializable("CONFIRM_WIP_IMG_REF_AFTER_COMPLETED");
            topic = (String) savedInstanceState.getSerializable("CONFIRM_WIP_TOPIC");
            postingDate = (String) savedInstanceState.getSerializable("CONFIRM_WIP_POSTING_DATE");
            responsibleAgency = (String) savedInstanceState.getSerializable("CONFIRM_WIP_RESPONSIBLE_AGENCY");
            address = (String) savedInstanceState.getSerializable("CONFIRM_WIP_ADDRESS");
            userRole = (String) savedInstanceState.getSerializable("CONFIRM_WIP_USER_ROLE");
            username = (String) savedInstanceState.getSerializable("CONFIRM_WIP_USERNAME");
            fullname = (String) savedInstanceState.getSerializable("CONFIRM_WIP_FULLNAME");
            status = (String) savedInstanceState.getSerializable("CONFIRM_WIP_STATUS");
            userMessage = (String) savedInstanceState.getSerializable("CONFIRM_WIP_USER_MESSAGE");

        }

    }

}
