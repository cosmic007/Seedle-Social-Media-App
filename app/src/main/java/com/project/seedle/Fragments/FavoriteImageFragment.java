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
import com.project.seedle.AdaptersClasses.GetFavoriteImageStatusAdapter;
import com.project.seedle.ModelClassess.Model_Favorite_Image_Status;
import com.project.seedle.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteImageFragment extends Fragment {


    private RecyclerView objectRecyclerView;

    private FirebaseFirestore objectFirebaseFirestore;

    private FirebaseAuth objectFirebaseAuth;

    private View parent;

    private GetFavoriteImageStatusAdapter objectGetFavoriteImageStatusAdapter;




    public FavoriteImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent = inflater.inflate(R.layout.fragment_favorite_image, container, false);


        initializeJavaObjects();
        getStatusIntoRV();

        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        objectGetFavoriteImageStatusAdapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        objectGetFavoriteImageStatusAdapter.stopListening();
    }

    private void getStatusIntoRV()
    {
        try {
            objectFirebaseAuth = FirebaseAuth.getInstance();

            if(objectFirebaseAuth!=null)
            {
                String currentLoggedinUser = objectFirebaseAuth.getCurrentUser().getEmail().toString();
                Query objectQuery = objectFirebaseFirestore.collection("UserFavorite")
                        .document(currentLoggedinUser)
                        .collection("FavoriteImageStatus").orderBy("currentdatetime",Query.Direction.DESCENDING);

                FirestoreRecyclerOptions<Model_Favorite_Image_Status> objectOptions = new FirestoreRecyclerOptions.Builder<Model_Favorite_Image_Status>()
                        .setQuery(objectQuery,Model_Favorite_Image_Status.class).build();



                objectGetFavoriteImageStatusAdapter = new GetFavoriteImageStatusAdapter(objectOptions);
                objectRecyclerView.setAdapter(objectGetFavoriteImageStatusAdapter);

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


    private void initializeJavaObjects()
    {
        try {

            objectRecyclerView = parent.findViewById(R.id.favorite_ImageStatusRV);
            objectFirebaseFirestore = FirebaseFirestore.getInstance();

        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}