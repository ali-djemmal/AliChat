package com.example.alilo.alichat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setting_Activity extends AppCompatActivity {
private DatabaseReference mUserDatabases;
    private FirebaseUser mCurentUser ;
    private CircleImageView circleImageView ;
    private TextView mName,mstatus;
    private Button chSButton,chatButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);

        //5555555555555555555555555555555

        circleImageView =(CircleImageView) findViewById(R.id.profile_image);
        mName =(TextView) findViewById(R.id.textView);
        mstatus =(TextView) findViewById(R.id.textView2);





        mCurentUser = FirebaseAuth.getInstance().getCurrentUser();
        String curent_uId= mCurentUser.getUid();
        mUserDatabases= FirebaseDatabase.getInstance().getReference().child("Users").child(curent_uId);
        mUserDatabases.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               String name = dataSnapshot.child("name").getValue().toString() ;
                String image = dataSnapshot.child("image").getValue().toString() ;
                String statut = dataSnapshot.child("statut").getValue().toString() ;
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString() ;

                 mName.setText(name);
                 mstatus.setText(statut);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent statusIntent = new Intent(Setting_Activity.this,StatusActivity.class) ;
                startActivity(statusIntent);
            }
        });
    }
}
