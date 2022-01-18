package com.busrasonmez.socialenglish.education.Notebook.Subject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.education.Notebook.Sentence.NotebookSentenceActivity;
import com.busrasonmez.socialenglish.education.Notebook.Word.NotebookWordActivity;

public class NotebookWord2Activity extends AppCompatActivity {

    Intent intent;
    String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word2);

        intent = getIntent();
        subject = intent.getStringExtra("subject");
    }

    public void Word(View v){

        Intent intent = new Intent(getApplicationContext(), NotebookWordActivity.class);
        intent.putExtra("subject",subject);
        startActivity(intent);
    }

    public void Sentence(View v){

        Intent intent = new Intent(getApplicationContext(), NotebookSentenceActivity.class);
        intent.putExtra("subject",subject);
        startActivity(intent);

    }
}