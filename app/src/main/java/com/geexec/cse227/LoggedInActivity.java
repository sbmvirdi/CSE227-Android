package com.geexec.cse227;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LoggedInActivity extends AppCompatActivity {

    private Button mLogout,saveButton;
    private FirebaseAuth mAuth;
    private EditText phoneNo,Name;
    private DatabaseReference mRef;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        mAuth  = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser()!=null){
            uid = mAuth.getCurrentUser().getUid();
        }else{
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }
        mRef = FirebaseDatabase.getInstance().getReference(uid);

        mLogout  = findViewById(R.id.logout);
        saveButton = findViewById(R.id.saveButton);
        phoneNo = findViewById(R.id.phoneNo);
        Name = findViewById(R.id.Name);


        saveButton.setOnClickListener(view -> {
            if (TextUtils.isEmpty(Name.getText().toString().trim())){
                Name.setError("Enter a Name");
                Name.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(phoneNo.getText().toString().trim())){
                phoneNo.setError("Enter a Phone no");
                phoneNo.requestFocus();
                return;
            }
            Map<String,Object> map = new HashMap<>();
            map.put("name",Name.getText().toString().trim());
            map.put("phoneNo",phoneNo.getText().toString().trim());

            mRef.updateChildren(map).addOnCompleteListener(task ->{
                if (task.isSuccessful()){
                    Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show();
                    Name.setText("");
                    phoneNo.setText("");
                }
            });
        });

        mLogout.setOnClickListener(v->{
            mAuth.signOut();
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        });
    }
}