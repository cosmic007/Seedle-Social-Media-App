package com.project.seedle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.seedle.Fragments.ProfileFragment;
import com.project.seedle.Fragments.SettingFragment;
import com.project.seedle.R;

public class ProfilePage extends AppCompatActivity {



    private BottomNavigationView objectBottomNavigationView;
    private ProfileFragment objectProfileFragment;
    private SettingFragment objectSettingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        initializeObjects();
        replaceFragment(objectProfileFragment);

        objectBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.profilePage_itemprofile:
                        replaceFragment(objectProfileFragment);
                        return true;

                    case R.id.profilePage_itemsettings:
                        replaceFragment(objectSettingFragment);
                        return true;

                }

                return false;
            }
        });
    }


    private void replaceFragment(Fragment objectFragment)
    {
        FragmentTransaction objectFragmentTransaction =getSupportFragmentManager().beginTransaction();
        objectFragmentTransaction.replace(R.id.ppcontainer,objectFragment);
        objectFragmentTransaction.commit();
    }



    public void initializeObjects()
    {
        try {

            objectBottomNavigationView=findViewById(R.id.profilePage_bottomNV);
            objectProfileFragment = new ProfileFragment();
            objectSettingFragment = new SettingFragment();


        }
        catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}