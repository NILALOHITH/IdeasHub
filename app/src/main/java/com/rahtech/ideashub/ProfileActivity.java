package com.rahtech.ideashub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private  final String TAG = this.getClass().getSimpleName();

    ProgressBar profileProgressBar;
    ImageView profilePic;
    EditText userName_editText;
    EditText email_editText;

    Uri photo_url;
    String email;
    String displayName;

    private final DatabaseReference firebaseReference=FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference usersReference= firebaseReference.child("user_details");
    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePic = findViewById(R.id.profile_pic);
        email_editText = findViewById(R.id.email);
        userName_editText = findViewById(R.id.name);
        profileProgressBar=findViewById(R.id.profileProgressBar);

        if(currentUser!=null){
            Log.e(TAG, "user not  null");
            profileProgressBar.setVisibility(View.VISIBLE);
            // TODO fill userDetails if already there


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


    }

    private void saveUserDetails(){



    }

    private void toLoginActvity(){
        Intent loginIntent=new Intent(ProfileActivity.this,SignInActivity.class);
        startActivity(loginIntent);
        finish();
    }
}