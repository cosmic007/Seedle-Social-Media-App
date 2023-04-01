package com.project.seedle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.seedle.AdaptersClasses.TextCommentAdapter;
import com.project.seedle.ModelClassess.Model_Comment;
import com.project.seedle.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TextCommentPage extends AppCompatActivity {
    //XML Variables

    private RecyclerView objectRecyclerView;
    private Button SendCommentBtn;

    private EditText commentET;


    //Class Variables
    private Bundle objectBundle;
    private String documentID;

    private Date currentDate;
    private SimpleDateFormat objectSimpleDateFormat;

    int noofcomments;
    private String currentLoggedinUser;
    private String currentLoggedinUsername;
    private TextCommentAdapter objectGetTextCommentAdapter;

    //Firebase Variables
    private FirebaseFirestore objectFirebaseFirestore;
    private FirebaseAuth objectFirebaseAuth;

    private DocumentReference objectDocumentReference;
    private CollectionReference objectCollectionReference;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_comment_page);

        try {
            AttachJavaObjectsToXML();
            objectBundle =getIntent().getExtras();
            documentID=objectBundle.getString("documentId");
            getCommentIntoRV();

            SendCommentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addcomment();

                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    private String getCurrentDate()
    {
        try{

            currentDate= Calendar.getInstance().getTime();
            objectSimpleDateFormat = new SimpleDateFormat("hh:mm:ss dd-MMM-yyyy");
            return objectSimpleDateFormat.format(currentDate);


        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void getCommentIntoRV()
    {
        try {

            Query objectQuery = objectFirebaseFirestore.collection("TextStatus")
                    .document(documentID).collection("Comments")
                    .orderBy("flag",Query.Direction.DESCENDING);

            FirestoreRecyclerOptions<Model_Comment> objectOptions =
                    new FirestoreRecyclerOptions.Builder<Model_Comment>()
                            .setQuery(objectQuery,Model_Comment.class).build();

            objectGetTextCommentAdapter = new TextCommentAdapter(objectOptions);
            objectRecyclerView.setAdapter(objectGetTextCommentAdapter);
            objectRecyclerView.setLayoutManager(new LinearLayoutManager(this));




        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        try {

            objectGetTextCommentAdapter.startListening();

        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

            objectGetTextCommentAdapter.stopListening();

        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void addcomment()
    {
        try {
            if(objectFirebaseAuth!=null && !commentET.getText().toString().isEmpty())
            {
                currentLoggedinUser=objectFirebaseAuth.getCurrentUser().getEmail();
                objectFirebaseFirestore =FirebaseFirestore.getInstance();
                objectDocumentReference = objectFirebaseFirestore.collection("UserProfileData")
                        .document(currentLoggedinUser);
                objectDocumentReference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                String profileUrl=documentSnapshot.getString("profileimageurl");
                                currentLoggedinUsername=documentSnapshot.getString("username");

                                Map<String,Object> objectMap=new HashMap<>();
                                objectMap.put("commentperson",currentLoggedinUser);
                                objectMap.put("username",currentLoggedinUsername);
                                objectMap.put("comment",commentET.getText().toString());
                                objectMap.put("profilepicurl",profileUrl);
                                objectMap.put("currentdatetime",getCurrentDate());
                                objectMap.put("flag",1);
                                objectFirebaseFirestore.collection("TextStatus")
                                        .document(documentID).collection("Comments")
                                        .add(objectMap)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(TextCommentPage.this, "Comment Added", Toast.LENGTH_SHORT).show();
                                                commentET.setText("");
                                                objectCollectionReference=objectFirebaseFirestore.collection("TextStatus")
                                                        .document(documentID)
                                                        .collection("Comments");
                                                objectCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                        int commentCount=queryDocumentSnapshots.size();

                                                    objectDocumentReference=objectFirebaseFirestore.collection("TextStatus")
                                                            .document(documentID);
                                                    objectDocumentReference.update("noofcomments",commentCount);

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(TextCommentPage.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            }
                        });
            }
            else if(objectFirebaseAuth!=null){
                Toast.makeText(this,"No user is logged in", Toast.LENGTH_SHORT).show();
            }
            else if (commentET.getText().toString().isEmpty()) {

                Toast.makeText(this, "Commentbox is Empty, Please add comment", Toast.LENGTH_SHORT).show();


            }

        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }














    private void AttachJavaObjectsToXML()
    {
        try {
            objectFirebaseAuth=FirebaseAuth.getInstance();
            objectFirebaseFirestore=FirebaseFirestore.getInstance();

            objectRecyclerView = findViewById(R.id.textstatus_commentRV);
            commentET = findViewById(R.id.textstatus_commentET);
            SendCommentBtn = findViewById(R.id.textStatus_commentSendBtn);


        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }





































}