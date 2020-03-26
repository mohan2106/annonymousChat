package com.example.annonymouschat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RequestFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView,verticleRecyclerView;
    private councellorAdapter adapter;
    private ArrayList<allUserClass> itemList;
    private ArrayList<friendsClass> mentorLsit;
    private FriendsAdapter frndAdapter;
    private TextView orgName,orgText;
    private ProgressDialog dialog;
    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v  = inflater.inflate(R.layout.fragment_request, container, false);
        verticleRecyclerView = v.findViewById(R.id.vericleRecyclerView);
        verticleRecyclerView.setHasFixedSize(true);
        orgName = v.findViewById(R.id.orgName);
        orgText = v.findViewById(R.id.orgText);
        verticleRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
//        recyclerView = v.findViewById(R.id.horizontalRecyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,true));
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getContext(),R.style.MyAlertDialogStyle);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
//        dialog.setMessage("getting mentor list");
//        dialog.show();
//        itemList = new ArrayList<>();
//        itemList.add(new allUserClass("pallavita mam","default","xyz","hey there","true"));
//        itemList.add(new allUserClass("Rohini mam","default","xyz","hey there","true"));
//        itemList.add(new allUserClass("Roshini mam","default","xyz","hey there","true"));
//        itemList.add(new allUserClass("Priyanka  mam","default","xyz","hey there","true"));
//        itemList.add(new allUserClass("Rohit sir","default","xyz","hey there","true"));
//        itemList.add(new allUserClass("Pallavi mam","default","xyz","hey there","true"));
//        adapter = new councellorAdapter(itemList);
//        recyclerView.setAdapter(adapter);

        mentorLsit = new ArrayList<>();
        frndAdapter = new FriendsAdapter(mentorLsit,this.getContext());
        verticleRecyclerView.setAdapter(frndAdapter);

        SharedPreferences sp = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        final String org = sp.getString("org","NULL");
        final String uid = sp.getString("id",mAuth.getUid());

        Boolean isCouncellor = sp.getBoolean("iscouncellor",false);

        orgName.setText(org);

        if(isCouncellor){
            orgText.setText("");
            databaseReference.child("chatHistory").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mentorLsit.clear();

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String id = snapshot.getKey();

                        mentorLsit.add(new friendsClass(id , "10 months"));
                    }
                    frndAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else{
            orgText.setText("Councellors of your organization ");
            databaseReference.child(org).child("councellors").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mentorLsit.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String id = snapshot.child("id").getValue().toString();
                        if(!id.equals(uid)){
                            mentorLsit.add(new friendsClass(id , "10 months"));
                        }
                    }
//                dialog.dismiss();
                    frndAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }




    }

    @Override
    public void onStop() {
        super.onStop();
    }

    // TODO: Rename method, update argument and hook method into UI event

}
