package com.project.seedle.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.seedle.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainContentPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar objectToolBar;
    private NavigationView objectNavigationView;

    private DrawerLayout objectDrawerLayout;
    private ImageView header_backgroundProfile;
    private CircleImageView header_profilepic;
    private TextView header_username,header_userEmail;
    private ProgressBar header_progressBar;

    private FirebaseAuth objectFirebaseAuth;
    private FirebaseFirestore objectFirebaseFirestore;

    private DocumentReference objectDocumentReference;
    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content_page);

        objectToolBar=findViewById(R.id.toolBar);
        objectNavigationView=findViewById(R.id.navigationView);
        objectDrawerLayout=findViewById(R.id.drawerLayout);
        View headerXMLFile=objectNavigationView.getHeaderView(0);
        header_backgroundProfile=headerXMLFile.findViewById(R.id.header_profilePicbg);
        header_profilepic=headerXMLFile.findViewById(R.id.header_profilepic);

        header_username=headerXMLFile.findViewById(R.id.header_username);
        header_userEmail=headerXMLFile.findViewById(R.id.header_userEmail);
        header_progressBar=headerXMLFile.findViewById(R.id.header_progressBar);

        setUpDrawerMenu();
        getCurrentUserDetails();
        objectNavigationView.setNavigationItemSelectedListener(this);



    }
    private void getCurrentUserDetails()
    {
        try
        {
            currentUserEmail=getCurrentLoggedInUser();
            if(currentUserEmail.equals("No user is logged in"))
            {
                Toast.makeText(this,"No user is logged in",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,LoginPage.class));
                finish();
            }
            else
            {
                header_progressBar.setVisibility(View.VISIBLE);
                objectDocumentReference=objectFirebaseFirestore.collection("UserProfileData").document(currentUserEmail);
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        header_username.setText(documentSnapshot.getString("username"));
                        header_username.setAllCaps(true);

                        header_userEmail.setText(currentUserEmail);
                        String linkofProfileImage=documentSnapshot.getString("profileimageurl");
                        Glide.with(MainContentPage.this).load(linkofProfileImage).into(header_profilepic);
                        Glide.with(MainContentPage.this).load(linkofProfileImage).into(header_backgroundProfile);
                        header_progressBar.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainContentPage.this,"Loading User Details:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
        catch (Exception e)
        {
            Toast.makeText(this,"MainContentPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentLoggedInUser()
    {
        try
        {
            if(objectFirebaseAuth!=null)
            {
                return objectFirebaseAuth.getCurrentUser().getEmail().toString();
            }
            else
            {
                return "No user is logged in";
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this,"MainContentPage"+e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;

        }
    }

    private void setUpDrawerMenu()
    {
        try
        {
            ActionBarDrawerToggle objectActionBarDrawerToggle = new ActionBarDrawerToggle(
                    this,objectDrawerLayout,objectToolBar,R.string.open,R.string.close);

            objectDrawerLayout.addDrawerListener(objectActionBarDrawerToggle);
            objectActionBarDrawerToggle.syncState();

        }
        catch (Exception e)
        {
            Toast.makeText(this,"MainContentPage"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void openDrawer()
    {
        try
        {
            objectDrawerLayout.openDrawer(GravityCompat.START);

        }
        catch(Exception e)
        {
            Toast.makeText(this,"MainContentPage"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void closeDrawer()
    {
        try
        {
            objectDrawerLayout.closeDrawer(GravityCompat.START);
        }
        catch (Exception e)
        {
            Toast.makeText(this,"MainContentPage"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        try
        {
            switch (item.getItemId())
            {
                case R.id.item_profile:
            }

        }
        catch (Exception e)
        {

        }


        return false;
    }
}