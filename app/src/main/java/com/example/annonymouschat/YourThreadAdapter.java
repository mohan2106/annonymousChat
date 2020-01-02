package com.example.annonymouschat;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class YourThreadAdapter extends RecyclerView.Adapter<YourThreadAdapter.ViewHolder> {

    private ArrayList<YourThredClass> itemList;
    private Context context;

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
    public void onBindViewHolder(@NonNull final YourThreadAdapter.ViewHolder holder, int position) {

        YourThredClass ne = itemList.get(position);
        final String user_image = ne.getProfile_image();
        Picasso.get().load(user_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile_image).into(holder.postUserImage, new Callback() {
            @Override
            public void onSuccess() {

            }
            @Override
            public void onError(Exception e) {
                Picasso.get().load(user_image).placeholder(R.drawable.profile_image).into(holder.postUserImage);
            }
        });
        Picasso.get().load(ne.getPost_image()).into(holder.postImage);

        holder.postUserName.setText(ne.getName());
        String tm = ne.getDate();
        /*String time = DateUtils.formatDateTime(context, Long.parseLong(tm), DateUtils.FORMAT_SHOW_TIME);
        String date = DateUtils.formatDateTime(context, Long.parseLong(tm), DateUtils.FORMAT_SHOW_TIME);
        gettime tim = new gettime();

        String last = tim.getTimeAgo(Long.parseLong(ne.getDate()),context);*/

        holder.postDateTime.setText(tm);
        holder.postContent.setText(ne.getText());
        holder.postThumbsUpText.setText(ne.getnLike());
        holder.postThumbsDownText.setText(ne.getnDislike());
        holder.postCommentText.setText(ne.getNcomment());

        holder.postCommentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Comment clicked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.postContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Content clicked", Toast.LENGTH_SHORT).show();
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postUserImage = itemView.findViewById(R.id.post_user_image);
            postUserName = itemView.findViewById(R.id.post_name);
            postDateTime  = itemView.findViewById(R.id.post_dateTime);
            postContent = itemView.findViewById(R.id.post_content);
            postThumbsUpText = itemView.findViewById(R.id.post_thumb_up_text);
            postThumbsDownText = itemView.findViewById(R.id.post_thumb_down_text);
            postCommentText = itemView.findViewById(R.id.post_comment_text);
            postImage = itemView.findViewById(R.id.post_image);
            postThumbsUpImage = itemView.findViewById(R.id.post_thumb_up_image);
            postThumbDownImage = itemView.findViewById(R.id.post_thumb_down);
            postCommentImage = itemView.findViewById(R.id.post_comment);
        }
    }
}
