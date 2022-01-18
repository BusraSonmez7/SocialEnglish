package com.busrasonmez.socialenglish.social_media.Search;

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
import com.busrasonmez.socialenglish.social_media.ProfileSetting.ProfileActicity;
import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.social_media.ProfileSetting.Persons.Person;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    private ArrayList<Person> personArrayList;
    private ArrayList<Person> personArrayListFull;

    private Context mContext;

    public SearchAdapter(Context mContext, ArrayList<Person> personArrayList) {
        this.personArrayList = personArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sm_persons_search_row,parent,false);
        return new SearchAdapter.SearchHolder(view);
    }

    @Override
    public int getItemCount() {
        return personArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        Person person = personArrayList.get(position);
        holder.username.setText(person.getUsername());
        holder.about.setText(person.getAbout());
        if(!personArrayList.get(position).getImage().equals("default")){
            Glide.with(mContext).load(person.getImage()).into(holder.profile);

        }
        else {
            holder.profile.setImageResource(R.drawable.profile_n);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.itemView.getContext(), ProfileActicity.class);
                intent.putExtra("username",person.getUsername());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }


    class SearchHolder extends RecyclerView.ViewHolder{

        ImageView profile;
        TextView username, about;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile1);
            username = itemView.findViewById(R.id.txtusername);
            about = itemView.findViewById(R.id.txtabout);
        }
    }

}
