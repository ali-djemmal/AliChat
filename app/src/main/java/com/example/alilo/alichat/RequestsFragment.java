package com.example.alilo.alichat;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {



    private RecyclerView mRecyclerView ;
    private DatabaseReference mDatabaseReference ;
    private DatabaseReference mDatabaseReferenceUs ;
    private FirebaseAuth mAuth ;
    String mCurrentUserId ;
    public RequestsFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);
     /*   mAuth =FirebaseAuth.getInstance();
        mCurrentUserId= mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Friend_req").child(mCurrentUserId);
        mDatabaseReference.keepSynced(true);
        mDatabaseReferenceUs= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseReferenceUs.keepSynced(true);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.requestlist)   ;

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));*/
        return view ;
    }

    @Override
    public void onStart() {
        super.onStart();

/*        final FirebaseRecyclerAdapter<Request ,FriendsVeiwHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request,FriendsVeiwHolder>(Request.class,R.layout.users_layout,
                FriendsVeiwHolder.class,mDatabaseReference) {


            @Override
            protected void populateViewHolder(final FriendsVeiwHolder viewHolder, Request model, int position) {
            //    viewHolder.setStatu(model.getData());
                final String request_type = model.getRequest_type();
                final String  List_user_id = getRef(position).getKey();
                mDatabaseReferenceUs.child(List_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String ThumI = dataSnapshot.child("thumb_image").getValue().toString();
                        String image =  dataSnapshot.child("image").getValue().toString();
                        String onlineIm =  dataSnapshot.child("online").getValue().toString();
                     //   if(request_type.equals("received")){
                            viewHolder.setName(name);
                            viewHolder.setImage(image,getContext());
                      //  }


                      //  viewHolder.setUserOnline(onlineIm);

//------------------------------------------- dailog in frend list----------------------------------


                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[]=new CharSequence[]{"Open Profile","Send message"}  ;
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(i==0){
                                            Intent profileIntent = new Intent(getContext(),ProfilActivity.class);
                                            profileIntent.putExtra("userId",List_user_id);
                                            startActivity(profileIntent);
                                        }

                                        if(i ==1){
                                            //  final String User_id = getRef(position).getKey();//get user id



                                            Intent ChatIntent = new Intent(getContext(),ChatActivity.class);
                                            ChatIntent.putExtra("userId",List_user_id);
                                            startActivity(ChatIntent);





                                        }}
                                });
                                builder.show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);*/
    }


    public  static class  FriendsVeiwHolder extends  RecyclerView.ViewHolder{


        View view;
        public FriendsVeiwHolder(View itemView) {
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
            Picasso.with(context).load(image).placeholder(R.drawable.utilisateur).into(imageV) ;
            //  imageV.setImageURI();
        }

        public void setUserOnline(String onlineIm) {
            ImageView userOnlineView = (ImageView)  view.findViewById(R.id.OnlineimageView);
            if(onlineIm.equalsIgnoreCase("true")){
                userOnlineView.setVisibility(View.VISIBLE);
            }else {
                userOnlineView.setVisibility(View.INVISIBLE);
            }
        }
    }
}