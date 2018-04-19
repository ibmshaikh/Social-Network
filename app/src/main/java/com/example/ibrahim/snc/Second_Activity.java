package com.example.ibrahim.snc;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.ibrahim.snc.Fregments.NewFeed;
import com.example.ibrahim.snc.Fregments.notification;
import com.example.ibrahim.snc.Fregments.profile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Second_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_);

        //------------------------------------Default Activity---------------------------------//


        NewFeed mm=new NewFeed();
        FragmentManager manager2=getSupportFragmentManager();
        manager2.beginTransaction().replace(R.id.content,mm,mm.getTag()).commit();

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_home_white_24dp, R.color.colorAccent);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Notification", R.drawable.ic_notifications_white_24dp, R.color.colorAccent);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Profile", R.drawable.ic_perm_identity_white_24dp, R.color.colorAccent);

        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                if (position==0){


                    NewFeed mm=new NewFeed();
                    FragmentManager manager2=getSupportFragmentManager();
                    manager2.beginTransaction().replace(R.id.content,mm,mm.getTag()).commit();



                    Toast.makeText(Second_Activity.this, "dosri Activity", Toast.LENGTH_SHORT).show();

                }
                else if (position==1){

                    notification notification=new notification();
                    FragmentManager manager1=getSupportFragmentManager();

                    manager1.beginTransaction().replace(R.id.content,notification,notification.getTag()).commit();
                    Toast.makeText(Second_Activity.this, "pehli Activity", Toast.LENGTH_SHORT).show();

                }
                else if (position==2){

                    profile profile=new profile();
                    FragmentManager manager3=getSupportFragmentManager();
                    manager3.beginTransaction().replace(R.id.content,profile,profile.getTag()).commit();


                }

                return true;
            }
        });

    }
}
