package com.project.seedle.Fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.seedle.Activities.MainContentPage;
import com.project.seedle.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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

    private Uri selectedImageUri;

    private Date currentDate;
    private SimpleDateFormat objectSimpleDateFormat;

    private String currentLoggedInUserEmail;
    private String getCurrentLoggedInUserProfileUrl;

    //Firebase Objects

    private FirebaseAuth objectFirebaseAuth;
    private FirebaseFirestore objectFirebaseFirestore;
    private DocumentReference objectDocumentReference;
    private StorageReference objectStorageReference;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        objectView=inflater.inflate(R.layout.fragment_add_image_thought, container, false);
        ConnectJavaViewToXMLView();

        objectStorageReference= FirebaseStorage.getInstance().getReference("ImageStatusFolder");
        choosePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMobileGallery();

            }
        });

        publishStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishStatus();
            }
        });


        return objectView;
    }


    private  void publishStatus()
    {
        try {

            if(selectedImageUri!=null)
            {
                objectFirebaseAuth=FirebaseAuth.getInstance();
                objectFirebaseFirestore=FirebaseFirestore.getInstance();

                if(objectFirebaseAuth!=null && !statusET.getText().toString().isEmpty())
                {
                    objectProgressBar.setVisibility(View.VISIBLE);
                    publishStatus.setEnabled(false);

                    currentLoggedInUserEmail=objectFirebaseAuth.getCurrentUser().getEmail();
                    objectDocumentReference=objectFirebaseFirestore.collection("UserProfileData")
                            .document(currentLoggedInUserEmail);
                    objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            getCurrentLoggedInUserProfileUrl=documentSnapshot.getString("profileimageurl");



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed to get Profile Url:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            objectProgressBar.setVisibility(View.INVISIBLE);
                            publishStatus.setEnabled(true);

                        }
                    });
                    String statusImageToBeStore=System.currentTimeMillis()+"."+getExtension(selectedImageUri);
                    StorageReference imgRef=objectStorageReference.child(statusImageToBeStore);
                    UploadTask objectUploadTask=imgRef.putFile(selectedImageUri);
                    objectUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();

                            }
                            return imgRef.getDownloadUrl();

                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful())
                            {
                                Map<String,Object> statusMap=new HashMap<>();
                                statusMap.put("currentdatetime", getCurrentDate());
                                statusMap.put("useremail", currentLoggedInUserEmail);

                                statusMap.put("profileurl", getCurrentLoggedInUserProfileUrl);
                                statusMap.put("status", statusET.getText().toString());
                                statusMap.put("noofhaha", 0);
                                statusMap.put("nooflove", 0);
                                statusMap.put("nofsad", 0);
                                statusMap.put("noofcomments", 0);

                                statusMap.put("currentflag", "none");
                                statusMap.put("statusimageurl",task.getResult().toString());
                                objectFirebaseFirestore.collection("ImageStatus")
                                        .document(String.valueOf(System.currentTimeMillis()))
                                        .set(statusMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                objectDocumentReference=objectFirebaseFirestore.collection("UserProfileData")
                                                        .document(currentLoggedInUserEmail);
                                                objectDocumentReference.get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                long totalNumberOfImageStatus=documentSnapshot.getLong("noofimagestatus");
                                                                totalNumberOfImageStatus++;
                                                                Map<String,Object> updatedDataMap= new HashMap<>();
                                                                updatedDataMap.put("noofimagestatus",totalNumberOfImageStatus);

                                                                objectFirebaseFirestore.collection("UserProfileData")
                                                                        .document(currentLoggedInUserEmail)
                                                                        .update(updatedDataMap);

                                                                Toast.makeText(getContext(), "ImageStatus Published", Toast.LENGTH_SHORT).show();
                                                                objectProgressBar.setVisibility(View.INVISIBLE);
                                                                publishStatus.setEnabled(true);
                                                                startActivity(new Intent(getContext(),MainContentPage.class));
                                                                getActivity().finish();

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getContext(), "Failed to update no of image status"+e.getMessage(), Toast.LENGTH_SHORT).show();

                                                            }
                                                        });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }

                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Please check if logged in or image status decription is provided", Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(getContext(), "Please choose an image first", Toast.LENGTH_SHORT).show();
            }




        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),"ImageThought:"+e.getMessage(),Toast.LENGTH_SHORT).show();

        }

    }

    private String getExtension(Uri selectedImageUri) {

        try {
            ContentResolver objectContentResolver=getActivity().getContentResolver();
            MimeTypeMap objectMimeTypeMap=MimeTypeMap.getSingleton();

            return objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(selectedImageUri));


        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),"ImageThought:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return "No Extension";

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
            Toast.makeText(getContext(),"ImageThought:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return "No Date";



        }


    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null && data.getData() !=null)
        {
            selectedImageUri = data.getData();
            statusImageView.setImageURI(selectedImageUri);


        }
        else {
            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }
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