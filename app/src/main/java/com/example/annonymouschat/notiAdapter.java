package com.example.annonymouschat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class notiAdapter extends RecyclerView.Adapter<notiAdapter.ViewHolder> {
    private ArrayList<notiClass> itemList;
    private Context context;

    public notiAdapter(ArrayList<notiClass> itemList,Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public notiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.noti_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull notiAdapter.ViewHolder holder, int position) {
        notiClass n = itemList.get(position);
        holder.body.setText(n.getBody());
        holder.title.setText(n.getTitle());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView title,body;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.notiImage);
            title = itemView.findViewById(R.id.notiTitle);
            body = itemView.findViewById(R.id.notiBody);
        }
    }
}
