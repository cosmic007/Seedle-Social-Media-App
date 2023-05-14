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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.seedle.Activities.AllNotifications;
import com.project.seedle.ModelClassess.Model_AllNotifications;
import com.project.seedle.R;

public class AllNotificationsAdapter extends FirestoreRecyclerAdapter<Model_AllNotifications, AllNotificationsAdapter.AllNotificationsViewHolder>{

    FirebaseFirestore objectFirebaseFirestore =FirebaseFirestore.getInstance();


    public AllNotificationsAdapter(@NonNull FirestoreRecyclerOptions<Model_AllNotifications> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AllNotificationsViewHolder holder, int position, @NonNull Model_AllNotifications model) {


        holder.useremail.setText(model.getEmail());
        String actionofUser = model.getAction();
        String type = model.getType();
        String  finalStatus =actionofUser+" your "+type;

        holder.action.setText(finalStatus);
        objectFirebaseFirestore.collection("UserProfileData").document(model.getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String linkofProfilepic = documentSnapshot.getString("profileimageurl");
                        Glide.with(holder.userProfilepicIV.getContext()).load(linkofProfilepic).into(holder.userProfilepicIV);
                    }
                });


    }

    @NonNull
    @Override
    public AllNotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllNotificationsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_all_notification,parent,false));
    }

    public class AllNotificationsViewHolder extends RecyclerView.ViewHolder
    {

        ImageView userProfilepicIV;
        TextView useremail,action;

        public AllNotificationsViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfilepicIV = itemView.findViewById(R.id.model_allnotification_profilepic);
            useremail = itemView.findViewById(R.id.model_allnotification_useremail);
            action = itemView.findViewById(R.id.model_allnotification_action);
        }
    }
}
