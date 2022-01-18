package com.busrasonmez.socialenglish.education.Work.Sentence;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.education.Notebook.Sentence.NotebookSentenceContentsActivity;

import java.util.ArrayList;

public class WorkSentenceAdapter extends RecyclerView.Adapter<WorkSentenceAdapter.SentenceHolder> {

    private ArrayList<WorkSentenceModel> workSentenceModelArrayList;

    private Context mContext;

    public WorkSentenceAdapter(Context mContext, ArrayList<WorkSentenceModel> sentenceModels) {
        this.workSentenceModelArrayList = sentenceModels;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SentenceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.e_sentence_row,parent,false);
        return new SentenceHolder(view);
    }

    @Override
    public int getItemCount() {
        return workSentenceModelArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SentenceHolder holder, int position) {
        WorkSentenceModel sentenceModel = workSentenceModelArrayList.get(position);
        holder.sentence_header.setText(sentenceModel.getSentence_header());
        holder.sentence.setText(sentenceModel.getSentence());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.itemView.getContext(), NotebookSentenceContentsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("sentence_header",sentenceModel.getSentence_header());
                intent.putExtra("sentence",sentenceModel.getSentence());
                intent.putExtra("subject",sentenceModel.getSubject());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }


    class SentenceHolder extends RecyclerView.ViewHolder{

        TextView sentence_header, sentence;

        public SentenceHolder(@NonNull View itemView) {
            super(itemView);
            sentence_header = itemView.findViewById(R.id.sentence_headertxt);
            sentence = itemView.findViewById(R.id.sentencetxt);
        }
    }

}
