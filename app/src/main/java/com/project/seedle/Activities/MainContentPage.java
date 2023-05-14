package com.project.seedle.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.seedle.Favorites;
import com.project.seedle.Fragments.Community;
import com.project.seedle.Fragments.News;
import com.project.seedle.Fragments.ImageThoughts;
import com.project.seedle.Fragments.TextThoughts;
import com.project.seedle.MainActivity;
import com.project.seedle.NoInternetActivity;
import com.project.seedle.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainContentPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Fragment Objects

    private TextThoughts objectTextThoughts;
    private ImageThoughts objectImageThoughts;
    private News objectNews;
    private Community objectCommunity;

    private AddThoughtPage objectAddThoughtPage;


    private Toolbar objectToolBar;
    private NavigationView objectNavigationView;



    private DrawerLayout objectDrawerLayout;
    private ImageView header_backgroundProfile;
    private CircleImageView header_profilepic;
    private TextView header_username,header_userEmail;
    private ProgressBar header_progressBar;

    public String mobileno;

    private TextView notificationTV;

    private FirebaseAuth objectFirebaseAuth;
    private FirebaseFirestore objectFirebaseFirestore;
    private BottomNavigationView objectBottomNavigationView;

    private DocumentReference objectDocumentReference,obj2;

    private CollectionReference objectCollectionReference;
    private FirebaseFirestore notificationFirebaseFirestore;


    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isInternetConnected()) {
            Intent intent = new Intent(MainContentPage.this, NoInternetActivity.class);
            startActivity(intent);
            finish();// Optional: Finish the current activity if you don't want the user to go back to it without an internet connection
        }



        setContentView(R.layout.activity_main_content_page);


        objectFirebaseAuth= FirebaseAuth.getInstance();
        objectFirebaseFirestore= FirebaseFirestore.getInstance();
        notificationFirebaseFirestore = FirebaseFirestore.getInstance();
        objectCollectionReference = notificationFirebaseFirestore.collection("UserProfileData")
                .document(objectFirebaseAuth.getCurrentUser().getEmail().toString())
                .collection("Notifications");

        objectTextThoughts =new TextThoughts();
        objectImageThoughts = new ImageThoughts();
        objectNews = new News();
        objectCommunity = new Community();
        objectAddThoughtPage = new AddThoughtPage();


        changeFragments(objectImageThoughts);


        objectToolBar=findViewById(R.id.toolBar);
        objectNavigationView=findViewById(R.id.navigationView);
        objectDrawerLayout=findViewById(R.id.drawerLayout);
        View headerXMLFile=objectNavigationView.getHeaderView(0);
        header_backgroundProfile=headerXMLFile.findViewById(R.id.header_profilePicbg);
        header_profilepic=headerXMLFile.findViewById(R.id.header_profilepic);

        header_username=headerXMLFile.findViewById(R.id.header_username);
        header_userEmail=headerXMLFile.findViewById(R.id.header_userEmail);
        header_progressBar=headerXMLFile.findViewById(R.id.header_progressBar);
        objectBottomNavigationView= findViewById(R.id.bottom_nav_viewbar);

        objectToolBar.inflateMenu(R.menu.main_content_menu_bar);
        objectToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.maincontentpage_item_notificationicon:
                        startActivity(new Intent(MainContentPage.this,AllNotifications.class));

                        return true;
                }
                return false;
            }
        });

        notificationTV = (TextView) MenuItemCompat.getActionView(objectNavigationView.getMenu().findItem(R.id.item_notifications));



        setUpDrawerMenu();
        Drawable drawable = objectToolBar.getNavigationIcon();
        if (drawable != null) {
            drawable.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        }
        getCurrentUserDetails();
        objectNavigationView.setNavigationItemSelectedListener(this);





        objectBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                try{
                    switch (item.getItemId())
                    {
                        case Constants.itemImageThoughts:
                            changeFragments(objectImageThoughts);
                            return true;
                        case Constants.item_textThoughts:
                            changeFragments(objectTextThoughts);
                            return true;
                        case Constants.item_news:
                            changeFragments(objectNews);
                            return true;
                        case Constants.community:
                            changeFragments(objectCommunity);
                            return true;
                        case Constants.item_add_post:
                            startActivity(new Intent(MainContentPage.this,AddThoughtPage.class));
                            return true;
                        default:
                            return false;

                    }

                }
                catch (Exception e)
                {
                    Toast.makeText(MainContentPage.this,"MainContentPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });





    }


    private void changeFragments(Fragment objectFragment)
    {
        try
        {
            FragmentTransaction objectFragmentTransaction =getSupportFragmentManager().beginTransaction();
            objectFragmentTransaction.replace(R.id.container,objectFragment);
            objectFragmentTransaction.commit();

        }
        catch (Exception e)
        {
            Toast.makeText(this,"MainContentPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
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
                        
                        try {
                            Glide.with(MainContentPage.this).load(linkofProfileImage).into(header_profilepic);
                            Glide.with(MainContentPage.this).load(linkofProfileImage).into(header_backgroundProfile);
                            header_progressBar.setVisibility(View.INVISIBLE);
                            
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(MainContentPage.this, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                        
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
                    Toast.makeText(this,"Profile is Clicked",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item_notifications:
                    startActivity(new Intent(MainContentPage.this,AllNotifications.class));
                    return true;
                case R.id.item_settings:
                    Toast.makeText(this,"Settings is Clicked",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item_favorites:
                    Intent intent = new Intent(MainContentPage.this, Favorites.class);
                    startActivity(intent);
                    return true;
                case R.id.item_textstatus:
                    Toast.makeText(this,"TextStatus is Clicked",Toast.LENGTH_SHORT).show();
                    return true;
                case  R.id.item_signout:
                    signOutUser();
                    return true;
                default:
                    return false;
            }

        }
        catch (Exception e)
        {

        }


        return false;
    }


    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void signOutUser()
    {

        try{
            if(objectFirebaseAuth!=null)
            {
                objectFirebaseAuth.signOut();
                Toast.makeText(this,"You have successfully been logged out",Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("loggedIn", false);
                editor.apply();
                startActivity(new Intent(this,LoginPage.class));
                closeDrawer();
                finish();
            }

        }
        catch(Exception e)
        {
            Toast.makeText(this,"MainContentPage:"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private ListenerRegistration objectListenerRegistration;


    @Override
    protected void onStart() {
        super.onStart();
        objectListenerRegistration = objectCollectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(MainContentPage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (!value.isEmpty()) {
                    notificationTV.setGravity(Gravity.CENTER_VERTICAL);
                    notificationTV.setTypeface(null, Typeface.BOLD);
                    notificationTV.setTextColor(getResources().getColor(R.color.black));
                    int size = value.size();

                    notificationTV.setText(String.valueOf(size)); // Corrected line
                    Toast.makeText(MainContentPage.this, "You have " + size + " notifications", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        objectListenerRegistration.remove();
    }
}