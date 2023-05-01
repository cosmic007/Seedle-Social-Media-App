package com.project.seedle.Fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.seedle.Activities.MainContentPage;
import com.project.seedle.AdaptersClasses.ChatAdapter;
import com.project.seedle.ModelClassess.ChatMessage;
import com.project.seedle.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Community extends Fragment {

    private Date currentDate;
    private SimpleDateFormat objectSimpleDateFormat;

    private FloatingActionButton fab;

    private TextView Caption;
    public String captitle;

    public EditText setTitle;

    private int flagn = 0;




    private Context mContext;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }


    private ChatAdapter chatAdapter;


    public String url;

    DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("chat_messages");

    private ImageButton sendbtn;
    private EditText msg;
    private RecyclerView recyclerView;

    private VideoView videoView;
    private Button uploadButton,Setbtn;

    public String currentVideoUrl;
    private ProgressBar progressBar;
    private FirebaseStorage storage;


    public String User_Name;

    private final FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();
    private final FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();


    public Community() {

    }

    private void showNotification(String message, String username) {

        // Create a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "my_channel_id")
                .setSmallIcon(R.mipmap.seedle_app_logo)
                .setContentTitle(username)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);


        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(0, builder.build());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View objectview;

        objectview = inflater.inflate(R.layout.fragment_community, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("my_channel_id", "Global Chat", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Caption=objectview.findViewById(R.id.caption);
        setTitle = objectview.findViewById(R.id.setid);
        Setbtn=objectview.findViewById(R.id.setbtn);

        videoView = objectview.findViewById(R.id.video);
        uploadButton = objectview.findViewById(R.id.uploadbtn);
        progressBar = objectview.findViewById(R.id.progress_bar);

        MediaController mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);




        storage = FirebaseStorage.getInstance();















        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(Intent.createChooser(intent, "Select Video"), 1);
            }
        });























        String currentloggedinuser = objectFirebaseAuth.getCurrentUser().getEmail();

        FirebaseAuth objectFirebaseAuth= FirebaseAuth.getInstance();
        FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();

        if ("cosmicriderrr@gmail.com".equals(currentloggedinuser) || "shabanaofficial321@gmail.com".equals(currentloggedinuser)) {
            uploadButton.setVisibility(View.VISIBLE);
            setTitle.setVisibility(View.VISIBLE);
            Setbtn.setVisibility(View.VISIBLE);
        }

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


        CollectionReference collectionRefTitle = objectFirebaseFirestore.collection("Caption");
        DocumentReference documentReffTitle = collectionRefTitle.document("videotitle");

        Setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newText = setTitle.getText().toString();

                // Create a Map object with the field to update and the new value
                Map<String, Object> updates = new HashMap<>();
                updates.put("title", newText);

                // Update the document with the new value
                documentReffTitle.update(updates);

                // Clear the EditText
                setTitle.setText("");

                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(setTitle.getWindowToken(), 0);

            }
        });








        documentReffTitle.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String capt = documentSnapshot.getString("title");
                    captitle = capt;
                    Caption.setText(capt);

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


        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatMessagesList.clear();
                ChatMessage latestMessage = null;
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    ChatMessage message = messageSnapshot.getValue(ChatMessage.class);
                    chatMessagesList.add(message);
                    if (latestMessage == null || message.getTimestamp() > latestMessage.getTimestamp()) {
                        latestMessage = message;
                    }
                }

                if (latestMessage != null) {
                    String senderName = latestMessage.getSenderName();
                    String messageText = latestMessage.getMessageText();
                    if (!senderName.equals(User_Name)) {
                        showNotification(messageText, senderName);
                    }
                }

                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });







        return objectview;


    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri videoUri = data.getData();
            StorageReference storageRef = storage.getReference().child("videos/" + videoUri.getLastPathSegment());

            // Delete existing video
            if (currentVideoUrl != null) {
                StorageReference existingVideoRef = storage.getReferenceFromUrl(currentVideoUrl);
                existingVideoRef.delete();
            }

            UploadTask uploadTask = storageRef.putFile(videoUri);

            progressBar.setVisibility(View.VISIBLE);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String videoUrl = uri.toString();
                            currentVideoUrl = videoUrl; // Save the current video URL
                            // Update the current video URL in the real-time database
                            DatabaseReference currentVideoRef = FirebaseDatabase.getInstance().getReference().child("current_video");
                            currentVideoRef.setValue(videoUrl);


                            // Display the video on the VideoView
                            videoView.setVideoURI(Uri.parse(videoUrl));
                            videoView.start();
                            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {


                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    // Loop the video when it ends
                                    videoView.start();
                                }
                            });

                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle any errors
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }


    private void playVideo(String videourl) {
        videoView.setVideoURI(Uri.parse(videourl));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        DatabaseReference currentVideoRef = FirebaseDatabase.getInstance().getReference().child("current_video");
        currentVideoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String videoUrl = dataSnapshot.getValue(String.class);
                if (videoUrl != null) {
                    currentVideoUrl = videoUrl;
                    videoView.setVideoURI(Uri.parse(videoUrl));
                    videoView.start();
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            // Loop the video when it ends
                            videoView.start();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }










}