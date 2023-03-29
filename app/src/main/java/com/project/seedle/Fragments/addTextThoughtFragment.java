package com.project.seedle.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.seedle.Activities.MainContentPage;
import com.project.seedle.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class addTextThoughtFragment extends Fragment {

    public String User_Name;
    public long count;

    
    
    
    public interface GetURLInterface
    {
        void getProfileUrl(String profileUrlValue);
    }



    public addTextThoughtFragment() {
        // Required empty public constructor
    }

    //class variable
    
    private String profileURL;
    public String EMAIL;
    private View objectView;
    private Date currentDate;
    private SimpleDateFormat objectSimpleDateFormat;


    //Java object for XML Views
    private EditText statusET;
    private TextView publishStatusBtn,rewriteStatusBtn,goBackBtn;
    private ProgressBar objectProgressBar;

    //Firebase Object
    private FirebaseAuth objectFirebaseAuth;
    private FirebaseFirestore objectFirebaseFirestore;
    private DocumentReference objectDocumentReference;

            




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        objectView=inflater.inflate(R.layout.fragment_add_text_thought, container, false);
        ConnectJavaViewToXMLView();


        objectFirebaseAuth=FirebaseAuth.getInstance();
        objectFirebaseFirestore=FirebaseFirestore.getInstance();
        EMAIL=objectFirebaseAuth.getCurrentUser().getEmail();


        CollectionReference cRef = objectFirebaseFirestore.collection("Count");
        DocumentReference dRef = cRef.document("Countdata");

        dRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                     String Count =documentSnapshot.getString("nooftextstatus");
                     count= Long.parseLong(Count);
                     count++;
                     String updatecount= String.valueOf(count);
                    Map<String, Object> objectMap = new HashMap<>();
                    objectMap.put("nooftextstatus", updatecount);
                    objectFirebaseFirestore.collection("Count")
                            .document("Countdata").update(objectMap);


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

        publishStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                addStatus(new GetURLInterface() {
                    @Override
                    public void getProfileUrl(String profileUrlValue) {
                        profileURL=profileUrlValue;
                        publishStatus();
                        
                    }
                });
                

            }
        });





        return objectView;
    }







    
    private void publishStatus()
    {
        try {
            if(!statusET.getText().toString().isEmpty()) {

                final String currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail();
                Map<String, Object> statusData = new HashMap<>();


                String name=User_Name;


                statusData.put("currentdatetime", getCurrentDate());
                String COUNT= String.valueOf(count);
                statusData.put("postno",COUNT);
                statusData.put("useremail", currentLoggedInUser);
                statusData.put("username",name);
                statusData.put("profileurl", profileURL);
                statusData.put("status", statusET.getText().toString());
                statusData.put("noofhaha", 0);
                statusData.put("nooflove", 0);
                statusData.put("nofsad", 0);
                statusData.put("noofcomments", 0);
                statusData.put("currentflag", "none");
                objectFirebaseFirestore.collection("TextStatus")
                        .document(String.valueOf(System.currentTimeMillis()))
                        .set(statusData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(getContext(), "Status Published", Toast.LENGTH_SHORT).show();
                                objectProgressBar.setVisibility(View.INVISIBLE);

                                objectDocumentReference = objectFirebaseFirestore.collection("UserProfileData")
                                        .document(currentLoggedInUser);
                                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        long noOfTextStatus = documentSnapshot.getLong("noftextstatus");
                                        noOfTextStatus++;

                                        Map<String, Object> objectMap = new HashMap<>();
                                        objectMap.put("noftextstatus", noOfTextStatus);

                                        objectFirebaseFirestore.collection("UserProfileData")
                                                .document(currentLoggedInUser).update(objectMap);


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                                publishStatusBtn.setEnabled(true);
                                getActivity().finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
            else {
                Toast.makeText(getContext(), "Please enter your thoughts in status box", Toast.LENGTH_SHORT).show();
            }
                
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),"TextThought:"+e.getMessage(),Toast.LENGTH_SHORT).show();

        }
        
    }


    private void addStatus(final GetURLInterface objectGetUrlInterface)
    {
        try{

            objectFirebaseAuth=FirebaseAuth.getInstance();
            objectFirebaseFirestore=FirebaseFirestore.getInstance();

            if(objectFirebaseAuth!=null && !statusET.getText().toString().isEmpty())
            {
                objectProgressBar.setVisibility(View.VISIBLE);
                publishStatusBtn.setEnabled(false);
                
                String currentUserEmail=objectFirebaseAuth.getCurrentUser().getEmail();
                objectDocumentReference=objectFirebaseFirestore.collection("UserProfileData")
                        .document(currentUserEmail);
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        objectGetUrlInterface.getProfileUrl(documentSnapshot.getString("profileimageurl"));
                        
                        
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Fails to get profile URL", Toast.LENGTH_SHORT).show();
                        objectProgressBar.setVisibility(View.INVISIBLE);
                        publishStatusBtn.setEnabled(true);
                        
                    }
                });
                


            }
            else
            {
                Toast.makeText(getContext(),"Please check user login or please add text into status",Toast.LENGTH_SHORT).show();
            }



        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),"TextThought:"+e.getMessage(),Toast.LENGTH_SHORT).show();

        }

    }
    private String getCurrentDate()
    {
        try {
            currentDate= Calendar.getInstance().getTime();
            objectSimpleDateFormat=new SimpleDateFormat("HH:mm:ss dd-MMM-yyyy");
            return objectSimpleDateFormat.format(currentDate);


        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),"TextThought:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return "No Date";



        }


    }

    private void ConnectJavaViewToXMLView()
    {
        try {

            statusET=objectView.findViewById(R.id.FragAddTextThoughtET);
            publishStatusBtn=objectView.findViewById(R.id.FragAddTextTHoughts_PublishBtn);
            rewriteStatusBtn=objectView.findViewById(R.id.FragAddTextTHoughts_rewritePostBtn);
            goBackBtn=objectView.findViewById(R.id.FragAddTextTHoughts_BackBtn);
            objectProgressBar=objectView.findViewById(R.id.Frag_addTextThought_progressBar);
            rewriteStatusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    statusET.setText("");

                }
            });
            goBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), MainContentPage.class));
                    getActivity().finish();
                }
            });




        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),"TextThought:"+e.getMessage(),Toast.LENGTH_SHORT).show();



        }
    }













}