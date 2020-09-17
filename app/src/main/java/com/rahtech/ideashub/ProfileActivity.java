package com.rahtech.ideashub;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private  final String TAG = this.getClass().getSimpleName();

    static int requestCodeOpenGalleryIntent = 99;

    ProgressBar profileProgressBar;
    ImageView profilePic;
    EditText userName_editText;
    EditText bio_editText;
    //EditText email_editText;

    Uri photo_url;
    //String email;
    String displayName;
    String userid;

    FirebaseFirestore fstore;
    DocumentReference documentReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    private final DatabaseReference firebaseReference=FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference usersReference= firebaseReference.child("user_details");
    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic = findViewById(R.id.profile_pic);
        //email_editText = findViewById(R.id.email);
        userName_editText = findViewById(R.id.name);
        profileProgressBar=findViewById(R.id.profileProgressBar);
        bio_editText = findViewById(R.id.bio);

        userid = currentUser.getUid();

        fstore = FirebaseFirestore.getInstance();
        documentReference = fstore.collection("users").document(userid);

        storageReference = firebaseStorage.getInstance().getReference();

        if(currentUser!=null){
            Log.e(TAG, "user not  null");
            profileProgressBar.setVisibility(View.VISIBLE);
            // TODO fill userDetails if already there
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                    userName_editText.setText(value.getString("Name"));
                    bio_editText.setText(value.getString("Bio"));

                }
            });

            StorageReference profileImageRef = storageReference.child("users/"+currentUser.getUid()+"/profile.jpg");
            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //Picasso.get().load(uri).into(profilePic);
                    Glide.with(getApplicationContext())
                            .load(uri)
                            .into(profilePic);
                }
            });
        }
        else{
            toLoginActvity();
        }

        findViewById(R.id.profile_save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserDetails();
                }
        });

        findViewById(R.id.profile_image_change_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfileImage();
            }
        });
    }

    private void saveUserDetails(){

        Map<String,Object> user = new HashMap<>();
        user.put("Name",userName_editText.getText().toString());
        user.put("Bio",bio_editText.getText().toString());
        documentReference.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG,"Details Saved:" + userid);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d(TAG,"Failed" + e.toString());
            }
        });
    }

    private void changeProfileImage(){

        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, requestCodeOpenGalleryIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == requestCodeOpenGalleryIntent){
            if(resultCode == Activity.RESULT_OK){
                photo_url = data.getData();
                //profilePic.setImageURI(photo_url);

                uploadImageToFirebase(photo_url);
            }
        }
    }

    private void uploadImageToFirebase(Uri photo_url) {

        final StorageReference fileRef = storageReference.child("users/"+currentUser.getUid()+"/profile.jpg");
        fileRef.putFile(photo_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Picasso.get().load(uri).into(profilePic);
                        Glide.with(getApplicationContext())
                                .load(uri)
                                .into(profilePic);
                    }
                });
                    Log.d(TAG,"Image Uploaded");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"Image Uploading Failed");

            }
        });
    }

    private void toLoginActvity(){
        Intent loginIntent=new Intent(ProfileActivity.this,SignInActivity.class);
        startActivity(loginIntent);
        finish();
    }
}