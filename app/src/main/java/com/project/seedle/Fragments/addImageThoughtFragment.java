package com.project.seedle.Fragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.seedle.Activities.MainContentPage;
import com.project.seedle.CropActivity;
import com.project.seedle.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class addImageThoughtFragment extends Fragment {

    public int flag=1;

    public addImageThoughtFragment() {
        // Required empty public constructor
    }
    //Java Object for XML Objects
    private ImageView choosePictureBtn,statusImageView;
    private EditText statusET;


    public String isVerified;
    private TextView publishStatus,goBackBtn;
    private ProgressBar objectProgressBar;

    public String User_Name,EMAIL;
    public String eMail;




    //Class Variables
    private View objectView;
    private int PreCode=1000;

    private Uri selectedImageUri,compressedUri;

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

        objectFirebaseFirestore=FirebaseFirestore.getInstance();

        objectStorageReference= FirebaseStorage.getInstance().getReference("ImageStatusFolder");

        objectFirebaseAuth=FirebaseAuth.getInstance();
        eMail=objectFirebaseAuth.getCurrentUser().getEmail();

        if(Objects.equals(eMail, "cosmicriderrr@gmail.com") || Objects.equals(eMail, "shabanaofficial321@gmail.com") || Objects.equals(eMail, "sairaseira187@gmail.com"))
        {

            isVerified = "verified";


        }
        else {
            isVerified = "notverified";
        }
        choosePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMobileGallery();

            }
        });

        FirebaseAuth object = FirebaseAuth.getInstance();
        EMAIL = object.getCurrentUser().getEmail();


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

                if(objectFirebaseAuth!=null)
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
                                statusMap.put("username", User_Name);
                                statusMap.put("profileurl", getCurrentLoggedInUserProfileUrl);
                                statusMap.put("status", statusET.getText().toString());
                                statusMap.put("noofhaha", 0);
                                statusMap.put("nooflove", 0);
                                statusMap.put("flag",flag);
                                statusMap.put("nofsad", 0);
                                statusMap.put("noofcomments", 0);
                                statusMap.put("verified",isVerified);
                                statusMap.put("currentflag", "none");
                                statusMap.put("statusimageurl",compressedUri);
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
                    Toast.makeText(getContext(), "Please check if logged in", Toast.LENGTH_SHORT).show();
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
            objectSimpleDateFormat=new SimpleDateFormat("hh:mm a  dd-MM-yyyy", Locale.getDefault());
            return objectSimpleDateFormat.format(currentDate);


        }
        catch (Exception e)
        {
            Toast.makeText(getContext(),"ImageThought:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return "No Date";



        }


    }











    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String croppedImageUriString = data.getStringExtra("croppedImageUri");
            Uri croppedImageUri = Uri.parse(croppedImageUriString);

            // Do something with the cropped image Uri, e.g. set it to an ImageView
            statusImageView.setImageURI(croppedImageUri);
            compressedUri = compressImage(croppedImageUri);
        } else if (data == null || data.getData() == null) {
            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        } else {
            selectedImageUri = data.getData();
            Intent intent = new Intent(getContext(), CropActivity.class);
            intent.putExtra("imageUri", selectedImageUri.toString());
            intent.putExtra("content","status");
            startActivityForResult(intent, 1);
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

    private Uri compressImage(Uri imageUri) {
        try {
            // Get the original bitmap from the image URI
            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

            // Calculate the new dimensions for the compressed bitmap
            int width = originalBitmap.getWidth();
            int height = originalBitmap.getHeight();
            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = 1024;
                height = (int) (width / bitmapRatio);
            } else {
                height = 1024;
                width = (int) (height * bitmapRatio);
            }

            // Create a new bitmap with the new dimensions
            Bitmap compressedBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true);

            // Create a file to store the compressed image
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Write the compressed bitmap to the file
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
            outputStream.flush();
            outputStream.close();

            // Return the URI of the compressed image file
            return Uri.fromFile(imageFile);


        } catch (IOException e) {
            Log.e(TAG, "Failed to compress image: " + e.getMessage());
            return null;
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