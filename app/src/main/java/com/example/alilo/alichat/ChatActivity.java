package com.example.alilo.alichat;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    private String mChatUser;
    private DatabaseReference mDatabaseReference, mRootReference, mMessageDatabaseReference;

    private TextView Tvname, TvDesc;
    private CircleImageView mCircleImageView;
    private FirebaseAuth mAuth;
    private Button mAddButton, mSendButton;
    private EditText mMessText;
    private RecyclerView recyclerView;
    String mCurrentUserId;
    private final List<message> messageList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MyrecycleViewAdapterS messageAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mChatUser = getIntent().getStringExtra("userId");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mChatUser);
        mRootReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

      //  mAddButton = (ImageButton) findViewById(R.id.imageButton);
        mSendButton = (Button) findViewById(R.id.imageButton2);
        mMessText = (EditText) findViewById(R.id.textView);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionbarView = inflater.inflate(R.layout.chat_custom_bar, null);
        actionBar.setCustomView(actionbarView);


        TvDesc = (TextView) findViewById(R.id.descripId);
        Tvname = (TextView) findViewById(R.id.nameOnid);
        mCircleImageView = (CircleImageView) findViewById(R.id.imageOnlineId);


        final String[] mnom = new String[1];


        messageAdapter = new MyrecycleViewAdapterS(getApplicationContext(), messageList);
        recyclerView = (RecyclerView) findViewById(R.id.id_message_list);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);
         loadMessage();


        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String nom = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String statut = dataSnapshot.child("online").getValue().toString();

                Tvname.setText(nom);
                if (statut.equalsIgnoreCase("true")) {
                    TvDesc.setText("Online");
                } else {
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    long lastTime = Long.parseLong(statut);
                    String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, getApplicationContext());

                    TvDesc.setText(lastSeenTime);
                }

                mRootReference.child("Chat").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(nom)) {
                            Map ChatMap = new HashMap();
                            ChatMap.put("seen", false);
                            ChatMap.put("timestamp", ServerValue.TIMESTAMP);

                            Map ChatUser = new HashMap();
                            ChatMap.put("Chat/" + mCurrentUserId + "/" + mChatUser, ChatMap);
                            ChatMap.put("Chat/" + mChatUser + "/" + mCurrentUserId, ChatMap);

                            mRootReference.updateChildren(ChatUser, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                Picasso.with(getApplicationContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.utilisateur).into(mCircleImageView);

                //  getSupportActionBar().setTitle(nom);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    private void loadMessage() {
        mRootReference.child("messages").child(mCurrentUserId).child(mChatUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                message M = new message(dataSnapshot.child("message").getValue().toString(),"eezez",false,0)  ;
                messageList.add(M);
                messageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void sendMessage() {
        String message = mMessText.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
            String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

            DatabaseReference user_message_push = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser).push();
            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            Map messgeUserMap = new HashMap();
            messgeUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messgeUserMap.put(chat_user_ref + "/" + push_id, messageMap);
            mRootReference.updateChildren(messgeUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d("CHAT8LOG", databaseError.getMessage().toString());
                    }
                }
            });


        }
    }

}
