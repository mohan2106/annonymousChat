package com.example.annonymouschat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private ArrayList<friendsClass> itemList;
    private Context context;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("User");
    private String name="",image="";

    public FriendsAdapter(ArrayList<friendsClass> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_user,parent,false);
        return new FriendsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        friendsClass ne = itemList.get(position);
        final String key = ne.getKey();
        name="";
        image="";
        databaseReference.child(ne.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("name").getValue().toString();
                image = dataSnapshot.child("thumb_image").getValue().toString();
                holder.nm.setText(name);
                holder.st.setText(dataSnapshot.child("status").getValue().toString());

                if(dataSnapshot.child("online").getValue().toString().equals("true")){
                    holder.onl.setVisibility(View.VISIBLE);
                }else{
                    holder.onl.setVisibility(View.GONE);
                }
                Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile_image).into(holder.img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(image).placeholder(R.drawable.profile_image).into(holder.img);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,chatActivity.class);
                intent.putExtra("id",key);
                intent.putExtra("name",name);
                intent.putExtra("image",image);
                context.startActivity(intent);
//                CharSequence options[] = new CharSequence[]{"Open Profile","Send Message"};
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Select Options");
//                builder.setItems(options, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if(which == 0){
//                            Intent intent = new Intent(context,profileActivity.class);
//                            intent.putExtra("id",key);
//                            context.startActivity(intent);
//                        }else{
//
//                        }
//                    }
//                });
//                builder.show();
            }
        });
//        holder.nm.setText(ne.getName());
//        holder.st.setText(ne.getStatus());
//        final String im =ne.image;
////        Picasso.get().load(ne.image).placeholder(R.drawable.profile_image).into(holder.img);
//        Picasso.get().load(ne.image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile_image).into(holder.img, new Callback() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Picasso.get().load(im).placeholder(R.drawable.profile_image).into(holder.img);
//            }
//        });
//        final String id = ne.id;
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context,profileActivity.class);
//                intent.putExtra("id",id);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img,onl;
        private TextView nm,st;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.single_image);
            nm = itemView.findViewById(R.id.single_user_name);
            st = itemView.findViewById(R.id.single_status);
            onl = itemView.findViewById(R.id.online);
        }
    }
}
