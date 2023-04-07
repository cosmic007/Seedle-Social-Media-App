package com.project.seedle.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.project.seedle.AdaptersClasses.ImageStatusAdapterClass;
import com.project.seedle.ModelClassess.Model_ImageStatus;
import com.project.seedle.R;

public class ImageThoughts extends Fragment {

    private View parent;
    private RecyclerView objectRecyclerView;
    private ImageStatusAdapterClass objectImageStatusAdapterClass;
    private FirebaseFirestore objectFirebaseFirestore;

    public ImageThoughts() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parent = inflater.inflate(R.layout.fragment_image_thoughts, container, false);
        objectRecyclerView = parent.findViewById(R.id.imageStatus_RV);
        objectFirebaseFirestore = FirebaseFirestore.getInstance();

        // Querying Firestore to get data
        Query objectQuery = objectFirebaseFirestore.collection("ImageStatus")
                .orderBy("flag", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Model_ImageStatus> objectOptions
                =new FirestoreRecyclerOptions.Builder<Model_ImageStatus>()
                .setQuery(objectQuery, Model_ImageStatus.class).build();

        // Initializing the adapter for RecyclerView
        objectImageStatusAdapterClass = new ImageStatusAdapterClass(objectOptions, getActivity());

        // Setting the adapter for RecyclerView

        objectRecyclerView.setAdapter(objectImageStatusAdapterClass);
        objectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return parent;
    }

    // Method to start listening to the FirestoreRecyclerAdapter
    @Override
    public void onStart() {
        super.onStart();
        objectImageStatusAdapterClass.startListening();
    }

    // Method to stop listening to the FirestoreRecyclerAdapter
    @Override
    public void onStop() {
        super.onStop();
        objectImageStatusAdapterClass.stopListening();
    }
}
