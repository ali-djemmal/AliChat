package com.example.alilo.alichat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setting_Activity extends AppCompatActivity {
private DatabaseReference mUserDatabases;


    private StorageReference mStorageRef;
    private FirebaseUser mCurentUser ;
    private CircleImageView circleImageView ;
    private TextView mName,mstatus;
    private Button chSButton,chatButton;
    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);
        mStorageRef = FirebaseStorage.getInstance().getReference();        //5555555555555555555555555555555

        circleImageView =(CircleImageView) findViewById(R.id.profile_image);
        mName =(TextView) findViewById(R.id.mProfilenameid);
        mstatus =(TextView) findViewById(R.id.Statusprof);

        chSButton =(Button) findViewById(R.id.changeStatus) ;
       // chatButton =(Button) findViewById(R.id.changeImg) ;


        //Image storage from firebase storage


        //user firebase

        mCurentUser = FirebaseAuth.getInstance().getCurrentUser();
        String curent_uId= mCurentUser.getUid();
        mUserDatabases= FirebaseDatabase.getInstance().getReference().child("Users").child(curent_uId);
        mUserDatabases.keepSynced(true);


        mUserDatabases.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString() ;
                String image = dataSnapshot.child("image").getValue().toString() ;
                String statut = dataSnapshot.child("statut").getValue().toString() ;
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString() ;

              //   mName.setText(name);
                 mstatus.setText(statut);
            if (image.equalsIgnoreCase("default")){
                Picasso.with(getApplicationContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.utilisateur).into(circleImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                }) ;

}  else{
    Picasso.with(getApplicationContext()).load(image).into(circleImageView) ;
}
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();


            }
        });



        chSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statsdata = mstatus.getText().toString();
                Intent statusIntent = new Intent(Setting_Activity.this,StatusActivity.class) ;
                statusIntent.putExtra("status",statsdata) ;
                startActivity(statusIntent);
            }
        });
    }
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = data.getData();
                CropImage.activity(resultUri).setAspectRatio(1,1)
                        .start(this);

                CropImage.ActivityResult result1 =CropImage.getActivityResult(data) ;
                if (resultCode == RESULT_OK){

                    Uri resultatUri = result.getUri();
                    StorageReference Filepath = mStorageRef.child("profile_images").child("profile_image.jpg") ;
                    Filepath.putFile(resultatUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(Setting_Activity.this, "Working", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Setting_Activity.this, "Error uploading firestore", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }  else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }



        }
    }*/
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

          //  StorageReference ref = storageRefer.child("images/"+ UUID.randomUUID().toString());
            StorageReference Filepath = mStorageRef.child("profile_images").child(mCurentUser.getUid()+".jpeg") ;
            Filepath.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(Setting_Activity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            String Url_image=taskSnapshot.getDownloadUrl().toString();
                            mUserDatabases.child("image").setValue(Url_image) ;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Setting_Activity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();

            try {
                uploadImage();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                circleImageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
