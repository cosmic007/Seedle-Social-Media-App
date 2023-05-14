package com.project.seedle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.project.seedle.AdaptersClasses.FavoriteStatusTabAdapter;
import com.project.seedle.Fragments.FavoriteImageFragment;
import com.project.seedle.Fragments.favoriteTextFragment;

public class Favorites extends AppCompatActivity {

    private TabLayout objectTabLayout;
    private ViewPager objectViewPager;

    private FavoriteStatusTabAdapter objectFavoriteStatusTabAdapter;
    private FavoriteImageFragment objectFavoriteImageFragment;
    private favoriteTextFragment objectFavoriteTextFragment;

    private int[] tabIcons = {
            R.drawable.icon_text_thoughts, R.drawable.icon_image_thoughts
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        initializeVariables();
        addFragmentsToTabLayout();
    }

    private void setUpTabIcons() {
        try {
            objectTabLayout.getTabAt(0).setIcon(tabIcons[0]);
            objectTabLayout.getTabAt(1).setIcon(tabIcons[1]);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addFragmentsToTabLayout() {
        try {
            objectFavoriteStatusTabAdapter = new FavoriteStatusTabAdapter(getSupportFragmentManager());
            objectFavoriteStatusTabAdapter.addFragment(objectFavoriteTextFragment);
            objectFavoriteStatusTabAdapter.addFragment(objectFavoriteImageFragment);

            objectViewPager.setAdapter(objectFavoriteStatusTabAdapter);

            objectTabLayout.setupWithViewPager(objectViewPager);
            objectViewPager.setSaveFromParentEnabled(false);
            setUpTabIcons();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeVariables() {
        try {
            objectFavoriteTextFragment = new favoriteTextFragment();
            objectFavoriteImageFragment = new FavoriteImageFragment();

            objectTabLayout = findViewById(R.id.favoriteFragment_tabLayout);
            objectViewPager = findViewById(R.id.favoriteFragment_Viewpager);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}