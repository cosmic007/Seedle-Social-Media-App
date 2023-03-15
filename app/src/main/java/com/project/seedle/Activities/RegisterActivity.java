package com.project.seedle.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.circularreveal.CircularRevealLinearLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.seedle.R;

import java.sql.Struct;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    //xml variables
    private CircleImageView profilepic;
    private EditText userName,userEmail,userPassword,userConfirmPassword;

    private TextView userDob;
    private Button registerbtn;
    private RadioGroup objectRadioGroup;
    private RadioButton objectRadioButton;

    //class variable

    private Uri profileimageURL;
    private static int REQUEST_CODE=1;

    private String finalpassword;
    private int radioID;

    //Firebase Variables
    private FirebaseFirestore objectFirebaseFirestore;
    private FirebaseAuth objectFirebaseAuth;
    private StorageReference objectStorageReference;

    private DatePickerDialog.OnDateSetListener objectOnDateSetListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        attachJavatoXmlObjects();

        objectFirebaseFirestore= FirebaseFirestore.getInstance();
        objectFirebaseAuth= FirebaseAuth.getInstance();
        objectStorageReference= FirebaseStorage.getInstance().getReference("ImageFolder");
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserAccount();
            }
        });


    }

    private void createUserAccount()
    {
        try
        {
            if (objectFirebaseAuth.getCurrentUser()!=null)
            {
                objectFirebaseAuth.signOut();

            }
            if(objectFirebaseAuth.getCurrentUser()==null
                    && !userName.getText().toString().isEmpty()
                    && !userEmail.getText().toString().isEmpty()
                    && profileimageURL!=null
                    && !userPassword.getText().toString().isEmpty()) {
                if (userPassword.getText().toString().equals(userConfirmPassword.getText().toString()))
                {
                    Toast.makeText(this,"Registering the user",Toast.LENGTH_SHORT).show();
                    finalpassword=userPassword.getText().toString();
                    objectFirebaseAuth.createUserWithEmailAndPassword(
                            userEmail.getText().toString(),finalpassword
                    ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            uploadUserDataToFirebase();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this,"Failed to create user:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(this,"Password did not match",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this,"Please check user data fields or user profile image",Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception e)
        {
            Toast.makeText(this,"RegisterPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadUserDataToFirebase()
    {
        try
        {
            if(profileimageURL!=null)
            {
                String imageName=userName.getText().toString()+"."+getExtension(profileimageURL);
                final StorageReference imageRef=objectStorageReference.child(imageName);

                Toast.makeText(this,"Uploading user profile picture",Toast.LENGTH_SHORT).show();
                UploadTask objectUploadTask = imageRef.putFile(profileimageURL);
                objectUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this,"Uploading User Information",Toast.LENGTH_SHORT).show();
                            Map<String,Object> objectMap=new HashMap<>();
                            objectMap.put("profileimageurl",task.getResult().toString());
                            objectMap.put("username",userName.getText().toString());
                            objectMap.put("useremail",userEmail.getText().toString());
                            objectMap.put("dob",userDob.getText().toString());
                            objectMap.put("userpassword",finalpassword);
                            radioID=objectRadioGroup.getCheckedRadioButtonId();
                            objectRadioButton=findViewById(radioID);
                            objectMap.put("noofemotions",0);
                            objectMap.put("gender",objectRadioButton.getText().toString());
                            objectMap.put("noofimagestatus",0);
                            objectMap.put("noftextstatus",0);
                            objectMap.put("usercity","NA");
                            objectMap.put("usercountry","NA");
                            objectMap.put("userbio","NA");

                            objectFirebaseFirestore.collection("UserProfileData")
                                    .document(userEmail.getText().toString())
                                    .set(objectMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegisterActivity.this,"User is registered",Toast.LENGTH_SHORT).show();
                                            if(objectFirebaseAuth.getCurrentUser()!=null)
                                            {
                                                objectFirebaseAuth.signOut();
                                            }
                                            startActivity(new Intent(RegisterActivity.this,LoginPage.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this,"Failed to create user and upload data:"+e.getMessage(),Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        }
                        else if(!task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this,"Error:"+task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            else
            {
                Toast.makeText(this,"Please Choose a Profile Image",Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(this,"RegisterPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }



    private void  attachJavatoXmlObjects()
    {
        try {
            profilepic=findViewById(R.id.Registerpage_userprofile);
            userName=findViewById(R.id.Resgisterpage_username);
            userEmail=findViewById(R.id.Resgisterpage_emailid);
            userPassword=findViewById(R.id.Registerpage_password);
            userConfirmPassword=findViewById(R.id.Registerpage_confirm_password);
            userDob=findViewById(R.id.Resgisterpage_dob);
            registerbtn=findViewById(R.id.RegisterPage_RegisterBtn);
            objectRadioGroup=findViewById(R.id.RegisterPage_Radiogroup);

            profilepic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseImageFromGallery();
                }
            });
            userDob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseDOB();
                }
            });


        }
        catch (Exception e)
        {
            Toast.makeText(this,"RegisterPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    private String getExtension(Uri uri)
    {
        try
        {
            ContentResolver objectContentResolver = getContentResolver();
            MimeTypeMap objectMimeTypeMap = MimeTypeMap.getSingleton();

            return objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(uri));
        }
        catch (Exception e)
        {
            Toast.makeText(this,"RegisterPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;

        }


    }


    private void chooseDOB()
    {

        Calendar ObjectCalendar=Calendar.getInstance();
        int year= ObjectCalendar.get(Calendar.YEAR);
        int month=ObjectCalendar.get(Calendar.MONTH);
        int day=ObjectCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog ObjectDatePickerDialog= new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, objectOnDateSetListener,year,month,day);
        ObjectDatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ObjectDatePickerDialog.show();
        objectOnDateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                userDob.setText(dayOfMonth+"-"+month+"-"+year);
            }
        };

    }

    private void chooseImageFromGallery()
    {
        try {

            openMobileGallery();

        }
        catch (Exception e)
        {
            Toast.makeText(this,"RegisterPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    private void openMobileGallery(){
        try {

            Intent galleryIntent= new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/");

            startActivityForResult(galleryIntent,REQUEST_CODE);


        }
        catch (Exception e)
        {
            Toast.makeText(this,"RegisterPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData()!=null && data != null)
        {
            profileimageURL=data.getData();
            profilepic.setImageURI(profileimageURL);
        }
        else {
            Toast.makeText(this,"Image Not Selected",Toast.LENGTH_SHORT).show();
        }
    }
}