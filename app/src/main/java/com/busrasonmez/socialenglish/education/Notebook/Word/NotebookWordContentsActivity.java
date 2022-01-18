package com.busrasonmez.socialenglish.education.Notebook.Word;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.busrasonmez.socialenglish.R;
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

import java.util.ArrayList;
import java.util.HashMap;

public class NotebookWordContentsActivity extends AppCompatActivity {

    String word, subject, isemail;
    Intent intent;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    TextView txtword,txtenglish,txtturkish;

    ArrayList<NotebookWordModel> wordModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_contents);

        txtword = findViewById(R.id.txtword);
        txtenglish = findViewById(R.id.txtenglishmean);
        txtturkish = findViewById(R.id.txtturkishmean);

        wordModelArrayList = new ArrayList<>();

        intent = getIntent();
        word = intent.getStringExtra("word");
        subject = intent.getStringExtra("subject");
        isemail = intent.getStringExtra("isemail");


        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

//        txtword.setText(word);
//        txtenglish.setText(subject);

        getWords(isemail);

    }

    private void getWords(String isemail){

        if(isemail.equals("work")){
            firebaseFirestore.collection("Words").whereEqualTo("email","admin").whereEqualTo("subject",subject).whereEqualTo("word",word).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error !=null){
                        //Toast.makeText(getApplicationContext(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                    }

                    if(value != null){
                        wordModelArrayList.clear();
                        for(DocumentSnapshot snapshot : value.getDocuments()){
                            //bildirimlerArrayList.clear();
                            HashMap<String,Object> data = (HashMap<String, Object>) snapshot.getData();
                            String subject = (String)  data.get("subject");
                            String image = (String)  data.get("image");
                            String word = (String)  data.get("word");
                            String email = (String)  data.get("email");
                            String english = (String)  data.get("english_mean");
                            String turkish = (String)  data.get("turkish_mean");
                            String level = (String)  data.get("level");
                            String group = (String)  data.get("group");



//                                profilee = "Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE" +
//                                        "://" + "getResources().getResourcePackageName(R.drawable.profile)"
//                                        + '/' + "getResources().getResourceTypeName(R.drawable.profile)" + '/' + "getResources().getResourceEntryName(R.drawable.profile) )";


                            NotebookWordModel p = new NotebookWordModel(subject,word,email,english,turkish,level,group,image);
                            wordModelArrayList.add(p);

                        }
                        if(!wordModelArrayList.isEmpty()){
                            txtword.setText(wordModelArrayList.get(0).getWord());
                            txtenglish.setText(wordModelArrayList.get(0).getEnglish_meaning());
                            txtturkish.setText(wordModelArrayList.get(0).getTurkish_meaning());

                        }

                    }
                }
            });

        }

        else if(isemail.equals("notebook")){
            FirebaseUser user = auth.getCurrentUser();
            String email = user.getEmail();
            firebaseFirestore.collection("Words").whereEqualTo("email",email).whereEqualTo("subject",subject).whereEqualTo("word",word).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error !=null){
                        //Toast.makeText(getApplicationContext(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                    }

                    if(value != null){
                        wordModelArrayList.clear();
                        for(DocumentSnapshot snapshot : value.getDocuments()){
                            //bildirimlerArrayList.clear();
                            HashMap<String,Object> data = (HashMap<String, Object>) snapshot.getData();
                            String subject = (String)  data.get("subject");
                            String image = (String)  data.get("image");
                            String word = (String)  data.get("word");
                            String email = (String)  data.get("email");
                            String english = (String)  data.get("english_mean");
                            String turkish = (String)  data.get("turkish_mean");
                            String level = (String)  data.get("level");
                            String group = (String)  data.get("group");



//                                profilee = "Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE" +
//                                        "://" + "getResources().getResourcePackageName(R.drawable.profile)"
//                                        + '/' + "getResources().getResourceTypeName(R.drawable.profile)" + '/' + "getResources().getResourceEntryName(R.drawable.profile) )";


                            NotebookWordModel p = new NotebookWordModel(subject,word,email,english,turkish,level,group,image);
                            wordModelArrayList.add(p);

                        }
                        if(!wordModelArrayList.isEmpty()){
                            txtword.setText(wordModelArrayList.get(0).getWord());
                            txtenglish.setText(wordModelArrayList.get(0).getEnglish_meaning());
                            txtturkish.setText(wordModelArrayList.get(0).getTurkish_meaning());

                        }

                    }
                }
            });

        }




    }
}