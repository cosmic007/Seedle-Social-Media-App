package com.project.seedle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.project.seedle.Activities.LoginPage;
import com.project.seedle.Activities.MainContentPage;

public class MainActivity extends AppCompatActivity {

    ImageView background;
    Button tologinbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attachJavaObjectToXML();
        tologinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginPage();
            }
        });



    }
    private void attachJavaObjectToXML()
    {
        try
        {
            tologinbtn=findViewById(R.id.movebtn);

        }
        catch (Exception e)
        {
            Toast.makeText(this,"MainAction"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void LoginPage()
    {
        try{
            startActivity(new Intent(this, LoginPage.class));

        }
        catch (Exception e)
        {
            Toast.makeText(this,"MainActivity"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}