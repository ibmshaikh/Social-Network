package com.example.ibrahim.snc.Fregments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ibrahim.snc.NewsFeed;
import com.example.ibrahim.snc.Post;
import com.example.ibrahim.snc.R;
import com.example.ibrahim.snc.models.BlogPost;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewFeed extends Fragment {

    View v;
    private FloatingActionButton floatingActionButton;
    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list;



    public NewFeed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_new_feed, container, false);
        floatingActionButton=(FloatingActionButton)v.findViewById(R.id.floatingActionButton4);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(), Post.class);

                startActivity(i);
            }
        });


        return v;
    }



}
