package com.busrasonmez.socialenglish.social_media.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.busrasonmez.socialenglish.user_login;
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

public class SearchFragment extends Fragment {

    Toolbar toolbar;
    ImageView searchbtn;
    EditText edtSearch;

    public SearchAdapter searchAdapter;
    private RecyclerView recylerView;
    ArrayList<Person> personArrayList;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sm_fragment_search,container,false);
        toolbar = view.findViewById(R.id.toolbar);
        searchbtn = view.findViewById(R.id.searchbtn);
        recylerView = view.findViewById(R.id.r_view);
        edtSearch = view.findViewById(R.id.edtsearch);


        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
//
        setHasOptionsMenu(true);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        personArrayList = new ArrayList<>();
        recylerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        searchAdapter = new SearchAdapter(getActivity(),personArrayList);
        recylerView.setLayoutManager(layoutManager);
        recylerView.setAdapter(searchAdapter);

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPerson();
            }
        });


        return view;
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
//                auth.signOut();
//
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

    private void getPerson(){

        firebaseFirestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error !=null){
                    Toast.makeText(getActivity(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

                if(value != null){
                    String txtsearch = edtSearch.getText().toString();
                    FirebaseUser user = auth.getCurrentUser();
                    String email = user.getEmail();
                    personArrayList.clear();
                    for(DocumentSnapshot snapshot : value.getDocuments()){
                        //bildirimlerArrayList.clear();
                        HashMap<String,Object> data = (HashMap<String, Object>) snapshot.getData();
                        if(data.get("username").toString().contains(txtsearch)){
                            String username = (String)  data.get("username");
                            String aboutt = (String)  data.get("about");
                            String profilee = (String)  data.get("profile");
//                                profilee = "Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE" +
//                                        "://" + "getResources().getResourcePackageName(R.drawable.profile)"
//                                        + '/' + "getResources().getResourceTypeName(R.drawable.profile)" + '/' + "getResources().getResourceEntryName(R.drawable.profile) )";


                            Person p = new Person(username,aboutt,profilee);
                            personArrayList.add(p);
                        }

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
