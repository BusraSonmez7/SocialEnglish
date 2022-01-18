package com.busrasonmez.socialenglish.education.Work.Word;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.education.Notebook.Word.NotebookWordContentsActivity;

import java.util.ArrayList;

public class WorkWordAdapter extends RecyclerView.Adapter<WorkWordAdapter.WorkHolder> {

    private ArrayList<WorkWordModel> wordModelArrayList;

    private Context mContext;

    public WorkWordAdapter(Context mContext, ArrayList<WorkWordModel> wordModelArrayList) {
        this.wordModelArrayList = wordModelArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public WorkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.e_word_row,parent,false);
        return new WorkHolder(view);
    }

    @Override
    public int getItemCount() {
        return wordModelArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull WorkHolder holder, int position) {
        WorkWordModel wordModel = wordModelArrayList.get(position);
        holder.wordtxt.setText(wordModel.getWord());
        if(!wordModelArrayList.get(position).getImage().equals("default")){
            Glide.with(mContext).load(wordModel.getImage()).into(holder.wordimage);


        }
        else {
            holder.wordimage.setImageResource(R.drawable.icon);
            holder.wordimage.setBackgroundColor(Color.argb(255,4,99,128));


        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.itemView.getContext(), NotebookWordContentsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("word",wordModel.getWord());
                intent.putExtra("subject",wordModel.getSubject());
                intent.putExtra("isemail","work");
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }


    class WorkHolder extends RecyclerView.ViewHolder{

        ImageView wordimage;
        TextView wordtxt;

        public WorkHolder(@NonNull View itemView) {
            super(itemView);
            wordimage = itemView.findViewById(R.id.image_s);
            wordtxt = itemView.findViewById(R.id.txtsubject);
        }
    }

}
