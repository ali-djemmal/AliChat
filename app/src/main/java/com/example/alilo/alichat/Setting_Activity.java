package com.example.alilo.alichat;

import android.content.Intent;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setting_Activity extends AppCompatActivity {
private DatabaseReference mUserDatabases;


    private StorageReference mStorageRef;
    private FirebaseUser mCurentUser ;
    private CircleImageView circleImageView ;
    private TextView mName,mstatus;
    private Button chSButton,chatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);
        mStorageRef = FirebaseStorage.getInstance().getReference();        //5555555555555555555555555555555

        circleImageView =(CircleImageView) findViewById(R.id.profile_image);
        mName =(TextView) findViewById(R.id.textView);
        mstatus =(TextView) findViewById(R.id.textView2);

        chSButton =(Button) findViewById(R.id.changeStatus) ;
        chatButton =(Button) findViewById(R.id.changeImg) ;


        //Image storage from firebase storage


        //user firebase

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


        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent();
                pickPhoto.setType("image/*") ;
                pickPhoto.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(pickPhoto,"Select image"),1) ;
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
    }

}
