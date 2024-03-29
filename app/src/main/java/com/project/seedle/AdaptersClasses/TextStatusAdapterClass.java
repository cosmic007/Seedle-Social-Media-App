package com.project.seedle.AdaptersClasses;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.seedle.Activities.TextCommentPage;
import com.project.seedle.Admin;
import com.project.seedle.AppClasses.AddNotifications;
import com.project.seedle.ModelClassess.Model_TextStatus;
import com.project.seedle.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class TextStatusAdapterClass extends FirestoreRecyclerAdapter<Model_TextStatus,TextStatusAdapterClass.TextStatusViewHolder> {

    public String url,USERNAME;

    public String AdminName;

    public Admin admin = new Admin();




    private  FirebaseFirestore objectFirebaseFirestore;






    public TextStatusAdapterClass(@NonNull FirestoreRecyclerOptions<Model_TextStatus> options) {
        super(options);
    }




    @Override
    protected void onBindViewHolder(@NonNull TextStatusViewHolder textStatusViewHolder, int i, @NonNull Model_TextStatus model_textStatus) {

        objectFirebaseFirestore = FirebaseFirestore.getInstance();
        textStatusViewHolder.usernameTV.setText(model_textStatus.getUsername());
        textStatusViewHolder.dateTimeTV.setText(model_textStatus.getCurrentdatetime());
        textStatusViewHolder.userStatusTV.setText((model_textStatus.getStatus()));
        AdminName = model_textStatus.getUsername();


        textStatusViewHolder.heartCountTV.setText(Integer.toString(model_textStatus.getNooflove()));

        textStatusViewHolder.hahaCountTV.setText(Integer.toString(model_textStatus.getNoofhaha()));
        textStatusViewHolder.sadCountTV.setText(Integer.toString(model_textStatus.getNofsad()));
        textStatusViewHolder.commentCountTV.setText(Integer.toString(model_textStatus.getNoofcomments()));
        String linkOfProfileImage= model_textStatus.getProfileurl();
        try
        {
            Glide.with(textStatusViewHolder.profileIV.getContext())
                    .load(linkOfProfileImage).into(textStatusViewHolder.profileIV);
            
        }
        catch (Exception e)
        {
            Toast.makeText(textStatusViewHolder.profileIV.getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }


        AddNotifications objectAddNotifications = new AddNotifications();








        String userN= model_textStatus.getUsername();
        String admin1 = admin.getAdmin1();
        String admin2 = admin.getAdmin2();
        String admin3 = admin.getAdmin3();

        if(Objects.equals(userN, admin1) || Objects.equals(userN, admin2))
        {
            textStatusViewHolder.verified.setVisibility(View.VISIBLE);
            textStatusViewHolder.admintag.setText("Developer");
            textStatusViewHolder.admintag.setVisibility(View.VISIBLE);

        } else if (Objects.equals(userN, admin3)) {
            textStatusViewHolder.verified.setVisibility(View.VISIBLE);
            textStatusViewHolder.admintag.setText("Tester");
            textStatusViewHolder.admintag.setVisibility(View.VISIBLE);
        } else {
            textStatusViewHolder.verified.setVisibility(View.INVISIBLE);
            textStatusViewHolder.admintag.setVisibility(View.INVISIBLE);

        }



        FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();
        String EMAIL= objectFirebaseAuth.getCurrentUser().getEmail();






        CollectionReference collectionRef = objectFirebaseFirestore.collection("UserProfileData");
        DocumentReference documentRef = collectionRef.document(EMAIL);


        // Iterate through the list of items in the adapter to find the item to update
        for (int j = 0; j < getItemCount(); j++) {
            Model_TextStatus item = getItem(j);

            FirebaseAuth objFirebaseAuth = FirebaseAuth.getInstance();

            String userEmail = objFirebaseAuth.getCurrentUser().getEmail();
            String documentID=getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId();



            DocumentReference objectDocumentReferecnce = objectFirebaseFirestore.collection("TextStatus")
                    .document(documentID).collection("Emotions")
                    .document(userEmail);

            objectDocumentReferecnce.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult().exists()) {
                        String currentFlag = task.getResult().getString("currentflag");
                        if (currentFlag.equals("love")) {

                            textStatusViewHolder.heartIV.setImageResource(R.drawable.icon_liked);

                        }
                        else if(currentFlag.equals("sad")){
                            textStatusViewHolder.heartIV.setImageResource(R.drawable.icon_love);


                        }
                        else if(currentFlag.equals("haha")){
                            textStatusViewHolder.heartIV.setImageResource(R.drawable.icon_love);

                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {



                }
            });


        }
        for (int j = 0; j < getItemCount(); j++) {
            Model_TextStatus item = getItem(j);

            FirebaseAuth objFirebaseAuth = FirebaseAuth.getInstance();

            final String userEmail = objFirebaseAuth.getCurrentUser().getEmail();
            String documentID=getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId();


            final DocumentReference objectDocumentReferecnce2 = objectFirebaseFirestore.collection("UserFavorite")
                    .document(userEmail).collection("FavoriteTextStatus")
                    .document(documentID);
            objectDocumentReferecnce2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    try
                    {
                        if (task.getResult().exists()) {
                            String currentFlag = task.getResult().getString("currentflag");
                            if (currentFlag.equals("flag")) {

                                textStatusViewHolder.favoriteIV.setImageResource(R.drawable.icon_fav_filled);

                            }
                        }

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(textStatusViewHolder.favoriteIV.getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {




                }
            });

            


        }



        documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String Userprofile = documentSnapshot.getString("profileimageurl");
                    String UserName = documentSnapshot.getString("username");
                    USERNAME = UserName;
                    url = Userprofile;
                    if(Objects.equals(url, model_textStatus.getProfileurl()) || EMAIL.equals("cosmicriderrr@gmail.com"))
                    {
                        textStatusViewHolder.deleteIV.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting document", e);
            }
        });





        textStatusViewHolder.favoriteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objFirebaseAuth = FirebaseAuth.getInstance();

                String userEmail3 = objFirebaseAuth.getCurrentUser().getEmail();
                String documentID3=getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId();


                DocumentReference objectDocumentReferecnce5 = objectFirebaseFirestore.collection("UserFavorite")
                        .document(userEmail3).collection("FavoriteTextStatus")
                        .document(documentID3);
                objectDocumentReferecnce5.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            String currentFlag = task.getResult().getString("currentflag");
                            if (currentFlag.equals("flag")) {

                                textStatusViewHolder.favoriteIV.setImageResource(R.drawable.icon_fav);
                                Map<String, Object> objectMap = new HashMap<>();
                                objectMap.put("currentflag","none");
                                FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();
                                String currentLoggedInUser=objectFirebaseAuth.getCurrentUser().getEmail();
                                objectFirebaseFirestore.collection("UserFavorite").document(currentLoggedInUser)
                                        .collection("FavoriteTextStatus")
                                        .document(documentID3).set(objectMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                objectFirebaseFirestore.collection("UserFavorite").document(currentLoggedInUser).collection("FavoriteTextStatus")
                                                        .document(getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId())
                                                        .delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(textStatusViewHolder.deleteIV.getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(textStatusViewHolder.deleteIV.getContext(), "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                                                                textStatusViewHolder.favoriteIV.setImageResource(R.drawable.icon_fav_filled);

                                                            }
                                                        });




                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                textStatusViewHolder.favoriteIV.setImageResource(R.drawable.icon_fav_filled);
                                                Toast.makeText(textStatusViewHolder.favoriteIV.getContext(), "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                            else {
                                textStatusViewHolder.favoriteIV.setImageResource(R.drawable.icon_fav_filled);
                                String documentID=getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId();
                                FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore .getInstance();
                                DocumentReference objectDocumentReference = objectFirebaseFirestore.collection("TextStatus").document(documentID);
                                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String username= model_textStatus.getUsername();
                                        String userEmail =documentSnapshot.getString("useremail");
                                        String status = documentSnapshot.getString("status");
                                        String profileUrl=documentSnapshot.getString("profileurl");
                                        String statusDate =documentSnapshot.getString("currentdatetime");
                                        Map<String, Object> objectMap = new HashMap<>();
                                        objectMap.put("username",username);
                                        objectMap.put("useremail",userEmail);
                                        objectMap.put("status",status);
                                        objectMap.put("profileurl",profileUrl);
                                        objectMap.put("currentdatetime",statusDate);
                                        objectMap.put("currentflag","flag");
                                        FirebaseAuth objectFirebaseAuth =FirebaseAuth.getInstance();

                                        if(objectFirebaseAuth!=null)
                                        {
                                            String currentLoggedInUser=objectFirebaseAuth.getCurrentUser().getEmail();
                                            objectFirebaseFirestore.collection("UserFavorite").document(currentLoggedInUser)
                                                    .collection("FavoriteTextStatus")
                                                    .document(documentID).set(objectMap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                            Toast.makeText(textStatusViewHolder.favoriteIV.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                                                            objectAddNotifications.generateNotification(userEmail,"Added","Text status",
                                                                    model_textStatus.getUseremail());



                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(textStatusViewHolder.favoriteIV.getContext(), "Failed to add into favorites", Toast.LENGTH_SHORT).show();
                                                            textStatusViewHolder.favoriteIV.setImageResource(R.drawable.icon_fav);
                                                        }
                                                    });


                                        }
                                        else {
                                            Toast.makeText(textStatusViewHolder.favoriteIV.getContext(),"User not online", Toast.LENGTH_SHORT).show();
                                            textStatusViewHolder.favoriteIV.setImageResource(R.drawable.icon_fav);
                                        }


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        textStatusViewHolder.favoriteIV.setImageResource(R.drawable.icon_fav);

                                    }
                                });

                            }
                        }
                        else {
                            textStatusViewHolder.favoriteIV.setImageResource(R.drawable.icon_fav_filled);
                            String documentID=getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId();
                            FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore .getInstance();
                            DocumentReference objectDocumentReference = objectFirebaseFirestore.collection("TextStatus").document(documentID);
                            objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String username= model_textStatus.getUsername();
                                    String userEmail =documentSnapshot.getString("useremail");
                                    String status = documentSnapshot.getString("status");
                                    String profileUrl=documentSnapshot.getString("profileurl");
                                    String statusDate =documentSnapshot.getString("currentdatetime");
                                    Map<String, Object> objectMap = new HashMap<>();
                                    objectMap.put("username",username);
                                    objectMap.put("useremail",userEmail);
                                    objectMap.put("status",status);
                                    objectMap.put("profileurl",profileUrl);
                                    objectMap.put("currentdatetime",statusDate);
                                    objectMap.put("currentflag","flag");
                                    FirebaseAuth objectFirebaseAuth =FirebaseAuth.getInstance();

                                    if(objectFirebaseAuth!=null)
                                    {
                                        String currentLoggedInUser=objectFirebaseAuth.getCurrentUser().getEmail();
                                        objectFirebaseFirestore.collection("UserFavorite").document(currentLoggedInUser)
                                                .collection("FavoriteTextStatus")
                                                .document(documentID).set(objectMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        Toast.makeText(textStatusViewHolder.favoriteIV.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                                                        objectAddNotifications.generateNotification(userEmail,"Added","Text status",
                                                                model_textStatus.getUseremail());



                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(textStatusViewHolder.favoriteIV.getContext(), "Failed to add into favorites", Toast.LENGTH_SHORT).show();
                                                        textStatusViewHolder.favoriteIV.setImageResource(R.drawable.icon_fav);
                                                    }
                                                });


                                    }
                                    else {
                                        Toast.makeText(textStatusViewHolder.favoriteIV.getContext(),"User not online", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    textStatusViewHolder.favoriteIV.setImageResource(R.drawable.icon_fav);

                                }
                            });
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {




                    }
                });

            }
        });







        textStatusViewHolder.heartIV.setOnClickListener(v -> {
            textStatusViewHolder.heartIV.setImageResource(R.drawable.icon_liked);
            FirebaseAuth objFirestoreAuth=FirebaseAuth.getInstance();
            if(objFirestoreAuth!=null)
            {
                final String userEmail = objFirestoreAuth.getCurrentUser().getEmail();
                final String documentID=getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId();

                final FirebaseFirestore objectFirebaseFirestore=FirebaseFirestore.getInstance();
                final DocumentReference objectDocumentReferecnce = objectFirebaseFirestore.collection("TextStatus")
                        .document(documentID).collection("Emotions")
                        .document(userEmail);
                objectDocumentReferecnce.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists())
                        {
                            String currentFlag=task.getResult().getString("currentflag");
                            objectAddNotifications.generateNotification(userEmail,"liked","Text status",
                                    model_textStatus.getUseremail());
                            if(currentFlag.equals("love"))
                            {

                                objectDocumentReferecnce.update("currentflag","love");

                            }
                            else if(currentFlag.equals("haha"))
                            {
                                Long totalHearts= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("nooflove");
                                totalHearts++;
                                getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                        .getReference().update("nooflove",totalHearts);
                                objectDocumentReferecnce.update("currentflag","love");
                                Long totalHaha= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("noofhaha");
                                totalHaha--;
                                getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                        .getReference().update("noofhaha",totalHaha);


                            }
                            else if(currentFlag.equals("sad"))
                            {
                                Long totalHearts= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("nooflove");
                                totalHearts++;
                                getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                        .getReference().update("nooflove",totalHearts);
                                objectDocumentReferecnce.update("currentflag","love");
                                Long totalSad= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                        .get("nofsad");
                                totalSad--;

                                getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getReference()
                                        .update("nofsad",totalSad);

                            }

                        }
                        else {
                            Map<String,Object> objectMap=new HashMap<>();
                            objectMap.put("currentflag","love");
                            objectFirebaseFirestore.collection("TextStatus")
                                    .document(documentID).collection("Emotions")
                                    .document(userEmail)
                                    .set(objectMap);
                            Long totalHearts= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("nooflove");
                            totalHearts++;
                            getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                    .getReference().update("nooflove",totalHearts);
                            objectDocumentReferecnce.update("currentflag","love");
                            objectAddNotifications.generateNotification(userEmail,"liked","Text status",
                                    model_textStatus.getUseremail());
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(textStatusViewHolder.heartIV.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
            else
            {
                Toast.makeText(textStatusViewHolder.heartIV.getContext(), "No user is online", Toast.LENGTH_SHORT).show();

            }

        });

        textStatusViewHolder.hahaIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objFirestoreAuth=FirebaseAuth.getInstance();
                if(objFirestoreAuth!=null)
                {
                    final String userEmail = objFirestoreAuth.getCurrentUser().getEmail();
                    final String documentID=getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId();

                    final FirebaseFirestore objectFirebaseFirestore=FirebaseFirestore.getInstance();
                    final DocumentReference objectDocumentReferecnce = objectFirebaseFirestore.collection("TextStatus")
                            .document(documentID).collection("Emotions")
                            .document(userEmail);
                    objectDocumentReferecnce.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult().exists())
                            {
                                String currentFlag=task.getResult().getString("currentflag");

                                objectAddNotifications.generateNotification(userEmail,"liked","Text status",
                                        model_textStatus.getUseremail());
                                if(currentFlag.equals("haha"))
                                {
                                    objectDocumentReferecnce.update("currentflag","haha");

                                }
                                else if(currentFlag.equals("love"))
                                {
                                    Long totalHaha= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("noofhaha");
                                    totalHaha++;
                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("noofhaha",totalHaha);

                                    objectDocumentReferecnce.update("currentflag","haha");
                                    Long totalLove= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("nooflove");
                                    totalLove--;
                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("nooflove",totalLove);


                                }
                                else if(currentFlag.equals("sad"))
                                {
                                    Long totalHaha= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("noofhaha");
                                    totalHaha++;
                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("noofhaha",totalHaha);

                                    objectDocumentReferecnce.update("currentflag","haha");
                                    Long totalSad= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .get("nofsad");
                                    totalSad--;

                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getReference()
                                            .update("nofsad",totalSad);

                                }

                            }
                            else {
                                Map<String,Object> objectMap=new HashMap<>();
                                objectMap.put("currentflag","haha");
                                objectFirebaseFirestore.collection("TextStatus")
                                        .document(documentID).collection("Emotions")
                                        .document(userEmail)
                                        .set(objectMap);
                                Long totalHaha= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("noofhaha");
                                totalHaha++;
                                getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                        .getReference().update("noofhaha",totalHaha);
                                objectDocumentReferecnce.update("currentflag","haha");
                                objectAddNotifications.generateNotification(userEmail,"liked","Text status",
                                        model_textStatus.getUseremail());
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(textStatusViewHolder.hahaIV.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                else
                {
                    Toast.makeText(textStatusViewHolder.hahaIV.getContext(), "No user is online", Toast.LENGTH_SHORT).show();

                }

            }
        });

        textStatusViewHolder.sadIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth objFirestoreAuth=FirebaseAuth.getInstance();
                if(objFirestoreAuth!=null)
                {
                    final String userEmail = objFirestoreAuth.getCurrentUser().getEmail();
                    final String documentID=getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId();

                    final FirebaseFirestore objectFirebaseFirestore=FirebaseFirestore.getInstance();
                    final DocumentReference objectDocumentReferecnce = objectFirebaseFirestore.collection("TextStatus")
                            .document(documentID).collection("Emotions")
                            .document(userEmail);
                    objectDocumentReferecnce.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult().exists())
                            {
                                String currentFlag=task.getResult().getString("currentflag");
                                objectAddNotifications.generateNotification(userEmail,"liked","Text status",
                                        model_textStatus.getUseremail());
                                if(currentFlag.equals("sad"))
                                {
                                    objectDocumentReferecnce.update("currentflag","sad");

                                }
                                else if(currentFlag.equals("love"))
                                {
                                    Long totalSad= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("nofsad");
                                    totalSad++;
                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("nofsad",totalSad);

                                    objectDocumentReferecnce.update("currentflag","sad");
                                    Long totalLove= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("nooflove");
                                    totalLove--;
                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("nooflove",totalLove);


                                }
                                else if(currentFlag.equals("haha"))
                                {
                                    Long totalSad= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("nofsad");
                                    totalSad++;
                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("nofsad",totalSad);

                                    objectDocumentReferecnce.update("currentflag","sad");
                                    Long totalHaha= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .get("noofhaha");
                                    totalHaha--;

                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getReference()
                                            .update("noofhaha",totalHaha);

                                }

                            }
                            else {
                                Map<String,Object> objectMap=new HashMap<>();
                                objectMap.put("currentflag","sad");
                                objectFirebaseFirestore.collection("TextStatus")
                                        .document(documentID).collection("Emotions")
                                        .document(userEmail)
                                        .set(objectMap);
                                Long totalSad= (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("nofsad");
                                totalSad++;
                                getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                        .getReference().update("nofsad",totalSad);
                                objectDocumentReferecnce.update("currentflag","sad");
                                objectAddNotifications.generateNotification(userEmail,"liked","Text status",
                                        model_textStatus.getUseremail());
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(textStatusViewHolder.sadIV.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                else
                {
                    Toast.makeText(textStatusViewHolder.sadIV.getContext(), "No user is online", Toast.LENGTH_SHORT).show();

                }



            }
        });





        textStatusViewHolder.commentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String documentID=getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId();
                Context objectContext = textStatusViewHolder.commentIV.getContext();

                Intent objectIntent=new Intent(objectContext, TextCommentPage.class);
                objectIntent.putExtra("documentId",documentID);

                objectIntent.putExtra("userEmailID",model_textStatus.getUseremail());

                objectContext.startActivity(objectIntent);
            }
        });
        textStatusViewHolder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objectFirebaseAuth=FirebaseAuth.getInstance();
                if(url.equals(model_textStatus.getProfileurl()) || EMAIL.equals("cosmicriderrr@gmail.com"))
                {

                    FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();
                    objectFirebaseFirestore.collection("TextStatus")
                            .document(getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(textStatusViewHolder.deleteIV.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(textStatusViewHolder.deleteIV.getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                                }
                            });


                }
                else
                {
                    Toast.makeText(textStatusViewHolder.deleteIV.getContext(), "You have no rights to delete this status", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private int getRandomColor() {
        Random random = new Random();
        float minSaturation = 1f;
        float[] hsv = new float[3];
        int color;

        do {
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            Color.RGBToHSV(red, green, blue, hsv);
            color = Color.rgb(red, green, blue);
        } while (hsv[1] < minSaturation);

        return color;
    }

    @NonNull
    @Override
    public TextStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TextStatusViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.model_text_status,parent,false
        ));
    }

    public class TextStatusViewHolder extends RecyclerView.ViewHolder
    {

        ImageView profileIV;
        ImageView heartIV,hahaIV,sadIV,deleteIV,favoriteIV,verified;

        TextView usernameTV,dateTimeTV,userStatusTV,heartCountTV,hahaCountTV,sadCountTV,commentCountTV,commentIV,admintag;

        public TextStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            admintag  = itemView.findViewById(R.id.admin);
            verified = itemView.findViewById(R.id.verified);
            profileIV=itemView.findViewById(R.id.model_textStatus_profileIV);
            heartIV=itemView.findViewById(R.id.model_textStatus_heartIV);
            hahaIV=itemView.findViewById(R.id.model_textStatus_hahaIV);
            sadIV=itemView.findViewById(R.id.model_textStatus_sadIV);
            deleteIV=itemView.findViewById(R.id.model_textStatus_delete);
            commentIV=itemView.findViewById(R.id.model_textStatus_commentIV);
            favoriteIV=itemView.findViewById(R.id.model_textStatus_favoriteTextStatus);
            usernameTV=itemView.findViewById(R.id.model_textStatus_profileTV);
            dateTimeTV=itemView.findViewById(R.id.model_textStatus_dateTV);
            userStatusTV=itemView.findViewById(R.id.model_textStatus_textStatusTV);
            heartCountTV=itemView.findViewById(R.id.model_textStatus_heartCountTV);
            hahaCountTV=itemView.findViewById(R.id.model_textStatus_hahaCountTV);
            sadCountTV=itemView.findViewById(R.id.model_textStatus_sadCountTV);
            commentCountTV=itemView.findViewById(R.id.model_textStatus_commentCountTV);


        }
    }


}
