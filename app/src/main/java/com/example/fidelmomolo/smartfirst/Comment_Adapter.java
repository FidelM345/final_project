package com.example.fidelmomolo.smartfirst;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Fidel M Omolo on 5/8/2018.
 */

public class Comment_Adapter extends  RecyclerView.Adapter<Comment_Adapter.ViewHolder> {


    List<Comment_model> bloglist;
    List<User_model_class> userlist;

    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    Context context;

    public Comment_Adapter(List<Comment_model> bloglist,List<User_model_class> userlist) {
        this.bloglist = bloglist;
        this.userlist=userlist;
    }

    @NonNull
    @Override
    public Comment_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //it inflates the custom made Layout file for list items
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.commentlist,parent,false);
        context=parent.getContext();
        firestore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        return new Comment_Adapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Comment_Adapter.ViewHolder holder, int position) {

        //gets data stored in the bloglist List data structure, the getDescription model class is found in the model class
        String  comment_message=bloglist.get(position).getMessage();
        Date timestamp=bloglist.get(position).getTime_stamp();

        String  imageuri=userlist.get(position).getImageuri();
        String  fname=userlist.get(position).getFname();
        String  lname=userlist.get(position).getLname();



//        DateFormat df = new SimpleDateFormat("HH:mm", Locale.US);
//        final String time_chat_s = df.format(timestamp);

        holder.setUser_details(imageuri,fname,lname);

        holder.setComment_message(comment_message);
        holder.timeStamp(timestamp);


    }

    @Override
    public int getItemCount() {


        return bloglist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView comment_message,comment_user_name,comment_time;
        CircleImageView profile_pic;




        public ViewHolder(View itemView) {
            super(itemView);

            mView=itemView;

        }

        public  void setComment_message(String message){

            comment_message=mView.findViewById(R.id.comment_message);
            comment_message.setText(message);
        }


        public void setUser_details(String image_uri, String fname,String lname){

            comment_user_name=mView.findViewById(R.id.comment_user_name);
            comment_user_name.setText(fname+" "+lname);


            profile_pic=mView.findViewById(R.id.comment_userpic);

            RequestOptions placeHolder=new RequestOptions();
            placeHolder.placeholder(R.drawable.place_holder_3);
            Glide.with(context).applyDefaultRequestOptions(placeHolder).load(image_uri).into(profile_pic);


        }

        public void timeStamp(Date timestamp){
           // String dateString= DateFormat.format("MM/dd/yyy HH:mm",new Date(String.valueOf(timestamp))).toString();

           /* String current= DateFormat.getDateInstance().format( timestamp.getTime());
            comment_time=mView.findViewById(R.id.comment_time);
            comment_time.setText(current);*/








        }






    }
}
