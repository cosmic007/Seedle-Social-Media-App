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
import com.project.seedle.ModelClassess.ChatMessage;
import com.project.seedle.R;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatMessagesList;

    public ChatAdapter(List<ChatMessage> chatMessagesList) {
        this.chatMessagesList = chatMessagesList;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the chat item view layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_chat_item, parent, false);


        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        // Get the chat message at the current position
        ChatMessage message = chatMessagesList.get(position);

        // Set the message text and other properties on the view holder
        holder.textViewMessage.setText(message.getMessageText());
        holder.textViewSender.setText(message.getSenderName());


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
        public TextView textViewTime;

        public ImageView profilepic;

        public ChatViewHolder(View itemView) {
            super(itemView);

            // Get references to the views in the chat item view
            textViewMessage = itemView.findViewById(R.id.text_view_message_content);
            textViewSender = itemView.findViewById(R.id.text_view_user_name);
            textViewTime = itemView.findViewById(R.id.text_view_message_time);
            profilepic = itemView.findViewById(R.id.image_view_profile);
        }
    }
}
