package com.busrasonmez.socialenglish.social_media.ProfileSetting.Favorites;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.Share;
import com.busrasonmez.socialenglish.social_media.Home.HomeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoritesFragment extends AppCompatActivity {
    Toolbar toolbar;
    ImageView fovoritesbtn;
    private FirebaseAuth auth;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private RecyclerView recylerView;

    public HomeAdapter homeAdapter;

    ArrayList<Share> shareArrayList;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sm_fragment_favorites);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
        recylerView = findViewById(R.id.r_view);


        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        shareArrayList = new ArrayList<>();
        recylerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        homeAdapter = new HomeAdapter(shareArrayList,this);
        recylerView.setLayoutManager(layoutManager);
        recylerView.setAdapter(homeAdapter);

        getFavorites();
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.sm_fragment_favorites,container,false);
//        toolbar = view.findViewById(R.id.toolbar);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(toolbar);
////
//        recylerView = view.findViewById(R.id.r_view);
////
//        setHasOptionsMenu(true);
//
//
//        auth = FirebaseAuth.getInstance();
//        firebaseFirestore = FirebaseFirestore.getInstance();
//        firebaseStorage = FirebaseStorage.getInstance();
//        storageReference = firebaseStorage.getReference();
//
//        shareArrayList = new ArrayList<>();
//        recylerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        homeAdapter = new HomeAdapter(shareArrayList,getActivity());
//        recylerView.setLayoutManager(layoutManager);
//        recylerView.setAdapter(homeAdapter);
//
//        getFavorites();
//
//        return view;
//    }

    public void getFavorites(){
        FirebaseUser user = auth.getCurrentUser();
        String email1 = user.getEmail();

        firebaseFirestore.collection("Favorites").whereEqualTo("email",email1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                }
                if(value != null){
                    for (DocumentSnapshot snapshot : value.getDocuments()) {

                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();
                        String sharedID = (String)  data.get("shareID");

                        firebaseFirestore.collection("Shares").whereEqualTo("shareID",sharedID).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                }
                                if(value != null) {
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();

                                        String shareimg,comment,shareID;
                                        comment = (String) data.get("comment");
                                        shareimg = (String) data.get("shareURL");
                                        shareID = (String) data.get("shareID");
                                        UserName(shareimg,comment,shareID,email1);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void UserName(String shareimg, String comment, String shareID, String email){
        firebaseFirestore.collection("Users").whereEqualTo("email",email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                }
                if(value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();

                        String username, profile;
                        username = (String) data.get("username");
                        profile = (String) data.get("profile");
                        Share p = new Share(username,profile,shareimg,comment,email,shareID);
                        shareArrayList.add(p);
                    }
                    if(!shareArrayList.isEmpty()){
                        homeAdapter.notifyDataSetChanged();
                        // bildirimlerArrayList.clear();

                    }
                }
            }
        });
    }
}

