package com.sai.hackbandung;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaCodecInfo;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sai.hackbandung.Constants.Constants;
import com.sai.hackbandung.DatabaseClass.ReportInfo;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CompletionReportByAgencyActivity extends AppCompatActivity {

    Button buttonTakePhoto;
    Button buttonSubmit;
    ImageView imageViewVerification_BEFORE;
    ImageView imageViewVerification_AFTER;
    TextView textViewCompletionTime_START_TIME_VALUE;
    TextView textViewCompletionTime_FINISH_TIME_VALUE;


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

    private String finishTime;

    // Reference to an image file in Firebase Storage
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Completion Report");
        setContentView(R.layout.activity_completion_report_by_agency);

        //storageReference = FirebaseStorage.getInstance().getReference(Constants.STORAGE_PATH_UPLOADS);
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        // retrieve intent data
        retrieveIntentData(savedInstanceState);

        // intialize views
        buttonTakePhoto = (Button) findViewById(R.id.buttonTakePhoto);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        imageViewVerification_BEFORE = (ImageView) findViewById(R.id.imageViewVerification_BEFORE);
        imageViewVerification_AFTER = (ImageView) findViewById(R.id.imageViewVerification_AFTER);
        textViewCompletionTime_START_TIME_VALUE = (TextView) findViewById(R.id.textViewCompletionTime_START_TIME_VALUE);
        textViewCompletionTime_FINISH_TIME_VALUE = (TextView) findViewById(R.id.textViewCompletionTime_FINISH_TIME_VALUE);


        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vv) {

                // call take a photo method when user clicks this button
                takeImageFromCamera();

            }

        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vv) {

                // call send report method when user clicks this button
                uploadFileBytes();

            }

        });

        // set value to the views
        setValueToViews();

        //set properties
        //buttonTakePhoto.setBackgroundColor(0xff852b);
        //buttonSubmit.setBackgroundColor(0xff852b);

    }

    public void takeImageFromCamera() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageViewVerification_AFTER.setImageBitmap(photo);

        }
    }

    private void setValueToViews() {

        final long ONE_MEGABYTE = 1024 * 1024;

        // image verification before
        storageReference = FirebaseStorage.getInstance().getReference().child(Constants.STORAGE_PATH_UPLOADS + imgREF + ".jpg");

        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                //Toast.makeText(WIPConfirmationActivity.this, "Success image verify", Toast.LENGTH_LONG).show();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.outWidth = 100;
                options.outHeight = 100;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                imageViewVerification_BEFORE.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Toast.makeText(CompletionReportByAgencyActivity.this, "Error", Toast.LENGTH_LONG).show();

            }
        });

        // start date
        textViewCompletionTime_START_TIME_VALUE.setText(postingDate);

        // finish date (get current date and time)
        finishTime = getFinishDate();
        textViewCompletionTime_FINISH_TIME_VALUE.setText(finishTime);

    }

    private String getFinishDate() {

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        return formattedDate;

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
                finishTime = null;
                responsibleAgency = null;
                address = null;
                userRole = null;
                username = null;
                fullname = null;
                status = null;
                userMessage = null;

            } else {

                REPORT_ID = extras.getString("COMPLETION_DONE_REPORT_ID");
                imgREF = extras.getLong("COMPLETION_DONE_IMG_REF");
                imgREF_AFTER_COMPLETED = extras.getLong("COMPLETION_DONE_IMG_REF_AFTER_COMPLETED");
                topic = extras.getString("COMPLETION_DONE_TOPIC");
                postingDate = extras.getString("COMPLETION_DONE_POSTING_DATE");
                finishTime = extras.getString("COMPLETION_DONE_FINISH_DATE");
                responsibleAgency = extras.getString("COMPLETION_DONE_RESPONSIBLE_AGENCY");
                address = extras.getString("COMPLETION_DONE_ADDRESS");
                userRole = extras.getString("COMPLETION_DONE_USER_ROLE");
                username = extras.getString("COMPLETION_DONE_USERNAME");
                fullname = extras.getString("COMPLETION_DONE_FULLNAME");
                status = extras.getString("COMPLETION_DONE_STATUS");
                userMessage = extras.getString("COMPLETION_DONE_USER_MESSAGE");

            }

        } else {

            REPORT_ID = (String) savedInstanceState.getSerializable("COMPLETION_DONE_REPORT_ID");
            imgREF = (Long) savedInstanceState.getSerializable("COMPLETION_DONE_IMG_REF");
            imgREF_AFTER_COMPLETED = (Long) savedInstanceState.getSerializable("COMPLETION_DONE_IMG_REF_AFTER_COMPLETED");
            topic = (String) savedInstanceState.getSerializable("COMPLETION_DONE_TOPIC");
            postingDate = (String) savedInstanceState.getSerializable("COMPLETION_DONE_POSTING_DATE");
            finishTime = (String) savedInstanceState.getSerializable("COMPLETION_DONE_FINISH_DATE");
            responsibleAgency = (String) savedInstanceState.getSerializable("COMPLETION_DONE_RESPONSIBLE_AGENCY");
            address = (String) savedInstanceState.getSerializable("COMPLETION_DONE_ADDRESS");
            userRole = (String) savedInstanceState.getSerializable("COMPLETION_DONE_USER_ROLE");
            username = (String) savedInstanceState.getSerializable("COMPLETION_DONE_USERNAME");
            fullname = (String) savedInstanceState.getSerializable("COMPLETION_DONE_FULLNAME");
            status = (String) savedInstanceState.getSerializable("COMPLETION_DONE_STATUS");
            userMessage = (String) savedInstanceState.getSerializable("COMPLETION_DONE_USER_MESSAGE");

        }

    }


    private void uploadFileBytes() {

        // get the data from an ImageView as bytes
        imageViewVerification_AFTER.setDrawingCacheEnabled(true);
        imageViewVerification_AFTER.buildDrawingCache();

        Bitmap bitmap = imageViewVerification_AFTER.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] data = baos.toByteArray();

        //displaying progress dialog while image is uploading
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();


        imgREF_AFTER_COMPLETED = System.currentTimeMillis();

        // upload / imgREF / upload / imgREF_AFTER
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + imgREF_AFTER_COMPLETED + ".jpg");

        UploadTask uploadTask = sRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                //dismissing the progress dialog
                progressDialog.dismiss();

                //displaying success toast
                Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                // Data:
                // - imgREF
                // - imgREF_AFTER_COMPLETED
                // - topic
                // - posting date
                // - responsible agency
                // - address
                // - user role
                // - username
                // - status (all, WIP, done)
                // - message

                // FINISH DATE !!!

                ReportInfo uploadReport = new ReportInfo(REPORT_ID, imgREF, imgREF_AFTER_COMPLETED, topic, postingDate, finishTime, responsibleAgency, address,
                        userRole, username, fullname, status, userMessage);


                // adding an upload to firebase database
                //String uploadId = mDatabase.push().getKey();
                //mDatabase.child(uploadId).setValue(uploadReport);

                mDatabase.child(REPORT_ID).setValue(uploadReport);

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //displaying the upload progress
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
            }
        });

    }

}
