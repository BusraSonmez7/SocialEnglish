package com.busrasonmez.socialenglish.social_media.Home;

import android.content.Context;
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
import com.busrasonmez.socialenglish.Share;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder>{

    private ArrayList<Share> shareArrayList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseAuth auth;

    private Context mContext;

    public HomeAdapter(ArrayList<Share> personArrayList, Context mContext) {
        this.shareArrayList = personArrayList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sm_main_page_row,parent,false);
        return new HomeAdapter.HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.HomeHolder holder, int position) {
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        Share sharee = shareArrayList.get(position);
        holder.username.setText(sharee.getUsername());
        holder.comment.setText(sharee.getComment());
        if(!sharee.getProfile().equals("default")){
            Glide.with(mContext).load(sharee.getProfile()).into(holder.profile);

        }
        else {
            holder.profile.setImageResource(R.drawable.profile_n);

        }

        if(!sharee.getShareimg().equals("default")){
            Glide.with(mContext).load(sharee.getShareimg()).into(holder.share);

        }
        else {
            holder.share.setImageResource(R.drawable.icon);

        }
        FirebaseUser user = auth.getCurrentUser();
        String email1 = user.getEmail();
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Favorites").whereEqualTo("email",email1).whereEqualTo("shareID",sharee.getShareID())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String documentt = null;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        ArrayList<Share> deneme = new ArrayList<>();
                                        DocumentReference docRef = firebaseFirestore.collection("Favorites").document(document.getId());
                                        documentt = docRef.toString();
                                    }

                                    if(documentt == null){
                                        HashMap<String,Object> favoritesss = new HashMap<>();
                                        favoritesss.put("shareID",sharee.getShareID());
                                        favoritesss.put("email",email1);
                                        favoritesss.put("date",FieldValue.serverTimestamp());
                                        holder.favorite.setImageResource(R.drawable.star_yellow);
                                        firebaseFirestore.collection("Favorites").add(favoritesss).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(mContext,"Added to favorites",Toast.LENGTH_LONG).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                Toast.makeText(mContext,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                                            }
                                        });
                                    }
                                    else {
                                        holder.favorite.setImageResource(R.drawable.star);
                                        firebaseFirestore.collection("Favorites").whereEqualTo("shareID", sharee.getShareID()).whereEqualTo("email",email1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        DocumentReference washingtonRef = firebaseFirestore.collection("Favorites").document(document.getId());
                                                        washingtonRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(mContext,"Removed from favorites",Toast.LENGTH_SHORT).show();

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                            }
                                                        });
                                                    }
                                                } else {
                                                }
                                            }
                                        });
                                    }
                                } else {
                                }
                            }
                        });


            }
        });
        firebaseFirestore.collection("Favorites").whereEqualTo("email",email1).whereEqualTo("shareID",sharee.getShareID()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                String documentt = null;

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList<Share> deneme = new ArrayList<>();
                        DocumentReference docRef = firebaseFirestore.collection("Favorites").document(document.getId());
                        documentt = docRef.toString();
                    }

                    if (documentt != null){
                        holder.favorite.setImageResource(R.drawable.star_yellow);
                    }
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return shareArrayList.size();
    }

    class HomeHolder extends RecyclerView.ViewHolder{

        ImageView profile, share;
        TextView username, comment;
        ImageView favorite, sharebtn, commentbtn;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile_img);
            username = itemView.findViewById(R.id.name_txt);
            share = itemView.findViewById(R.id.share_img);
            comment = itemView.findViewById(R.id.txtcomment);
            favorite = itemView.findViewById(R.id.favorite_btn);
            sharebtn = itemView.findViewById(R.id.share_btn);
            commentbtn = itemView.findViewById(R.id.comment_btn);

        }
    }
}
