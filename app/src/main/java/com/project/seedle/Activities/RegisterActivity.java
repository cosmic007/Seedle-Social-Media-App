package com.project.seedle.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.seedle.Admin;
import com.project.seedle.CropActivity;
import com.project.seedle.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://seedle-2a24f-default-rtdb.firebaseio.com/");

    //xml variables
    private CircleImageView profilepic;

    private Admin admin = new Admin();

    public String IMAGEName;

    private EditText userName,userEmail,userPassword,userConfirmPassword,mobile;

    private TextView userDob;
    private Button registerbtn;
    private RadioGroup objectRadioGroup;
    private RadioButton objectRadioButton;

    //class variable

    private Uri profileimageURL,compressedUri;
    private static int REQUEST_CODE=1;

    private String finalpassword;

    private int radioID;
    private Dialog objectDialog;

    //Firebase Variables
    private FirebaseFirestore objectFirebaseFirestore;
    private FirebaseAuth objectFirebaseAuth;
    private StorageReference objectStorageReference;

    private DatePickerDialog datePickerDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objectDialog= new Dialog(this);
        objectDialog.setContentView(R.layout.please_wait_dialogue);

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
            String admin1=admin.getAdmin1();
            String admin2=admin.getAdmin2();
            String admin3=admin.getAdmin3();

            if(objectFirebaseAuth.getCurrentUser()==null
                    && !userName.getText().toString().isEmpty()
                    && !userName.getText().toString().equals(admin1)
                    && !userName.getText().toString().equals(admin2)
                    && !userName.getText().toString().equals(admin3)
                    && !userEmail.getText().toString().isEmpty()
                    && !userPassword.getText().toString().isEmpty()) {
                if (userPassword.getText().toString().equals(userConfirmPassword.getText().toString()))
                {


                    objectDialog.show();
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
                            objectDialog.dismiss();
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
                Toast.makeText(this,"Please make sure your profile picture is selected or You have no rights to select this Username",Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this,"RegisterPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadUserDataToFirebase()
    {
        try
        {
            if(profileimageURL!=null)
            {
                String imageName=userName.getText().toString()+"."+getExtension(compressedUri);
                final StorageReference imageRef=objectStorageReference.child(imageName);




                Toast.makeText(this,"Uploading user profile picture",Toast.LENGTH_SHORT).show();
                UploadTask objectUploadTask = imageRef.putFile(compressedUri);
                objectUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            objectDialog.dismiss();
                            Toast.makeText(RegisterActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                            throw task.getException();
                        }

                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {


                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {

                            Uri downloaduri = task.getResult();
                            String profileurl= downloaduri.toString();


                            Toast.makeText(RegisterActivity.this,"Uploading User Information",Toast.LENGTH_SHORT).show();
                            Map<String,Object> objectMap=new HashMap<>();
                            objectMap.put("profileimageurl",profileurl);
                            objectMap.put("username",userName.getText().toString());
                            objectMap.put("useremail",userEmail.getText().toString());
                            objectMap.put("mobile",mobile.getText().toString());
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
                                            objectDialog.dismiss();
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
                                            objectDialog.dismiss();
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
                Uri defaultProfileUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.defaultprofilepic);

                IMAGEName=userName.getText().toString()+"."+getExtension(defaultProfileUri);
                final StorageReference imageRef=objectStorageReference.child(IMAGEName);




                Toast.makeText(this,"Uploading user profile picture",Toast.LENGTH_SHORT).show();
                UploadTask objectUploadTask = imageRef.putFile(defaultProfileUri);
                objectUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            objectDialog.dismiss();
                            Toast.makeText(RegisterActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                            throw task.getException();
                        }

                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {


                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {

                            Uri downloaduri = task.getResult();
                            String profileurl= downloaduri.toString();


                            Toast.makeText(RegisterActivity.this,"Uploading User Information",Toast.LENGTH_SHORT).show();
                            Map<String,Object> objectMap=new HashMap<>();
                            objectMap.put("profileimageurl",profileurl);
                            objectMap.put("username",userName.getText().toString());
                            objectMap.put("useremail",userEmail.getText().toString());
                            objectMap.put("mobile",mobile.getText().toString());
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
                                            objectDialog.dismiss();
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
                                            objectDialog.dismiss();
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
            mobile = findViewById(R.id.Resgisterpage_mobile);

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

    private String getExtension(Uri selectedImageUri) {
        try {
            String filePath = getRealPathFromURI(selectedImageUri, this.getContentResolver());
            if (filePath != null) {
                return filePath.substring(filePath.lastIndexOf(".") + 1);
            }
        } catch (Exception e) {
            Toast.makeText(this, "ImageThought:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return "No Extension";
    }

    private String getRealPathFromURI(Uri uri, ContentResolver contentResolver) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
    }




    private void chooseDOB()
    {

        try
        {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // Do something when the user selects a date
                    // For example, update a text view with the selected date
                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    userDob.setText(selectedDate);
                }
            }, year, month, dayOfMonth);

            datePickerDialog.show();

        }
        catch (Exception e){
            Toast.makeText(this,"RegisterPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
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
    private void openMobileGallery() {


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        startActivityForResult(intent, REQUEST_CODE);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
            String croppedImageUriString = data.getStringExtra("croppedImageUri");
            Uri croppedImageUri = Uri.parse(croppedImageUriString);

            // Do something with the cropped image Uri, e.g. set it to an ImageView
            compressedUri = compressImage(croppedImageUri);
            profilepic.setImageURI(compressedUri);
        } else if (data == null || data.getData() == null) {
            Toast.makeText(RegisterActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        } else {

            if(data.getData()!=null && data != null)
            {
                profileimageURL=data.getData();


            }
            else
            {
                Toast.makeText(this, "Please upload your profile picture", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(RegisterActivity.this, CropActivity.class);
            intent.putExtra("imageUri", profileimageURL.toString());
            intent.putExtra("content","profile");
            startActivityForResult(intent, 12);

        }
    }
    private Uri compressImage(Uri imageUri) {
        try {
            // Get the original bitmap from the image URI
            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(RegisterActivity.this.getContentResolver(), imageUri);

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
            File storageDir = RegisterActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Write the compressed bitmap to the file
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.flush();
            outputStream.close();

            // Return the URI of the compressed image file
            return Uri.fromFile(imageFile);


        } catch (IOException e) {
            Log.e(TAG, "Failed to compress image: " + e.getMessage());
            return null;
        }
    }
}