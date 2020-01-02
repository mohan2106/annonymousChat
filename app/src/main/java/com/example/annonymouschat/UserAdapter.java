package com.example.annonymouschat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {



    private ArrayList<allUserClass> itemList;
    private Context context;

    public UserAdapter(ArrayList<allUserClass> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_user,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        allUserClass ne = itemList.get(position);
        holder.nm.setText(ne.getName());
        holder.st.setText(ne.getStatus());

        Picasso.get().load(ne.image).placeholder(R.drawable.profile_image).into(holder.img);
        final String id = ne.id;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,profileActivity.class);
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        });
//        Glide.with(context).load(ne.image).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img;
        private TextView nm,st;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.single_image);
            nm = itemView.findViewById(R.id.single_user_name);
            st = itemView.findViewById(R.id.single_status);
        }
    }
}
