package com.example.annonymouschat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class YourThreadAdapter extends RecyclerView.Adapter<YourThreadAdapter.ViewHolder> {

    private ArrayList<YourThredClass> itemList;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private boolean isLiked = false;
    private boolean Flag = false;


    public YourThreadAdapter(ArrayList<YourThredClass> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public YourThreadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.post_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final YourThreadAdapter.ViewHolder holder, final int position) {

        YourThredClass ne = itemList.get(position);
        final String user_image = ne.getProfile_image();

        final String iimg;
        String iname;
        databaseReference.child("User").child(ne.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Integer intImg= Integer.parseInt(dataSnapshot.child("intImage").getValue().toString());
                holder.postUserImage.setImageResource(intImg);
                holder.postUserName.setText(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Threads Profile Image",databaseError.getMessage());
            }
        });
//        Picasso.get().load(ne.getPost_image()).into(holder.postImage);

//        holder.postUserName.setText(ne.getName());
        String tm = ne.getDate();
        final String postId = ne.getPost_id();
        final String post = ne.getText();
        final String from = ne.getId();

        SharedPreferences sp = context.getSharedPreferences("User",Context.MODE_PRIVATE);
        final String org = sp.getString("org","NULL");
        final String userid = sp.getString("id",mAuth.getUid());

        databaseReference.child(org).child("upvote").child(postId).child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    isLiked = true;
                    Flag = true;
                    itemList.get(position).setLiked(true);
                    holder.upvoteText.setText("Upvoted");
                    holder.postThumbsUpImage.setImageResource(R.drawable.good);
                }else{
                    isLiked = false;
                    Flag = false;
                    holder.postThumbsUpImage.setImageResource(R.drawable.good_black);
                    holder.upvoteText.setText("Upvote");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.postDateTime.setText(tm);
        holder.postContent.setText(ne.getText());
        holder.postThumbsUpText.setText("("+ne.getnLike()+")");
        holder.postCommentText.setText("("+ne.getNcomment()+")");
        final int nLike = Integer.parseInt(ne.getnLike());
        final String tm2 = tm;

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,singleThreadActivity.class);
                intent.putExtra("postId",postId);
                intent.putExtra("post",post);
                intent.putExtra("from",from);
                intent.putExtra("date",tm2);
                context.startActivity(intent);
            }
        });


        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean x = itemList.get(position).isLiked();
                x = !x;
                itemList.get(position).setLiked(x);
                if(x){
                    String s;
                    int n = Integer.parseInt(itemList.get(position).getnLike());
                    liked(n+1,postId);
                    itemList.get(position).setnLike(String.valueOf(n+1));
                    holder.upvoteText.setText("Upvoted");
                    holder.postThumbsUpImage.setImageResource(R.drawable.good);

                    holder.postThumbsUpText.setText("("+(n+1)+")");

                }else{

                    int n = Integer.parseInt(itemList.get(position).getnLike());
                    liked(n-1,postId);
                    itemList.get(position).setnLike(String.valueOf(n-1));
                    holder.postThumbsUpImage.setImageResource(R.drawable.good_black);
                    holder.upvoteText.setText("Upvote");
                    holder.postThumbsUpText.setText("("+(n-1)+")");

                }
//                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView postUserImage;
        private TextView postUserName;
        private TextView postDateTime;
        private TextView postContent;
        private TextView postThumbsUpText;
        private TextView postThumbsDownText;
        private TextView postCommentText;
        private ImageView postImage;
        private ImageView postThumbsUpImage;
        private ImageView postThumbDownImage;
        private ImageView postCommentImage;
        private LinearLayout upvote,comment;
        private TextView upvoteText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postUserImage = itemView.findViewById(R.id.post_user_image);
            postUserName = itemView.findViewById(R.id.post_name);
            postDateTime  = itemView.findViewById(R.id.post_dateTime);
            postContent = itemView.findViewById(R.id.post_content);
            postThumbsUpText = itemView.findViewById(R.id.post_thumb_up_text);
            postCommentText = itemView.findViewById(R.id.post_comment_text);
            postImage = itemView.findViewById(R.id.post_image);
            postThumbsUpImage = itemView.findViewById(R.id.post_thumb_up_image);
            postCommentImage = itemView.findViewById(R.id.post_comment);
            upvote = itemView.findViewById(R.id.upvote_button);
            comment = itemView.findViewById(R.id.comment_btn);
            upvoteText = itemView.findViewById(R.id.upvoteText);
        }
    }

    public void liked(final int nlike, final String postId){

        SharedPreferences sp = context.getSharedPreferences("User",Context.MODE_PRIVATE);
        final String org = sp.getString("org","NULL");
        final String userid = sp.getString("id",mAuth.getUid());
        boolean res = true;
        if(!org.equals("NULL")){
            Map<String,Object> mp = new HashMap<>();
            mp.put("liked",true);
            databaseReference.child(org).child("upvote").child(postId).child(userid).setValue(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Map<String ,Object> mp2 = new HashMap<>();
                        mp2.put("nLikes",nlike);
                        databaseReference.child(org).child("threads").child(postId).updateChildren(mp2).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d("Mohan","Liked successfull to post "+ postId);

                                }else{
                                    Toast.makeText(context, "ERROR: "+task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }else{
                        Toast.makeText(context, "ERROR: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            String org2;
            databaseReference.child("User").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    SharedPreferences sp2 = context.getSharedPreferences("User",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp2.edit();
                    editor.putString("name",dataSnapshot.child("name").getValue().toString());
                    editor.putString("email",dataSnapshot.child("email").getValue().toString());
                    editor.putString("id",mAuth.getUid());
                    editor.putInt("image",Integer.parseInt(dataSnapshot.child("intImage").getValue().toString()));
                    editor.putString("org",dataSnapshot.child("organisation").getValue().toString());
                    editor.putBoolean("iscouncellor",Boolean.parseBoolean(dataSnapshot.child("isCouncellor").getValue().toString()));
                    editor.commit();

                    final String org2 = dataSnapshot.child("organisation").getValue().toString();
                    final String userid2 = mAuth.getUid();
                    Map<String,Object> mp = new HashMap<>();
                    mp.put("liked",true);
                    databaseReference.child(org2).child("upvote").child(postId).child(userid2).setValue(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Map<String ,Object> mp2 = new HashMap<>();
                                mp2.put("nLikes",nlike);
                                databaseReference.child(org2).child("threads").child(postId).updateChildren(mp2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.d("Mohan","Liked successfull to post "+ postId);

                                        }else {
                                            Toast.makeText(context, "ERROR: " + task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }else{
                                Toast.makeText(context, "ERROR: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        return ;
    }

    public void dislike(final int nlike,final String postId){
        SharedPreferences sp = context.getSharedPreferences("User",Context.MODE_PRIVATE);
        final String org = sp.getString("org","NULL");
        final String userid = sp.getString("id",mAuth.getUid());

        if(!org.equals("NULL")){

            databaseReference.child(org).child("upvote").child(postId).child(userid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Map<String ,Object> mp2 = new HashMap<>();
                        mp2.put("nLikes",nlike);
                        databaseReference.child(org).child("threads").child(postId).updateChildren(mp2).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d("Mohan","Disliked successfull to post "+ postId);

                                }else{
                                    Toast.makeText(context, "ERROR: "+task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }else{
                        Toast.makeText(context, "ERROR: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            String org2;
            databaseReference.child("User").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    SharedPreferences sp2 = context.getSharedPreferences("User",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp2.edit();
                    editor.putString("name",dataSnapshot.child("name").getValue().toString());
                    editor.putString("email",dataSnapshot.child("email").getValue().toString());
                    editor.putString("id",mAuth.getUid());
                    editor.putInt("image",Integer.parseInt(dataSnapshot.child("intImage").getValue().toString()));
                    editor.putString("org",dataSnapshot.child("organisation").getValue().toString());
                    editor.putBoolean("iscouncellor",Boolean.parseBoolean(dataSnapshot.child("isCouncellor").getValue().toString()));
                    editor.commit();

                    final String org2 = dataSnapshot.child("organisation").getValue().toString();
                    final String userid2 = mAuth.getUid();

                    databaseReference.child(org2).child("upvote").child(postId).child(userid2).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Map<String ,Object> mp2 = new HashMap<>();
                                mp2.put("nLikes",nlike);
                                databaseReference.child(org2).child("threads").child(postId).updateChildren(mp2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.d("Mohan","disLiked successfull to post "+ postId);

                                        }else {
                                            Toast.makeText(context, "ERROR: " + task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }else{
                                Toast.makeText(context, "ERROR: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

}
