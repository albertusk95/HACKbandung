package com.sai.hackbandung;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class CompleteDetailActivity extends AppCompatActivity {

    String agency;
    Long imgREF;
    Long imgREF_AFTER_COMPLETED;
    String postingDate;
    String finishDate;

    TextView textView_HandledBy;
    ImageView imageView_imgREF;
    ImageView imageView_imgREF_AFTER_COMPLETED;
    TextView textViewCompleteDetailActivity_START_TIME_VALUE;
    TextView textViewCompleteDetailActivity_FINISH_TIME_VALUE;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Complete Detail Info");
        setContentView(R.layout.activity_complete_detail);

        // retrieve the username from signup
        retrieveDataFromAdapterCompleted(savedInstanceState);

        //Toast.makeText(this, "Intent CDA: " + agency + " : " + imgREF + " : " + imgREF_AFTER_COMPLETED, Toast.LENGTH_LONG).show();

        // set views
        textView_HandledBy = (TextView) findViewById(R.id.textViewCompleteDetailActivity_HANDLE_BY_VALUE);
        imageView_imgREF = (ImageView) findViewById(R.id.imageViewVerification_BEFORE);
        imageView_imgREF_AFTER_COMPLETED = (ImageView) findViewById(R.id.imageViewVerification_AFTER);
        textViewCompleteDetailActivity_START_TIME_VALUE = (TextView) findViewById(R.id.textViewCompleteDetailActivity_START_TIME_VALUE);
        textViewCompleteDetailActivity_FINISH_TIME_VALUE = (TextView) findViewById(R.id.textViewCompleteDetailActivity_FINISH_TIME_VALUE);


        // set value to the views
        setValueToViews();

    }

    private void setValueToViews() {

        // agency
        textView_HandledBy.setText(agency);

        final long ONE_MEGABYTE = 1024 * 1024;

        // image verification BEFORE
        storageReference = FirebaseStorage.getInstance().getReference().child(Constants.STORAGE_PATH_UPLOADS + imgREF + ".jpg");

        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.outWidth = 200;
                options.outHeight = 200;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                imageView_imgREF.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Toast.makeText(CompleteDetailActivity.this, "Error storing image to ImageView BEFORE", Toast.LENGTH_LONG).show();

            }
        });

        // image verification AFTER
        //String childRef = Constants.STORAGE_PATH_UPLOADS + imgREF + ".jpg/" + Constants.STORAGE_PATH_UPLOADS + imgREF_AFTER_COMPLETED + ".jpg";
        storageReference = FirebaseStorage.getInstance().getReference().child(Constants.STORAGE_PATH_UPLOADS + imgREF_AFTER_COMPLETED + ".jpg");

        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.outWidth = 200;
                options.outHeight = 200;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                imageView_imgREF_AFTER_COMPLETED.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Toast.makeText(CompleteDetailActivity.this, "Error storing image to ImageView AFTER", Toast.LENGTH_LONG).show();

            }
        });

        // start date
        textViewCompleteDetailActivity_START_TIME_VALUE.setText(postingDate);

        // finish date
        textViewCompleteDetailActivity_FINISH_TIME_VALUE.setText(finishDate);

    }

    private void retrieveDataFromAdapterCompleted(Bundle savedInstanceState) {

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();

            if(extras == null) {

                agency = null;
                imgREF = null;
                imgREF_AFTER_COMPLETED = null;
                postingDate = null;
                finishDate = null;

            } else {

                agency = extras.getString("COMPLETED_AGENCY");
                imgREF = extras.getLong("COMPLETED_IMAGE_BEFORE");
                imgREF_AFTER_COMPLETED = extras.getLong("COMPLETED_IMAGE_AFTER");
                postingDate = extras.getString("COMPLETED_POSTING_DATE");
                finishDate = extras.getString("COMPLETED_FINISH_DATE");

            }

        } else {

            agency = (String) savedInstanceState.getSerializable("COMPLETED_AGENCY");
            imgREF = (Long) savedInstanceState.getSerializable("COMPLETED_IMAGE_BEFORE");
            imgREF_AFTER_COMPLETED = (Long) savedInstanceState.getSerializable("COMPLETE_IMAGE_AFTER");
            postingDate = (String) savedInstanceState.getSerializable("COMPLETED_POSTING_DATE");
            finishDate = (String) savedInstanceState.getSerializable("COMPLETED_FINISH_DATE");

        }

    }

}
