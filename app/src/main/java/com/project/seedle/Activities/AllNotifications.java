package com.project.seedle.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.project.seedle.AdaptersClasses.AllNotificationsAdapter;
import com.project.seedle.ModelClassess.Model_AllNotifications;
import com.project.seedle.R;

import java.util.ArrayList;

public class AllNotifications extends AppCompatActivity {


    private FirebaseFirestore objectFirebaseFirestore;
    private RecyclerView objectRecyclerView;

    private AllNotificationsAdapter objectAllNotificationsAdapter;
    private Toolbar objectToolbar;

    private FirebaseAuth objectFirebaseAuth;
    private Dialog objectDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notifications);


        objectFirebaseFirestore=FirebaseFirestore.getInstance();
        objectFirebaseAuth = FirebaseAuth.getInstance();
        attachJAVAtoXML();
        getAllNotificationIntoRV();


    }

    @Override
    protected void onStart() {
        super.onStart();
        objectAllNotificationsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        objectAllNotificationsAdapter.stopListening();
    }

    private void getAllNotificationIntoRV()
    {
        try {

            if(objectFirebaseAuth!=null)
            {
                String currentloggedinuser = objectFirebaseAuth.getCurrentUser().getEmail();
                Query objectQuery = objectFirebaseFirestore.collection("UserProfileData")
                        .document(currentloggedinuser).collection("Notifications");

                FirestoreRecyclerOptions<Model_AllNotifications> objectOptions = new FirestoreRecyclerOptions.Builder<Model_AllNotifications>()
                        .setQuery(objectQuery,Model_AllNotifications.class).build();

                objectAllNotificationsAdapter = new AllNotificationsAdapter(objectOptions);
                objectRecyclerView.setAdapter(objectAllNotificationsAdapter);

                objectRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            }
            else {
                Toast.makeText(this, "User not online", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




    ArrayList <String> objectStringArrayList = new ArrayList<>();


    private void clearAllNotifications()
    {
        try {

            if(objectFirebaseAuth!=null)
            {
                String currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail();
                objectFirebaseFirestore.collection("UserProfileData")
                        .document(currentLoggedInUser)
                        .collection("Notifications")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    for(QueryDocumentSnapshot objectQueryDocumentSnapshot:task.getResult())
                                    {
                                        objectStringArrayList.add(objectQueryDocumentSnapshot.getId());
                                        WriteBatch objectWriteBatch = objectFirebaseFirestore.batch();

                                        for(int count = 0; count<objectStringArrayList.size(); count++)
                                        {
                                            objectWriteBatch.delete(
                                                    objectFirebaseFirestore.collection("UserProfileData")
                                                            .document(currentLoggedInUser).collection("Notifications")
                                                            .document(objectStringArrayList.get(count))
                                            );
                                        }
                                        objectWriteBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(AllNotifications.this, "Notifications cleared", Toast.LENGTH_SHORT).show();
                                                } else if (!task.isSuccessful())
                                                {

                                                    Toast.makeText(AllNotifications.this, task.getException().toString(), Toast.LENGTH_SHORT).show();


                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });

            }
            else {
                Toast.makeText(this, "User not online", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void attachJAVAtoXML()
    {
        try {
            objectDialog = new Dialog(this);
            objectDialog.setContentView(R.layout.please_wait_dialogue);

            objectToolbar = findViewById(R.id.allNotifications_toolbar);
            objectRecyclerView=findViewById(R.id.allNotifications_RV);
            objectToolbar.setTitle("Notifications");
            objectToolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText_LeftAligned);

            objectToolbar.inflateMenu(R.menu.all_notification_clear_menu);
            objectToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId())
                    {
                        case R.id.allnotification_item_clear:
                            clearAllNotifications();
                            return true;
                        case R.id.allnotification_item_goback:
                            startActivity(new Intent(AllNotifications.this,MainContentPage.class));
                            return true;
                    }
                    return false;
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}