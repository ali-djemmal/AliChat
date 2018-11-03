package com.example.alilo.alichat;

import android.icu.text.DateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity {



    private CircleImageView mImageView ;
    private TextView mProfilename , mprofileStatus,mprofileFrendCount;
    private Button  msendButton ,mcanlButton;
    private DatabaseReference mUserDatabases ;
    private DatabaseReference mRequestReference ;
    private DatabaseReference mFrienddataReference ;
    private DatabaseReference mNotificationReference ;


    private FirebaseUser mCurentUser ;
    private  String mcurent_stat ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        final String Uid = getIntent().getStringExtra("userId") ;
        mImageView=(CircleImageView) findViewById(R.id.profile_image) ;
        mProfilename= (TextView) findViewById(R.id.mProfilenameid) ;
        mprofileStatus= (TextView) findViewById(R.id.Statusprof) ;
        mprofileFrendCount= (TextView) findViewById(R.id.frendscount) ;

        msendButton =(Button) findViewById(R.id.sendId);
        mcanlButton =(Button) findViewById(R.id.cancalId);


        mUserDatabases= FirebaseDatabase.getInstance().getReference().child("Users").child(Uid);
        mRequestReference=FirebaseDatabase.getInstance().getReference().child("Friend_req") ;
        mFrienddataReference = FirebaseDatabase.getInstance().getReference().child("Friends") ;
        mNotificationReference = FirebaseDatabase.getInstance().getReference().child("Notification") ;
        mcurent_stat ="not_friends" ;

        mCurentUser = FirebaseAuth.getInstance().getCurrentUser();

        mUserDatabases.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString() ;
                String image = dataSnapshot.child("image").getValue().toString() ;
                String statut = dataSnapshot.child("statut").getValue().toString() ;
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString() ;

                mProfilename.setText(name);
                mprofileStatus.setText(statut);
                Picasso.with(getApplicationContext()).load(image).into(mImageView) ;

             //---------------------------------- Annuller l annvitation ----------------------------

                mRequestReference.child(mCurentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(Uid)){
                            String req_type= dataSnapshot.child(Uid).child("request_type").getValue().toString();
                            if(req_type.equalsIgnoreCase("received")){
                                mcurent_stat="req_received" ;
                                msendButton.setText("Accepte Freind request");
                                mcanlButton.setVisibility(View.VISIBLE);
                                mcanlButton.setEnabled(true);
                            }else  if(req_type.equalsIgnoreCase("sent")){
                                mcurent_stat="req_sent" ;
                                msendButton.setText("Cancel Freind request");
                                mcanlButton.setVisibility(View.INVISIBLE);
                                mcanlButton.setEnabled(false);

                            }else {

                                mFrienddataReference.child(mCurentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild(Uid)){
                                            mcurent_stat="friend" ;
                                            msendButton.setText("unFreind");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
















                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });








            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        msendButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {


                //-------------------------------------- SEND FRENDS REQUEST --------------------------------------------------



        if(mcurent_stat.equalsIgnoreCase("not_friends")){


          msendButton.setEnabled(false);

          mRequestReference.child(mCurentUser.getUid()).child(Uid).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {



      @Override
       public void onComplete(@NonNull Task<Void> task) {

        if(task.isSuccessful()){

            mRequestReference.child(Uid).child(mCurentUser.getUid()).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    HashMap<String,String> notificationData = new HashMap<String, String>();
                    notificationData.put("from",  mCurentUser.getUid());
                    notificationData.put("type","request") ;
                    mNotificationReference.child(Uid).push().setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mcurent_stat="req_sent" ;
                            msendButton.setText("cancal Freind request");
                            mcanlButton.setVisibility(View.INVISIBLE);
                            mcanlButton.setEnabled(false);
                        }
                    });









                    Toast.makeText(ProfilActivity.this, " send request secsuss", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(ProfilActivity.this, "Faild send request", Toast.LENGTH_SHORT).show();
        }
          msendButton.setEnabled(true);
    }
}) ;
}




                //-------------------------------------- SEND FRENDS REQUEST --------------------------------------------------



                if(mcurent_stat.equalsIgnoreCase("req_sent")){




                    mRequestReference.child(mCurentUser.getUid()).child(Uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mRequestReference.child(Uid).child(mCurentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    msendButton.setEnabled(true);
                                    mcurent_stat="not_friends" ;
                                    msendButton.setText("sent Freind request");
                                    mcanlButton.setVisibility(View.INVISIBLE);
                                    mcanlButton.setEnabled(false);
                                }
                            });
                        }
                    });
                }



                // ------------------------------- REQ RECEVED STAT ----------------------------------------------------------------



                if(mcurent_stat.equalsIgnoreCase("req_received")){
                     final String dateRequesst = DateFormat.getDateTimeInstance().format(new Date());

                    mFrienddataReference.child(mCurentUser.getUid()).child(Uid).child("data").setValue(dateRequesst).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFrienddataReference.child(Uid).child(mCurentUser.getUid()).child("data").setValue(dateRequesst).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {



                                    mRequestReference.child(mCurentUser.getUid()).child(Uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mRequestReference.child(Uid).child(mCurentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    msendButton.setEnabled(true);
                                                    mcurent_stat="friend" ;
                                                    msendButton.setText("unFreind");
                                                    mcanlButton.setVisibility(View.INVISIBLE);
                                                    mcanlButton.setEnabled(false);
                                                }
                                            });
                                        }
                                    });


                                }
                            });
                        }
                    });





                }




            }
        });
































    }
}
