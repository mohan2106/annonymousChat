package com.example.annonymouschat;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private YourThreadAdapter adapter;
    private ArrayList<YourThredClass> itemlist;
    private DatabaseReference threads = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sp = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        final String org = sp.getString("org","NULL");
//        final String userid = sp.getString("id",mAuth.getUid());

        if(org.equals("NULL")) {
            Toast.makeText(getActivity(), "Shared Preference is empty", Toast.LENGTH_SHORT).show();
            threads.child("User").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    SharedPreferences sp2 = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp2.edit();
                    editor.putString("name", dataSnapshot.child("name").getValue().toString());
                    editor.putString("email", dataSnapshot.child("email").getValue().toString());
                    editor.putString("id", mAuth.getUid());
                    editor.putInt("image", Integer.parseInt(dataSnapshot.child("intImage").getValue().toString()));
                    editor.putString("org", dataSnapshot.child("organisation").getValue().toString());
                    editor.putBoolean("iscouncellor", Boolean.parseBoolean(dataSnapshot.child("isCouncellor").getValue().toString()));
                    editor.commit();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_chats, container, false);
        threads.keepSynced(true);
        recyclerView = v.findViewById(R.id.threads_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemlist = new ArrayList<>();
//        itemlist.add(new YourThredClass("mohan","xyz","yesterday","I am so much depressed. My girlfriend broked up with me last night.\nMy academics is also not good.\nI am getting F grade in two subjects. \nI have lots of pressure \nPlease help","hdf","kjfhdj","lkdsfjs","45","26",0));
//        itemlist.add(new YourThredClass("nikhil","xyz","yesterday","I am so much depressed. My girlfriend broked up with me last night.\nMy academics is also not good.\nI am getting F grade in two subjects. \nI have lots of pressure \nPlease help","hdf","kjfhdj","lkdsfjs","45","26",0));
//        itemlist.add(new YourThredClass("aman","xyz","yesterday","I am so much depressed. My girlfriend broked up with me last night.\nMy academics is also not good.\nI am getting F grade in two subjects. \nI have lots of pressure \nPlease help","hdf","kjfhdj","lkdsfjs","45","26",0));
//        itemlist.add(new YourThredClass("sohan","xyz","yesterday","I am so much depressed. My girlfriend broked up with me last night.\nMy academics is also not good.\nI am getting F grade in two subjects. \nI have lots of pressure \nPlease help","hdf","kjfhdj","lkdsfjs","45","26",0));
//        itemlist.add(new YourThredClass("sudarshan","xyz","yesterday","I am so much depressed. My girlfriend broked up with me last night.\nMy academics is also not good.\nI am getting F grade in two subjects. \nI have lots of pressure \nPlease help","hdf","kjfhdj","lkdsfjs","45","26",0));
//        itemlist.add(new YourThredClass("ram","xyz","yesterday","I am so much depressed. My girlfriend broked up with me last night.\nMy academics is also not good.\nI am getting F grade in two subjects. \nI have lots of pressure \nPlease help","hdf","kjfhdj","lkdsfjs","45","26",0));
//        itemlist.add(new YourThredClass("shyam","xyz","yesterday","I am so much depressed. My girlfriend broked up with me last night.\nMy academics is also not good.\nI am getting F grade in two subjects. \nI have lots of pressure \nPlease help","hdf","kjfhdj","lkdsfjs","45","26",0));
        adapter = new YourThreadAdapter(itemlist,getContext());
        recyclerView.setAdapter(adapter);

        SharedPreferences sp = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        final String org = sp.getString("org","NULL");
        final String userid = sp.getString("id",mAuth.getUid());

        if(!org.equals("NULL")){
            loadData(org);
        }else{

            threads.child("User").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    SharedPreferences sp2 = getActivity().getSharedPreferences("User",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp2.edit();
                    editor.putString("name",dataSnapshot.child("name").getValue().toString());
                    editor.putString("email",dataSnapshot.child("email").getValue().toString());
                    editor.putString("id",mAuth.getUid());
                    editor.putInt("image",Integer.parseInt(dataSnapshot.child("intImage").getValue().toString()));
                    editor.putString("org",dataSnapshot.child("organisation").getValue().toString());
                    editor.putBoolean("iscouncellor",Boolean.parseBoolean(dataSnapshot.child("isCouncellor").getValue().toString()));
                    editor.commit();
                    String org2 = dataSnapshot.child("organisation").getValue().toString();
                    loadData(org2);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



        return v;
    }

    private void loadData(String org){


        threads.child(org).child("threads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemlist.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    long t = Long.parseLong(snapshot.child("time").getValue().toString());
                    long temp = t;
                    String like = (snapshot.child("nLikes").getValue().toString());
                    if(like.equals("0")){
                        like = "1";
                    }
                    double credit = Math.log10(Double.parseDouble(like));
                    Date date = new Date();
                    long tt = date.getTime();
//                    tt -= t;
                    t -= tt;
                    credit += ((double)(t))/45000;
                    gettime tm = new gettime();
//                    Long lastseen = Long.parseLong(dataSnapshot.child("lastSeen").getValue().toString());
                    String last = tm.getTimeAgo(temp,getContext());
                    itemlist.add(new YourThredClass("annonymous",snapshot.child("from").getValue().toString(),last,snapshot.child("message").getValue().toString(),"xyz","xyz",snapshot.getKey(),snapshot.child("nLikes").getValue().toString(),snapshot.child("nComments").getValue().toString(),credit,false));
                }
                Collections.sort(itemlist,new Sortbyroll());

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        threads.child(org).child("threads").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

}

class Sortbyroll implements Comparator<YourThredClass>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(YourThredClass a, YourThredClass b)
    {
        return Double.compare(b.credits,a.credits);
    }
}

