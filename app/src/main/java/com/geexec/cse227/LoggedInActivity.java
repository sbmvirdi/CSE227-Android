package com.geexec.cse227;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoggedInActivity extends AppCompatActivity {

    private Button mLogout,saveButton,readButton,listshow;
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
        readButton = findViewById(R.id.readButton);
        listshow = findViewById(R.id.listshow);



        listshow.setOnClickListener(view -> {
            Intent i = new Intent(this,ListShow.class);
            startActivity(i);
        });


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

        readButton.setOnClickListener(view -> {
            FirebaseDatabase.getInstance().getReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String name = (String) snapshot.child("name").getValue();
                        String phoneno = (String) snapshot.child("phoneNo").getValue();
                        Name.setText(name);
                        phoneNo.setText(phoneno);
                        Toast.makeText(LoggedInActivity.this, "Read Successful!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LoggedInActivity.this, "Save Data First!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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