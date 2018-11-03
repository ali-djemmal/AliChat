package com.example.alilo.alichat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView ;
    private DatabaseReference mDatabaseReference ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);



       mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mRecyclerView = (RecyclerView)  findViewById(R.id.RecylUserid) ;

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseRecyclerAdapter<Users ,UsersVeiwHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersVeiwHolder>(Users.class,R.layout.users_layout,
                UsersVeiwHolder.class,mDatabaseReference) {
            @Override
            protected void populateViewHolder(UsersVeiwHolder viewHolder, Users model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setStatu(model.getStatut());
                viewHolder.setImage(model.getImage(),getApplicationContext());
// go tobprofile detaill
                final String User_id = getRef(position).getKey();//get user id
                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(AllUsersActivity.this,ProfilActivity.class);
                        profileIntent.putExtra("userId",User_id);
                        startActivity(profileIntent);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    public static class UsersVeiwHolder extends  RecyclerView.ViewHolder {

        View view;
        public UsersVeiwHolder(View itemView) {
            super(itemView);
            view=itemView ;
        }

        public void setName(String name) {
            TextView Tv_name = (TextView) view.findViewById(R.id.Single);
            Tv_name.setText(name);
        }
        public void setStatu(String status) {
            TextView Tv_statu = (TextView) view.findViewById(R.id.Status);
            Tv_statu.setText(status);
        }
        public void setImage(String image, Context context) {
            CircleImageView imageV = (CircleImageView) view.findViewById(R.id.circleImageSingl);
            Picasso.with(context).load(image).into(imageV) ;
          //  imageV.setImageURI();
        }
    }
}
