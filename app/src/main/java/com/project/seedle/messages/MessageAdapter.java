package com.project.seedle.messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.seedle.R;
import com.project.seedle.chat.Chat;
import com.project.seedle.chatActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private List<MessageList> messageLists;
    private final Context context;




    public MessageAdapter(List<MessageList> messageLists, Context context) {
        this.messageLists = messageLists;
        this.context = context;
    }



    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {

        MessageList list2 = messageLists.get(position);
        if(!list2.getProfilePic().isEmpty()){
            Glide.with(context)
                    .load(list2.getProfilePic())
                    .into(holder.profilePic);
        }
        holder.name.setText(list2.getName());
        holder.lastMessages.setText(list2.getLastMessage());
        if(list2.getUnseenMessages()==0){
            holder.unseenMessages.setVisibility(View.GONE);
            holder.lastMessages.setTextColor(Color.parseColor("#959595"));
        }
        else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.lastMessages.setText(list2.getUnseenMessages()+"");
            holder.lastMessages.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("mobile",list2.getMobile());
                intent.putExtra("name",list2.getName());
                intent.putExtra("profilepic",list2.getProfilePic());
                intent.putExtra("chat_key",list2.getChatKey());
                context.startActivity(intent);
            }
        });

    }

    public void updateData(List<MessageList> messageLists)
    {
        this.messageLists = messageLists;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return messageLists.size();
    }
    static class MyViewHolder extends  RecyclerView.ViewHolder {

        private CircleImageView profilePic;
        private TextView name;
        private TextView lastMessages;
        private TextView unseenMessages;

        private LinearLayout rootLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            profilePic=itemView.findViewById(R.id.profilepic);
            name = itemView.findViewById(R.id.name);
            lastMessages = itemView.findViewById(R.id.lastMessage);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);


            rootLayout = itemView.findViewById(R.id.rootLayout);
        }
    }
}
