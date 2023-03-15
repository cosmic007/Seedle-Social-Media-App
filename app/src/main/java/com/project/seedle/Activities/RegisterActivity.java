package com.project.seedle.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.circularreveal.CircularRevealLinearLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.seedle.R;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    //xml variables
    private CircleImageView profilepic;
    private EditText userName,userEmail,userPassword,userConfirmPassword;

    private TextView userDob;
    private Button registerbtn;
    private RadioGroup objectRadioGroup;

    //class variable

    private Uri profileimageURL;
    private static int REQUEST_CODE=1;

    private String finalpassword;

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

    }

    private void createUserAccount(){
        try
        {
            if (objectFirebaseAuth.getCurrentUser()!=null)
            {
                objectFirebaseAuth.signOut();

            }
            else if(objectFirebaseAuth.getCurrentUser()!=null && userName.getText().toString().isEmpty()
            && !userEmail.getText().toString().isEmpty() && !userPassword.getText().toString().isEmpty()) {
                if (userPassword.getText().toString().equals(userConfirmPassword.getText().toString()))
                {
                    finalpassword=userPassword.getText().toString();
                    objectFirebaseAuth.createUserWithEmailAndPassword(
                            userEmail.getText().toString(),finalpassword
                    ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

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

    private void chooseDOB()
    {
        try{
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
        catch (Exception e)
        {
            Toast.makeText(this,"Registerpage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
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