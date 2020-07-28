package com.geexec.cse227;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private EditText mEmail,mPass;
    private Button mSign,mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.email);
        mPass = findViewById(R.id.pass);
        mSign = findViewById(R.id.signin);
        mSignUp  = findViewById(R.id.signup);

        if (mAuth.getCurrentUser()!=null){
            Intent i = new Intent(this,LoggedInActivity.class);
            startActivity(i);
            finish();
        }

        mSignUp.setOnClickListener(v->{

            if (TextUtils.isEmpty(mEmail.getText().toString().trim())){
                mEmail.setError("Enter appropriate email");
                mEmail.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(mPass.getText().toString().trim()) || mPass.getText().toString().length() <=8){
                mPass.setError("Enter appropriate password");
                mPass.requestFocus();
                return;
            }

            mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(),mPass.getText().toString()).addOnCompleteListener(task-> {
                if (!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        mPass.setError("Weak Password");
                        mPass.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        mEmail.setError("Invalid Email/Password");
                        mEmail.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        mEmail.setError("User Already Exists");
                        mEmail.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage() + "");
                    }
                } else {
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(verify->{
                        if (verify.isSuccessful()){
                            Toast.makeText(this, "Verification email sent", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.e(TAG,verify.getException()+"");
                        }
                    });

                }
            });



        });


        mSign.setOnClickListener(v->{

         if (TextUtils.isEmpty(mEmail.getText().toString().trim())){
             mEmail.setError("Enter appropriate email");
             mEmail.requestFocus();
             return;
         }

         if (TextUtils.isEmpty(mPass.getText().toString().trim()) || mPass.getText().toString().length() <=8){
             mPass.setError("Enter appropriate password");
             mPass.requestFocus();
             return;
         }

         mAuth.signInWithEmailAndPassword(mEmail.getText().toString(),mPass.getText().toString()).addOnCompleteListener(task->{
             if (!task.isSuccessful()){
                 try {
                     throw task.getException();
                 }catch(FirebaseAuthWeakPasswordException e) {
                     mPass.setError("Weak Password");
                     mPass.requestFocus();
                 } catch(FirebaseAuthInvalidCredentialsException e) {
                     mEmail.setError("Invalid Email/Password");
                     mEmail.requestFocus();
                 } catch(FirebaseAuthUserCollisionException e) {
                     mEmail.setError("User Already Exists");
                     mEmail.requestFocus();
                 } catch(Exception e) {
                     Log.e(TAG, e.getMessage()+"");
                 }
             }else{
                 if (mAuth.getCurrentUser().isEmailVerified()) {
                     Intent i = new Intent(this, LoggedInActivity.class);
                     startActivity(i);
                     finish();
                 }else{
                     Log.e(TAG,"Verify Email first");
                 }
             }
         });

        });


    }
}