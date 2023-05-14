package com.project.seedle.AdaptersClasses;

import static android.content.ContentValues.TAG;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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
import com.project.seedle.Activities.ImageCommentPage;
import com.project.seedle.Activities.TextCommentPage;
import com.project.seedle.Admin;
import com.project.seedle.CropActivity;
import com.project.seedle.ModelClassess.Model_ImageStatus;
import com.project.seedle.ModelClassess.Model_TextStatus;
import com.project.seedle.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ImageStatusAdapterClass extends FirestoreRecyclerAdapter<Model_ImageStatus, ImageStatusAdapterClass.ImageStatusViewHolderClass> {

    private Context context;


    public String url, USERNAME;

    public Admin admin = new Admin();

    public ImageStatusAdapterClass(@NonNull FirestoreRecyclerOptions<Model_ImageStatus> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull ImageStatusViewHolderClass imageStatusViewHolderClass, int i, @NonNull Model_ImageStatus model_imageStatus) {
        try {
            imageStatusViewHolderClass.objectProgressBar.setVisibility(View.VISIBLE);
            imageStatusViewHolderClass.userName.setText(model_imageStatus.getUsername());

            imageStatusViewHolderClass.statusDate.setText(model_imageStatus.getCurrentdatetime());
            imageStatusViewHolderClass.StatusDesc.setText(model_imageStatus.getStatus());

            imageStatusViewHolderClass.heartCount.setText(String.valueOf(model_imageStatus.getNooflove()));
            imageStatusViewHolderClass.commentCount.setText(String.valueOf(model_imageStatus.getNoofcomments()));

            String linkofprofileimage = model_imageStatus.getProfileurl();
            String linkofimageStatus = model_imageStatus.getStatusimageurl();



            Glide.with(context).load(linkofprofileimage)
                    .into(imageStatusViewHolderClass.profileImageIV);
            Glide.with(context).load(linkofimageStatus)
                    .into(imageStatusViewHolderClass.imageStatus);
            imageStatusViewHolderClass.objectProgressBar.setVisibility(View.INVISIBLE);
            String userN= model_imageStatus.getUsername();
            String admin1 = admin.getAdmin1();
            String admin2 = admin.getAdmin2();
            String admin3 = admin.getAdmin3();

            if(Objects.equals(userN, admin1) || Objects.equals(userN, admin2))
            {
                imageStatusViewHolderClass.verified.setVisibility(View.VISIBLE);
                imageStatusViewHolderClass.devtv.setText("Developer");
                imageStatusViewHolderClass.devtv.setVisibility(View.VISIBLE);

            } else if (Objects.equals(userN, admin3)) {
                imageStatusViewHolderClass.verified.setVisibility(View.VISIBLE);
                imageStatusViewHolderClass.devtv.setText("Tester");
                imageStatusViewHolderClass.devtv.setVisibility(View.VISIBLE);
            } else {
                imageStatusViewHolderClass.verified.setVisibility(View.INVISIBLE);
                imageStatusViewHolderClass.devtv.setVisibility(View.INVISIBLE);

            }



            FirebaseFirestore objectFirebaseFirestore =FirebaseFirestore.getInstance();
            FirebaseAuth objFirebaseAuth = FirebaseAuth.getInstance();

            String userEmail = objFirebaseAuth.getCurrentUser().getEmail();
            CollectionReference collectionRef = objectFirebaseFirestore.collection("UserProfileData");
            DocumentReference documentRef = collectionRef.document(userEmail);

            documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String Userprofile = documentSnapshot.getString("profileimageurl");
                        String UserName= documentSnapshot.getString("username");
                        USERNAME = UserName;
                        url = Userprofile;
                        if(Objects.equals(url, model_imageStatus.getProfileurl()) || userEmail.equals("cosmicriderrr@gmail.com"))
                        {
                            imageStatusViewHolderClass.deleteIV.setVisibility(View.VISIBLE);
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
            String documentID=getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getId();




            DocumentReference objectDocumentReferecnce = objectFirebaseFirestore.collection("ImageStatus")
                    .document(documentID).collection("Emotions")
                    .document(userEmail);

            objectDocumentReferecnce.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult().exists()) {
                        String currentFlag = task.getResult().getString("currentflag");
                        if (currentFlag.equals("love")) {

                            imageStatusViewHolderClass.HeartIV.setImageResource(R.drawable.icon_liked);

                        }
                        else {
                            imageStatusViewHolderClass.HeartIV.setImageResource(R.drawable.icon_love);


                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {



                }
            });

            FirebaseAuth objFirebaseAuth2 = FirebaseAuth.getInstance();

            final String userEmail2 = objFirebaseAuth2.getCurrentUser().getEmail();
            String documentID2=getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getId();


            DocumentReference objectDocumentReferecnce3 = objectFirebaseFirestore.collection("UserFavorite")
                    .document(userEmail2).collection("FavoriteImageStatus")
                    .document(documentID2);
            objectDocumentReferecnce3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult().exists()) {
                        String currentFlag = task.getResult().getString("currentflag");
                        if (currentFlag.equals("flag")) {

                            imageStatusViewHolderClass.favoriteIV.setImageResource(R.drawable.icon_fav_filled);

                        }
                        else {
                            imageStatusViewHolderClass.favoriteIV.setImageResource(R.drawable.icon_fav);

                        }
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {




                }
            });


            imageStatusViewHolderClass.HeartIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    imageStatusViewHolderClass.HeartIV.setImageResource(R.drawable.icon_liked);
                    FirebaseAuth objFirestoreAuth=FirebaseAuth.getInstance();
                    if(objFirestoreAuth!=null)
                    {
                        String userEmail = objFirestoreAuth.getCurrentUser().getEmail();
                        String documentID=getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getId();

                        FirebaseFirestore objectFirebaseFirestore=FirebaseFirestore.getInstance();
                        DocumentReference objectDocumentReferecnce = objectFirebaseFirestore.collection("ImageStatus")
                                .document(documentID).collection("Emotions")
                                .document(userEmail);


                        objectDocumentReferecnce.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.getResult().exists())
                                {
                                    String currentFlag=task.getResult().getString("currentflag");
                                    if(currentFlag.equals("love"))
                                    {

                                        objectDocumentReferecnce.update("currentflag","none");
                                        imageStatusViewHolderClass.HeartIV.setImageResource(R.drawable.icon_love);
                                        Long totalHearts= (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("nooflove");
                                        totalHearts--;
                                        getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                                .getReference().update("nooflove",totalHearts);

                                    }
                                    else {
                                        Map<String,Object> objectMap=new HashMap<>();
                                        objectMap.put("currentflag","love");
                                        objectFirebaseFirestore.collection("ImageStatus")
                                                .document(documentID).collection("Emotions")
                                                .document(userEmail)
                                                .set(objectMap);
                                        Long totalHearts= (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("nooflove");
                                        totalHearts++;
                                        getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                                .getReference().update("nooflove",totalHearts);
                                        objectDocumentReferecnce.update("currentflag","love");
                                    }

                                }
                                else {
                                    Map<String,Object> objectMap=new HashMap<>();
                                    objectMap.put("currentflag","love");
                                    objectFirebaseFirestore.collection("ImageStatus")
                                            .document(documentID).collection("Emotions")
                                            .document(userEmail)
                                            .set(objectMap);
                                    Long totalHearts= (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("nooflove");
                                    totalHearts++;
                                    getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                            .getReference().update("nooflove",totalHearts);
                                    objectDocumentReferecnce.update("currentflag","love");
                                }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(imageStatusViewHolderClass.HeartIV.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(imageStatusViewHolderClass.HeartIV.getContext(), "No user is online", Toast.LENGTH_SHORT).show();

                    }

                }
            });

            imageStatusViewHolderClass.commentIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String documentID=getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getId();
                    Context objectContext = imageStatusViewHolderClass.commentIV.getContext();

                    Intent objectIntent=new Intent(objectContext, ImageCommentPage.class);
                    objectIntent.putExtra("documentId",documentID);

                    objectContext.startActivity(objectIntent);

                }
            });


            imageStatusViewHolderClass.deleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth objectFirebaseAuth=FirebaseAuth.getInstance();
                    if(url.equals(model_imageStatus.getProfileurl()) || userEmail.equals("cosmicriderrr@gmail.com"))
                    {

                        FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();
                        objectFirebaseFirestore.collection("ImageStatus")
                                .document(getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(imageStatusViewHolderClass.deleteIV.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(imageStatusViewHolderClass.deleteIV.getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                    else
                    {
                        Toast.makeText(imageStatusViewHolderClass.deleteIV.getContext(), "You have no rights to delete this status", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            imageStatusViewHolderClass.favoriteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    objectDocumentReferecnce3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                String currentFlag = task.getResult().getString("currentflag");
                                if (currentFlag.equals("flag")) {

                                    imageStatusViewHolderClass.favoriteIV.setImageResource(R.drawable.icon_fav);
                                    Map<String, Object> objectMap = new HashMap<>();
                                    objectMap.put("currentflag","none");
                                    FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();
                                    String currentLoggedInUser=objectFirebaseAuth.getCurrentUser().getEmail();
                                    objectFirebaseFirestore.collection("UserFavorite").document(currentLoggedInUser)
                                            .collection("FavoriteImageStatus")
                                            .document(documentID).set(objectMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    objectFirebaseFirestore.collection("UserFavorite").document(currentLoggedInUser).collection("FavoriteImageStatus")
                                                            .document(getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getId())
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(imageStatusViewHolderClass.favoriteIV.getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(imageStatusViewHolderClass.deleteIV.getContext(), "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                                                                    imageStatusViewHolderClass.favoriteIV.setImageResource(R.drawable.icon_fav_filled);
                                                                }
                                                            });




                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(imageStatusViewHolderClass.favoriteIV.getContext(), "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                                                    imageStatusViewHolderClass.favoriteIV.setImageResource(R.drawable.icon_fav_filled);
                                                }
                                            });

                                }
                                else
                                {
                                    imageStatusViewHolderClass.favoriteIV.setImageResource(R.drawable.icon_fav_filled);
                                    String documentID=getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getId();
                                    FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore .getInstance();
                                    DocumentReference objectDocumentReference = objectFirebaseFirestore.collection("ImageStatus").document(documentID);
                                    objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String username = model_imageStatus.getUsername();
                                            String userEmail =documentSnapshot.getString("useremail");
                                            String statusurl = documentSnapshot.getString("statusimageurl");
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
                                                        .collection("FavoriteImageStatus")
                                                        .document(documentID).set(objectMap)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                                Toast.makeText(imageStatusViewHolderClass.favoriteIV.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();



                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(imageStatusViewHolderClass.favoriteIV.getContext(), "Failed to add into favorites", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });


                                            }
                                            else {
                                                Toast.makeText(imageStatusViewHolderClass.favoriteIV.getContext(),"User not online", Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                                }
                            }
                            else {

                                imageStatusViewHolderClass.favoriteIV.setImageResource(R.drawable.icon_fav_filled);
                                String documentID=getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getId();
                                FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore .getInstance();
                                DocumentReference objectDocumentReference = objectFirebaseFirestore.collection("ImageStatus").document(documentID);
                                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String username = model_imageStatus.getUsername();
                                        String userEmail =documentSnapshot.getString("useremail");
                                        String statusurl = documentSnapshot.getString("statusimageurl");
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
                                                    .collection("FavoriteImageStatus")
                                                    .document(documentID).set(objectMap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                            Toast.makeText(imageStatusViewHolderClass.favoriteIV.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();



                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(imageStatusViewHolderClass.favoriteIV.getContext(), "Failed to add into favorites", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });


                                        }
                                        else {
                                            Toast.makeText(imageStatusViewHolderClass.favoriteIV.getContext(),"User not online", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

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




        }
        catch(IndexOutOfBoundsException e){
                // catch the exception and do nothing
        }
    }

    @NonNull
    @Override
    public ImageStatusViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageStatusViewHolderClass(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_image_status, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull ImageStatusViewHolderClass holder) {
        super.onViewRecycled(holder);
        Glide.with(context).clear(holder.profileImageIV);
        Glide.with(context).clear(holder.imageStatus);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ImageStatusViewHolderClass holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.objectProgressBar.getVisibility() == View.VISIBLE) {
            holder.objectProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public static class ImageStatusViewHolderClass extends RecyclerView.ViewHolder {
        ImageView imageStatus,verified;
        TextView userName, StatusDesc, statusDate, heartCount, commentCount, devtv,commentIV;
        ImageView favoriteIV, deleteIV, profileImageIV, HeartIV;
        ProgressBar objectProgressBar;




        public ImageStatusViewHolderClass(@NonNull View itemView) {
            super(itemView);

            verified = itemView.findViewById(R.id.verified);
            devtv = itemView.findViewById(R.id.admin);

            imageStatus = itemView.findViewById(R.id.idIVPost);
            favoriteIV = itemView.findViewById(R.id.idIVfavorite);
            deleteIV = itemView.findViewById(R.id.idIVdelete);
            profileImageIV = itemView.findViewById(R.id.idCVUserprofile);
            HeartIV = itemView.findViewById(R.id.idIVHeart);
            commentIV=itemView.findViewById(R.id.idTVComment);
            userName = itemView.findViewById(R.id.idTVUsername);
            StatusDesc = itemView.findViewById(R.id.idTVPostDesc);
            statusDate = itemView.findViewById(R.id.idTVDate);
            heartCount = itemView.findViewById(R.id.idTVheartcount);
            commentCount = itemView.findViewById(R.id.idTVCommentCount);
            objectProgressBar = itemView.findViewById(R.id.idProgressBar);
        }
    }
}
