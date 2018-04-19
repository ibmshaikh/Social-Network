package com.example.ibrahim.snc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CollegeSerch extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private String Username,User_Email;
    private int User_age;
    private RecyclerView mResultList;
    private Button create;
    private Uri User_profile_uri;
    private TextView show;
    private DatabaseReference mUserDatabase;
    private FirebaseRecyclerAdapter<College_class, UsersViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_serch);

        create=(Button)findViewById(R.id.create_college);
        show=(TextView)findViewById(R.id.textView);

        Username=getIntent().getStringExtra("Name");

        User_age=getIntent().getIntExtra("AGE",15);

        User_Email=getIntent().getStringExtra("Email");


        String uriString = getIntent().getStringExtra("user_profile_uri");
        User_profile_uri = Uri.parse(uriString);


       // Toast.makeText(CollegeSerch.this, s, Toast.LENGTH_SHORT).show();

        mUserDatabase = FirebaseDatabase.getInstance().getReference("College");

        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) findViewById(R.id.search_btn);

        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));
        create.setVisibility(View.GONE);
        show.setVisibility(View.GONE);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = mSearchField.getText().toString();

                DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("College");

               // College_class c=new College_class();
              //  c.setCollege_Description("Good College");
              ///  c.setCollege_Name("Rizvi College OF Engineering");
               /* mref.push().setValue(c).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CollegeSerch.this,"Successfully Created College",Toast.LENGTH_SHORT).show();
                    }
                });*/
                firebaseUserSearch(searchText);

            }
        });
    }

    private void firebaseUserSearch(String searchText) {

        //Toast.makeText(CollegeSerch.this, "Started Search", Toast.LENGTH_LONG).show();

        final Query firebaseSearchQuery = mUserDatabase.orderByChild("College_Name").startAt(searchText).endAt(searchText + "\uf8ff");

        firebaseSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {

                    create.setVisibility(View.GONE);
                    show.setVisibility(View.GONE);
                    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<College_class, UsersViewHolder>(

                            College_class.class,
                            R.layout.college_single_layout,
                            UsersViewHolder.class,
                            firebaseSearchQuery

                    ) {
                        @Override
                        protected void populateViewHolder(UsersViewHolder viewHolder, final College_class model, int position) {


                            viewHolder.setDetails(getApplicationContext(), model.getCollege_Name(), model.getCollege_Description(), "IImm");
                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(CollegeSerch.this, College_view_profile.class);
                                    i.putExtra("College Name",model.getCollege_Name());
                                    i.putExtra("Name",Username);
                                    i.putExtra("Age",User_age);
                                    i.putExtra("Email",User_Email);
                                    i.putExtra("user_profile_uri",User_profile_uri.toString());

                                    startActivity(i);
                                }
                            });

                        }
                    };

                    mResultList.setAdapter(firebaseRecyclerAdapter);


                } else if (!dataSnapshot.exists()) {

                    show.setVisibility(View.VISIBLE);
                    create.setVisibility(View.VISIBLE);

                    create.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent i = new Intent(CollegeSerch.this, FillCollege.class);

                            i.putExtra("Name",Username);
                            i.putExtra("Age",User_age);
                            i.putExtra("Email",User_Email);
                            i.putExtra("user_profile_uri",User_profile_uri.toString());

                            startActivity(i);

                        }
                    });

                    Toast.makeText(CollegeSerch.this, "College is not Exixst", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDetails(Context ctx, String userName, String userStatus, String userImage){

            TextView user_name = (TextView) mView.findViewById(R.id.name_text);
            TextView user_status = (TextView) mView.findViewById(R.id.status_text);
            ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);


            user_name.setText(userName);
            user_status.setText(userStatus);

            Glide.with(ctx).load(userImage).into(user_image);


        }




    }
}
