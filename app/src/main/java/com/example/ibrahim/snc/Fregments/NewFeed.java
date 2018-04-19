package com.example.ibrahim.snc.Fregments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ibrahim.snc.NewsFeed;
import com.example.ibrahim.snc.Post;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewFeed extends Fragment {

    View v;
    private FloatingActionButton floatingActionButton;
    private RecyclerView blog_list_view;
    private List<postclass> postlist;
    private FirebaseFirestore firebaseFirestore;
    private PostAdapter postAdapter;
    private String FileName = "myFile", CollegeName, currentUserId;
    private DatabaseReference mref;

    public NewFeed() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_new_feed, container, false);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.floatingActionButton4);
        blog_list_view = (RecyclerView) v.findViewById(R.id.blog_list_view);
        postlist = new ArrayList<>();
        postAdapter = new PostAdapter(postlist);
        blog_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        blog_list_view.setAdapter(postAdapter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        getCollegeName();
        CollegeName = print();

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> hashMap = (HashMap) childsnapshot.getValue();
                    String collegeName = (String) hashMap.get("CollegeMember");
                    String name=(String)hashMap.get("User Name");
                    String Profile= (String) hashMap.get("Profile");
                    String Email= (String) hashMap.get("Email");
                    Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
                    save(collegeName,name,Profile,Email);


                    Query query = firebaseFirestore.collection("College sckn").document("College Post").collection(collegeName).orderBy("timestamp", Query.Direction.DESCENDING);
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



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Post.class);
                i.putExtra("CollegeName", CollegeName);
                startActivity(i);
            }
        });
        return v;
    }





    private String print() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("College Name", "nope");
        return value;

    }

    private void getCollegeName() {

        final String collegeNames = null;
        mref = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childsnapshot : dataSnapshot.getChildren()) {
                    HashMap<String, Object> hashMap = (HashMap) childsnapshot.getValue();
                    String collegeName = (String) hashMap.get("CollegeMember");
                    String name=(String)hashMap.get("User Name");
                    String Profile= (String) hashMap.get("Profile");
                    String Email= (String) hashMap.get("Email");
                    Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
                    save(collegeName,name,Profile,Email);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void save(String collegeName, String name, String profile, String email) {

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("College Name",collegeName);
        editor.putString("Username",name);
        editor.putString("Email",email);
        editor.putString("Profile",profile);
        editor.commit();

    }


}
