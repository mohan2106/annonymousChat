package com.example.annonymouschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.admin.v1beta1.Progress;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class profileActivity extends AppCompatActivity {

    private Button btn,decline;
    private ImageView img;
    private TextView name,status;
    private FirebaseFirestore firestore;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference notify = database.getReference("notification");
    private DatabaseReference frnd = database.getReference("friends");
    private DatabaseReference frend_req = database.getReference().child("frnd_req");
    private int frnd_status=0;
    String username ;
    private String userStatus,userimage,otherimage;

    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        dialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
//        Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show();
        dialog.setMessage("Get user details...");
        dialog.show();
        btn = findViewById(R.id.profileButton);
        img = findViewById(R.id.profileImage);
        decline = findViewById(R.id.decline_btn);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        name = findViewById(R.id.profileName);
        status = findViewById(R.id.profileStatus);
        firestore = FirebaseFirestore.getInstance();

        databaseReference.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("name").getValue().toString();
                userStatus = dataSnapshot.child("status").getValue().toString();
                userimage = dataSnapshot.child("thumb_image").getValue().toString();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(profileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
//        firestore.collection("Users").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                username = documentSnapshot.get("name").toString();
//                userStatus = documentSnapshot.get("status").toString();
//                userimage = documentSnapshot.get("thumb_image").toString();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).placeholder(R.drawable.profile_image).into(img);
                name.setText(dataSnapshot.child("name").getValue().toString());
                status.setText(dataSnapshot.child("status").getValue().toString());
                otherimage = dataSnapshot.child("thumb_image").getValue().toString();
                frend_req.child(mAuth.getUid()).child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.child("req").getValue().toString().equals("sent")){
                                frnd_status = 1;
                                btn.setText("Cancel Friend Request");

                            }else if(dataSnapshot.child("req").getValue().toString().equals("received")){
                                frnd_status = 2;
                                btn.setText("Accept Friend Request");
                                decline.setVisibility(View.VISIBLE);
                            }else{
                                frnd_status = 0;
                            }
                        }else{
                            frnd.child(mAuth.getUid()).child(id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        frnd_status = 3;
                                        btn.setText("Unfriend");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    frnd_status = 0;
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*firestore.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Picasso.get().load(documentSnapshot.getString("image")).placeholder(R.drawable.profile_image).into(img);
                name.setText(documentSnapshot.getString("name").toString());
                status.setText(documentSnapshot.get("status").toString());
                otherimage = documentSnapshot.get("thumb_image").toString();
                firestore.collection("Users").document(mAuth.getUid()).collection("friend_req").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists()){
                                if(doc.get("req").toString().equals("sent")){
                                    frnd_status = 1;
                                    btn.setText("Cancel Friend Request");

                                }else if(doc.get("req").toString().equals("received")){
                                    frnd_status = 2;
                                    btn.setText("Accept Friend Request");
                                    decline.setVisibility(View.VISIBLE);
                                }else{
                                    frnd_status = 0;
                                }

                            }else{
                                firestore.collection("Users").document(mAuth.getUid()).collection("friends").document(id).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    DocumentSnapshot doc2 = task.getResult();
                                                    if(doc2.exists()){
                                                        frnd_status = 3;
                                                        btn.setText("Unfriend this user");
                                                        dialog.dismiss();
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                        else{
                            Toast.makeText(profileActivity.this, "Something Went Wrong "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });*/

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineFriendRequest();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (frnd_status){
                    case 0:
                        btn.setEnabled(false);
                        sendFriendRequest();
                        return;
                    case 1:
                        cancelFriendRequest();
                        return;
                    case 2:
                        acceptFriendRequest();
                        return;
                    case 3:
                        unfriend();
                    default:
                        return;
                }
            }
        });
    }
    private void sendFriendRequest(){
        dialog.setMessage("Sending Friend Request...");
        dialog.show();
        Map<String,Object> data = new HashMap<>();
        data.put("req","sent");


        frend_req.child(mAuth.getUid()).child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Map<String,Object> data2 = new HashMap<>();
                    data2.put("req","received");
                    frend_req.child(id).child(mAuth.getUid()).setValue(data2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                Map<String,Object> notification = new HashMap<>();
                                notification.put("from",mAuth.getUid());
                                notification.put("type","request");
                                notify.child(id).push().setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(profileActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                                        frnd_status = 1;
                                        btn.setText("Cancel frnd request");
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(profileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(profileActivity.this, task2.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                            btn.setEnabled(true);
                            dialog.dismiss();
                        }
                    });
                    btn.setEnabled(true);
                    dialog.dismiss();

                }else{
                    dialog.dismiss();
                    Toast.makeText(profileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*firestore.collection("Users").document(mAuth.getUid()).collection("friend_req").document(id).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Map<String,Object> data2 = new HashMap<>();
                    data2.put("req","received");
                    firestore.collection("Users").document(id).collection("friend_req").document(mAuth.getUid()).set(data2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Map<String,Object> notification = new HashMap<>();
                                notification.put("from",mAuth.getUid());
                                notification.put("type","request");
                                notify.child(id).push().setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(profileActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                                        frnd_status = 1;
                                        btn.setText("Cancel frnd request");
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(profileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
//                                firestore.collection("Notifications").document(id).collection("Noti").add(notification).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                    @Override
//                                    public void onSuccess(DocumentReference documentReference) {
//                                        Toast.makeText(profileActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
//                                        frnd_status = 1;
//                                        btn.setText("Cancel frnd request");
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//
//                                    }
//                                });

                            }else{
                                Toast.makeText(profileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            btn.setEnabled(true);
                            dialog.dismiss();
                        }
                    });
                }else{
                    btn.setEnabled(true);
                    dialog.dismiss();
                    Toast.makeText(profileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }
    private void cancelFriendRequest(){
        dialog.setMessage("Cancelling Friend Request");
        dialog.show();
        frend_req.child(mAuth.getUid()).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    frend_req.child(id).child(mAuth.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                Toast.makeText(profileActivity.this, "Successfully Deleted Request", Toast.LENGTH_SHORT).show();
                                frnd_status = 0;
                                btn.setText("Send Friend Request");
                                dialog.dismiss();
                            }

                        }
                    });
                }else{
                    Toast.makeText(profileActivity.this, "something went wrong "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        /*firestore.collection("Users").document(mAuth.getUid()).collection("friend_req").document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firestore.collection("Users").document(id).collection("friend_req").document(mAuth.getUid()).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(profileActivity.this, "Successfully Deleted Request", Toast.LENGTH_SHORT).show();
                                        frnd_status = 0;
                                        btn.setText("Send Friend Request");
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(profileActivity.this, "something went wrong "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profileActivity.this, "Something went Wrong "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });*/

    }

    private void acceptFriendRequest(){

        dialog.setMessage("Adding in your friend List...");
        dialog.show();
        final Map<String,Object> data = new HashMap<>();
        data.put("from",new Timestamp(new Date()));
        data.put("name",name.getText());
        data.put("status",status.getText());
        data.put("image",otherimage);

        frnd.child(mAuth.getUid()).child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    final Map<String,Object> data2 = new HashMap<>();
                    data2.put("from",new Timestamp(new Date()));
                    data2.put("name",username);
                    data2.put("status",userStatus);
                    data2.put("image",userimage);
                    frnd.child(id).child(mAuth.getUid()).setValue(data2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
//                                cancelFriendRequest();
                                Toast.makeText(profileActivity.this, "Friend added", Toast.LENGTH_SHORT).show();
                                deleteFriendRequest();
                                btn.setText("Unfriend");
                                frnd_status = 3;
                                decline.setVisibility(View.GONE);
                                dialog.dismiss();
                            }else{
                                Toast.makeText(profileActivity.this, "Something went wrong "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }else{
                    Toast.makeText(profileActivity.this, "Something went wrong "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        /*firestore.collection("Users").document(mAuth.getUid()).collection("friends").document(id).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    final Map<String,Object> data2 = new HashMap<>();
                    data2.put("from",new Timestamp(new Date()));
                    data2.put("name",username);
                    data2.put("status",userStatus);
                    data2.put("image",userimage);
                    firestore.collection("Users").document(id).collection("friends").document(mAuth.getUid()).set(data2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                cancelFriendRequest();
                                Toast.makeText(profileActivity.this, "Friend added", Toast.LENGTH_SHORT).show();

                                btn.setText("Unfriend");
                                frnd_status = 3;
                                decline.setVisibility(View.GONE);
                                dialog.dismiss();
                            }else{
                                Toast.makeText(profileActivity.this, "Something went wrong "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }else{
                    dialog.dismiss();
                    Toast.makeText(profileActivity.this, "Something went Wrong "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });*/

    }

    private void deleteFriendRequest(){
        frend_req.child(mAuth.getUid()).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    frend_req.child(id).child(mAuth.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
//                                Toast.makeText(profileActivity.this, "Successfully Deleted Request", Toast.LENGTH_SHORT).show();
                                frnd_status = 3;
                                btn.setText("Unfriend");
//                                dialog.dismiss();
                            }

                        }
                    });
                }else{
                    Toast.makeText(profileActivity.this, "something went wrong "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                }
            }
        });
    }

    private void unfriend(){
        dialog.setMessage("Deleting user from your friend List");
        dialog.show();

        frnd.child(mAuth.getUid()).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    frnd.child(id).child(mAuth.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(profileActivity.this, "Deleted user from friend List", Toast.LENGTH_SHORT).show();
                                frnd_status = 0;
                                btn.setText("Send Friend Request");
                                dialog.dismiss();
                            }else{
                                Toast.makeText(profileActivity.this, "Something went Worng "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }else{
                    Toast.makeText(profileActivity.this, "Something went Worng "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        /*firestore.collection("Users").document(id).collection("friends").document(mAuth.getUid()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            firestore.collection("Users").document(mAuth.getUid()).collection("friends").document(id).delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(profileActivity.this, "Deleted user from friend List", Toast.LENGTH_SHORT).show();
                                                frnd_status = 0;
                                                btn.setText("Send Friend Request");
                                                dialog.dismiss();
                                            }else{
                                                Toast.makeText(profileActivity.this, "Something went Worng "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }

                                        }
                                    });

                        }else{
                            Toast.makeText(profileActivity.this, "Something went Worng "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    }
                });*/
    }
    private void declineFriendRequest(){
        dialog.setMessage("Declinig friend Request");
        dialog.show();


        frend_req.child(mAuth.getUid()).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    frend_req.child(id).child(mAuth.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if(task2.isSuccessful()){
                                Toast.makeText(profileActivity.this, "Successfully Deleted Request", Toast.LENGTH_SHORT).show();
                                frnd_status = 0;
                                btn.setText("Send Friend Request");
                                decline.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }

                        }
                    });
                }else{
                    Toast.makeText(profileActivity.this, "something went wrong "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        /*firestore.collection("Users").document(mAuth.getUid()).collection("friend_req").document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firestore.collection("Users").document(id).collection("friend_req").document(mAuth.getUid()).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(profileActivity.this, "Successfully Declined Request", Toast.LENGTH_SHORT).show();
                                        frnd_status = 0;
                                        btn.setText("Send Friend Request");
                                        decline.setVisibility(View.GONE);
                                        dialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(profileActivity.this, "something went wrong "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profileActivity.this, "Something went Wrong "+e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });*/
    }
}

