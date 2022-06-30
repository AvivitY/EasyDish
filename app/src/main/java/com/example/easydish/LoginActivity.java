package com.example.easydish;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private MaterialButton login_BTN_login;
    private TextInputEditText login_EDT_email,login_EDT_password;
    private TextView login_LBL_signup,login_LBL_guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        //findViews();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

//    private void findViews() {
//        login_BTN_login = findViewById(R.id.login_BTN_login);
//        login_LBL_signup = findViewById(R.id.login_LBL_signup);
//        login_LBL_guest = findViewById(R.id.login_LBL_guest);
//        login_EDT_email = findViewById(R.id.login_EDT_email);
//        login_EDT_password = findViewById(R.id.login_EDT_password);

//        login_BTN_login.setOnClickListener(view -> {
//            signIn();
//        });
//        login_LBL_signup.setOnClickListener(view -> {
//            signUp();
//        });
//        login_LBL_guest.setOnClickListener(view -> {
//            Intent main = new Intent(this,MainActivity.class);
//            startActivity(main);
//            finish();
//        });
//
//    }
//
//    private void signIn(){
//        String email = login_EDT_email.getText().toString();
//        String password = login_EDT_password.getText().toString();
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("ccc", "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Log.d("ccc", "Existing User:" + user);
//                            Intent main = new Intent(LoginActivity.this,MainActivity.class);
//                            startActivity(main);
//                            finish();
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("ccc", "signInWithEmail:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    private void signUp() {
//        String email = login_EDT_email.getText().toString();
//        String password = login_EDT_password.getText().toString();
//        mAuth.createUserWithEmailAndPassword(email ,password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("ccc", "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Log.d("ccc", "New User:" + user);
//                            //Intent reg = new Intent(LoginActivity.this,RegistrationActivity.class);
//                            //startActivity(reg);
//                            //finish();
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("ccc", "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
}