package com.busrasonmez.socialenglish.social_media.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.Share;
import com.busrasonmez.socialenglish.user_login;
import com.google.android.material.navigation.NavigationView;
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

public class HomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    ImageView fovoritesbtn;
    private FirebaseAuth auth;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private RecyclerView recylerView;


    public HomeAdapter homeAdapter;


    ArrayList<Share> shareArrayList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sm_fragment_home,container,false);
//        toolbar = view.findViewById(R.id.toolbar);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(toolbar);
        recylerView = view.findViewById(R.id.r_view);

        drawerLayout = (DrawerLayout) view.findViewById(R.id.homedrawer);
        navigationView = view.findViewById(R.id.drawerfragment);

//        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.open,R.string.close);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
//        actionBarDrawerToggle.syncState();

//
        setHasOptionsMenu(true);


        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        shareArrayList = new ArrayList<>();
        recylerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        homeAdapter = new HomeAdapter(shareArrayList,getActivity());
        recylerView.setLayoutManager(layoutManager);
        recylerView.setAdapter(homeAdapter);

        LinkControl();

        return view;
    }


    public void LinkControl() {

        FirebaseUser user = auth.getCurrentUser();
        String email1 = user.getEmail();

        firebaseFirestore.collection("Users").whereEqualTo("email",email1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                }

                if (value != null) {

                    String usernameAB;

                    int i = 0;
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();
                        usernameAB = (String) data.get("username");

                        String finalUsernameAB = usernameAB;
                        firebaseFirestore.collection("Persons").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                }

                                if (value != null) {

                                    int i = 0;
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();

                                        if (data.get("usernameA").equals(finalUsernameAB)) {

                                            String usernameT = (String)  data.get("usernameB");
                                            UserInformation(usernameT);


                                        }
                                        else if(data.get("usernameB").equals(finalUsernameAB)){
                                            String usernameT = (String)  data.get("usernameA");
                                            UserInformation(usernameT);
                                        } else{

                                        }

                                    }
                                }
                            }
                        });
//

                    }
                }
            }
        });
    }

    public void UserInformation(String usernameT){
        firebaseFirestore.collection("Users").whereEqualTo("username",usernameT).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                }

                if (value != null) {

                    int i = 0;
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();

                        String email,profilee;
                        email = (String) data.get("email");
                        profilee = (String) data.get("profile");

                        firebaseFirestore.collection("Shares").whereEqualTo("email",email).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                }

                                if (value != null) {

                                    int i = 0;
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();

                                        String shareimg,comment,shareID;
                                        comment = (String) data.get("comment");
                                        shareimg = (String) data.get("shareURL");
                                        shareID = (String) data.get("shareID");
                                        Share p = new Share(usernameT,profilee,shareimg,comment,email,shareID);
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
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(getActivity(), user_login.class);
        startActivity(intent);

        switch (item.getItemId()){
            case R.id.like:

                break;
            case R.id.profile:
                break;
            case R.id.favori:
                break;
            case R.id.person:
                break;
            case R.id.sign_out:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.profile_icon,menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.profile_icon:
//                break;
//            case R.id.list_item:
//
//                auth.signOut();
//                Intent intent = new Intent(getActivity(), user_login.class);
//                startActivity(intent);
//                break;
//            case R.id.add:
//                Intent intent2 = new Intent(getActivity(), PostSharing.class);
//                startActivity(intent2);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
