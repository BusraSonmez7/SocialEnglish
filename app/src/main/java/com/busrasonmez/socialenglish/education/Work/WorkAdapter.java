package com.busrasonmez.socialenglish.education.Work;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.education.Work.Subject.WorkSubjectContent;
import com.busrasonmez.socialenglish.education.Work.Subject.WorkSubjectModel;

import java.util.ArrayList;

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.WorkHolder> {

    private ArrayList<WorkSubjectModel> subjectArrayList;

    private Context mContext;

    public WorkAdapter(Context mContext, ArrayList<WorkSubjectModel> subjectModels) {
        this.subjectArrayList = subjectModels;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public WorkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.e_work_subject,parent,false);
        return new WorkHolder(view);
    }

    @Override
    public int getItemCount() {
        return subjectArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull WorkHolder holder, int position) {
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

                Intent intent = new Intent(holder.itemView.getContext(), WorkSubjectContent.class);
                intent.putExtra("subject",subjectModel.getSubject());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }


    class WorkHolder extends RecyclerView.ViewHolder{

        ImageView subjectimage;
        TextView subjecttxt;

        public WorkHolder(@NonNull View itemView) {
            super(itemView);
            subjectimage = itemView.findViewById(R.id.image_s);
            subjecttxt = itemView.findViewById(R.id.txtsubject);
        }
    }

}
