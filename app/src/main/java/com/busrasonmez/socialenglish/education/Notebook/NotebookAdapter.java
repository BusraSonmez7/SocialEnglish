package com.busrasonmez.socialenglish.education.Notebook;

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
import com.busrasonmez.socialenglish.education.Notebook.Subject.NotebookWord2Activity;
import com.busrasonmez.socialenglish.education.Work.Subject.WorkSubjectModel;

import java.util.ArrayList;

public class NotebookAdapter extends RecyclerView.Adapter<NotebookAdapter.NotebookHolder> {

    private ArrayList<WorkSubjectModel> subjectArrayList;

    private Context mContext;

    public NotebookAdapter(Context mContext, ArrayList<WorkSubjectModel> subjectModels) {
        this.subjectArrayList = subjectModels;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NotebookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.e_notebook_subject_row,parent,false);
        return new NotebookHolder(view);
    }

    @Override
    public int getItemCount() {
        return subjectArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull NotebookHolder holder, int position) {
        WorkSubjectModel subjectModel = subjectArrayList.get(position);
        holder.subjecttxt.setText(subjectModel.getSubject());
        if(!subjectArrayList.get(position).getImage().equals("default")){
            Glide.with(mContext).load(subjectModel.getImage()).into(holder.subjectimage);

        }
        else {
            holder.subjectimage.setImageResource(R.drawable.icon);
            holder.subjectimage.setBackgroundColor(Color.argb(255,4,99,128));

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.itemView.getContext(), NotebookWord2Activity.class);
                intent.putExtra("subject",subjectModel.getSubject());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }


    class NotebookHolder extends RecyclerView.ViewHolder{

        ImageView subjectimage;
        TextView subjecttxt;

        public NotebookHolder(@NonNull View itemView) {
            super(itemView);
            subjectimage = itemView.findViewById(R.id.image_s);
            subjecttxt = itemView.findViewById(R.id.txtsubject);
        }
    }

}
