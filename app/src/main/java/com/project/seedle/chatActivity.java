package com.project.seedle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.seedle.Activities.MainContentPage;
import com.project.seedle.messages.MessageAdapter;
import com.project.seedle.messages.MessageList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatActivity extends AppCompatActivity {

    public  String name;
    public   String mobile;

    private final List<MessageList> messageLists = new ArrayList<>();

    public String profilepic;
    private int unseenMessages=0;

    private String chatKey="";

    private boolean dataSet = false;
    private String lastMessage="";



    private  String mailid;
    private RecyclerView messagesRecyclerView;
    private MessageAdapter messageAdapter;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://seedle-2a24f-default-rtdb.firebaseio.com/");


    private FirebaseFirestore objectFirebaseFirestore;
    private DocumentReference objectDocumentReference;
    private FirebaseAuth objectFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final CircleImageView userProfilePic = findViewById(R.id.userprofile);


        objectFirebaseFirestore= FirebaseFirestore.getInstance();
        objectFirebaseAuth=FirebaseAuth.getInstance();


        messagesRecyclerView = findViewById(R.id.messageRecyclerView);

        mailid = objectFirebaseAuth.getCurrentUser().getEmail().toString();

        objectDocumentReference = objectFirebaseFirestore.collection("UserProfileData").document(mailid);
        objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String Name = documentSnapshot.getString("username");
                String Mobile = documentSnapshot.getString("mobile");
                String ProfilePic = documentSnapshot.getString("profileimageurl");
                mobile = Mobile;
                name = Name;
                profilepic = ProfilePic;

            }
        });

        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(messageLists, chatActivity.this);



        messagesRecyclerView.setAdapter(messageAdapter);


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String ProfilePic = documentSnapshot.getString("profileimageurl");
                        profilepic = ProfilePic;

                        Glide.with(chatActivity.this)
                                .load(profilepic)
                                .into(userProfilePic);


                    }
                });






                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageLists.clear();
                unseenMessages = 0;
                lastMessage="";
                chatKey="";

                for(DataSnapshot dataSnapshot : snapshot.child("users").getChildren()){

                    Bundle bundle2 = getIntent().getExtras();
                    String value = bundle2.getString("Mobile");

                    if(!value.isEmpty()||value!=null)
                    {
                        mobile=value;
                    }


                    final String getMobile = dataSnapshot.getKey();

                    dataSet = false;
                    if(!Objects.equals(getMobile, mobile)){
                        final String getName = dataSnapshot.child("name").getValue(String.class);
                        final String getProfilePic = dataSnapshot.child("profilepic").getValue(String.class);



                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int getChatCounts =(int) snapshot.getChildrenCount();
                                if(getChatCounts>0)
                                {
                                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                        final String getKey = dataSnapshot1.getKey();
                                        chatKey = getKey;

                                        if(dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")){
                                            final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);

                                            if((getUserOne.equals(getMobile) && getUserTwo.equals(mobile)) || (getUserOne.equals(mobile) && getUserTwo.equals(getMobile)))
                                            {
                                                for(DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()){

                                                    final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                    final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTS(chatActivity.this,getKey));
                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                    if(getMessageKey > getLastSeenMessage)
                                                    {
                                                        unseenMessages++;

                                                    }


                                                }
                                            }

                                        }


                                    }
                                }
                                if(!dataSet){
                                    dataSet=true;
                                    MessageList messageList = new MessageList(getName,lastMessage,unseenMessages,getMobile,getProfilePic,chatKey);
                                    messageLists.add(messageList);
                                    messageAdapter.updateData(messageLists);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });












    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainContentPage.class);
        startActivity(intent);

    }
}