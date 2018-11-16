package com.example.alilo.alichat;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by alilo on 08/11/2018.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<message> messagesList;
    private FirebaseAuth mAuth ;
    private DatabaseReference mUserDatabase ;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder


    public MyAdapter(List<message> messagesList, Context context) {
        this.messagesList = messagesList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView displayName;
        public ImageView messageImage;
        public CircleImageView profileImage;
        public MyViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.name_text_layout) ;
            profileImage = (CircleImageView) v.findViewById(R.id.circleImageSingl);
            displayName = (TextView) v.findViewById(R.id.name_text_layout);
            messageImage = (ImageView) v.findViewById(R.id.message_image_layout);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<message> myDataset) {
        this.messagesList = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.message_single_layout2, parent, false);


        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        //
        // e contents of the view with that element
      //  Toast.makeText(messagesList.get(position).getFrom().equalsIgnoreCase(Curent_userId), this, Toast.LENGTH_SHORT).show();



        final String curentFrom = messagesList.get(position).getFrom();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(curentFrom);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    String name = dataSnapshot.child("name").getValue().toString();
                                                    String image = dataSnapshot.child("image").getValue().toString();

                                                    holder.displayName.setText(name);

                                                    Picasso.with(holder.profileImage.getContext()).load(image)
                                                            .placeholder(R.drawable.defaumag).into(holder.profileImage);

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }});

        if(messagesList.get(position).equals("text")) {

            holder.mTextView.setText(messagesList.get(position).getMessage());
            holder.messageImage.setVisibility(View.INVISIBLE);


        } else {

            holder.mTextView.setVisibility(View.INVISIBLE);
            Picasso.with(holder.profileImage.getContext()).load(messagesList.get(position).getMessage())
                    .placeholder(R.drawable.defaumag).into(holder.messageImage);

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return messagesList.size();
    }
}