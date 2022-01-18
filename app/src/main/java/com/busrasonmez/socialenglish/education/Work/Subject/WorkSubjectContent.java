package com.busrasonmez.socialenglish.education.Work.Subject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.education.Work.Sentence.WorkSentenceActivity;
import com.busrasonmez.socialenglish.education.Work.Word.WorkWordActivity;
import com.busrasonmez.socialenglish.education.Work.Quiz.QuizActivity;

public class WorkSubjectContent extends AppCompatActivity {

    Intent intent;
    String subject2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_content);

        intent = getIntent();
        subject2 = intent.getStringExtra("subject");

        Toast.makeText(getApplicationContext(),"subject: "+subject2,Toast.LENGTH_LONG).show();

    }

    public void Word(View view){
        Intent intent = new Intent(getApplicationContext(), WorkWordActivity.class);
        intent.putExtra("subject",subject2);
        startActivity(intent);
    }

    public void Sentence(View view){
        Intent intent = new Intent(getApplicationContext(), WorkSentenceActivity.class);
        intent.putExtra("subject",subject2);
        startActivity(intent);
    }

    public void StartQuiz(View view){

        Intent intent = new Intent(getApplicationContext(),QuizActivity.class);
        startActivity(intent);


    }
}