package com.geexec.cse227;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LoggedInActivity extends AppCompatActivity {

    private Button mLogout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        mAuth  = FirebaseAuth.getInstance();
        mLogout  = findViewById(R.id.logout);

        mLogout.setOnClickListener(v->{
            mAuth.signOut();
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        });
    }
}