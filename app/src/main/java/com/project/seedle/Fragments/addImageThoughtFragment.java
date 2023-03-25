package com.project.seedle.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.seedle.Activities.MainContentPage;
import com.project.seedle.R;


public class addImageThoughtFragment extends Fragment {

    public addImageThoughtFragment() {
        // Required empty public constructor
    }
    //Java Object for XML Objects
    private ImageView statusImageView;
    private EditText statusET;
    private TextView publishStatus,goBackBtn,choosePictureBtn;
    private ProgressBar objectProgressBar;



    //Class Variables
    private View objectView;
    private int PreCode=1000;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        objectView=inflater.inflate(R.layout.fragment_add_image_thought, container, false);
        ConnectJavaViewToXMLView();
        return objectView;
    }


    private void openMobileGallery()
    {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


            startActivityForResult(intent,PreCode);

        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), "ImageThought:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }



    private void ConnectJavaViewToXMLView()
    {
        try {
            statusImageView=objectView.findViewById(R.id.Frag_addImageThoughtIV);
            statusET=objectView.findViewById(R.id.Frag_addImageThoughtET);
            publishStatus=objectView.findViewById(R.id.FragImageTHoughts_PublishImageBtn);
            goBackBtn=objectView.findViewById(R.id.FragImageTHoughts_BackBtn);
            choosePictureBtn=objectView.findViewById(R.id.Frag_addImageThoughtBtn);
            objectProgressBar=objectView.findViewById(R.id.Frag_addImageThought_ProgressBar);

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
            Toast.makeText(getContext(), "ImageThought:"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }






}