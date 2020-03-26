package com.example.annonymouschat;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class AnnonymousChat extends Application {

    private FirebaseAuth mAuth;
//    private FirebaseFirestore firestore;
    private DatabaseReference mUserData;
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getUid() != null){
            mUserData = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getUid());
            mUserData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null){
                        mUserData.child("online").onDisconnect().setValue("false");
                        mUserData.child("lastSeen").onDisconnect().setValue(ServerValue.TIMESTAMP);
                        mUserData.child("online").setValue("true");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }
}
