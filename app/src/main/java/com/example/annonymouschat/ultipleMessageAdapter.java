package com.example.annonymouschat;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ultipleMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<messagesClass> itemList;
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public ultipleMessageAdapter(ArrayList<messagesClass> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    class sendHolder extends RecyclerView.ViewHolder{

        private TextView mess1,time1;

        public sendHolder(@NonNull View itemView) {
            super(itemView);

            mess1 = itemView.findViewById(R.id.recieved_message);
            time1 = itemView.findViewById(R.id.time_receivedMessage);

        }
    }

    class revieveHolder extends RecyclerView.ViewHolder{

        private TextView mess2,time2;

        public revieveHolder(@NonNull View itemView) {
            super(itemView);

            mess2 = itemView.findViewById(R.id.recieved_message);
            time2 = itemView.findViewById(R.id.time_receivedMessage);

        }
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        messagesClass ne = itemList.get(position);
        String from = ne.getFrom();
        if(from.equals(mAuth.getUid())){
            return 0;
        }else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_message_layout, parent, false);
                return new sendHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_message_layout, parent, false);
                return new revieveHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        messagesClass ne = itemList.get(position);
        String from = ne.getFrom();
        String txt = ne.getMessage();
        String tm = ne.getTime();
        String time = DateUtils.formatDateTime(context, Long.parseLong(tm), DateUtils.FORMAT_SHOW_TIME);

        if(from.equals(mAuth.getUid())){
            ((sendHolder)holder).mess1.setText(txt);

            ((sendHolder)holder).time1.setText(time);
        }else{
            ((revieveHolder)holder).mess2.setText(txt);
            ((revieveHolder)holder).time2.setText(time);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
