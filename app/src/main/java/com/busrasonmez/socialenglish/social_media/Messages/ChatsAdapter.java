package com.busrasonmez.socialenglish.social_media.Messages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.social_media.ProfileSetting.Persons.Person;
import com.busrasonmez.socialenglish.social_media.ProfileSetting.ProfileActicity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatsHolder> {

    private ArrayList<MessageModel> messagesArraylist;

    private Context mContext;

    public ChatsAdapter(Context mContext, ArrayList<MessageModel> messageModels) {
        this.messagesArraylist = messageModels;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ChatsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sm_messages_row,parent,false);
        return new ChatsHolder(view);
    }

    @Override
    public int getItemCount() {
        return messagesArraylist.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsHolder holder, int position) {
        MessageModel messageModel = messagesArraylist.get(position);
        holder.username.setText(messageModel.getUsername());
        holder.lastmessage.setText(messageModel.getMessage());
        holder.date.setText(messageModel.getDate());

        if(!messagesArraylist.get(position).getProfile().equals("")){
            Glide.with(mContext).load(messageModel.getProfile()).into(holder.profile);

        }
        else {
            holder.profile.setImageResource(R.drawable.profile_n);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), MessagePage.class);

                intent.putExtra("email_share",messagesArraylist.get(position).getBuyer_username());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }


    class ChatsHolder extends RecyclerView.ViewHolder{

        CircleImageView profile;
        TextView username, lastmessage, date;

        public ChatsHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile1);
            username = itemView.findViewById(R.id.txtusername);
            lastmessage = itemView.findViewById(R.id.last_message);
            date = itemView.findViewById(R.id.date);

        }
    }

}
