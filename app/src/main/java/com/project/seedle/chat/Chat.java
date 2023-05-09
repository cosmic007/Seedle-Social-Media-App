package com.project.seedle.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.seedle.MemoryData;
import com.project.seedle.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://seedle-2a24f-default-rtdb.firebaseio.com/");
    private String chatKey;
    String getUserMobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);


        final ImageView backbutton= findViewById(R.id.backbtn);
        final TextView nameTV = findViewById(R.id.name);
        final EditText messageEditText = findViewById(R.id.messageEditText);
        final ImageView sendBtn = findViewById(R.id.sendbutton);
        final CircleImageView profilePic = findViewById(R.id.profilepicture);

        final String getName= getIntent().getStringExtra("name");
        final String getProfilePic = getIntent().getStringExtra("profilepic");
        chatKey = getIntent().getStringExtra("chat_key");
        final String getMobile = getIntent().getStringExtra("mobile");

        getUserMobile = MemoryData.getData(Chat.this);

        nameTV.setText(getName);
        Glide.with(this)
                .load(getProfilePic)
                .into(profilePic);

        if(chatKey.isEmpty()){

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatKey = "1";
                    if(snapshot.hasChild("chat")){
                        chatKey = String.valueOf((snapshot.child("chat").getChildrenCount()+1));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final String getTextMessage = messageEditText.getText().toString();
                final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0,10);

                MemoryData.saveLastMsgTS(currentTimestamp, chatKey,Chat.this);
                databaseReference.child("chat").child(chatKey).child("user_1").setValue(getUserMobile);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(getMobile);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("msg").setValue(getTextMessage);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("mobile").setValue(getUserMobile);


            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}