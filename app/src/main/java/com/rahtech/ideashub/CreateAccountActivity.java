package com.rahtech.ideashub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccount Activity" ;

    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private FirebaseUser currentUser=firebaseAuth.getCurrentUser();
    private DatabaseReference firebaseReference= FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userDatabaseReference= firebaseReference.child("user_details");
    ProgressBar loginProgressBar;

    private EditText emailEditText ;
    private EditText passwordEditText ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
    }

    private String authFailedreason(Task task){
        String[] exception = task.getException().toString().split(":");
        return exception[1];
    }


    // to validate the email and pwd is not null and pwd greateer than or equals to 6 characters
    private boolean validateForm(String email, String password) {

        boolean valid = true;

        //String email = mBinding.fieldEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            // mBinding.fieldEmail.setError("Required.");
            valid = false;
        } else {
            // mBinding.fieldEmail.setError(null);
        }

        //String password = mBinding.fieldPassword.getText().toString();
        if (TextUtils.isEmpty(password) && password.length()>=6) {
            // mBinding.fieldPassword.setError("Required.");
            valid = false;
        } else {
            //mBinding.fieldPassword.setError(null);
        }

        return valid;
    }


    //creating an account with email
    public void createAccount(View view) {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Log.d(TAG, "createAccount:" + email);
         if (!validateForm(email,password)) {
             Toast.makeText(this, "Please fill your details. Password must be atleast six characters.", Toast.LENGTH_LONG).show();
            return;
         }

        //showProgressBar();

        // [START create_user_with_email]

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification of email
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(CreateAccountActivity.this, "email sent verify your account", Toast.LENGTH_SHORT).show();
                                        emailEditText.setText("");
                                        passwordEditText.setText("");
                                    }else{
                                        Toast.makeText(CreateAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            Log.d(TAG, "createUserWithEmail:success");

                            Intent profileIntent = new Intent(CreateAccountActivity.this, SignInActivity.class);
                            startActivity(profileIntent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            String msg = authFailedreason(task);
                            Log.i(TAG,msg);
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed." + msg,
                                    Toast.LENGTH_LONG).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]

    }
}