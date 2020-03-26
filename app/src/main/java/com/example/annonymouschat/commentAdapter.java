package com.example.annonymouschat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.ViewHolder> {

    private ArrayList<commentClass> itemList;
    private Context context;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private boolean Flag = false;
    private boolean Flag2 = false;

    public commentAdapter(ArrayList<commentClass> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final commentClass ne = itemList.get(position);

        databaseReference.child("User").child(ne.getCommenterId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String pp = dataSnapshot.child("thumb_image").getValue().toString();
                Integer intImg  = Integer.parseInt(dataSnapshot.child("intImage").getValue().toString());
                holder.image.setImageResource(intImg);
                holder.name.setText(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Threads Profile Image",databaseError.getMessage());
            }
        });

        final  String postId = singleThreadActivity.thread_id;

        holder.comment.setText(ne.getComment());

        final gettime tm = new gettime();
        final String t  = tm.getTimeAgo(Long.parseLong(ne.getTime()),context);

        holder.time.setText(t);

        //===================================================Initializing likes and dislike of  a commment

        SharedPreferences sp = context.getSharedPreferences("User",Context.MODE_PRIVATE);
        final String org = sp.getString("org","NULL");
        final String userid = sp.getString("id",mAuth.getUid());
        final String commentId = ne.getCommentId();
        databaseReference.child(org).child("commentLike").child(commentId).child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    boolean isLiked = false;
                    boolean isDisliked = false;
                    isLiked = Boolean.parseBoolean(dataSnapshot.child("isLiked").getValue().toString());
                    isDisliked = Boolean.parseBoolean(dataSnapshot.child("isDisliked").getValue().toString());
                    itemList.get(position).setDownvoted(isDisliked);
                    itemList.get(position).setUpvoted(isLiked);
                    holder.like.setBackground(context.getResources().getDrawable(R.drawable.edit_text_back));
                    holder.dislike.setBackground(context.getResources().getDrawable(R.drawable.edit_text_back));
                    if(isLiked){
                        holder.like.setBackground(context.getResources().getDrawable(R.drawable.like_dislike_back));
                    }
                    if(isDisliked){
                        holder.dislike.setBackground(context.getResources().getDrawable(R.drawable.like_dislike_back));
                    }
                }else{
                    holder.like.setBackground(context.getResources().getDrawable(R.drawable.edit_text_back));
                    holder.dislike.setBackground(context.getResources().getDrawable(R.drawable.edit_text_back));
//                    holder.postThumbsUpImage.setImageResource(R.drawable.good_black);
//                    holder.upvoteText.setText("Upvote");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //====================================================================================

        final int nLikes = Integer.parseInt(ne.getLikes());
        final int nDislikes = Integer.parseInt(ne.getDislike());


        holder.like.setText("Helpful("+nLikes+")");
        holder.dislike.setText("Not Helpful("+nDislikes+")");
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLiked = itemList.get(position).isUpvoted();
                boolean isDisliked = itemList.get(position).isDownvoted();
                isLiked = !isLiked;
                itemList.get(position).setUpvoted(isLiked);
                itemList.get(position).setDownvoted(false);
                Toast.makeText(context, "liked"+" "+isLiked, Toast.LENGTH_SHORT).show();
                int n = Integer.valueOf(itemList.get(position).getLikes());
                if(isLiked){

                    liked(n+1,commentId,postId,true,false);
                    holder.like.setText("Helpful("+(n+1)+")");
                    itemList.get(position).setLikes(String.valueOf(n+1));

                }else{
                    liked(n-1,commentId,postId,false,false);
                    holder.like.setText("Helpful("+(n-1)+")");
                    itemList.get(position).setLikes(String.valueOf(n-1));

                }
                int n2 = Integer.valueOf(itemList.get(position).getDislike());
                if(isDisliked){
                    disliked(n2-1,commentId,postId,false,isLiked);
                    holder.dislike.setText("Not Helpful("+(n2 -1)+")");
                    itemList.get(position).setDislike(String.valueOf(n2-1));
                }

//                liked(nLikes,commentId,!isLiked);
                if(isLiked){
                    holder.like.setBackground(context.getResources().getDrawable(R.drawable.like_dislike_back));
                    holder.dislike.setBackground(context.getResources().getDrawable(R.drawable.edit_text_back));
                }else{
                    holder.like.setBackground(context.getResources().getDrawable(R.drawable.edit_text_back));
                    holder.dislike.setBackground(context.getResources().getDrawable(R.drawable.edit_text_back));
                }
//                notifyDataSetChanged();
            }
        });
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLiked = itemList.get(position).isUpvoted();
                boolean isDisliked = itemList.get(position).isDownvoted();
                isDisliked = !isDisliked;
                itemList.get(position).setUpvoted(false);
                itemList.get(position).setDownvoted(isDisliked);
                Toast.makeText(context, "disliked"+" "+isDisliked, Toast.LENGTH_SHORT).show();
                int n = Integer.valueOf(itemList.get(position).getDislike());
                if(isDisliked){
                    disliked(n+1,commentId,postId,true,false);
                    holder.dislike.setText("Not Helpful("+(n+1)+")");
                    itemList.get(position).setDislike(String.valueOf(n+1));

                }else{
                    disliked(n-1,commentId,postId,false,false);
                    holder.like.setText("Not Helpful("+(n-1)+")");
                    itemList.get(position).setDislike(String.valueOf(n-1));

                }
                int n2 = Integer.valueOf(itemList.get(position).getLikes());
                if(isLiked){
                    liked(n2-1,commentId,postId,false,isDisliked);
                    holder.like.setText("Helpful("+(n2 -1)+")");
                    itemList.get(position).setLikes(String.valueOf(n2-1));
                }

                if(isDisliked){
                    holder.dislike.setBackground(context.getResources().getDrawable(R.drawable.like_dislike_back));
                    holder.like.setBackground(context.getResources().getDrawable(R.drawable.edit_text_back));
                }else{
                    holder.like.setBackground(context.getResources().getDrawable(R.drawable.edit_text_back));
                    holder.dislike.setBackground(context.getResources().getDrawable(R.drawable.edit_text_back));
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
        private TextView name,comment,time,like,dislike;
        private CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.commenterName);
            comment = itemView.findViewById(R.id.comment_text);
            time = itemView.findViewById(R.id.comment_time);
            image = itemView.findViewById(R.id.commenterImage);
            like = itemView.findViewById(R.id.comment_upvote);
            dislike = itemView.findViewById(R.id.comment_downvote);
        }
    }


    public boolean liked(final int nlike, final String commentId,final String postId,final boolean like,final boolean dislike){

        SharedPreferences sp = context.getSharedPreferences("User",Context.MODE_PRIVATE);
        final String org = sp.getString("org","NULL");
        final String userid = sp.getString("id",mAuth.getUid());
        boolean res = true;
        if(!org.equals("NULL")){
            Map<String,Object> mp = new HashMap<>();
            mp.put("isLiked",like);
            mp.put("isDisliked",dislike);
            databaseReference.child(org).child("commentLike").child(commentId).child(userid).setValue(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Map<String ,Object> mp2 = new HashMap<>();

                        mp2.put("nLikes",nlike);

                        databaseReference.child(org).child("comments").child(postId).child(commentId).updateChildren(mp2).addOnCompleteListener(new OnCompleteListener<Void>() {
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
            databaseReference.child("User").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
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
                    mp.put("isLiked",like);
                    mp.put("isDisliked",dislike);
                    databaseReference.child(org).child("commentLike").child(commentId).child(userid).setValue(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Map<String ,Object> mp2 = new HashMap<>();

                                mp2.put("nLikes",nlike);

                                databaseReference.child(org).child("comments").child(postId).child(commentId).updateChildren(mp2).addOnCompleteListener(new OnCompleteListener<Void>() {
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


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        return true;
    }

    public boolean disliked(final int nlike, final String commentId,final String postId,final boolean dislike,final boolean like){

        SharedPreferences sp = context.getSharedPreferences("User",Context.MODE_PRIVATE);
        final String org = sp.getString("org","NULL");
        final String userid = sp.getString("id",mAuth.getUid());
        boolean res = true;
        if(!org.equals("NULL")){
            Map<String,Object> mp = new HashMap<>();
            mp.put("isLiked",like);
            mp.put("isDisliked",dislike);
            databaseReference.child(org).child("commentLike").child(commentId).child(userid).setValue(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Map<String ,Object> mp2 = new HashMap<>();

                        mp2.put("nDislikes",nlike);

                        databaseReference.child(org).child("comments").child(postId).child(commentId).updateChildren(mp2).addOnCompleteListener(new OnCompleteListener<Void>() {
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
            databaseReference.child("User").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
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
                    mp.put("isLiked",like);
                    mp.put("isDisliked",dislike);
                    databaseReference.child(org).child("commentLike").child(commentId).child(userid).setValue(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Map<String ,Object> mp2 = new HashMap<>();

                                mp2.put("nDislikes",nlike);

                                databaseReference.child(org).child("comments").child(postId).child(commentId).updateChildren(mp2).addOnCompleteListener(new OnCompleteListener<Void>() {
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


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        return true;
    }
}
