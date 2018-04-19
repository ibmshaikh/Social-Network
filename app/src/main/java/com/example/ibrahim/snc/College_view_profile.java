package com.example.ibrahim.snc;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class College_view_profile extends AppCompatActivity {

    private String clgName,currentUserid;
    private String Username,User_Email;
    private int User_age;
    private Uri User_profile_uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_view_profile);


        clgName=getIntent().getStringExtra("College Name");
        currentUserid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        TextView m=(TextView)findViewById(R.id.textView2);
        m.setText(clgName);


        Username=getIntent().getStringExtra("Name");

        User_age=getIntent().getIntExtra("AGE",15);

        User_Email=getIntent().getStringExtra("Email");


        String uriString = getIntent().getStringExtra("user_profile_uri");
        User_profile_uri = Uri.parse(uriString);

        Button b=(Button)findViewById(R.id.button4);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("Notification").child(clgName);

                final DatabaseReference mUserRef=FirebaseDatabase.getInstance().getReference().child("User").child(currentUserid);

                HashMap h=new HashMap();
                h.put("UserName",currentUserid);
                h.put("Type","Request");
                mref.push().setValue(h).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            StorageReference fileToUpload2 = FirebaseStorage.getInstance().getReference().child("Images").child(getFileName(User_profile_uri));

                            fileToUpload2.putFile(User_profile_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    String downloadUrl=taskSnapshot.getDownloadUrl().toString();
                                    HashMap h=new HashMap();
                                    h.put("User Name", Username);
                                    h.put("Age", User_age);
                                    h.put("Email", User_Email);
                                    h.put("UID", currentUserid);
                                    h.put("Admin", "Nope");
                                    h.put("Profile",downloadUrl);
                                    h.put("CollegeMember","not yet");
                                    mUserRef.push().setValue(h).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){

                                                Toast.makeText(College_view_profile.this,"U Request Has been Sent",Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                                }
                            });




                        }
                    }
                });


            }
        });


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
}
