package com.sai.hackbandung.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
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
import com.sai.hackbandung.R;

import java.util.List;

/**
 * Created by AlbertusK95 on 4/2/2017.
 */

public class MyAdapterGovernment extends RecyclerView.Adapter<MyAdapterGovernment.ViewHolder> {

    private Context context;
    private List<ReportInfo> reportInfos;

    private String resAgency;

    // Reference to an image file in Firebase Storage
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    public MyAdapterGovernment(Context context, List<ReportInfo> reportInfos) {
        this.reportInfos = reportInfos;
        this.context = context;
    }

    public void setAgency(String resAgency) {
        this.resAgency = resAgency;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_report_government, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyAdapterGovernment.ViewHolder holder, int position) {

        final ReportInfo reportInfo = reportInfos.get(position);

        storageReference = FirebaseStorage.getInstance().getReference().child(Constants.STORAGE_PATH_UPLOADS + reportInfo.imgREF + ".jpg");

        holder.textViewCitizensFullname.setText(reportInfo.fullname);
        holder.textViewCitizensPostingDate.setText(reportInfo.postingDate);
        holder.textViewCitizensTopic.setText(reportInfo.topic);
        holder.textViewCitizensMessage.setText(reportInfo.userMessage);


        // set status background color
        if (reportInfo.status.equals("wip")) {
            holder.buttonForward.setBackgroundResource(android.R.drawable.btn_default);
            holder.buttonWIP.setBackgroundColor(Color.CYAN);
            holder.buttonDone.setBackgroundResource(android.R.drawable.btn_default);
        } else if (reportInfo.status.equals("completed")){
            holder.buttonForward.setBackgroundResource(android.R.drawable.btn_default);
            holder.buttonWIP.setBackgroundResource(android.R.drawable.btn_default);
            holder.buttonDone.setBackgroundColor(Color.CYAN);
        }


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
        public Button buttonForward;
        public Button buttonWIP;
        public Button buttonDone;

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
            buttonForward = (Button) itemView.findViewById(R.id.buttonForward);
            buttonWIP = (Button) itemView.findViewById(R.id.buttonWIP);
            buttonDone = (Button) itemView.findViewById(R.id.buttonDone);

            // HANDLER for button W.I.P
            buttonWIP.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // change the status and the agency

                    String newStatus = "wip";

                    ReportInfo new_RI = new ReportInfo(reportInfos.get(getAdapterPosition()).REPORT_ID,
                                                        reportInfos.get(getAdapterPosition()).imgREF,
                                                        reportInfos.get(getAdapterPosition()).imgREF_AFTER_COMPLETED,
                                                        reportInfos.get(getAdapterPosition()).topic,
                                                        reportInfos.get(getAdapterPosition()).postingDate,
                                                        reportInfos.get(getAdapterPosition()).finishDate,
                                                        resAgency,
                                                        reportInfos.get(getAdapterPosition()).address,
                                                        reportInfos.get(getAdapterPosition()).userRole,
                                                        reportInfos.get(getAdapterPosition()).username,
                                                        reportInfos.get(getAdapterPosition()).fullname,
                                                        newStatus,
                                                        reportInfos.get(getAdapterPosition()).userMessage);


                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child(Constants.DATABASE_PATH_UPLOADS + "/" + reportInfos.get(getAdapterPosition()).REPORT_ID).setValue(new_RI);


/*
                    Toast.makeText(context, "REPORT_ID " + reportInfos.get(getAdapterPosition()).REPORT_ID, Toast.LENGTH_LONG).show();

                    // redirect to WIP Confirmation page
                    Intent intent = new Intent(v.getContext(), WIPConfirmationActivity.class);
                    intent.putExtra("CONFIRM_WIP_REPORT_ID", reportInfos.get(getAdapterPosition()).REPORT_ID);
                    intent.putExtra("CONFIRM_WIP_IMG_REF", reportInfos.get(getAdapterPosition()).imgREF);
                    intent.putExtra("CONFIRM_WIP_IMG_REF_AFTER_COMPLETED", reportInfos.get(getAdapterPosition()).imgREF_AFTER_COMPLETED);
                    intent.putExtra("CONFIRM_WIP_TOPIC", reportInfos.get(getAdapterPosition()).topic);
                    intent.putExtra("CONFIRM_WIP_POSTING_DATE", reportInfos.get(getAdapterPosition()).postingDate);
                    intent.putExtra("CONFIRM_WIP_RESPONSIBLE_AGENCY", reportInfos.get(getAdapterPosition()).responsibleAgency);
                    intent.putExtra("CONFIRM_WIP_ADDRESS", reportInfos.get(getAdapterPosition()).address);
                    intent.putExtra("CONFIRM_WIP_USER_ROLE", reportInfos.get(getAdapterPosition()).userRole);
                    intent.putExtra("CONFIRM_WIP_USERNAME", reportInfos.get(getAdapterPosition()).username);
                    intent.putExtra("CONFIRM_WIP_FULLNAME", reportInfos.get(getAdapterPosition()).fullname);
                    intent.putExtra("CONFIRM_WIP_STATUS", "wip");
                    intent.putExtra("CONFIRM_WIP_USER_MESSAGE", reportInfos.get(getAdapterPosition()).userMessage);

                    v.getContext().startActivity(intent);
*/

                }

            });

            // HANDLER for button Done click
            buttonDone.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // send the data to the completion report page
                    // AND store into the database after the completion report is completed

                    Intent intent = new Intent(v.getContext(), CompletionReportByAgencyActivity.class);
                    intent.putExtra("COMPLETION_DONE_REPORT_ID", reportInfos.get(getAdapterPosition()).REPORT_ID);
                    intent.putExtra("COMPLETION_DONE_IMG_REF", reportInfos.get(getAdapterPosition()).imgREF);
                    intent.putExtra("COMPLETION_DONE_IMG_REF_AFTER_COMPLETED", reportInfos.get(getAdapterPosition()).imgREF_AFTER_COMPLETED);
                    intent.putExtra("COMPLETION_DONE_TOPIC", reportInfos.get(getAdapterPosition()).topic);
                    intent.putExtra("COMPLETION_DONE_POSTING_DATE", reportInfos.get(getAdapterPosition()).postingDate);
                    intent.putExtra("COMPLETION_DONE_FINISH_DATE", reportInfos.get(getAdapterPosition()).finishDate);
                    intent.putExtra("COMPLETION_DONE_RESPONSIBLE_AGENCY", reportInfos.get(getAdapterPosition()).responsibleAgency);
                    intent.putExtra("COMPLETION_DONE_ADDRESS", reportInfos.get(getAdapterPosition()).address);
                    intent.putExtra("COMPLETION_DONE_USER_ROLE", reportInfos.get(getAdapterPosition()).userRole);
                    intent.putExtra("COMPLETION_DONE_USERNAME", reportInfos.get(getAdapterPosition()).username);
                    intent.putExtra("COMPLETION_DONE_FULLNAME", reportInfos.get(getAdapterPosition()).fullname);
                    intent.putExtra("COMPLETION_DONE_STATUS", "completed");
                    intent.putExtra("COMPLETION_DONE_USER_MESSAGE", reportInfos.get(getAdapterPosition()).userMessage);

                    v.getContext().startActivity(intent);

                }

            });

        }

    }

}
