package com.busrasonmez.socialenglish.social_media.ProfileSetting.Persons;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.social_media.Search.SearchAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class PersonsFragment extends AppCompatActivity {
    Toolbar toolbar;
    private RecyclerView recylerView;
    public SearchAdapter searchAdapter;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<Person> personArrayList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sm_fragment_persons);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recylerView = findViewById(R.id.r_view);
////
//        setHasOptionsMenu(true);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        personArrayList = new ArrayList<>();
        recylerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        searchAdapter = new SearchAdapter(this,personArrayList);
        recylerView.setLayoutManager(layoutManager);
        recylerView.setAdapter(searchAdapter);

        LinkControl();

    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.sm_fragment_persons,container,false);
//        toolbar = view.findViewById(R.id.toolbar);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(toolbar);
//
//        recylerView = view.findViewById(R.id.r_view);
////
//        setHasOptionsMenu(true);
//
//        auth = FirebaseAuth.getInstance();
//        firebaseFirestore = FirebaseFirestore.getInstance();
//
//        personArrayList = new ArrayList<>();
//        recylerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        searchAdapter = new SearchAdapter(getActivity(),personArrayList);
//        recylerView.setLayoutManager(layoutManager);
//        recylerView.setAdapter(searchAdapter);
//
//        LinkControl();
//
//        return view;
//    }

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

                        String aboutt,profilee;
                        aboutt = (String) data.get("about");
                        profilee = (String) data.get("profile");
                            Person p = new Person(usernameT,aboutt,profilee);
                            personArrayList.add(p);

                        }
                    if(!personArrayList.isEmpty()){
                        searchAdapter.notifyDataSetChanged();
                        // bildirimlerArrayList.clear();

                    }

                    }
                }
        });
    }
}
