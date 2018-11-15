package com.example.alilo.alichat;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by alilo on 03/11/2018.
 */

public class AliChat extends Application {
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        /* picaso
        Picasso.Builder builder = new Picasso.Builder(this);
      builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso picasso = builder.build();
        picasso.setIndicatorsEnabled(true);
        Picasso.setSingletonInstance(picasso);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String curent_uId= user.getUid();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(curent_uId);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 FirebaseUser creFirebaseUser= mAuth.getCurrentUser();

        if(creFirebaseUser != null){
             mDatabaseReference.child("online").onDisconnect().setValue(false);
        mDatabaseReference.child("lastSeen").setValue(ServerValue.TIMESTAMP);

            }

                }

            @Override
            public void onCancelled(DatabaseError databaseError ) {

            }
        });*/
    }
}
