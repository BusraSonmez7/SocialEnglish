package com.busrasonmez.socialenglish.social_media.Messages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busrasonmez.socialenglish.social_media.AddPost.PostSharing;
import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.social_media.ProfileSetting.Persons.Person;
import com.busrasonmez.socialenglish.social_media.Search.SearchAdapter;
import com.busrasonmez.socialenglish.user_login;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MessagesFragment extends Fragment {

    Toolbar toolbar;
    public ChatsAdapter chatsAdapter;
    private RecyclerView recylerView;
    private static ArrayList<MessageModel> messageModelArrayList;
    ArrayList<MessageModel> messageModelArrayList2;
    ArrayList<MessageModel> messageModelArrayList3;



    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sm_fragment_messages,container,false);
        toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        recylerView = view.findViewById(R.id.r_view);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        messageModelArrayList = new ArrayList<>();
        messageModelArrayList2 = new ArrayList<>();

        recylerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        chatsAdapter = new ChatsAdapter(getActivity(),messageModelArrayList2);
        recylerView.setLayoutManager(layoutManager);
        recylerView.setAdapter(chatsAdapter);

//
        setHasOptionsMenu(true);

        auth = FirebaseAuth.getInstance();

        getChats();
        UsernameAndProfile();

        return view;
    }
    public void getChats(){
        firebaseFirestore.collection("Messages").orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error !=null){
                    Toast.makeText(getActivity(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

                if(value != null){
                    FirebaseUser user = auth.getCurrentUser();
                    String email = user.getEmail();
                    //messageModelArrayList.clear();

                    for(DocumentSnapshot snapshot : value.getDocuments()){
                        //bildirimlerArrayList.clear();
                        HashMap<String,Object> data = (HashMap<String, Object>) snapshot.getData();
                        String sender_emailx = (String) data.get("sender_email");
                        String buyer_emailx = (String) data.get("buyer_email");
                        String messagex = (String) data.get("message");
                        String message_image = (String) data.get("message_image");
                        Timestamp time = (Timestamp) data.get("date");

                        if(time !=null){
                            Date date = time.toDate();
                            String tarih = date.toString();
                            String pattern = "dd/MM/yyyy kk:mm";
                            SimpleDateFormat format = new SimpleDateFormat(pattern);

                            if(buyer_emailx.equals(email)){
                                int count = 0;
                                MessageModel p = new MessageModel(sender_emailx,messagex,format.format(date));
                                for(int i = 0; i<messageModelArrayList.size();i++){
                                    if(messageModelArrayList.get(i).getBuyer_username().equals(sender_emailx)){
                                        messageModelArrayList.set(i,p);
                                        count ++;
                                    }
                                }

                                if(count == 0){
                                    messageModelArrayList.add(p);
                                }
                            }
                            else if(sender_emailx.equals(email)){
                                int count = 0;
                                MessageModel p = new MessageModel(buyer_emailx,messagex,format.format(date));

                                for(int i = 0; i<messageModelArrayList.size();i++){
                                    if(messageModelArrayList.get(i).getBuyer_username().equals(buyer_emailx)){
                                        messageModelArrayList.set(i,p);
                                        count ++;
                                    }
                                }
                                if(count == 0){
                                    messageModelArrayList.add(p);

                                }
                            }
                        }
                    }
                    if(!messageModelArrayList.isEmpty()){
                        UsernameAndProfile();

                    }
                    else{
                    }

                }
            }
        });

    }

    public void UsernameAndProfile(){
        messageModelArrayList2.clear();

        for(int i = 0; i<messageModelArrayList.size(); i++){
            String message = messageModelArrayList.get(i).getMessage();
            String email = messageModelArrayList.get(i).getBuyer_username();
            String date = messageModelArrayList.get(i).getDate();

            firebaseFirestore.collection("Users").whereEqualTo("email",email).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                    if(error !=null){
                        Toast.makeText(getActivity(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                    }

                    if(value != null){
                        for(DocumentSnapshot snapshot : value.getDocuments()){
                            //bildirimlerArrayList.clear();
                            HashMap<String,Object> data = (HashMap<String, Object>) snapshot.getData();
                            String username = (String) data.get("username");
                            String profile = (String) data.get("profile");

                            MessageModel p = new MessageModel(username,email,message,profile,date,false);
                            messageModelArrayList2.add(p);

                            // Toast.makeText(getActivity(),"count: "+count1,Toast.LENGTH_SHORT).show();


                        }
                        if(!messageModelArrayList2.isEmpty()){
                            Toast.makeText(getActivity(),"NEDEN: ",Toast.LENGTH_SHORT).show();
                            chatsAdapter.notifyDataSetChanged();
                            // bildirimlerArrayList.clear();
                        }
                        else{
                            //Toast.makeText(getActivity(),"Liste boş",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }
        messageModelArrayList.clear();


    }

//    public void UsernameAndProfile(ArrayList<MessageModel> model){
//        firebaseFirestore.collection("Users").whereEqualTo("email",sender_emailx).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
//                if(error !=null){
//                    Toast.makeText(getActivity(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
//                }
//                if(value != null){
//                    for(DocumentSnapshot snapshot : value.getDocuments()) {
//                        //bildirimlerArrayList.clear();
//                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();
//                        String username = (String) data.get("username");
//                        String profile = (String) data.get("profile");
//
//
//                        int count = 0;
//                        for(int i = 0; i<messageModelArrayList.size();i++){
//                            if(messageModelArrayList.get(i).getUsername().equals(username)){
//                                messageModelArrayList.set(i,p);
//                                count ++;
//                            }
//                        }
//
//                        if(count == 0){
//                            messageModelArrayList.add(p);
//                        }
//
//
//                    }
//                }
//
//            }
//        });
//
//    }
}
