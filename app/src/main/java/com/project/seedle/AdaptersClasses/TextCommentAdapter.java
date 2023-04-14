package com.project.seedle.AdaptersClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.seedle.Activities.TextCommentPage;
import com.project.seedle.Admin;
import com.project.seedle.ModelClassess.Model_Comment;
import com.project.seedle.ModelClassess.Model_TextStatus;
import com.project.seedle.R;

import java.util.Objects;

public class TextCommentAdapter extends FirestoreRecyclerAdapter<Model_Comment,TextCommentAdapter.GetTextCommentViewHolder> {


    public Admin admin = new Admin();



    public TextCommentAdapter(@NonNull FirestoreRecyclerOptions<Model_Comment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GetTextCommentViewHolder getTextCommentViewHolder, int i, @NonNull Model_Comment model_comment) {
        getTextCommentViewHolder.userNameTV.setText(model_comment.getUsername());
        getTextCommentViewHolder.commentDateTV.setText(model_comment.getCurrentdatetime());
        getTextCommentViewHolder.commentTV.setText(model_comment.getComment());
        String profileImageUri=model_comment.getProfilepicurl();

        String userN = model_comment.getUsername();
        String admin1 = admin.getAdmin1();
        String admin2 = admin.getAdmin2();
        String admin3 = admin.getAdmin3();


        if(Objects.equals(userN, admin1) || Objects.equals(userN, admin2))
        {
            getTextCommentViewHolder.verified.setVisibility(View.VISIBLE);
            getTextCommentViewHolder.devTV.setText("Developer");
            getTextCommentViewHolder.devTV.setVisibility(View.VISIBLE);

        } else if (Objects.equals(userN, admin3)) {
            getTextCommentViewHolder.verified.setVisibility(View.VISIBLE);
            getTextCommentViewHolder.devTV.setText("Tester");
            getTextCommentViewHolder.devTV.setVisibility(View.VISIBLE);
        } else {
            getTextCommentViewHolder.verified.setVisibility(View.INVISIBLE);
            getTextCommentViewHolder.devTV.setVisibility(View.INVISIBLE);

        }

        Glide.with(getTextCommentViewHolder.userProfileIV.getContext())
                .load(profileImageUri).into(getTextCommentViewHolder.userProfileIV);



    }

    @NonNull
    @Override
    public GetTextCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetTextCommentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_text_comments,parent,false));
    }

    public class GetTextCommentViewHolder extends RecyclerView.ViewHolder
    {
        ImageView userProfileIV,verified;
        TextView userNameTV,commentDateTV,commentTV,devTV;

        public GetTextCommentViewHolder(@NonNull View itemView) {
            super(itemView);
            devTV = itemView.findViewById(R.id.admin);
            userProfileIV=itemView.findViewById(R.id.model_addcomment_profile);
            userNameTV=itemView.findViewById(R.id.model_addcomment_userName);
            commentDateTV=itemView.findViewById(R.id.model_addcomment_currentDateTime);
            commentTV=itemView.findViewById(R.id.model_addcomment_comment);
            verified = itemView.findViewById(R.id.verified);

        }
    }
}
