package com.project.seedle.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.seedle.Activities.MainContentPage;
import com.project.seedle.R;

import java.util.Objects;


public class ProfileFragment extends Fragment {



    public ProfileFragment() {
        // Required empty public constructor
    }


    private View parent;

    private Dialog objectDialogWait;

    private String currentLoggedInUser;

    private FirebaseFirestore objectFirebaseFirestore;

    private FirebaseAuth objectFirebaseAuth;

    private DocumentReference objectDocumentReference;



    private ImageView gobackBtn,profileimage,backgroundpic;

    private TextView username,useremail,TextStatuscount,imageStatusCount,gender,usercity,usercountry,bio;
    private Button bioBtn;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent =  inflater.inflate(R.layout.fragment_profile, container, false);

        objectDialogWait = new Dialog(getContext());
        objectDialogWait.setContentView(R.layout.please_wait_dialogue);

        initializeobjects();
        loadUserData();
        return parent;
    }


    private void loadUserData()
    {
        try {
            if(objectFirebaseAuth!=null)
            {
                objectDialogWait.show();
                currentLoggedInUser = Objects.requireNonNull(objectFirebaseAuth.getCurrentUser()).getEmail();

                objectDocumentReference = objectFirebaseFirestore.collection("UserProfileData").document(currentLoggedInUser);
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        try {
                            String linkofprofileurl = documentSnapshot.getString("profileimageurl");
                            Glide.with(getContext()).load(linkofprofileurl).into(profileimage);
                            String Username = documentSnapshot.getString("username");
                            username.setText(Username);
                            useremail.setText(currentLoggedInUser);
                            long ntextstatus = documentSnapshot.getLong("noftextstatus");
                            long nimagestatus = documentSnapshot.getLong("noofimagestatus");

                            TextStatuscount.setText(String.valueOf(ntextstatus));
                            imageStatusCount.setText(String.valueOf(nimagestatus));

                            gender.setText(documentSnapshot.getString("gender"));
                            usercity.setText(documentSnapshot.getString("usercity"));
                            usercountry.setText(documentSnapshot.getString("usercountry"));
                            bio.setText(documentSnapshot.getString("userbio"));
                            objectDialogWait.dismiss();

                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getContext(), "Failed to load profile info", Toast.LENGTH_SHORT).show();

                        }




                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
            else {
                Toast.makeText(getContext(), "User not online", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void initializeobjects()
    {
        try {

            objectFirebaseAuth = FirebaseAuth.getInstance();
            objectFirebaseFirestore = FirebaseFirestore.getInstance();
            profileimage = parent.findViewById(R.id.profile_image);

            bio = parent.findViewById(R.id.bio);

            username =parent.findViewById(R.id.profile_name);
            useremail = parent.findViewById(R.id.mailid);
            TextStatuscount = parent.findViewById(R.id.textStatusCount);
            imageStatusCount = parent.findViewById(R.id.imageStatusCount);
            gender = parent.findViewById(R.id.gender);
            usercity = parent.findViewById(R.id.location);
            usercountry = parent.findViewById(R.id.country);
            gobackBtn = parent.findViewById(R.id.goback);

            gobackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requireActivity().finish();
                }
            });





        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }
}