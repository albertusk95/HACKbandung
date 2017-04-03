package com.sai.hackbandung.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.sai.hackbandung.Location.GpsLocationTracker;
import com.sai.hackbandung.Location.TrackGPS;
import com.sai.hackbandung.R;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by AlbertusK95 on 4/1/2017.
 */

public class WriteANewReportCitizens extends Fragment {

    View v;
    WebView myWeb;

    Button buttonTakeAPhoto;
    Button buttonSend;

    EditText editTextTopic;
    EditText editTextLocation;
    EditText editTextReport;

    ImageView imageViewTakenPhoto;

    // user input
    String topic;
    String postingDate;
    String finishDate;
    String responsibleAgency;
    String address;
    String userRole;
    String username;
    String fullname;
    String status;
    String userMessage;

    Long imgREF;
    Long imgREF_AFTER_COMPLETED;

    // firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    private String usernameFromNAVDRAWCITIZENS;
    private String fullnameFromNAVDRAWCITIZENS;

    private static final int CAMERA_REQUEST = 1888;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        // add layout
        v = inflater.inflate(R.layout.write_a_new_report_citizens, container, false);

        // initialize the reference for storage and database
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);


        // initialize the views
        buttonTakeAPhoto = (Button) v.findViewById(R.id.buttonWriteANewReportActivity_CAMERA);
        buttonSend = (Button) v.findViewById(R.id.buttonWriteANewReportActivity_SEND);

        editTextTopic = (EditText) v.findViewById(R.id.editTextWriteANewReportActivity_TOPIC);
        editTextLocation = (EditText) v.findViewById(R.id.editTextWriteANewReportActivity_LOCATION);
        editTextReport = (EditText) v.findViewById(R.id.editTextWriteANewReportActivity_MESSAGE);

        imageViewTakenPhoto = (ImageView) v.findViewById(R.id.imageViewWriteANewReportActivity);

        // get argument that passed from activity in "USERNAME_FROM_NAVDRAWERCITIZENS" key value
        usernameFromNAVDRAWCITIZENS = getArguments().getString("USERNAME_FROM_NAVDRAWERCITIZENS");
        fullnameFromNAVDRAWCITIZENS = getArguments().getString("FULLNAME_FROM_NAVDRAWERCITIZENS");

        buttonTakeAPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vv) {

                // call take a photo method when user clicks this button
                takeImageFromCamera();

            }

        });

        buttonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vv) {

                // call send report method when user clicks this button
                uploadFileBytes();

            }

        });

        return v;

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Write A New Report");

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
            imageViewTakenPhoto.setImageBitmap(photo);

            Toast.makeText(getActivity().getApplicationContext(), "User data: " + data.getData(), Toast.LENGTH_LONG).show();

        }
    }

    // Method for getting current time and date
    // For show the current time, use System.out.println("Current time =&gt; "+c.getTime());
    private String getPostingDate() {

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        return formattedDate;

    }

    // Method for getting address name based on the latitude and longitude
    private String getAddressNameFromLATLONG(double LATITUDE, double LONGITUDE) {

        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {

                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }

                strAdd = strReturnedAddress.toString();

            } else {

                strAdd = "No Address returned";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strAdd;

    }

    // Method for getting the user location address
    private String getAddress() {

        /*
        TrackGPS gps;

        double longitude = 0;
        double latitude = 0;

        // GET THE LATITUDE AND LONGITUDE
        gps = new TrackGPS(getActivity());

        if(gps.canGetLocation()){

            longitude = gps.getLongitude();
            latitude = gps .getLatitude();

            Toast.makeText(getActivity().getApplicationContext(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude),Toast.LENGTH_SHORT).show();

        } else {

            gps.showSettingsAlert();

        }
        */

        GpsLocationTracker mGpsLocationTracker = new GpsLocationTracker(getActivity());

        /**
         * Set GPS Location fetched address
         */
        double latitude = 0;
        double longitude = 0;

        if (mGpsLocationTracker.canGetLocation())
        {
            latitude = mGpsLocationTracker.getLatitude();
            longitude = mGpsLocationTracker.getLongitude();

        }
        else
        {
            mGpsLocationTracker.showSettingsAlert();
        }

        Toast.makeText(getActivity().getApplicationContext(), "LAT LONG " + latitude + " : " + longitude, Toast.LENGTH_LONG).show();

        // GET THE LOCATION NAME BASED ON THE LATITUDE AND LONGITUDE
        String addressName = getAddressNameFromLATLONG(latitude, longitude);

        return addressName;

    }

    private String preprocessAddress(String preAddress) {

        // split with space as the delimiter
        String[] splitted_preAddres = preAddress.split("\\s+");

        String tmp_res = "";

        for (int idx = 0; idx < splitted_preAddres.length; idx++) {

            if (idx != splitted_preAddres.length - 1) {
                tmp_res = tmp_res + splitted_preAddres[idx] + "+";
            } else {
                tmp_res = tmp_res + splitted_preAddres[idx];
            }
        }

        return tmp_res;

    }

    private void uploadFileBytes() {

        // initialize user input
        topic = editTextTopic.getText().toString().trim();
        postingDate = getPostingDate();
        finishDate = "Not Assigned";
        responsibleAgency = "Not Assigned";
        address = preprocessAddress(editTextLocation.getText().toString().trim());
        userRole = "citizens";
        username = usernameFromNAVDRAWCITIZENS;
        fullname = fullnameFromNAVDRAWCITIZENS;
        status = "waiting";
        userMessage = editTextReport.getText().toString().trim();
        imgREF_AFTER_COMPLETED = null;


        // get the data from an ImageView as bytes
        imageViewTakenPhoto.setDrawingCacheEnabled(true);
        imageViewTakenPhoto.buildDrawingCache();

        Bitmap bitmap = imageViewTakenPhoto.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] data = baos.toByteArray();

        //displaying progress dialog while image is uploading
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading");
        progressDialog.show();


        imgREF = System.currentTimeMillis();

        final String REPORT_ID = String.valueOf(imgREF);

        StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + imgREF + ".jpg");

        UploadTask uploadTask = sRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                progressDialog.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                //dismissing the progress dialog
                progressDialog.dismiss();

                //displaying success toast
                Toast.makeText(getActivity().getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

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

                ReportInfo uploadReport = new ReportInfo(REPORT_ID, imgREF, imgREF_AFTER_COMPLETED, topic, postingDate, finishDate, responsibleAgency, address,
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




    /*
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    */

    /*
    private void uploadFile() {

        //displaying progress dialog while image is uploading
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        //getting the storage reference
        StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

        //adding the file to reference
        sRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //dismissing the progress dialog
                        progressDialog.dismiss();

                        //displaying success toast
                        Toast.makeText(getActivity().getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                        //creating the upload object to store uploaded image details
                        UploadInfo upload = new UploadInfo(editTextFileName.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString());

                        //adding an upload to firebase database
                        String uploadId = mDatabase.push().getKey();
                        mDatabase.child(uploadId).setValue(upload);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //displaying the upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                });

    }
    */

}
