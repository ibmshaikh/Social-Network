package com.example.ibrahim.snc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FillUserDetails extends AppCompatActivity {

    private String Name,Email;
    private double Phone_Number;
    private int AGE;
    public static final int PICK_IMAGE_REQUEST=123;
    EditText Phone_N,emmial,aage,NName;
    public Button next;
    private String FileName="myFile";
    private String currentUserId;
    private DatabaseReference mCheck;
    private Uri imageUri=null;
    private ProgressDialog progressDialog;
    private String a=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_user_details);
        progressDialog=new ProgressDialog(FillUserDetails.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        cheack();

    }

    private void cheack() {

        currentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference aref=FirebaseDatabase.getInstance().getReference().child("Users");

        Query mref =aref.orderByChild("UserName").equalTo(currentUserId);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){
                    progressDialog.dismiss();
                    finish();
                    startActivity(new Intent(FillUserDetails.this,Second_Activity.class));

                }
                else if (!dataSnapshot.exists()){
                    progressDialog.dismiss();
                    filldetails();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {



            }
        });



    }


    //------------------------------Gallary Result----------------------------//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                CircleImageView imageView = (CircleImageView) findViewById(R.id.profile_image);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
    //-------------------------------------END-------------------------------------//






    private void filldetails() {

        ImageView bb=(ImageView)findViewById(R.id.imageView3);

        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

        Phone_N=(EditText)findViewById(R.id.phone_number);
        emmial=(EditText)findViewById(R.id.email);
        aage=(EditText) findViewById(R.id.age);
        NName=(EditText)findViewById(R.id.name);
        next=(Button)findViewById(R.id.button3);
        //----------------------Image Picker------------------//
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name = NName.getText().toString();
                Email = emmial.getText().toString();
                AGE = Integer.parseInt(aage.getText().toString());
                //Phone_Number=Double.parseDouble(Phone_N.getText().toString());
                Intent ii = new Intent(FillUserDetails.this, CollegeSerch.class);
                ii.putExtra("Name", Name);
                ii.putExtra("Email", Email);
                ii.putExtra("AGE", AGE);
                ii.putExtra("user_profile_uri", imageUri.toString());
                // ii.putExtra("Phone Number",Phone_Number);
                startActivity(ii);
            }
        });

    }
}
