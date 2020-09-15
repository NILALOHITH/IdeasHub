package com.rahtech.ideashub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser currentUser=firebaseAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null || !firebaseAuth.getCurrentUser().isEmailVerified()){
                    startActivity(new Intent(SplashScreen.this,SignInActivity.class));
                }
                else{
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                }
            }
        });
        finish();

    }
}