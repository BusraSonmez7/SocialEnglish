package com.busrasonmez.socialenglish.education.Notebook.Sentence;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.education.Work.Sentence.WorkSentenceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class NotebookSentenceContentsActivity extends AppCompatActivity {

    String sentence, sentence_header, subject;
    Intent intent;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    TextView txtsentence, txttitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence_contents);

        txtsentence = findViewById(R.id.txtsentence);
        txttitle = findViewById(R.id.txttitle);

        intent = getIntent();
        subject = intent.getStringExtra("subject");
        sentence = intent.getStringExtra("sentence");
        sentence_header = intent.getStringExtra("sentence_header");

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        txttitle.setText(sentence_header);
        txtsentence.setText(sentence);

    }
}