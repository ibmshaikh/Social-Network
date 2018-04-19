package com.example.ibrahim.snc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.request.target.ImageViewTarget;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FillCollege extends AppCompatActivity {

    private String Username,User_Email;
    private int User_age;
    private Uri User_profile_uri,collegeUri=null;
    private String Userrrrrrr;
    public static final int PICK_IMAGE_REQUEST=123;
    private ImageView button;
    public ProgressDialog mdi;
    private CircleImageView CollegeImage;
    private String College_Name,College_Description,College_location;
    private EditText C_Name,C_Des,C_loc;
    private Button submit;
    private String FileName="myFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_college);


        Userrrrrrr= FirebaseAuth.getInstance().getCurrentUser().getUid();
        mdi = new ProgressDialog(FillCollege.this);
        Username=getIntent().getStringExtra("Name");
        User_age=getIntent().getIntExtra("AGE",15);
        User_Email=getIntent().getStringExtra("Email");
        button=(ImageView)findViewById(R.id.imageView3);
        CollegeImage=(CircleImageView)findViewById(R.id.college_image);
        C_Name=(EditText)findViewById(R.id.Colege_Name);
        C_Des=(EditText)findViewById(R.id.College_Description);
        C_loc=(EditText)findViewById(R.id.College_location);
        submit=(Button)findViewById(R.id.submit_college);

        String uriString = getIntent().getStringExtra("user_profile_uri");
        User_profile_uri = Uri.parse(uriString);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (collegeUri!=null){

                        fillcollegeDetails();

                  }
                  else if (collegeUri==null){
                    Toast.makeText(FillCollege.this,"Image Should Not be Empty",Toast.LENGTH_LONG).show();

                }
            }


        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            collegeUri = data.getData();

        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    private void fillcollegeDetails() {

        mdi.setTitle("Processing");
        mdi.setMessage("Creating The College");
        mdi.setCanceledOnTouchOutside(false);
        mdi.show();

        College_Name = C_Name.getText().toString();
        College_Description = C_Des.getText().toString();
        College_location = C_loc.getText().toString();
        //Phone_Number=Double.parseDouble(Phone_N.getText().toString());
        StorageReference fileToUpload = FirebaseStorage.getInstance().getReference().child("Images").child(getFileName(collegeUri));
        fileToUpload.putFile(collegeUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                DatabaseReference mCollegeDetails = FirebaseDatabase.getInstance().getReference().child("College Details").child(College_Name);

                DatabaseReference muserref = FirebaseDatabase.getInstance().getReference().child("Users");

                final DatabaseReference mCollege_admin = FirebaseDatabase.getInstance().getReference().child("College Admin").child(College_Name);
                DatabaseReference college = FirebaseDatabase.getInstance().getReference().child("College");
                DatabaseReference no_of_users = FirebaseDatabase.getInstance().getReference().child("No of CollegeUsers").child(College_Name);
                DatabaseReference cusersid = FirebaseDatabase.getInstance().getReference().child("College Users").child(College_Name);
                HashMap mm = new HashMap();
                mm.put("College_Name", College_Name);
                college.push().setValue(mm);
                HashMap his = new HashMap();
                his.put("UserName", Userrrrrrr);
                muserref.push().setValue(his);
                HashMap h = new HashMap();
                h.put("College_Profile", downloadUrl);
                h.put("College_name", College_Name);
                h.put("College Description", College_Description);
                h.put("College location", College_location);
                h.put("Timestamp Created", ServerValue.TIMESTAMP);
                no_of_users.push().setValue(1);
                cusersid.push().setValue(his);

                mCollegeDetails.push().setValue(h).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HashMap hh = new HashMap();
                        hh.put("Admin", Userrrrrrr);
                        mCollege_admin.push().setValue(hh);

                    }
                });


            }
        });

        StorageReference fileToUpload2 = FirebaseStorage.getInstance().getReference().child("Images").child(getFileName(User_profile_uri));
        fileToUpload2.putFile(User_profile_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                DatabaseReference muserDetaisl = FirebaseDatabase.getInstance().getReference().child("User").child(Userrrrrrr);
                HashMap hh = new HashMap();
                hh.put("User Name", Username);
                hh.put("Age", User_age);
                hh.put("Email", User_Email);
                hh.put("UID", Userrrrrrr);
                hh.put("Admin", "YESSSS");
                hh.put("Profile",downloadUrl);
                hh.put("CollegeMember", College_Name);
                muserDetaisl.push().setValue(hh).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        save(College_Name);
                        Intent i = new Intent(FillCollege.this, Second_Activity.class);
                        Toast.makeText(FillCollege.this, "College Group Created Successfully", Toast.LENGTH_SHORT).show();
                        mdi.dismiss();
                        startActivity(i);

                    }
                });

            }
        });


    }


    private void save(String college_Name) {

        SharedPreferences sharedPreferences=getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("College Name",college_Name);
        editor.commit();

    }



}
