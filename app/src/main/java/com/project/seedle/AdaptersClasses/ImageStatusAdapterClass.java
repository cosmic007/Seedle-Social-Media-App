package com.project.seedle.AdaptersClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.project.seedle.ModelClassess.Model_ImageStatus;
import com.project.seedle.R;

public class ImageStatusAdapterClass extends FirestoreRecyclerAdapter<Model_ImageStatus, ImageStatusAdapterClass.ImageStatusViewHolderClass> {

    private Context context;

    public ImageStatusAdapterClass(@NonNull FirestoreRecyclerOptions<Model_ImageStatus> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull ImageStatusViewHolderClass imageStatusViewHolderClass, int i, @NonNull Model_ImageStatus model_imageStatus) {
        try {
            imageStatusViewHolderClass.objectProgressBar.setVisibility(View.VISIBLE);
            imageStatusViewHolderClass.userName.setText(model_imageStatus.getUsername());

            imageStatusViewHolderClass.statusDate.setText(model_imageStatus.getCurrentdatetime());
            imageStatusViewHolderClass.StatusDesc.setText(model_imageStatus.getStatus());

            imageStatusViewHolderClass.heartCount.setText(String.valueOf(model_imageStatus.getNooflove()));
            imageStatusViewHolderClass.commentCount.setText(String.valueOf(model_imageStatus.getNoofcomments()));

            String linkofprofileimage = model_imageStatus.getProfileurl();
            String linkofimageStatus = model_imageStatus.getStatusimageurl();

            Glide.with(context).load(linkofprofileimage)
                    .into(imageStatusViewHolderClass.profileImageIV);
            Glide.with(context).load(linkofimageStatus)
                    .into(imageStatusViewHolderClass.imageStatus);
            imageStatusViewHolderClass.objectProgressBar.setVisibility(View.INVISIBLE);
        } catch (IndexOutOfBoundsException e) {
            // catch the exception and do nothing
        }
    }

    @NonNull
    @Override
    public ImageStatusViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageStatusViewHolderClass(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_image_status, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull ImageStatusViewHolderClass holder) {
        super.onViewRecycled(holder);
        Glide.with(context).clear(holder.profileImageIV);
        Glide.with(context).clear(holder.imageStatus);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ImageStatusViewHolderClass holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.objectProgressBar.getVisibility() == View.VISIBLE) {
            holder.objectProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public static class ImageStatusViewHolderClass extends RecyclerView.ViewHolder {
        ImageView imageStatus;
        TextView userName, StatusDesc, statusDate, heartCount, commentCount;
        ImageView favoriteIV, deleteIV, profileImageIV, HeartIV;
        ProgressBar objectProgressBar;




        public ImageStatusViewHolderClass(@NonNull View itemView) {
            super(itemView);

            imageStatus = itemView.findViewById(R.id.idIVPost);
            favoriteIV = itemView.findViewById(R.id.idIVfavorite);
            deleteIV = itemView.findViewById(R.id.idIVdelete);
            profileImageIV = itemView.findViewById(R.id.idCVUserprofile);
            HeartIV = itemView.findViewById(R.id.idIVHeart);
            userName = itemView.findViewById(R.id.idTVUsername);
            StatusDesc = itemView.findViewById(R.id.idTVPostDesc);
            statusDate = itemView.findViewById(R.id.idTVDate);
            heartCount = itemView.findViewById(R.id.idTVheartcount);
            commentCount = itemView.findViewById(R.id.idTVCommentCount);
            objectProgressBar = itemView.findViewById(R.id.idProgressBar);
        }
    }
}
