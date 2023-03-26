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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.project.seedle.AdaptersClasses.TextStatusAdapterClass;
import com.project.seedle.ModelClassess.Model_TextStatus;
import com.project.seedle.R;



public class TextThoughts extends Fragment {


    public TextThoughts() {

    }
    //XML Variable
    private RecyclerView objectRecyclerView;

    //Class Vairable
    private  View parent;
    private TextStatusAdapterClass objectTextStatusAdapterClass;


    //Firebase Variable
    FirebaseFirestore objectFirebaseFirestore;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent=inflater.inflate(R.layout.fragment_text_thoughts, container, false);
        objectRecyclerView=parent.findViewById(R.id.textStatus_RV);
        objectFirebaseFirestore=FirebaseFirestore.getInstance();
        addStatusToRV();


        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            objectTextStatusAdapterClass.startListening();

        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {


        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void addStatusToRV()
    {
        try
        {
            Query objectQuery=objectFirebaseFirestore.collection("TextStatus")
                    .orderBy("currentdatetime",Query.Direction.DESCENDING);

            FirestoreRecyclerOptions<Model_TextStatus> options
                    =new FirestoreRecyclerOptions.Builder<Model_TextStatus>()
                    .setQuery(objectQuery,Model_TextStatus.class).build();
            objectTextStatusAdapterClass =new TextStatusAdapterClass(options);
            objectRecyclerView.setAdapter(objectTextStatusAdapterClass);

            objectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




}