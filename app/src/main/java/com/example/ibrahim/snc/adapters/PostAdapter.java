package com.example.ibrahim.snc.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ibrahim.snc.R;
import com.example.ibrahim.snc.models.postclass;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ibrahim on 24/3/18.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder> {

    public List<postclass> postlist;


    public PostAdapter(List<postclass> postlist){

        this.postlist=postlist;

    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {

        String desc=postlist.get(position).getDescription();
        String name=postlist.get(position).getUserName();
        String imageUrl=postlist.get(position).getImageURL();
        String UserimgUrl=postlist.get(position).getUserProfile();

        long millisecond=postlist.get(position).getTimestamp().getTime();
        String dateString= DateFormat.format("MM/dd/yy",new Date(millisecond)).toString();

        holder.setDescText(desc);
        holder.setName(name);
        holder.setImageUrl(imageUrl);
        holder.setTime(dateString);
        holder.setUserImg(UserimgUrl);


    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView descview, UserName, postTime;
        private View mView;
        private CircleImageView postImage,UserImg;


        public viewHolder(View itemView) {
            super(itemView);


            mView = itemView;


        }

        public void setDescText(String Desc) {

            descview = (TextView) mView.findViewById(R.id.blog_desc);
            descview.setText(Desc);


        }


        public void setName(String name) {

            UserName = (TextView) mView.findViewById(R.id.blog_user_name);
            UserName.setText(name);



        }

        public void setImageUrl(String imageUrl) {

            final Uri uri = Uri.parse(imageUrl);

            final SimpleDraweeView draweeView = (SimpleDraweeView) mView.findViewById(R.id.my_image_view);
            final ArrayList<String> list=new ArrayList<String>();

            list.add(imageUrl);

            draweeView.setImageURI(uri);
            draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        Toast.makeText(view.getContext(),"image",Toast.LENGTH_SHORT).show();

                    new ImageViewer.Builder(view.getContext(), list)
                            .setStartPosition(0)
                            .show();


                }
            });


        }

        public void setTime(String time) {

            postTime = (TextView) mView.findViewById(R.id.blog_date);
            postTime.setText(time);



        }

        public void setUserImg(String img){
            UserImg=(CircleImageView)mView.findViewById(R.id.blog_user_image);
            //Toast.makeText(mView.getContext(),img,Toast.LENGTH_SHORT).show();
            Glide.with(mView.getContext()).load(img).into(UserImg);

        }


    }

}
