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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sai.hackbandung.CompleteDetailActivity;
import com.sai.hackbandung.Constants.Constants;
import com.sai.hackbandung.DatabaseClass.ReportInfo;
import com.sai.hackbandung.MainActivity;
import com.sai.hackbandung.R;

import java.util.List;


/**
 * Created by AlbertusK95 on 4/2/2017.
 */

public class MyAdapterCompleted extends RecyclerView.Adapter<MyAdapterCompleted.ViewHolder> {

    private Context context;
    private List<ReportInfo> reportInfos;

    //OnItemClickListener mItemClickListener;

    // Reference to an image file in Firebase Storage
    StorageReference storageReference;

    public MyAdapterCompleted(Context context, List<ReportInfo> reportInfos) {
        this.reportInfos = reportInfos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_report_completed, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ReportInfo reportInfo = reportInfos.get(position);

        storageReference = FirebaseStorage.getInstance().getReference().child(Constants.STORAGE_PATH_UPLOADS + reportInfo.imgREF + ".jpg");

        holder.textViewTopic.setText(reportInfo.topic);
        holder.textViewPostingDate.setText(reportInfo.postingDate);
        holder.textViewGovernmentAgency.setText(reportInfo.responsibleAgency);
        holder.textViewAddress.setText(reportInfo.address);
        holder.textViewMessage.setText(reportInfo.userMessage);

        // set status background color
        if (reportInfo.status.equals("waiting")) {
            holder.textViewStatusWaiting.setBackgroundColor(Color.CYAN);
            //holder.textViewStatusWaiting.setBackgroundDrawable(holder.gd);
        } else if (reportInfo.status.equals("wip")) {
            holder.textViewStatusWIP.setBackgroundColor(Color.CYAN);
            //holder.textViewStatusWIP.setBackgroundDrawable(holder.gd);
        } else {
            holder.textViewStatusDone.setBackgroundColor(Color.CYAN);
            //holder.textViewStatusDone.setBackgroundDrawable(holder.gd);
        }


        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Toast.makeText(context, "onSuccess", Toast.LENGTH_LONG).show();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.outWidth = 100;
                options.outHeight = 100;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                holder.imageViewVerification.setImageBitmap(bitmap);

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

        public ImageView imageViewVerification;
        public TextView textViewTopic;
        public TextView textViewStatusWaiting;
        public TextView textViewStatusWIP;
        public TextView textViewStatusDone;
        public TextView textViewPostingDate;
        public TextView textViewGovernmentAgency;
        public TextView textViewAddress;
        public TextView textViewMessage;
        public Button buttonCompletedDetail;

        public GradientDrawable gd;

        public ViewHolder(View itemView) {

            super(itemView);

            gd = new GradientDrawable();
            gd.setColor(0xFF00FF00);
            gd.setCornerRadius(5);
            gd.setStroke(1, 0xFF000000);

            imageViewVerification = (ImageView) itemView.findViewById(R.id.imageViewVerification);

            textViewTopic = (TextView) itemView.findViewById(R.id.textViewTopic);
            textViewStatusWaiting = (TextView) itemView.findViewById(R.id.textViewStatusWaiting);
            textViewStatusWIP = (TextView) itemView.findViewById(R.id.textViewStatusWIP);
            textViewStatusDone = (TextView) itemView.findViewById(R.id.textViewStatusDone);
            textViewPostingDate = (TextView) itemView.findViewById(R.id.textViewPostingDate);
            textViewGovernmentAgency = (TextView) itemView.findViewById(R.id.textViewGovernmentAgency);
            textViewAddress = (TextView) itemView.findViewById(R.id.textViewAddress);
            textViewMessage = (TextView) itemView.findViewById(R.id.textViewMessage);

            buttonCompletedDetail = (Button) itemView.findViewById(R.id.buttonViewDetailsCompleted_CITIZENS);

            buttonCompletedDetail.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //Toast.makeText(v.getContext(), "OVERRIDE onClickCompletedDetail", Toast.LENGTH_LONG).show();

                    // redirect to completed detail page
                    Intent intent = new Intent(v.getContext(), CompleteDetailActivity.class);
                    intent.putExtra("COMPLETED_AGENCY", reportInfos.get(getAdapterPosition()).responsibleAgency);
                    intent.putExtra("COMPLETED_IMAGE_BEFORE", reportInfos.get(getAdapterPosition()).imgREF);
                    intent.putExtra("COMPLETED_IMAGE_AFTER", reportInfos.get(getAdapterPosition()).imgREF_AFTER_COMPLETED);

                    v.getContext().startActivity(intent);

                }

            });

        }

    }

}
