package com.project.seedle.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.seedle.Activities.MainContentPage;
import com.project.seedle.R;


public class ProfileFragment extends Fragment {



    public ProfileFragment() {
        // Required empty public constructor
    }


    private View parent;

    Dialog objectDialogWait;

    private FirebaseFirestore objectFirebaseFirestore;

    private FirebaseAuth objectFirebaseAuth;

    private DocumentReference objectDocumentReference;



    private ImageView gobackBtn,profileimage,backgroundpic;

    private TextView username,useremail,TextStatuscount,imageStatusCount,gender,usercity,usercountry;
    private Button bioBtn;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent =  inflater.inflate(R.layout.fragment_profile, container, false);

        objectDialogWait = new Dialog(getContext());
        objectDialogWait.setContentView(R.layout.please_wait_dialogue);

        initializeobjects();
        return parent;
    }


    private void loadUserData()
    {
        try {
            if(objectFirebaseAuth!=null)
            {
                obj

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
            backgroundpic = parent.findViewById(R.id.cover_photo);

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
                    startActivity(new Intent(getContext(), MainContentPage.class));
                }
            });


        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}