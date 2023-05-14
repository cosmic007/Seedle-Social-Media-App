package com.project.seedle.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.project.seedle.AdaptersClasses.GetFavoriteTextStatusAdapter;
import com.project.seedle.ModelClassess.ModelFavoriteTextStatus;
import com.project.seedle.ModelClassess.Model_TextStatus;
import com.project.seedle.R;


public class favoriteTextFragment extends Fragment {

    private View parent;
    private RecyclerView objectRecyclerView;

    GetFavoriteTextStatusAdapter objectGetFavoriteTextStatusAdapter;
    FirebaseAuth objectFirebaseAuth;
    FirebaseFirestore objectFirebaseFirestore;



    public favoriteTextFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parent= inflater.inflate(R.layout.fragment_favorite_text, container, false);

        initializeVariables();

        getStatusintoRV();
        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        objectGetFavoriteTextStatusAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        objectGetFavoriteTextStatusAdapter.stopListening();
    }

    private void getStatusintoRV()
    {
        try {

            objectFirebaseAuth = FirebaseAuth.getInstance();
            if(objectFirebaseAuth!=null)
            {
                String currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail();
                Query objectQuery = objectFirebaseFirestore.collection("UserFavorite")
                        .document(currentLoggedInUser).collection("FavoriteTextStatus").orderBy("currentdatetime",Query.Direction.DESCENDING);

                FirestoreRecyclerOptions<ModelFavoriteTextStatus> objectOptions=
                        new FirestoreRecyclerOptions.Builder<ModelFavoriteTextStatus>().setQuery(
                                objectQuery, ModelFavoriteTextStatus.class
                        ).build();

                objectGetFavoriteTextStatusAdapter = new GetFavoriteTextStatusAdapter(objectOptions);
                objectRecyclerView.setAdapter(objectGetFavoriteTextStatusAdapter);
                objectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            }
            else {
                Toast.makeText(getContext(), "User not online", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void initializeVariables()
    {
        try{

            objectRecyclerView = parent.findViewById(R.id.favoriteTS_RV);
            objectFirebaseFirestore = FirebaseFirestore.getInstance();


        }
        catch(Exception e)
        {

            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


}