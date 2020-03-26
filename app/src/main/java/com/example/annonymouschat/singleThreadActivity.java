package com.example.annonymouschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class singleThreadActivity extends AppCompatActivity {

    private RecyclerView threadCommentRecyclerView;
    private ArrayList<commentClass> itemList;
    private commentAdapter adapter;
    private TextView threadContent;
    private EditText etcomment;
    public static String thread_id;
    private ImageButton addcomment;
    private ProgressDialog dialog;
    private CircleImageView threadOwnerImage;
    private TextView threadOwnerName,threadDate;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    public static String postId;
    private DatabaseReference comments = FirebaseDatabase.getInstance().getReference("IIT_Guwahati");
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_thread);
        Intent it = getIntent();
        thread_id = it.getStringExtra("postId");
        String post = it.getStringExtra("post");
        String from = it.getStringExtra("from");
        String tm = it.getStringExtra("date");
//        post += " " + thread_id;
        etcomment = findViewById(R.id.commentBody);
        addcomment = findViewById(R.id.addComment);
        comments.keepSynced(true);
        threadOwnerName = findViewById(R.id.threadOwnerName);
        threadDate = findViewById(R.id.threadDateTime);
        threadOwnerImage = findViewById(R.id.threadOwnerImage);
        dialog = new ProgressDialog(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        threadContent = findViewById(R.id.threadContent);
        threadContent.setText(post);

        Toast.makeText(this, from , Toast.LENGTH_SHORT).show();

        databaseReference.child("User").child(from).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Integer intImg= Integer.parseInt(dataSnapshot.child("intImage").getValue().toString());
                threadOwnerImage.setImageResource(intImg);
                final String ownerName = dataSnapshot.child("name").getValue().toString();
                threadOwnerName.setText(ownerName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Threads Profile Image",databaseError.getMessage());
            }
        });
        threadDate.setText(tm);

        threadCommentRecyclerView = findViewById(R.id.threadComment);
        threadCommentRecyclerView.setHasFixedSize(true);
        threadCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();
//        itemList.add(new commentClass("mohan kr","xyz","abcd","You should not be panic, We are here to help you out from this situation.","15 min sgo","15"));
//        itemList.add(new commentClass("rajnish jha","xyz","abcd","You should not be panic, We are here to help you out .","20 min sgo","15"));
//        itemList.add(new commentClass("prithvi sriram","xyz","abcd","You should not be panic.","15 min sgo","15"));
//        itemList.add(new commentClass("pankaj","xyz","abcd","You should not be panic, We are here to help you out from this situation.","15 min sgo","15"));
//        itemList.add(new commentClass("sohan","xyz","abcd","You should not be panic, We are with you","25 min sgo","15"));
//        itemList.add(new commentClass("sudarshan guha","xyz","abcd","You should not be panic, We are here to help you out from this situation.","15 min sgo","15"));
//        itemList.add(new commentClass("mohan mishra","xyz","abcd","You should not be panic, We are here to help you out from this situation.","15 min sgo","15"));
//        itemList.add(new commentClass("aman jha","xyz","abcd","You should not be panic, We can help you in this situation.","15 min sgo","15"));
        adapter = new commentAdapter(itemList,this);
        threadCommentRecyclerView.setAdapter(adapter);

        dialog.setMessage("Loading comments...");
        dialog.show();
//        Toast.makeText(this, thread_id, Toast.LENGTH_LONG).show();


        loadComments();


        addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });
    }

    private void loadComments(){
        comments.child("comments").child(thread_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    itemList.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String time = snapshot.child("time").getValue().toString();
//                        String  t = new gettime(Long.parseLong(time),this);
                        String nLike = snapshot.child("nLikes").getValue().toString();
                        String nDislike = snapshot.child("nDislikes").getValue().toString();
                        double credit = getCredit(nLike,nDislike);
                        itemList.add(new commentClass(snapshot.child("from").getValue().toString(),"",snapshot.child("from").getValue().toString(),snapshot.child("comment").getValue().toString(),time,nLike,nDislike,credit,snapshot.getKey()));
//                        itemList.add(new commentClass("tester","xyz","ijf","testing","12 minutes","5"));
                    }
                    Collections.sort(itemList,new Sortbyroll());
                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(singleThreadActivity.this, "No Comment Exist", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private double getCredit(String up,String down){
        long u = Long.valueOf(up);
        long d = Long.valueOf(down);
        long n = u+d;
        if(n==0){
            return 0;
        }
        double z = 1.281551565545;
        double p = (Double.parseDouble(up))/n;
        double left = p + (1.0/(2*n))*z*z;
        double right = z*Math.sqrt(p*(1-p)/n + (z*z)/(4*n*n));
        double under = 1 + (1.0/n)*z*z ;
        return (left-right)/under ;
    }

    private void addComment(){
        String mess = etcomment.getText().toString();
        if(TextUtils.isEmpty(mess)){
            etcomment.setError("Type some comment first...");
            etcomment.requestFocus();
            return;
        }

        Map<String,Object> data = new HashMap<>();
        data.put("from",mAuth.getUid());
        data.put("comment",mess);
        data.put("nLikes",0);
        data.put("nDislikes",0);
        data.put("time", ServerValue.TIMESTAMP);
        DatabaseReference commentRef = comments.child("comments").child(thread_id).push();

        commentRef.setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(singleThreadActivity.this, "SUccessfully updated", Toast.LENGTH_SHORT).show();
                    etcomment.setText("");
                    loadComments();
                }else{
                    Toast.makeText(singleThreadActivity.this, "ERROR: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    class Sortbyroll implements Comparator<commentClass>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(commentClass a, commentClass b)
        {
            return Double.compare(b.credit,a.credit);
        }
    }
}
