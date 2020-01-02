package com.example.annonymouschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllusersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private ArrayList<allUserClass> itemList;
    private FirebaseFirestore firestore;
    private DatabaseReference databaseReference;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allusers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("All users");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        databaseReference.keepSynced(true);
        recyclerView = findViewById(R.id.all_users_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        itemList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        adapter = new UserAdapter(itemList,this);
        recyclerView.setAdapter(adapter);
        dialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Getting all users details...");
        dialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();

                for(DataSnapshot user : dataSnapshot.getChildren()){
                    if(!mAuth.getUid().equals(user.child("id").getValue().toString())){
                        itemList.add(new allUserClass(user.child("name").getValue().toString(), user.child("thumb_image").getValue().toString(),
                                user.child("id").getValue().toString(), user.child("status").getValue().toString(),user.child("online").getValue().toString()));

                    }

                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AllusersActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

//        firestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        if(!mAuth.getUid().equals(document.get("id").toString())) {
//                            itemList.add(new allUserClass(document.get("name").toString(), document.get("thumb_image").toString(), document.get("id").toString(), document.get("status").toString()));
//                        }
////                        Log.d(TAG, document.getId() + " => " + document.getData());
//                    }
//                    adapter.notifyDataSetChanged();
//
//                } else {
//                    Toast.makeText(AllusersActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
////                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//                dialog.dismiss();
//            }
//        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
