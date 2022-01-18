package com.busrasonmez.socialenglish.education.Notebook.Sentence;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.education.Work.Sentence.WorkSentenceAdapter;
import com.busrasonmez.socialenglish.education.Work.Sentence.WorkSentenceModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class NotebookSentenceActivity extends AppCompatActivity {

    ProgressDialog dialog;
    Dialog dialog2;

    Uri imageData;

    EditText edttitle, edtcontent;

    FloatingActionButton addButton;

    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;

    public NotebookSentenceAdapter sentenceAdapter;
    private RecyclerView recylerView;
    ArrayList<NotebookSentenceModel> sentenceModelArrayList;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    Intent intent;
    String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook_sentence);

        addButton = findViewById(R.id.addbutton);

        intent = getIntent();
        subject = intent.getStringExtra("subject");

        recylerView = findViewById(R.id.r_view);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        sentenceModelArrayList = new ArrayList<>();
        recylerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        sentenceAdapter = new NotebookSentenceAdapter(getApplicationContext(),sentenceModelArrayList);
        recylerView.setLayoutManager(layoutManager);
        recylerView.setAdapter(sentenceAdapter);


        getSentences();

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog2 = new Dialog(NotebookSentenceActivity.this);
                dialog2.setContentView(R.layout.sentence_add);
                edttitle = dialog2.findViewById(R.id.edttitle);
                edtcontent = dialog2.findViewById(R.id.edtcontent);

                TextView btnadd = dialog2.findViewById(R.id.btnadd);
                //

                btnadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Save(v);

                    }
                });

                dialog2.show();
            }

        });
    }

    public void Save(View v){

        int isvoid = 0;

        if(edttitle.length()==0)
        {
            isvoid++;
            edttitle.requestFocus();
            edttitle.setError("Enter the title!");
        }
        if(edtcontent.length()==0)
        {
            isvoid++;
            edtcontent.requestFocus();
            edtcontent.setError("Please enter content!");
        }


        if(isvoid<1){
            boolean yok1;

            DataAdd();

            dialog2.dismiss();

            getSentences();


        }
    }

    public void DataAdd(){

        String title = edttitle.getText().toString();
        String content = edtcontent.getText().toString();

        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();

        HashMap<String,Object> subjectData = new HashMap<>();
        subjectData.put("sentence",content);
        subjectData.put("sentence_header",title);
        subjectData.put("email",email);
        subjectData.put("subject","");


        firebaseFirestore.collection("Sentences").add(subjectData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(),"Sentence saved",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(),"The sentence could not be registered",Toast.LENGTH_LONG).show();

            }
        });


    }

    private void getSentences(){

        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();

        firebaseFirestore.collection("Sentences").whereEqualTo("email",email).whereEqualTo("subject",subject).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error !=null){
                    //Toast.makeText(getApplicationContext(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

                if(value != null){
                    sentenceModelArrayList.clear();
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


                        NotebookSentenceModel p = new NotebookSentenceModel(subject,s_header,email,sentence);
                        sentenceModelArrayList.add(p);

                    }
                    if(!sentenceModelArrayList.isEmpty()){
                        sentenceAdapter.notifyDataSetChanged();
                        // bildirimlerArrayList.clear();
                    }

                }
            }
        });
    }
}