package com.example.annonymouschat;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class postAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<postClass> itemList;
    private Context context;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public postAdapter(ArrayList<postClass> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    class pendingPost extends RecyclerView.ViewHolder{
        private TextView date,text,dt;
        public pendingPost(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.pendingDate);
            text = itemView.findViewById(R.id.pendingText);
            dt = itemView.findViewById(R.id.postDate);
        }
    }

    class post extends RecyclerView.ViewHolder{
        private TextView date,text,name,dt;
        private CircleImageView img;
        public post(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.pendingDate);
            text = itemView.findViewById(R.id.pendingText);
            name=  itemView.findViewById(R.id.post_recieverName);
            img = itemView.findViewById(R.id.postImage);
            dt = itemView.findViewById(R.id.postDate);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_post, parent, false);
                return new pendingPost(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post, parent, false);
                return new post(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        postClass ne = itemList.get(position);
        String text = ne.getText();
        String time = ne.getTime();
        String receiver = ne.getReceivedBy();
        String tm = DateUtils.formatDateTime(context, Long.parseLong(time), DateUtils.FORMAT_SHOW_TIME);
        String dd = DateUtils.formatDateTime(context, Long.parseLong(time),DateUtils.FORMAT_SHOW_DATE);
        if(receiver.equals("false")){
            ((pendingPost)holder).text.setText(text);
            ((pendingPost)holder).date.setText(tm);
            ((pendingPost)holder).dt.setText(dd);
        }else{
            ((post)holder).text.setText(text);
            ((post)holder).date.setText(tm);
            ((post)holder).name.setText("name");
            ((post)holder).dt.setText(dd);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }



    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        postClass ne = itemList.get(position);
        if(ne.getReceivedBy().equals("false")){
            return 0;
        }else{
            return 1;
        }
    }
}
