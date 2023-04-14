package com.project.seedle.AdaptersClasses;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.seedle.Admin;
import com.project.seedle.ModelClassess.ChatMessage;
import com.project.seedle.R;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatMessagesList;

    public String EMAIL;
    public Admin admin =new Admin();
    public String User_Name;

    public ChatAdapter(List<ChatMessage> chatMessagesList) {
        this.chatMessagesList = chatMessagesList;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the chat item view layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_chat_item, parent, false);

        FirebaseAuth objectFirebaseAuth= FirebaseAuth.getInstance();
        EMAIL =objectFirebaseAuth.getCurrentUser().getEmail();
        FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();

        CollectionReference collectionRef = objectFirebaseFirestore.collection("UserProfileData");
        DocumentReference documentRef = collectionRef.document(EMAIL);

        documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String UserName = documentSnapshot.getString("username");
                    User_Name = UserName;
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting document", e);
            }
        });


        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        // Get the chat message at the current position
        ChatMessage message = chatMessagesList.get(position);

        // Set the message text and other properties on the view holder
        holder.textViewMessage.setText(message.getMessageText());
        holder.textViewSender.setText(message.getSenderName());

        String userN = message.getSenderName();
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




        long datetime = message.getTimestamp();


        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a  dd-MM-yyyy", Locale.getDefault());
        String dateString = formatter.format(new Date(datetime));


        String profilepic = message.getProfileurl();
        String time = dateString;


        holder.textViewTime.setText(time);
        Glide.with(holder.profilepic.getContext())
                .load(profilepic).into(holder.profilepic);



    }

    @Override
    public int getItemCount() {
        return chatMessagesList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewMessage;
        public TextView textViewSender;
        public TextView textViewTime,devTV;

        public ImageView profilepic,verified;

        public ChatViewHolder(View itemView) {
            super(itemView);

            // Get references to the views in the chat item view
            textViewMessage = itemView.findViewById(R.id.text_view_message_content);
            textViewSender = itemView.findViewById(R.id.text_view_user_name);
            textViewTime = itemView.findViewById(R.id.text_view_message_time);
            profilepic = itemView.findViewById(R.id.image_view_profile);
            verified = itemView.findViewById(R.id.verified);
            devTV =itemView.findViewById(R.id.admin);

        }
    }
}
