package com.project.seedle.AdaptersClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.project.seedle.Admin;
import com.project.seedle.ModelClassess.Model_GetImageComments;
import com.project.seedle.R;

import java.util.Objects;

public class GetImageCommentAdapter extends FirestoreRecyclerAdapter <Model_GetImageComments, GetImageCommentAdapter.GetImageCommentViewHolder>{
    public Admin admin = new Admin();


    public GetImageCommentAdapter(@NonNull FirestoreRecyclerOptions<Model_GetImageComments> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GetImageCommentViewHolder holder, int position, @NonNull Model_GetImageComments model) {
        holder.userNameTV.setText(model.getUsername());
        holder.commentDateTV.setText(model.getCurrentdatetime());
        holder.commentTV.setText(model.getComment());
        String profileImageUri=model.getProfilepicurl();

        String userN = model.getUsername();
        String admin1 = admin.getAdmin1();
        String admin2 = admin.getAdmin2();
        String admin3 = admin.getAdmin3();


        if(Objects.equals(userN, admin1) || Objects.equals(userN, admin2))
        {
            holder.verified.setVisibility(View.VISIBLE);
            holder.devTV.setText("Developer");
            holder.devTV.setVisibility(View.VISIBLE);

        } else if (Objects.equals(userN, admin3)) {
            holder.verified.setVisibility(View.VISIBLE);
            holder.devTV.setText("Tester");
            holder.devTV.setVisibility(View.VISIBLE);
        } else {
            holder.verified.setVisibility(View.INVISIBLE);
            holder.devTV.setVisibility(View.INVISIBLE);

        }

        Glide.with(holder.userProfileIV.getContext())
                .load(profileImageUri).into(holder.userProfileIV);

    }

    @NonNull
    @Override
    public GetImageCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetImageCommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_image_comments,parent,false));
    }

    public class GetImageCommentViewHolder extends RecyclerView.ViewHolder{


        ImageView userProfileIV,verified;
        TextView userNameTV,commentDateTV,commentTV,devTV;

        public GetImageCommentViewHolder(@NonNull View itemView) {
            super(itemView);
            devTV = itemView.findViewById(R.id.adminimage);
            userProfileIV=itemView.findViewById(R.id.model_addImagecomment_profile);
            userNameTV=itemView.findViewById(R.id.model_addImagecomment_userName);
            commentDateTV=itemView.findViewById(R.id.model_addImagecomment_currentDateTime);
            commentTV=itemView.findViewById(R.id.model_addImagecomment_comment);
            verified = itemView.findViewById(R.id.verifiedimage);

        }
    }
}
