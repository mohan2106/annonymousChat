package com.example.annonymouschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class writeThread extends AppCompatActivity {

    Button allBtn,conBtn;
    EditText edt;
    ProgressDialog dialog;
    DatabaseReference threads= FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth ;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        final String org = sp.getString("org","NULL");
//        final String userid = sp.getString("id",mAuth.getUid());

        if(org.equals("NULL")) {
            Toast.makeText(this, "Shared Preference is empty", Toast.LENGTH_SHORT).show();
            threads.child("User").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    SharedPreferences sp2 = getSharedPreferences("User", Context.MODE_PRIVATE);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_thread);
        mAuth = FirebaseAuth.getInstance();
        edt = findViewById(R.id.threadText);
        allBtn = findViewById(R.id.shareWithAll);
        conBtn = findViewById(R.id.shareWithCouncellorBtn);
        dialog = new ProgressDialog(this);
        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ss = edt.getText().toString();
                if(TextUtils.isEmpty(ss)){
                    edt.setError("Kindly write your problem...");
                    edt.requestFocus();
                }else{
                    shareMessageToMentors(ss,2);
                }
            }
        });
        conBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ss = edt.getText().toString();
                if(TextUtils.isEmpty(ss)){
                    edt.setError("Kindly write your problem...");
                    edt.requestFocus();
                }else{
                    shareMessageToMentors(ss,2);
                }
            }
        });
    }

    private void shareMessageToMentors(String str,int x){
        dialog.setTitle("Sending");
        dialog.setMessage("Sending your request to our councellors, Don't be panic...");
        dialog.show();
        SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        final String org = sp.getString("org","NULL");
        final String userid = sp.getString("id",mAuth.getUid());
        DatabaseReference messagepush = threads.child(org).child("threads").push();
        final String pushedKey = messagepush.getKey();

        Map data = new HashMap();
        data.put("message",str);
        data.put("from",mAuth.getUid());
        data.put("time", ServerValue.TIMESTAMP);
        data.put("receivedBy","false");
        data.put("visibleTo",x);
        data.put("nLikes",0);
        data.put("nComments",0);




        messagepush.updateChildren(data, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError!=null){
                    Toast.makeText(writeThread.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else{
                    Map ok = new HashMap();
                    ok.put("id",pushedKey);
                    threads.child(org).child("UserPost").child(mAuth.getUid()).updateChildren(ok, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            dialog.dismiss();
                            if(databaseError != null){
                                Toast.makeText(writeThread.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(writeThread.this, "SuccessFull", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }
            }
        });


    }

}
