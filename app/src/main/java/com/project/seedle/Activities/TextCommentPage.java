package com.project.seedle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
                                objectFirebaseFirestore.collection("TextStatus")
                                        .document(documentID).collection("Comments")
                                        .add(objectMap)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(TextCommentPage.this, "Comment Added", Toast.LENGTH_SHORT).show();
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