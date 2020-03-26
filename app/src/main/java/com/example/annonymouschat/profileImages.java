package com.example.annonymouschat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class profileImages extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Integer> itemList;
    private profileImageAdapter adapter;
    public static Activity pI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_images);
        pI = this;
        recyclerView = findViewById(R.id.imageRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        itemList = new ArrayList<>();
        itemList.add(new Integer(R.drawable.c1));
        itemList.add(new Integer(R.drawable.c2));
        itemList.add(new Integer(R.drawable.c3));
        itemList.add(new Integer(R.drawable.c4));
        itemList.add(new Integer(R.drawable.c5));
        itemList.add(new Integer(R.drawable.c6));
        itemList.add(new Integer(R.drawable.c7));
        itemList.add(new Integer(R.drawable.c8));
        itemList.add(new Integer(R.drawable.c9));
        itemList.add(new Integer(R.drawable.c10));
        itemList.add(new Integer(R.drawable.c11));
        itemList.add(new Integer(R.drawable.c11));
        itemList.add(new Integer(R.drawable.c12));
        itemList.add(new Integer(R.drawable.c13));
        itemList.add(new Integer(R.drawable.c14));
        itemList.add(new Integer(R.drawable.c15));
        itemList.add(new Integer(R.drawable.c16));
        itemList.add(new Integer(R.drawable.c17));
        itemList.add(new Integer(R.drawable.c18));
        itemList.add(new Integer(R.drawable.c19));
        itemList.add(new Integer(R.drawable.c20));
        itemList.add(new Integer(R.drawable.c21));
        itemList.add(new Integer(R.drawable.c22));
        itemList.add(new Integer(R.drawable.c23));
        adapter = new profileImageAdapter(this,itemList);
        recyclerView.setAdapter(adapter);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//        startActivity(new Intent(profileImages.this,registerActivity.class));
//
//    }
}



