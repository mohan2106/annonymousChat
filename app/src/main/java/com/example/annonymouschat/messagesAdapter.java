package com.example.annonymouschat;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class messagesAdapter extends RecyclerView.Adapter<messagesAdapter.ViewHolder> {

    private ArrayList<messagesClass> itemList;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public messagesAdapter(ArrayList<messagesClass> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.get_message_layout,parent,false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        messagesClass ne = itemList.get(position);
        holder.message.setText(ne.getMessage());
        holder.tm.setText(ne.getTime());
        String from = ne.getFrom();
        if(from.equals(mAuth.getUid())){
            holder.layout.setBackgroundResource(R.drawable.send_message);
            holder.message.setTextColor(Color.WHITE);
            holder.tm.setTextColor(Color.WHITE);
        }else{
            holder.layout.setBackgroundResource(R.drawable.message_get);
            holder.message.setTextColor(Color.BLACK);
            holder.tm.setTextColor(Color.BLACK);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layout;
        private TextView message,tm;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.recieved_message);
            tm = itemView.findViewById(R.id.time_receivedMessage);
            layout = itemView.findViewById(R.id.message_background);
        }
    }
}
