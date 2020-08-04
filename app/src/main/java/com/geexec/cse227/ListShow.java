package com.geexec.cse227;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListShow extends AppCompatActivity {
    private List<String> mList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_show);

        mList = new ArrayList<>();
        listView = findViewById(R.id.listView);

        FirebaseDatabase.getInstance().getReference("Entry").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> list = (List<String>) snapshot.getValue();
                if (!list.isEmpty()) {
                    mList.addAll(list);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ListShow.this, android.R.layout.simple_list_item_1, mList);
                listView.setAdapter(arrayAdapter);
                Toast.makeText(ListShow.this, "Data loaded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}