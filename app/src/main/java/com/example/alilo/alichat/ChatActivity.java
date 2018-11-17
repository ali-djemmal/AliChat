package com.example.alilo.alichat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
    private StorageReference mImageStorage;


    private SwipeRefreshLayout mSwipeRefreshLayout ;

    private TextView Tvname, TvDesc;
    private CircleImageView mCircleImageView;
    private FirebaseAuth mAuth;
    private ImageButton  mSendButton;
    private ImageButton mAddButton;
    private EditText mMessText;
    private RecyclerView recyclerView;
    String mCurrentUserId;
    private final List<message> messageList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MyrecycleViewAdapterS messageAdapter;
    private static  final int LIMIT_MESSAG = 10;
    int curentPage =1;
    private static final int GALLERY_PICK = 1;

    //New Solution
    private int itemPos = 0;

    private String mLastKey = "";
    private String mPrevKey = "";









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

        mAddButton = (ImageButton) findViewById(R.id.chat_add_btn);
        mSendButton = (ImageButton) findViewById(R.id.chat_send_btn);
        mMessText = (EditText) findViewById(R.id.chat_message_view);


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
        recyclerView = (RecyclerView) findViewById(R.id.messages_list);
        mSwipeRefreshLayout =(SwipeRefreshLayout)  findViewById(R.id.message_swipe_layout);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);

        mImageStorage = FirebaseStorage.getInstance().getReference();
        mRootReference.child("Chat").child(mCurrentUserId).child(mChatUser).child("seen").setValue(true);



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
                   /* GetTimeAgo getTimeAgo = new GetTimeAgo();
                    long lastTime = Long.parseLong(statut);
                    String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, getApplicationContext());

                    TvDesc.setText(lastSeenTime);*/
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




      //  ---------------------- Button message listenser -----------------------------------------------



        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


        //******************* add button ***********************


        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });






















        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curentPage++;
                itemPos = 0;
            //    messageList.clear();
                loadMoreMessages();

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            final String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
            final String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

            DatabaseReference user_message_push = mRootReference.child("messages")
                    .child(mCurrentUserId).child(mChatUser).push();

            final String push_id = user_message_push.getKey();


            StorageReference filepath = mImageStorage.child("message_images").child( push_id + ".jpeg");

            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()){

                        String download_url = task.getResult().getDownloadUrl().toString();


                        Map messageMap = new HashMap();
                        messageMap.put("message", download_url);
                        messageMap.put("seen", false);
                        messageMap.put("type", "image");
                        messageMap.put("time", ServerValue.TIMESTAMP);
                        messageMap.put("from", mCurrentUserId);

                        Map messageUserMap = new HashMap();
                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                        messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                        mMessText.setText("");

                        mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                if(databaseError != null){

                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());

                                }

                            }
                        });


                    }

                }
            });

        }

    }
    private void loadMoreMessages() {

        DatabaseReference messageRef = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser);

        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                message M = new message(dataSnapshot.child("message").getValue().toString(),"fff",dataSnapshot.child("from").getValue().toString(), false, 0);
              //  messageList.add(M);
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageList.size()-1);
                mSwipeRefreshLayout.setRefreshing(false);


              //  Messages message = dataSnapshot.getValue(Messages.class);
                String messageKey = dataSnapshot.getKey();

                if(!mPrevKey.equals(messageKey)){

                    messageList.add(itemPos++, M);

                } else {

                    mPrevKey = mLastKey;

                }


                if(itemPos == 1) {

                    mLastKey = messageKey;

                }


                Log.d("TOTALKEYS", "Last Key : " + mLastKey + " | Prev Key : " + mPrevKey + " | Message Key : " + messageKey);

                messageAdapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);

                linearLayoutManager.scrollToPositionWithOffset(10, 0);

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

    private void loadMessage() {
        DatabaseReference messagelistReference = mRootReference.child("messages").child(mCurrentUserId).child(mChatUser);
        Query mQuery = messagelistReference.limitToLast(curentPage * LIMIT_MESSAG);
        mQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                message M = new message(dataSnapshot.child("message").getValue().toString(),dataSnapshot.child("type").getValue().toString(),dataSnapshot.child("from").getValue().toString(), false, 0);
                itemPos++;

                if(itemPos == 1){

                    String messageKey = dataSnapshot.getKey();

                    mLastKey = messageKey;
                    mPrevKey = messageKey;

                }



                messageList.add(M);
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageList.size()-1);
                mSwipeRefreshLayout.setRefreshing(false);

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
            messageMap.put("from", mCurrentUserId);
            Map messgeUserMap = new HashMap();
            messgeUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messgeUserMap.put(chat_user_ref + "/" + push_id, messageMap);
            mMessText.setText("");


            mRootReference.child("Chat").child(mCurrentUserId).child(mChatUser).child("seen").setValue(true);
            mRootReference.child("Chat").child(mCurrentUserId).child(mChatUser).child("timestamp").setValue(ServerValue.TIMESTAMP);

            mRootReference.child("Chat").child(mChatUser).child(mCurrentUserId).child("seen").setValue(false);
            mRootReference.child("Chat").child(mChatUser).child(mCurrentUserId).child("timestamp").setValue(ServerValue.TIMESTAMP);










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
