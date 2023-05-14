package com.project.seedle.AdaptersClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.seedle.ModelClassess.ModelFavoriteTextStatus;
import com.project.seedle.R;

public class GetFavoriteTextStatusAdapter extends FirestoreRecyclerAdapter <ModelFavoriteTextStatus,GetFavoriteTextStatusAdapter.GetFavoriteTextStatusViewHolder>{


    public GetFavoriteTextStatusAdapter(@NonNull FirestoreRecyclerOptions<ModelFavoriteTextStatus> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GetFavoriteTextStatusViewHolder holder, int position, @NonNull ModelFavoriteTextStatus model) {
        holder.username.setText(model.getUsername());
        holder.dateofstatus.setText(model.getCurrentdatetime());
        holder.status.setText(model.getStatus());
        String linkofProfilepic = model.getProfileurl();

        Glide.with(holder.userProfile.getContext()).load(linkofProfilepic).into(holder.userProfile);

        holder.removeFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();

                if(objectFirebaseAuth!=null)
                {
                    String currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail();
                    FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();

                    objectFirebaseFirestore.collection("UserFavorite").document(currentLoggedInUser)
                            .collection("FavoriteTextStatus")
                            .document(getSnapshots().getSnapshot(holder.getAdapterPosition()).getId()
                            ).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(holder.removeFav.getContext(), "Status Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(holder.removeFav.getContext(), "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
                else {
                    Toast.makeText(holder.removeFav.getContext(), "User not online", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @NonNull
    @Override
    public GetFavoriteTextStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetFavoriteTextStatusViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.model_favorite_ts,parent,false
        ));
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
