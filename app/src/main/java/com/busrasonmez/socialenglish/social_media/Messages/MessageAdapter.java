package com.busrasonmez.socialenglish.social_media.Messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.social_media.ProfileSetting.Persons.Person;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private ArrayList<MessageModel> messageArrayList;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private Context mContext;


    public static final int message_right = 0;
    public static final int message_left = 1;


    public MessageAdapter(Context mContext, ArrayList<MessageModel> personArrayList) {
        this.messageArrayList = personArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == message_right) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageHolder(view);
        }

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        final MessageModel messageModel = this.messageArrayList.get(position);

        holder.message.setText(messageModel.getMessage());
        holder.date.setText(messageModel.getDate());
    }


    class MessageHolder extends RecyclerView.ViewHolder {

        TextView message, seen, date;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            seen = itemView.findViewById(R.id.seen);
            date = itemView.findViewById(R.id.date);
        }
    }

    int result = 1;
//
    @Override
    public int getItemViewType(int position) {

        FirebaseUser user = auth.getCurrentUser();
        String sender_email = user.getEmail();

        if (messageArrayList.get(position).getSender_username().equals(sender_email)) {
            return message_right;
        }

        else{
            return message_left;
        }

    }
}
