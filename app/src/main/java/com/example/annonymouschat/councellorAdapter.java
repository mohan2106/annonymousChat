package com.example.annonymouschat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class councellorAdapter extends RecyclerView.Adapter<councellorAdapter.ViewHolder> {

    private ArrayList<allUserClass> itemList;

    public councellorAdapter(ArrayList<allUserClass> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public councellorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.councellor_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final councellorAdapter.ViewHolder holder, int position) {

        allUserClass ne = itemList.get(position);
        final String image = ne.getImage();
        String nam=  ne.getName();
        holder.name.setText(nam);
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
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img;
        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.councellorImage);
            name = itemView.findViewById(R.id.councellor_name);
        }
    }
}
