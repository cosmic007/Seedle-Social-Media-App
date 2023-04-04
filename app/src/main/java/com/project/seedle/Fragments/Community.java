package com.project.seedle.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.seedle.AdaptersClasses.ChatAdapter;
import com.project.seedle.ModelClassess.ChatMessage;
import com.project.seedle.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Community extends Fragment {

    private Date currentDate;
    private SimpleDateFormat objectSimpleDateFormat;

    private FloatingActionButton fab;



    private ChatAdapter chatAdapter;
    public String url;

    DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("chat_messages");

    private ImageButton sendbtn;
    private EditText msg;
    private RecyclerView recyclerView;

    public String User_Name;

    private final FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();
    private final FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();


    public Community() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        String currentloggedinuser = objectFirebaseAuth.getCurrentUser().getEmail();

        FirebaseAuth objectFirebaseAuth= FirebaseAuth.getInstance();
        FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();

        CollectionReference collectionReff = objectFirebaseFirestore.collection("UserProfileData");
        DocumentReference documentReff = collectionReff.document(currentloggedinuser);

        documentReff.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String Userprofile = documentSnapshot.getString("profileimageurl");
                    url = Userprofile;
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









        CollectionReference collectionRef = objectFirebaseFirestore.collection("UserProfileData");
        DocumentReference documentRef = collectionRef.document(currentloggedinuser);

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

        FirebaseDatabase ObjectFirebaseDatabase = FirebaseDatabase.getInstance();
        View objectview;

        objectview = inflater.inflate(R.layout.fragment_community, container, false);



        sendbtn = objectview.findViewById(R.id.chat_send_button);
        msg = objectview.findViewById(R.id.chat_input_edit_text);

        recyclerView = objectview.findViewById(R.id.chat_messages_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the message text and sender name from the UI
                String messageText = msg.getText().toString();
                String senderName = User_Name;

                // Get the current timestamp


                long timestamp = System.currentTimeMillis();
                String profileurl = url;


                ChatMessage chatMessage = new ChatMessage(messageText, senderName, timestamp , profileurl);

                // Generate a unique key for the message
                String messageId = chatRef.push().getKey();

                // Set the value of the new child node to the ChatMessage object
                chatRef.child(messageId).setValue(chatMessage);

                // Clear the message text field
                msg.setText("");
            }
        });
        List<ChatMessage> chatMessagesList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessagesList);
        recyclerView.setAdapter(chatAdapter);

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                chatMessagesList.clear();

                // Loop through the chat messages in the dataSnapshot
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve the chat message object
                    ChatMessage message = messageSnapshot.getValue(ChatMessage.class);

                    // Add the chat message to your local data list or adapter
                    chatMessagesList.add(message);

                }
                chatAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });





        return objectview;


    }










}