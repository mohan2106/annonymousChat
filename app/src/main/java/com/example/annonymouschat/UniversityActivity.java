package com.example.annonymouschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UniversityActivity extends AppCompatActivity {

    private EditText searchText;
    private ImageButton searchBtn;
    private RecyclerView recyclerView;
    public static Activity activity;
    private ArrayList<universityClass> itemList;
    private universityAdapter adapter;
    private ProgressDialog dialog;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("organisations");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university);
        activity = this;
        dialog = new ProgressDialog(this);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String pass = intent.getStringExtra("password");
        Integer image = intent.getIntExtra("image",R.drawable.c1);

        recyclerView = findViewById(R.id.university_recycler);
        searchText = findViewById(R.id.university_edit_text);
        searchBtn = findViewById(R.id.university_search_button);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
        adapter = new universityAdapter(this,itemList,name,email,pass,dialog,image);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot org: dataSnapshot.getChildren()) {
                    itemList.add(new universityClass(org.child("name").getValue().toString(),org.child("id").getValue().toString(),Integer.parseInt(org.child("members").getValue().toString())));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
