package com.project.seedle.AppClasses;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNotifications {

    public void generateNotification(String currentLoggedInUser, String actionUser, String statusType, String ownerofPostEmail)
    {
        Map<String, Object> objectMap = new HashMap<>();
        FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();


        objectMap.put("email",currentLoggedInUser);
        objectMap.put("action",actionUser);
        objectMap.put("type",statusType);

        objectFirebaseFirestore.collection("UserProfileData")
                .document(ownerofPostEmail)
                .collection("Notifications")
                .document(String.valueOf(System.currentTimeMillis()))
                .set(objectMap);



    }
}
