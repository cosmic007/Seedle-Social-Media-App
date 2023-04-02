package com.project.seedle.AdaptersClasses;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
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
import com.project.seedle.ModelClassess.Model_TextStatus;
import com.project.seedle.R;

import java.util.HashMap;
import java.util.Map;



public class TextStatusAdapterClass extends FirestoreRecyclerAdapter<Model_TextStatus,TextStatusAdapterClass.TextStatusViewHolder> {


    public String url;


    public TextStatusAdapterClass(@NonNull FirestoreRecyclerOptions<Model_TextStatus> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TextStatusViewHolder textStatusViewHolder, int i, @NonNull Model_TextStatus model_textStatus) {
        textStatusViewHolder.usernameTV.setText(model_textStatus.getUsername());
        textStatusViewHolder.dateTimeTV.setText(model_textStatus.getCurrentdatetime());
        textStatusViewHolder.userStatusTV.setText((model_textStatus.getStatus()));

        textStatusViewHolder.heartCountTV.setText(Integer.toString(model_textStatus.getNooflove()));

        textStatusViewHolder.hahaCountTV.setText(Integer.toString(model_textStatus.getNoofhaha()));
        textStatusViewHolder.sadCountTV.setText(Integer.toString(model_textStatus.getNofsad()));
        textStatusViewHolder.commentCountTV.setText(Integer.toString(model_textStatus.getNoofcomments()));
        String linkOfProfileImage= model_textStatus.getProfileurl();
        Glide.with(textStatusViewHolder.profileIV.getContext())
                .load(linkOfProfileImage).into(textStatusViewHolder.profileIV);

        FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();
        String EMAIL= objectFirebaseAuth.getCurrentUser().getEmail();

        CollectionReference collectionRef = objectFirebaseFirestore.collection("UserProfileData");
        DocumentReference documentRef = collectionRef.document(EMAIL);



        documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String Userprofile = documentSnapshot.getString("profileimageurl");
                    url = Userprofile;
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




        textStatusViewHolder.heartIV.setOnClickListener(new View.OnClickListener() {
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

                objectContext.startActivity(objectIntent);
            }
        });
        textStatusViewHolder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objectFirebaseAuth=FirebaseAuth.getInstance();
                if(url.equals(model_textStatus.getProfileurl()))
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
        ImageView heartIV,hahaIV,sadIV,deleteIV,favoriteIV;

        TextView usernameTV,dateTimeTV,userStatusTV,heartCountTV,hahaCountTV,sadCountTV,commentCountTV,commentIV;

        public TextStatusViewHolder(@NonNull View itemView) {
            super(itemView);
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
