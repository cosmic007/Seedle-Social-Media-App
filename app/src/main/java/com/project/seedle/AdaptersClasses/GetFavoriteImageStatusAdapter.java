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
import com.project.seedle.ModelClassess.Model_Favorite_Image_Status;
import com.project.seedle.R;

public class GetFavoriteImageStatusAdapter extends FirestoreRecyclerAdapter <Model_Favorite_Image_Status,GetFavoriteImageStatusAdapter.GetFavoriteImageStatusViewHolder>{

    public GetFavoriteImageStatusAdapter(@NonNull FirestoreRecyclerOptions<Model_Favorite_Image_Status> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GetFavoriteImageStatusViewHolder holder, int position, @NonNull Model_Favorite_Image_Status model) {
        String linkofImageStatus=model.getStatusurl();
        String linkofProfilepic=model.getProfileurl();


        try{
            Glide.with(holder.status.getContext())
                    .load(linkofImageStatus).into(holder.status);

            Glide.with(holder.profilepic.getContext())
                    .load(linkofProfilepic).into(holder.profilepic);

        }
        catch (Exception e)
        {
            Toast.makeText(holder.status.getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
        holder.username.setText(model.getUsername());
        holder.statusdesc.setText(model.getStatus());

        holder.removestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();
                if(objectFirebaseAuth!=null)
                {
                    FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();
                    objectFirebaseFirestore.collection("UserFavorite").document(objectFirebaseAuth.getCurrentUser().getEmail().toString()).collection("FavoriteImageStatus")
                            .document(getSnapshots().getSnapshot(holder.getAdapterPosition()).getId()).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(holder.removestatus.getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(holder.removestatus.getContext(), "Failed to removed from favorites", Toast.LENGTH_SHORT).show();

                                }
                            });

                }
                else {
                    Toast.makeText(holder.removestatus.getContext(), "User not online", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @NonNull
    @Override
    public GetFavoriteImageStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetFavoriteImageStatusViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.model_favorite_is,parent,false
        ));
    }

    public class GetFavoriteImageStatusViewHolder extends RecyclerView.ViewHolder
    {
        ImageView status,profilepic,removestatus;

        TextView username,dateofstatus,statusdesc;


        public GetFavoriteImageStatusViewHolder(@NonNull View itemView) {
            super(itemView);

            status=itemView.findViewById(R.id.model_favorite_post);
            removestatus=itemView.findViewById(R.id.model_favorite_remove);
            profilepic=itemView.findViewById(R.id.model_favorite_profilepic);
            username=itemView.findViewById(R.id.model_favorite_username);
            dateofstatus=itemView.findViewById(R.id.model_favorite_date);
            statusdesc=itemView.findViewById(R.id.model_favorite_desc);



        }
    }
}
