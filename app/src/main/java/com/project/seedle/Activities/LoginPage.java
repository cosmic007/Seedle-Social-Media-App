package com.project.seedle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.seedle.R;

public class LoginPage extends AppCompatActivity {

    private Button login;

    private RelativeLayout objectRelativeLayout;
    private ImageView loginpageLogo;
    private EditText loginPageEmail,loginpagePassword;
    private TextView loginPageTagline,loginpagetoReg,Resetbtn;
    private Dialog objectDialog;

    //Firebase Variable
    private FirebaseAuth ObjectFirebaseAuth,firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objectDialog=new Dialog(this);
        objectDialog.setContentView(R.layout.please_wait_dialogue);
        setContentView(R.layout.activity_login_page);

        Resetbtn = findViewById(R.id.Resetbtn);

        ObjectFirebaseAuth = FirebaseAuth.getInstance();

        loginPageEmail=findViewById(R.id.loginPage_email);
        loginpagePassword=findViewById(R.id.LoginPage_password);

        loginpagetoReg=findViewById(R.id.LoginPage_RegBtn);
        login=findViewById(R.id.loginPage_loginbtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });


        loginpagetoReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToRegisterPage();
            }
        });


        Resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail();
            }
        });


    }
    private void signInUser()
    {
        try
        {
            if(!loginPageEmail.getText().toString().isEmpty() && !loginpagePassword.getText().toString().isEmpty())
            {
                objectDialog.show();
                if(ObjectFirebaseAuth.getCurrentUser()==null)
                {
                    ObjectFirebaseAuth.signInWithEmailAndPassword(
                            loginPageEmail.getText().toString(),loginpagePassword.getText().toString()
                    )
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    objectDialog.dismiss();
                                    Toast.makeText(LoginPage.this,"Welcome "+loginPageEmail.getText().toString(),Toast.LENGTH_SHORT).show();
                                    SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putBoolean("loggedIn", true);
                                    editor.apply();
                                    startActivity(new Intent(LoginPage.this,MainContentPage.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    objectDialog.dismiss();
                                    Toast.makeText(LoginPage.this,"LoginPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    objectDialog.dismiss();
                    ObjectFirebaseAuth.signOut();
                    Toast.makeText(this,"Previously Signed-In user logged out successfully, Login again please",Toast.LENGTH_SHORT).show();

                }

            }
            else{
                Toast.makeText(this,"Please fill both fields",Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception e)
        {
            Toast.makeText(this,"LoginPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }


    private void moveToRegisterPage()
    {
        try {
            startActivity(new Intent(this,RegisterActivity.class));

        }
        catch(Exception e)
        {
            Toast.makeText(this,"LoginPage"+e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    private void sendPasswordResetEmail() {
        firebaseAuth = FirebaseAuth.getInstance();


        String email = loginPageEmail.getText().toString();

        if(!email.isEmpty())
        {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginPage.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginPage.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        else {

            Toast.makeText(LoginPage.this, "Please fill email and try again", Toast.LENGTH_SHORT).show();

        }




    }
}