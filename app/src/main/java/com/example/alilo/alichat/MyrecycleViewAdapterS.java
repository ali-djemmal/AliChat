package com.example.alilo.alichat;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by alilo on 11/10/2018.
 */

public class MyrecycleViewAdapterS extends RecyclerView.Adapter<MyrecycleViewAdapterS.MyviewHolder> {
    private Context mContext;
    private List<message> messagesList;
    private DatabaseReference mUserDatabase ;
    private FirebaseAuth mAuth ;
    public MyrecycleViewAdapterS(Context mContext, List<message> messagesListe) {
        this.mContext = mContext;
        this.messagesList = messagesListe;
    }



    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* View view;
        LayoutInflater inflater= LayoutInflater.from(mContext);
        view =inflater.inflate(R.layout.message_single_layout,parent,false);*/


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout2, parent, false);


        return new MyviewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, int position) {
//holder.tv_title.setText(messagesList.get(position).getName());
        mAuth = FirebaseAuth.getInstance();

        String crent_userid = mAuth.getCurrentUser().getUid();
        String userfrom = messagesList.get(position).getFrom();
        if(crent_userid.equals(userfrom)){
            holder.tvdescr.setBackgroundColor(Color.WHITE);
            holder.tvdescr.setTextColor(Color.BLACK);
        }else{
            holder.tvdescr.setBackgroundResource(R.drawable.messge_text_bak);
            holder.tvdescr.setTextColor(Color.WHITE);

        }
         holder.tvdescr.setText(messagesList.get(position).getMessage());
       // holder.displayName.setText(messagesList.get(position).getFrom());

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(messagesList.get(position).getFrom());

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                holder.displayName.setText(name);

            /*    Picasso.with(holder.profileImage.getContext()).load(image)
                        .placeholder(R.drawable.defaumag).into(holder.profileImage);*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        mUserDatabase = FirebaseDatabase.getInstance().getReference();
        // holder.displayName.setText();


       // final String curentFrom = messagesList.get(position).getFrom();



//String name = dataSnapshot.child("name").getValue().toString();
              //  String image = dataSnapshot.child("image").getValue().toString();

        /*        holder.displayName.setText(name);

                Picasso.with(holder.profileImage.getContext()).load(image)
                        .placeholder(R.drawable.defaumag).into(holder.profileImage);*/








    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public static class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tv_title ;
        TextView tvdescr;
        public TextView displayName;
        public ImageView messageImage;
        public CircleImageView profileImage;







        public MyviewHolder(View v) {
            super(v);

          //  tv_title=(TextView)  itemView.findViewById(R.id.name);
            tvdescr=(TextView)  v.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) v.findViewById(R.id.circleImageSingl);
            displayName = (TextView) v.findViewById(R.id.name_text_layout);
            messageImage = (ImageView) v.findViewById(R.id.message_image_layout);
        }
    }
}
