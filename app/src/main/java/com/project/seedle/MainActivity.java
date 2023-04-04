package com.project.seedle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.project.seedle.Activities.LoginPage;
import com.project.seedle.Activities.MainContentPage;

public class MainActivity extends AppCompatActivity {




    Button tologinbtn;
    ImageView logo;
    public boolean isFirstLaunch;
    TextView Name,Tagline;


    private class CheckLoginTask extends AsyncTask<Void, Void, Boolean>{
        private final Context mContext;

        public CheckLoginTask(Context context) {
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            isFirstLaunch = sharedPref.getBoolean("firstlaunch", false);
            return sharedPref.getBoolean("loggedIn", false);

        }

        @Override
        protected void onPostExecute(Boolean loggedIn) {
            SharedPreferences sharedPref = mContext.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            if (!isFirstLaunch) {

                AnimationSet animationSet = new AnimationSet(true);

                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 100, 0);
                translateAnimation.setDuration(1000);


                AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                alphaAnimation.setDuration(1000);

                animationSet.addAnimation(translateAnimation);
                animationSet.addAnimation(alphaAnimation);


                logo.setVisibility(View.VISIBLE);
                logo.startAnimation(animationSet);



            }
            else if (!loggedIn) {

                tologinbtn.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
            else {
                tologinbtn.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, MainContentPage.class);
                startActivity(intent);
                finish();
            }





        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attachJavaObjectToXML();








        CheckLoginTask checkLoginTask = new CheckLoginTask(this);
        checkLoginTask.execute();


        tologinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginPage();
                SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("firstlaunch", true);
                editor.apply();

            }
        });













    }



    private void attachJavaObjectToXML()
    {
        try
        {
            tologinbtn=findViewById(R.id.movebtn);
            logo=findViewById(R.id.logo);
            Name = findViewById(R.id.name);
            Tagline = findViewById(R.id.tagline);

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