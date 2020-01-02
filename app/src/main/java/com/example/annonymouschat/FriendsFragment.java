package com.example.annonymouschat;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private RecyclerView recyclerView;
    private postAdapter adapter;
    private ArrayList<postClass> itemList ;
    private ArrayList<String> li=new ArrayList<>();
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog dialog;
    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
//        firestore = FirebaseFirestore.getInstance();
//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setPersistenceEnabled(true)
//                .build();
//        firestore.setFirestoreSettings(settings);
        recyclerView = v.findViewById(R.id.friends_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("openChat");
        databaseReference.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getContext(),R.style.MyAlertDialogStyle);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        itemList = new ArrayList<>();
        itemList.clear();
        dialog.setMessage("Getting Friend List...");
        dialog.show();
        adapter = new postAdapter(itemList,this.getContext());
        recyclerView.setAdapter(adapter);


        databaseReference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    itemList.add(new postClass( snapshot.child("message").getValue().toString(),snapshot.child("time").getValue().toString(),snapshot.child("receivedBy").getValue().toString()));

                }
                dialog.dismiss();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
