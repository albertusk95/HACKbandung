package com.sai.hackbandung.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sai.hackbandung.CompletionReportByAgencyActivity;
import com.sai.hackbandung.Constants.Constants;
import com.sai.hackbandung.DatabaseClass.ReportInfo;
import com.sai.hackbandung.InProgressGovernmentDetail;
import com.sai.hackbandung.R;

import java.util.List;

/**
 * Created by AlbertusK95 on 4/3/2017.
 */

public class MyAdapterGovernment_WIP extends RecyclerView.Adapter<MyAdapterGovernment_WIP.ViewHolder> {

    private Context context;
    private List<ReportInfo> reportInfos;

    // Reference to an image file in Firebase Storage
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    public MyAdapterGovernment_WIP(Context context, List<ReportInfo> reportInfos) {
        this.reportInfos = reportInfos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_report_government_wip, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyAdapterGovernment_WIP.ViewHolder holder, int position) {

        final ReportInfo reportInfo = reportInfos.get(position);

        storageReference = FirebaseStorage.getInstance().getReference().child(Constants.STORAGE_PATH_UPLOADS + reportInfo.imgREF + ".jpg");

        holder.textViewCitizensFullname.setText(reportInfo.fullname);
        holder.textViewCitizensPostingDate.setText(reportInfo.postingDate);
        holder.textViewCitizensTopic.setText(reportInfo.topic);
        holder.textViewCitizensMessage.setText(reportInfo.userMessage);


        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                //Toast.makeText(context, "onSuccess", Toast.LENGTH_LONG).show();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.outWidth = 100;
                options.outHeight = 100;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                holder.imageViewCitizensVerification.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                // Handle any errors
                Toast.makeText(context, "EXCP BRO: " + exception.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }


    @Override
    public int getItemCount() {

        return reportInfos.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewCitizensProfpic;
        public TextView textViewCitizensFullname;
        public TextView textViewCitizensPostingDate;
        public TextView textViewCitizensTopic;
        public TextView textViewCitizensMessage;
        public ImageView imageViewCitizensVerification;
        public Button buttonSeeMoreDetails;

        public GradientDrawable gd;

        public ViewHolder(View itemView) {

            super(itemView);

            gd = new GradientDrawable();
            gd.setColor(0xFF00FF00);
            gd.setCornerRadius(5);
            gd.setStroke(1, 0xFF000000);

            imageViewCitizensProfpic = (ImageView) itemView.findViewById(R.id.imageViewCitizensProfpic);
            textViewCitizensFullname = (TextView) itemView.findViewById(R.id.textViewCitizensFullname);
            textViewCitizensPostingDate = (TextView) itemView.findViewById(R.id.textViewCitizensPostingDate);
            textViewCitizensTopic = (TextView) itemView.findViewById(R.id.textViewCitizensTopic);
            textViewCitizensMessage = (TextView) itemView.findViewById(R.id.textViewCitizensMessage);
            imageViewCitizensVerification = (ImageView) itemView.findViewById(R.id.imageViewCitizensVerification);
            buttonSeeMoreDetails = (Button) itemView.findViewById(R.id.buttonSeeMoreDetails);


            // HANDLER for button Done click
            buttonSeeMoreDetails.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // send the data to the completion report page
                    // AND store into the database after the completion report is completed

                    Intent intent = new Intent(v.getContext(), InProgressGovernmentDetail.class);
                    intent.putExtra("GOVERNMENT_WIP_ADDRESS", reportInfos.get(getAdapterPosition()).address);

                    v.getContext().startActivity(intent);

                }

            });

        }

    }

}
