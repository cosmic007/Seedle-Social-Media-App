package com.project.seedle.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.project.seedle.AdaptersClasses.AddThoughtsTabAdapter;
import com.project.seedle.Fragments.addImageThoughtFragment;
import com.project.seedle.Fragments.addTextThoughtFragment;
import com.project.seedle.R;

public class AddThoughtPage extends AppCompatActivity {

    private AddThoughtsTabAdapter objectAddThoughtsTabAdapter;
    private TabLayout objectTabLayout;

    private ViewPager objectViewPager;
    private int [] tabIcons={
            R.drawable.icon_text_thoughts,R.drawable.icon_image_thoughts
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thought_page);

        objectTabLayout =findViewById(R.id.AddThoughts_tabsLayout);
        objectViewPager=findViewById(R.id.AddThoughts_PageViewer);
        objectAddThoughtsTabAdapter= new AddThoughtsTabAdapter(getSupportFragmentManager());

        objectAddThoughtsTabAdapter.addFragment(new addTextThoughtFragment(),"Text");
        objectAddThoughtsTabAdapter.addFragment(new addImageThoughtFragment(),"Image");

        objectViewPager.setAdapter(objectAddThoughtsTabAdapter);
        objectTabLayout.setupWithViewPager(objectViewPager);
        SetupIcons();

    }
    private void SetupIcons()
    {
        try
        {
            objectTabLayout.getTabAt(0).setIcon(tabIcons[0]);
            objectTabLayout.getTabAt(1).setIcon(tabIcons[1]);


        }
        catch(Exception e)
        {
            Toast.makeText(this,"AddThoughts:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

}