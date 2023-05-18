package com.project.seedle.Fragments;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.project.seedle.R;

import java.security.spec.ECField;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingFragment extends Fragment {

    public interface GetUserInfo
    {
        public default void getUserInfo(String urlofPP, String userName)
        {

        }
    }


    private View parent;

    private FirebaseAuth objectFirebaseAuth;
    private FirebaseFirestore objectFirebaseFirestore;

    private Uri newprofilepicuri;

    private static int REQUEST_CODE =1;

    private boolean checkforchangedp = false;

    private String extractedUrl,extractedName;

    private Dialog objectDialogWait;

    private TextView updatedpbutton;

    private DocumentReference objectDocumentReference;

    private FirebaseStorage objectFirebaseStorage;

    private StorageReference objectStorageReference;

    private CircleImageView profileIV;

    private Button updateUserinfobtn;

    private EditText usernameET,userbioET,userCountryET,userCityET;


    public SettingFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parent = inflater.inflate(R.layout.fragment_setting, container, false);

        objectDialogWait = new Dialog(getContext());
        objectDialogWait.setContentView(R.layout.please_wait_dialogue);

        initializeobject();
        loadProfileInfromationAtStart(new GetUserInfo() {
            @Override
            public void getUserInfo(String urlofPP, String userName) {

                extractedName = userName;
                extractedUrl=urlofPP;

            }
        });


        updatedpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        updateUserinfobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpdateUserinfo();
            }
        });
        return parent;
    }

    private String getExtension(Uri uri)
    {
        ContentResolver objectContentResolver= getActivity().getContentResolver();
        MimeTypeMap objectMimeTypeMap = MimeTypeMap.getSingleton();
        return objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(uri));
    }

    private void setUpdateUserinfo()
    {
        try {

            if(checkforchangedp)
            {
                objectDialogWait.show();
                objectStorageReference = FirebaseStorage.getInstance().getReference("ImageFolder");

                if(newprofilepicuri!=null)
                {
                    String imageName=extractedName+"."+getExtension(newprofilepicuri);
                    StorageReference imgRef = objectStorageReference.child(imageName);
                    UploadTask uploadTask =imgRef.putFile( newprofilepicuri);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                            if(!task.isSuccessful())
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
                                Map<String,Object > objectMap = new HashMap<>();

                                objectMap.put("profileimageurl",task.getResult().toString());

                                objectMap.put("username",usernameET.getText().toString());
                                objectMap.put("userbio",userbioET.getText().toString());
                                objectMap.put("usercity",userCityET.getText().toString());
                                objectMap.put("usercountry",userCountryET.getText().toString());

                                objectFirebaseFirestore.collection("UserProfileData").document(objectFirebaseAuth.getCurrentUser().getEmail())
                                        .update(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                objectDialogWait.dismiss();
                                                Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                objectDialogWait.dismiss();
                                                Toast.makeText(getContext(), "Failed to update", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                            }
                            else if (!task.isSuccessful()){
                                objectDialogWait.dismiss();
                                Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else
                {
                    Toast.makeText(getContext(), "Please choose an image", Toast.LENGTH_SHORT).show();
                }


            } else if (!checkforchangedp) {

                objectDialogWait.show();
                Map<String,Object > objectMap = new HashMap<>();
                objectMap.put("username",usernameET.getText().toString());
                objectMap.put("userbio",userbioET.getText().toString());
                objectMap.put("usercity",userCityET.getText().toString());
                objectMap.put("usercountry",userCountryET.getText().toString());

                objectFirebaseFirestore.collection("UserProfileData").document(objectFirebaseAuth.getCurrentUser().getEmail())
                        .update(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                objectDialogWait.dismiss();
                                Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                objectDialogWait.dismiss();
                                Toast.makeText(getContext(), "Failed to update", Toast.LENGTH_SHORT).show();

                            }
                        });







            }

        }
        catch ( Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getData()!=null && data !=null)
        {
            newprofilepicuri = data.getData();
            profileIV.setImageURI(newprofilepicuri);
            checkforchangedp = true;
        }
        else
        {
            Toast.makeText(getContext(), "No image was chosen", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGallery()
    {
        try {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


            startActivityForResult(intent, REQUEST_CODE);




        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void loadProfileInfromationAtStart(GetUserInfo objectGetUserinfo)
    {
        try {

            if(objectFirebaseAuth!=null)
            {
                objectDialogWait.show();
                objectDocumentReference = objectFirebaseFirestore.collection("UserProfileData").document(objectFirebaseAuth.getCurrentUser().getEmail());
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try {
                            usernameET.setText(documentSnapshot.getString("username"));
                            userbioET.setText(documentSnapshot.getString("userbio"));
                            userCityET.setText(documentSnapshot.getString("usercity"));
                            userCountryET.setText(documentSnapshot.getString("usercountry"));

                            Glide.with(getContext()).load(documentSnapshot.getString("profileimageurl"))
                                    .into(profileIV);

                            objectGetUserinfo.getUserInfo(documentSnapshot.getString("profileimageurl"),documentSnapshot.getString("username"));

                            objectDialogWait.dismiss();


                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                            objectDialogWait.dismiss();

                        }




                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        objectDialogWait.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
            else
            {
                Toast.makeText(getContext(), "User not online", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void initializeobject()
    {
        try {

            objectFirebaseAuth = FirebaseAuth.getInstance();
            objectFirebaseFirestore = FirebaseFirestore.getInstance();
            profileIV = parent.findViewById(R.id.USERPROFILE);

            updatedpbutton =parent.findViewById(R.id.selectpic);
            updateUserinfobtn =parent.findViewById(R.id.Savebtn);

            usernameET = parent.findViewById(R.id.setusername);
            userbioET = parent.findViewById(R.id.setbio);

            userCityET =parent.findViewById(R.id.setcity);
            userCountryET = parent.findViewById(R.id.setcountry);

            profileIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGallery();
                }
            });




        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}