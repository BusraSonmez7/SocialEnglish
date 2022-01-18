package com.busrasonmez.socialenglish.education.Work.Sentence;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class WorkSentenceActivity extends AppCompatActivity {

    public WorkSentenceAdapter sentenceAdapter;
    private RecyclerView recylerView;
    ArrayList<WorkSentenceModel> workSentenceModelArrayList;

    Intent intent;
    String subject2;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence);

        recylerView = findViewById(R.id.r_view);

        intent = getIntent();
        subject2 = intent.getStringExtra("subject");

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        workSentenceModelArrayList = new ArrayList<>();
        recylerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        sentenceAdapter = new WorkSentenceAdapter(getApplicationContext(), workSentenceModelArrayList);
        recylerView.setLayoutManager(layoutManager);
        recylerView.setAdapter(sentenceAdapter);
        getSentences();
    }

    private void getSentences(){

        firebaseFirestore.collection("Sentences").whereEqualTo("subject",subject2).whereEqualTo("email","admin").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error !=null){
                    Toast.makeText(getApplicationContext(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

                if(value != null){
                    workSentenceModelArrayList.clear();
                    for(DocumentSnapshot snapshot : value.getDocuments()){
                        //bildirimlerArrayList.clear();
                        HashMap<String,Object> data = (HashMap<String, Object>) snapshot.getData();
                        String subject = (String)  data.get("subject");
                        String s_header = (String)  data.get("sentence_header");
                        String sentence = (String)  data.get("sentence");
                        String email = (String)  data.get("email");

//                                profilee = "Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE" +
//                                        "://" + "getResources().getResourcePackageName(R.drawable.profile)"
//                                        + '/' + "getResources().getResourceTypeName(R.drawable.profile)" + '/' + "getResources().getResourceEntryName(R.drawable.profile) )";


                        WorkSentenceModel p = new WorkSentenceModel(subject,s_header,email,sentence);
                        workSentenceModelArrayList.add(p);

                    }
                    if(!workSentenceModelArrayList.isEmpty()){
                        sentenceAdapter.notifyDataSetChanged();
                        // bildirimlerArrayList.clear();
                    }

                }
            }
        });
    }

}