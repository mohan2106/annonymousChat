package com.example.annonymouschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatActivity extends AppCompatActivity {

    private String id;
    private String name,image;
    private DatabaseReference databaseReference;
    private Toolbar toolbar;
    private TextView chatterName,onlineStatus;
    private CircleImageView chatterImage;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText txt;
    private CircleImageView addImg;
    private RecyclerView recyclerView;
//    private messagesAdapter adapter;
    private ultipleMessageAdapter adapter;
    private static int NO_OF_MESSAGE_TO_DISPLAY = 10;
    private int NO_OF_PAGE = 1;
    private SwipeRefreshLayout swipeLayout;
    private ArrayList<messagesClass> itemList;
    private ImageButton sendBtn;
    private String lastKey="";
    private String prevKey = "";
    private int pos=0;
    private boolean flag = true;
    private String org;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        name="";
        image="";

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(chatActivity.this,startActivity.class));
            finish();
        }else{
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("User").child(mAuth.getUid()).child("online").setValue("true");
        }
//        updateUI(currentUser);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatterImage = findViewById(R.id.chatterImage);
        chatterName = findViewById(R.id.chatterName);
        onlineStatus = findViewById(R.id.onlieStatus);
        swipeLayout = findViewById(R.id.swipe_layout);
        recyclerView = findViewById(R.id.messageRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
//        adapter = new messagesAdapter(itemList,this);
        adapter = new ultipleMessageAdapter(itemList,this);
        recyclerView.setAdapter(adapter);
//        loadMessage();
        txt = findViewById(R.id.messageBody);
        addImg = findViewById(R.id.addDocs);
        sendBtn = findViewById(R.id.sendMessage);
//        mAuth = FirebaseAuth.getInstance();
        id="";
        name="";
        image="";
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        image= getIntent().getStringExtra("image");
        org = getIntent().getStringExtra("org");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



//        Toast.makeText(this, org, Toast.LENGTH_SHORT).show();

        SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
        org = sp.getString("org","NULL");
        Boolean isUser = sp.getBoolean("iscouncellor",true);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        toolbar.setTitle(name);
        chatterName.setText(name);
//        Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile_image).into(chatterImage, new Callback() {
//            @Override
//            public void onSuccess() {
//
//            }
//            @Override
//            public void onError(Exception e) {
//                Picasso.get().load(image).placeholder(R.drawable.profile_image).into(chatterImage);
//            }
//        });
        chatterImage.setImageResource(Integer.parseInt(image));

        DatabaseReference messageReference = databaseReference.child("messages").child(mAuth.getUid()).child(id);

        Query messageQuery = messageReference.limitToLast(NO_OF_MESSAGE_TO_DISPLAY*NO_OF_PAGE);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

//                itemList.add(new messagesClass("hey there","true","2340","text","annonymous"));
                pos++;
                if(pos==1){
                    lastKey = dataSnapshot.getKey();
                    prevKey = dataSnapshot.getKey();
                }
                itemList.add(new messagesClass(dataSnapshot.child("message").getValue().toString(),dataSnapshot.child("seen").getValue().toString()
                        ,dataSnapshot.child("time").getValue().toString(),dataSnapshot.child("type").getValue().toString(),dataSnapshot.child("from").getValue().toString()));
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(itemList.size()-1);

                swipeLayout.setRefreshing(false);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*databaseReference.child("User").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("online").getValue().toString().equals("false")){
//                    onlineStatus.setText(dataSnapshot.child("lastSeen").getValue().toString());
                    gettime tm = new gettime();
                    Long lastseen = Long.parseLong(dataSnapshot.child("lastSeen").getValue().toString());
                    String last = tm.getTimeAgo(lastseen,getApplicationContext());
                    onlineStatus.setText(last);

                }
                image = dataSnapshot.child("intImage").getValue().toString();
                toolbar.setTitle(dataSnapshot.child("name").getValue().toString());
                chatterName.setText(dataSnapshot.child("name").getValue().toString());
                Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile_image).into(chatterImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(image).placeholder(R.drawable.profile_image).into(chatterImage);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        databaseReference.child("Chat").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(id)){
                    Map<String,Object> chatmap = new HashMap<>();
                    chatmap.put("seen","false");
                    chatmap.put("timeStamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/"+mAuth.getUid()+"/"+id,chatmap);
                    chatUserMap.put("Chat/"+id+"/"+mAuth.getUid(),chatmap);

                    databaseReference.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference2) {
                            if(databaseError!=null){
                                Toast.makeText(chatActivity.this, databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NO_OF_PAGE++;
//                itemList.clear();
                pos=0;
                flag = true;
                loadMessage();
                swipeLayout.setRefreshing(false);
            }
        });

//        chatterImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(chatActivity.this,profileActivity.class);
//                            intent.putExtra("id",id);
//                            startActivity(intent);
//            }
//        });

    }

    private void loadMessage(){
        DatabaseReference messageReference = databaseReference.child("messages").child(mAuth.getUid()).child(id);

        Query messageQuery = messageReference.orderByKey().endAt(lastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

//                itemList.add(new messagesClass("hey there","true","2340","text","annonymous"));
                    if(!prevKey.equals(dataSnapshot.getKey())){
                        itemList.add(pos++,new messagesClass(dataSnapshot.child("message").getValue().toString(),dataSnapshot.child("seen").getValue().toString()
                                ,dataSnapshot.child("time").getValue().toString(),dataSnapshot.child("type").getValue().toString(),dataSnapshot.child("from").getValue().toString()));

                    }else{
                        prevKey = lastKey;
                    }

                    if(pos==1){
                        lastKey = dataSnapshot.getKey();
                    }

                adapter.notifyDataSetChanged();
                recyclerView.scrollBy(10,0);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void sendMessage() {

        String text = txt.getText().toString();

        if(!TextUtils.isEmpty(text)){
            Map messageMap = new HashMap();

            DatabaseReference messagepush = databaseReference.child("messages").child(mAuth.getUid()).child(id).push();

            String push_id = messagepush.getKey();

            String current_user_ref = "messages/"+mAuth.getUid() + "/" +  id;
            String chat_user_ref = "messages/" + id + "/" + mAuth.getUid();
            messageMap.put("seen","false");
            messageMap.put("message",text);
            messageMap.put("type","text");
            messageMap.put("time",ServerValue.TIMESTAMP);
            messageMap.put("from",mAuth.getUid());

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref+"/"+push_id,messageMap);
            messageUserMap.put(chat_user_ref+"/"+push_id,messageMap);

            databaseReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(databaseError!=null){
                        Log.d("CHAT_LOG",databaseError.getMessage().toString());
                    }
                }
            });

            String chatHistoryUser = "chatHistory/"+mAuth.getUid()+"/"+id;
            String chatHistoryCouncellor = "chatHistory/"+id+"/"+mAuth.getUid();

            Map lastMap = new HashMap();
            lastMap.put("message",text);
            lastMap.put("seen",false);

            Map lastHistoryMap = new HashMap();
            lastHistoryMap.put(chatHistoryUser,lastMap);
            lastHistoryMap.put(chatHistoryCouncellor,lastMap);

            databaseReference.child(chatHistoryUser).setValue(lastMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()) {
                        Log.d("CHAT_LOG", task.getException().getMessage());
                    }
                }
            });
            databaseReference.child(chatHistoryCouncellor).setValue(lastMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()) {
                        Log.d("CHAT_LOG", task.getException().getMessage());
                    }
                }
            });

            txt.setText("");


        }

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
