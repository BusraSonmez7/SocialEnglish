package com.busrasonmez.socialenglish.education.Work.Word;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.busrasonmez.socialenglish.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkWordActivity extends AppCompatActivity {

    public WorkWordAdapter wordAdapter;
    private RecyclerView recylerView;
    ArrayList<WorkWordModel> wordModelArrayList;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    Intent intent;
    String subject2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        recylerView = findViewById(R.id.r_view);

        intent = getIntent();
        subject2 = intent.getStringExtra("subject");

        Toast.makeText(getApplicationContext(),"subject: "+subject2,Toast.LENGTH_LONG).show();

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        wordModelArrayList = new ArrayList<>();
        recylerView.setHasFixedSize(true);

        recylerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3,RecyclerView.VERTICAL,false));
        wordAdapter = new WorkWordAdapter(getApplicationContext(), wordModelArrayList);
        recylerView.setAdapter(wordAdapter);

        getWords();
    }

    private void getWords(){

        firebaseFirestore.collection("Words").whereEqualTo("subject",subject2).whereEqualTo("email","admin").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error !=null){
                    Toast.makeText(getApplicationContext(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

                if(value != null){
                    //subjectModelArrayList.clear();
                    for(DocumentSnapshot snapshot : value.getDocuments()){
                        //bildirimlerArrayList.clear();
                        HashMap<String,Object> data = (HashMap<String, Object>) snapshot.getData();
                        String subject = (String)  data.get("subject");
                        String image = (String)  data.get("image");
                        String word = (String)  data.get("word");
                        String email = (String)  data.get("email");

//                                profilee = "Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE" +
//                                        "://" + "getResources().getResourcePackageName(R.drawable.profile)"
//                                        + '/' + "getResources().getResourceTypeName(R.drawable.profile)" + '/' + "getResources().getResourceEntryName(R.drawable.profile) )";


                        WorkWordModel p = new WorkWordModel(subject,word,email,image);
                        wordModelArrayList.add(p);

                    }
                    if(!wordModelArrayList.isEmpty()){
                        wordAdapter.notifyDataSetChanged();
                        // bildirimlerArrayList.clear();
                    }

                }
            }
        });

    }
}