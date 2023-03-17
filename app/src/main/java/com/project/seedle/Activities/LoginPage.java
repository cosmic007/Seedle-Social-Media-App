package com.project.seedle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.seedle.R;

public class LoginPage extends AppCompatActivity {

    private Button loginpagetoReg,login;

    private RelativeLayout objectRelativeLayout;
    private ImageView loginpageLogo;
    private EditText loginPageEmail,loginpagePassword;
    private TextView loginPageTagline;
    private Dialog objectDialog;

    //Firebase Variable
    private FirebaseAuth ObjectFirebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objectDialog=new Dialog(this);
        objectDialog.setContentView(R.layout.please_wait_dialogue);
        setContentView(R.layout.activity_login_page);

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
                                    startActivity(new Intent(LoginPage.this,MainContentPage.class));
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
}