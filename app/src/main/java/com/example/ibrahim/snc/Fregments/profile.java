package com.example.ibrahim.snc.Fregments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ibrahim.snc.R;
import com.example.ibrahim.snc.adapters.PostAdapter;
import com.example.ibrahim.snc.models.postclass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class profile extends Fragment {


    public profile() {
        // Required empty public constructor
    }
    private List<postclass> postlist;
    private PostAdapter postAdapter;
    private View v;
    private String currentUserid;
    private FirebaseFirestore firebaseFirestore;
    private String FileName = "myFile",CollegeName;
    private RecyclerView recyclerView;
    public String name;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_profile, container, false);
        currentUserid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollegeName=print();
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView=(RecyclerView)v.findViewById(R.id.user_profile);
      //  recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        postlist = new ArrayList<>();
        postAdapter = new PostAdapter(postlist);
        recyclerView.setAdapter(postAdapter);

        Query query = firebaseFirestore.collection("College sckn").document("College Post").collection(CollegeName).whereEqualTo("UploaderID",currentUserid);


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        postclass post = documentChange.getDocument().toObject(postclass.class);
                        postlist.add(post);

                        postAdapter.notifyDataSetChanged();
                    }
                }
            }
        });





        return v;

    }

    private String print() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        return sharedPreferences.getString("College Name", "nope");

    }


}
