package com.project.seedle.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.seedle.R;

public class LoginPage extends AppCompatActivity {

    Button loginpagetoReg;
    Button login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        loginpagetoReg=findViewById(R.id.LoginPage_RegBtn);
        login=findViewById(R.id.loginPage_loginbtn);


        loginpagetoReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToRegisterPage();
            }
        });


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