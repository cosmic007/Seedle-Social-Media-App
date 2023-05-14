package com.project.seedle.AdaptersClasses;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.project.seedle.ModelClassess.ModelFavoriteTextStatus;
import com.project.seedle.R;

public class GetFavoriteTextStatusAdapter extends FirestoreRecyclerAdapter <ModelFavoriteTextStatus,GetFavoriteTextStatusAdapter.GetFavoriteTextStatusViewHolder>{


    public GetFavoriteTextStatusAdapter(@NonNull FirestoreRecyclerOptions<ModelFavoriteTextStatus> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GetFavoriteTextStatusViewHolder holder, int position, @NonNull ModelFavoriteTextStatus model) {

    }

    @NonNull
    @Override
    public GetFavoriteTextStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class GetFavoriteTextStatusViewHolder extends RecyclerView.ViewHolder
    {

        ImageView removeFav,userProfile;
        TextView username,dateofstatus,status;

        public GetFavoriteTextStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            removeFav = itemView.findViewById(R.id.favorite_removebtn);
            userProfile = itemView.findViewById(R.id.favorite_profilepic);
            username = itemView.findViewById(R.id.favorite_username);
            status = itemView.findViewById(R.id.fav_textStatusTV);
            dateofstatus = itemView.findViewById(R.id.favorite_dateTV);
        }
    }
}
