package com.example.ibrahim.snc.Fregments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.file.FileResource;
import com.example.ibrahim.snc.MainActivity;
import com.example.ibrahim.snc.R;
import com.example.ibrahim.snc.models.notification_class;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class notification extends Fragment {

    private String CollegeName,FileName="myFile";
    private String mCurrentUser;
    private Button yy;
    private View v;
    private RecyclerView mntofication;
    private FirebaseRecyclerAdapter mRecyclerAdapter;
    public notification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_notification, container, false);
        CollegeName=print();

        mntofication=(RecyclerView)v.findViewById(R.id.notifications);
        mntofication.setLayoutManager(new LinearLayoutManager(getActivity()));
        mntofication.setHasFixedSize(true);

        loadNotification();



        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Notification").child(CollegeName);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childsnapshot:dataSnapshot.getChildren()) {
                    if (childsnapshot.hasChildren()) {

                        HashMap<String, Object> hashMap = (HashMap) childsnapshot.getValue();
                        String a = (String) hashMap.get("UserName");
                        String b = (String) hashMap.get("Type");
                        Toast.makeText(getActivity(), a + "   " + b, Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Toast.makeText(getActivity(),"No new Notification",Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return v;


    }

    private void loadNotification() {
        DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Notification").child(CollegeName);


        mRecyclerAdapter=new FirebaseRecyclerAdapter<notification_class,UsersViewHolder>(notification_class.class, R.layout.notification_single, UsersViewHolder.class, mref) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, notification_class model, int position) {

                String userName=model.getUserName();

                viewHolder.setName(userName,viewHolder.getAdapterPosition(),CollegeName,model.getType(),CollegeName);


            }
        };
        mntofication.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();



    }

    private String print() {
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("College Name", "nope");
        return value;

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView mUser;
        Button mm;

        public UsersViewHolder(View itemView) {
            super(itemView);
            view=itemView;

        }

        public void setName(final String name, final int adapterPosition, final String CollegeName, String type, final String Collegename){

            mm=(Button)view.findViewById(R.id.accept);
            Button a=(Button)view.findViewById(R.id.ignore);
            TextView hh=(TextView)view.findViewById(R.id.aaccept);
            mUser=(TextView)view.findViewById(R.id.blog_user_name);
            if(type.equals("Accepted")){

                a.setVisibility(View.GONE);
                mm.setVisibility(View.GONE);
                mUser.setText(name);
                hh.setText("Is Now Your College Member");



            }
            else {

                hh.setVisibility(View.GONE);
                mUser.setText(name);
                mm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
                        DatabaseReference mcollegeRef = FirebaseDatabase.getInstance().getReference().child("College Users").child(CollegeName);

                        HashMap huser = new HashMap();
                        huser.put("UserName", name);
                        mUserRef.push().setValue(huser);
                        HashMap collegeuser = new HashMap();
                        collegeuser.put("UserName", name);
                        mcollegeRef.push().setValue(collegeuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    DatabaseReference mUpdateRef = FirebaseDatabase.getInstance().getReference().child("User").child(name);

                                    mUpdateRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            for (DataSnapshot childsnaptshot : dataSnapshot.getChildren()) {

                                                childsnaptshot.getRef().child("CollegeMember").setValue(CollegeName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        removethatnotification(Collegename);

                                                    }
                                                });


                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }

                            }
                        });


                    }
                });

            }

        }

        private void removethatnotification(String CollegeName) {

            DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Notification").child(CollegeName);
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot childsnapshot:dataSnapshot.getChildren()){

                        String a="Accepted";

                        childsnapshot.getRef().child("Type").setValue(a).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(view.getContext(),"Successfully Added into the Group",Toast.LENGTH_SHORT).show();

                            }
                        });


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }


    }

}


