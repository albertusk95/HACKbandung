package com.sai.hackbandung.Adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sai.hackbandung.Constants.Constants;
import com.sai.hackbandung.DatabaseClass.ReportInfo;
import com.sai.hackbandung.R;

import java.util.List;

/**
 * Created by AlbertusK95 on 4/2/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<ReportInfo> reportInfos;

    // Reference to an image file in Firebase Storage
    StorageReference storageReference;

    public MyAdapter(Context context, List<ReportInfo> reportInfos) {
        this.reportInfos = reportInfos;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_report, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        ReportInfo reportInfo = reportInfos.get(position);

        storageReference = FirebaseStorage.getInstance().getReference().child(Constants.STORAGE_PATH_UPLOADS + reportInfo.imgREF + ".jpg");

        holder.textViewTopic.setText(reportInfo.topic);
        holder.textViewPostingDate.setText(reportInfo.postingDate);
        holder.textViewGovernmentAgency.setText(reportInfo.responsibleAgency);
        holder.textViewAddress.setText(reportInfo.address);
        holder.textViewMessage.setText(reportInfo.userMessage);

        // set status background color
        if (reportInfo.status.equals("waiting")) {
            //holder.textViewStatusWaiting.setBackgroundColor(Color.CYAN);
            holder.textViewStatusWaiting.setBackgroundDrawable(holder.gd);
        } else if (reportInfo.status.equals("wip")) {
            //holder.textViewStatusWIP.setBackgroundColor(Color.CYAN);
            holder.textViewStatusWIP.setBackgroundDrawable(holder.gd);
        } else {
            //holder.textViewStatusDone.setBackgroundColor(Color.CYAN);
            holder.textViewStatusDone.setBackgroundDrawable(holder.gd);
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

        }

    }
}
