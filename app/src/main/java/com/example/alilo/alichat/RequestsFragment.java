package com.example.alilo.alichat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StreamDownloadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class RequestsFragment extends Fragment {
    private RecyclerView mRecyclerView ;
    private DatabaseReference mDatabaseReference ;
    private DatabaseReference mDatabaseReferenceUs ;
    private DatabaseReference mRequestReference ;
    private FirebaseAuth mAuth ;
    private FirebaseUser mCurentUser ;
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
        mAuth =FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){




            mCurrentUserId= mAuth.getCurrentUser().getUid();
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
            mRequestReference =FirebaseDatabase.getInstance().getReference().child("Friend_req") ;
            mDatabaseReference.keepSynced(true);
            mDatabaseReferenceUs= FirebaseDatabase.getInstance().getReference().child("Users");
            mDatabaseReferenceUs.keepSynced(true);
            mCurentUser = FirebaseAuth.getInstance().getCurrentUser();



        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.requestlist)   ;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view ;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            FirebaseRecyclerAdapter<Users ,UsersVeiwHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersVeiwHolder>(Users.class,R.layout.users_layout,
                    UsersVeiwHolder.class,mDatabaseReference) {


                @Override

                protected void populateViewHolder(UsersVeiwHolder viewHolder, Users model, int position) {
                    final String User_id = getRef(position).getKey();//get user id
                 String f = mRequestReference.child(mCurentUser.getUid()).child(User_id).child("request_type").toString();
                    if(f.equalsIgnoreCase("received")){
                    viewHolder.setName(model.getName());
                    viewHolder.setStatu(model.getStatut());
                    viewHolder.setImage(model.getImage(), getContext());
                    viewHolder.setInvisibale();

                        }
                    viewHolder.setInvisibale();
// go to profile detaill

                    viewHolder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent profileIntent = new Intent(getContext(),ProfilActivity.class);
                            profileIntent.putExtra("userId",User_id);
                            startActivity(profileIntent);
                        }
                    });
                }
            };
            mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        }
    }


    public  static class UsersVeiwHolder extends  RecyclerView.ViewHolder {

        View view;
        public UsersVeiwHolder(View itemView) {
            super(itemView);
            view=itemView ;
        }
        public void setInvisibale(){
            ImageView mageView =(ImageView) view.findViewById(R.id.OnlineimageView);

            mageView.setVisibility(View.INVISIBLE);

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
    }
}
