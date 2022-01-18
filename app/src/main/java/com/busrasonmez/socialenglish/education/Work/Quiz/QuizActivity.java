package com.busrasonmez.socialenglish.education.Work.Quiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.education.Work.Word.WorkWordModel;
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
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class QuizActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextToSpeech ts;
    GridLayout rlt2,rlt5;
    LinearLayout rlt3;
    RadioGroup rg;

    Context context = this;

    ImageView image,imageaudio,audioS;

    RadioButton ra,rb,rc,rd;

    Button btnnext;

    TextView txtsentence, txtwords;
    static  int count = 1;

    int k = 1;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    ArrayList<QuizModel> quizArrayList;
    public ArrayList<QuizModel> quizModelArrayList;

    public static final int separatorNum = 3; // tek satırda 3 tane textview hizalar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        txtsentence = findViewById(R.id.txtsentence);
        txtwords = findViewById(R.id.txtsentenceanswer);
        btnnext = findViewById(R.id.btnnext);
        rg = findViewById(R.id.rgroup);
        ra = findViewById(R.id.rA);
        rb = findViewById(R.id.rB);
        rc = findViewById(R.id.rC);
        rd = findViewById(R.id.rD);
        image = findViewById(R.id.image);
        audioS = findViewById(R.id.audioS);
        imageaudio = findViewById(R.id.imageaudio);
        rlt2 = (GridLayout) findViewById(R.id.r2);
        rlt3 = (LinearLayout) findViewById(R.id.r3);

        ts = new TextToSpeech(context,this);


        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        quizModelArrayList = new ArrayList<>();
        quizArrayList = new ArrayList<>();

        GetQuestion(1,"E");
//        GetQuestion2(1);

        imageaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                convertTextToSpeech();

            }
        });

        int[] questions1 ;
        questions1 = new int[11];

        for(int i = 0 ; i<11;i++){
            Random random = new Random();
            questions1[i] = random.nextInt(17)+1;
        }

        for(int j = 1;j<questions1.length;j++){
            int k = 0;
            while (k!=j){
                if(questions1[j] == questions1[k] && j>k){
                    questions1[j] = questions1[j]+1;

                    if(questions1[j] == questions1.length){
                        questions1[j] = 0;
                    }

                    k=0;

                }

                else {
                    k++;
                }

            }
        }

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k++;
                if(k==3 || k ==6 || k == 8 || k==11 ){
                    Toast.makeText(getApplicationContext(),"question[]: "+questions1[k],Toast.LENGTH_SHORT).show();
                    GetQuestion(questions1[k],"E");

                }
                else if(k == 2 || k==5 || k ==9 ){
                    GetQuestion(questions1[k],"T");

                }
                else {
                    GetQuestion(questions1[k],"S");

                }

            }
        });





        //Toast.makeText(getApplicationContext(),t_sentence+" merhaba",Toast.LENGTH_SHORT).show();





    }

    int wordsum =0;


    public void GetQuestion(int index, String questiontype){

        firebaseFirestore.collection("Questions1").whereEqualTo("index",index).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error !=null){
                    Toast.makeText(getApplicationContext(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

                if(value != null){

                    String e = "x";
                    //subjectModelArrayList.clear();
                    for(DocumentSnapshot snapshot : value.getDocuments()){


                        //bildirimlerArrayList.clear();
                        HashMap<String,Object> data = (HashMap<String, Object>) snapshot.getData();
                        String e_Sentence = (String)  data.get("english_sentence");
                        String t_meaning = (String)  data.get("turkish_meaning");
                        String wrong_words = (String)  data.get("wrong_words");

//                                profilee = "Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE" +
//                                        "://" + "getResources().getResourcePackageName(R.drawable.profile)"
//                                        + '/' + "getResources().getResourceTypeName(R.drawable.profile)" + '/' + "getResources().getResourceEntryName(R.drawable.profile) )";

                        QuizModel p = new QuizModel(e_Sentence,t_meaning,wrong_words);
                        quizArrayList.add(p);

                        if(questiontype.equals("E")){

                            audioS.setVisibility(View.VISIBLE);
                            txtsentence.setVisibility(View.VISIBLE);
                            image.setVisibility(View.INVISIBLE);
                            imageaudio.setVisibility(View.INVISIBLE);
                            rg.setVisibility(View.INVISIBLE);
                            txtwords.setVisibility(View.VISIBLE);
                            rlt2.setVisibility(View.VISIBLE);
                            rg.removeAllViews();
                            txtwords.setText("");
                            txtsentence.setText("");
                            rlt2.removeAllViews();


                            e = e_Sentence;

                            rlt2.removeAllViews();

                            String sentences=t_meaning+" "+wrong_words;

                            String[] words = sentences.split(" ");

                            wordsum = words.length;


                            int[] numbers ;
                            numbers = new int[words.length];

                            boolean a = true;

                            for(int i = 0 ; i<words.length;i++){
                                Random random = new Random();
                                numbers[i] = random.nextInt(words.length);
                            }

                            for(int j = 1;j<numbers.length;j++){
                                int k = 0;
                                while (k!=j){
                                    if(numbers[j] == numbers[k] && j>k){
                                        numbers[j] = numbers[j]+1;

                                        if(numbers[j] == numbers.length){
                                            numbers[j] = 0;
                                        }

                                        k=0;

                                    }

                                    else {
                                        k++;
                                    }

                                }
                            }

                            for (int i = 0; i < numbers.length; i++) {
                                Button btn = new Button(getApplicationContext());
                                String message = words[numbers[i]];
                                btn.setText(message);
                                btn.setTextColor(Color.parseColor("#5E5B5B"));
                                btn.setPadding(5,5,5,5);
                                btn.setId(i+1);
                                btn.setBackground(getApplicationContext().getDrawable(R.drawable.button1_background3));
                                count++;
                                rlt2.addView(btn);

                                final int id=btn.getId();
                                btn = ((Button) findViewById(id));
                                String btnWord = btn.getText().toString();
                                btn.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {  // butonun click olayi
                                        TextView btn = ((Button) findViewById(id));
                                        String btnWord = btn.getText().toString();

                                        if(btnWord.contains(".")){
                                            view.setBackground(getApplicationContext().getDrawable(R.drawable.button1_background3));
                                            String k = btnWord.replace(".","");
                                            String t = txtwords.getText().toString().replace(k,"");
                                            t = t.replace("  "," ");
                                            txtwords.setText(t);
                                            btn.setText(k);
                                        }
                                        else {
                                            view.setBackground(getApplicationContext().getDrawable(R.drawable.button1_background4));
                                            txtwords.setText(txtwords.getText()+" "+btn.getText());
                                            btn.setText(btn.getText()+".");

                                        }

                                    }
                                });

                            }

                            txtsentence.setText(e+" ");

                        }

                        else if(questiontype.equals("T")){
                            e = t_meaning;

                            audioS.setVisibility(View.VISIBLE);
                            txtsentence.setVisibility(View.VISIBLE);
                            image.setVisibility(View.INVISIBLE);
                            imageaudio.setVisibility(View.INVISIBLE);
                            rg.setVisibility(View.INVISIBLE);
                            txtwords.setVisibility(View.VISIBLE);
                            rlt2.setVisibility(View.VISIBLE);
                            rg.removeAllViews();
                            txtwords.setText("");
                            txtsentence.setText("");
                            rlt2.removeAllViews();

                            txtsentence.setText(e+" ");

                            rlt2.removeAllViews();

                            String sentences=e_Sentence+" "+wrong_words;

                            String[] words = sentences.split(" ");

                            wordsum = words.length;


                            int[] numbers ;
                            numbers = new int[words.length];

                            boolean a = true;

                            for(int i = 0 ; i<words.length;i++){
                                Random random = new Random();
                                numbers[i] = random.nextInt(words.length);
                                txtsentence.setText(txtsentence.getText().toString());
                            }

                            for(int j = 1;j<numbers.length;j++){
                                int k = 0;
                                while (k!=j){
                                    if(numbers[j] == numbers[k] && j>k){
                                        numbers[j] = numbers[j]+1;

                                        if(numbers[j] == numbers.length){
                                            numbers[j] = 0;
                                        }

                                        k=0;

                                    }

                                    else {
                                        k++;
                                    }

                                }
                            }

                            for (int i = 0; i < numbers.length; i++) {
                                Button btn = new Button(getApplicationContext());
                                String message = words[numbers[i]];
                                btn.setText(message);
                                btn.setTextColor(Color.parseColor("#5E5B5B"));
                                btn.setPadding(5,5,5,5);
                                btn.setId(i+1);
                                btn.setBackground(getApplicationContext().getDrawable(R.drawable.button1_background3));
                                count++;
                                rlt2.addView(btn);

                                final int id=btn.getId();
                                btn = ((Button) findViewById(id));
                                String btnWord = btn.getText().toString();
                                btn.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {  // butonun click olayi
                                        TextView btn = ((Button) findViewById(id));
                                        String btnWord = btn.getText().toString();

                                        if(btnWord.contains(".")){
                                            view.setBackground(getApplicationContext().getDrawable(R.drawable.button1_background3));
                                            String k = btnWord.replace(".","");
                                            String t = txtwords.getText().toString().replace(k,"");
                                            t = t.replace("  "," ");
                                            txtwords.setText(t);
                                            btn.setText(k);
                                        }
                                        else {
                                            view.setBackground(getApplicationContext().getDrawable(R.drawable.button1_background4));
                                            txtwords.setText(txtwords.getText()+" "+btn.getText());
                                            btn.setText(btn.getText()+".");

                                        }

                                    }
                                });

                            }
                        }

                        else if(questiontype.equals("S")){
                            audioS.setVisibility(View.INVISIBLE);
                            txtsentence.setVisibility(View.INVISIBLE);
                            image.setVisibility(View.INVISIBLE);
                            imageaudio.setVisibility(View.VISIBLE);
                            rg.setVisibility(View.INVISIBLE);
                            txtwords.setVisibility(View.VISIBLE);
                            rlt2.setVisibility(View.VISIBLE);
                            rg.removeAllViews();
                            txtwords.setText("");
                            txtsentence.setText("");
                            rlt2.removeAllViews();

                            imageaudio.setImageResource(R.drawable.speaker);
                            imageaudio.getLayoutParams().height = 200;
                            imageaudio.getLayoutParams().width = 200;

                            e = e_Sentence;

                            rlt2.removeAllViews();

                            String sentences=t_meaning+" "+wrong_words;

                            String[] words = sentences.split(" ");

                            wordsum = words.length;


                            int[] numbers ;
                            numbers = new int[words.length];

                            boolean a = true;

                            for(int i = 0 ; i<words.length;i++){
                                Random random = new Random();
                                numbers[i] = random.nextInt(words.length);
                            }

                            for(int j = 1;j<numbers.length;j++){
                                int k = 0;
                                while (k!=j){
                                    if(numbers[j] == numbers[k] && j>k){
                                        numbers[j] = numbers[j]+1;

                                        if(numbers[j] == numbers.length){
                                            numbers[j] = 0;
                                        }

                                        k=0;

                                    }

                                    else {
                                        k++;
                                    }

                                }
                            }

                            for (int i = 0; i < numbers.length; i++) {
                                Button btn = new Button(getApplicationContext());
                                String message = words[numbers[i]];
                                btn.setText(message);
                                btn.setTextColor(Color.parseColor("#5E5B5B"));
                                btn.setPadding(5,5,5,5);
                                btn.setId(i+1);
                                btn.setBackground(getApplicationContext().getDrawable(R.drawable.button1_background3));
                                count++;
                                rlt2.addView(btn);

                                final int id=btn.getId();
                                btn = ((Button) findViewById(id));
                                String btnWord = btn.getText().toString();
                                btn.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {  // butonun click olayi
                                        TextView btn = ((Button) findViewById(id));
                                        String btnWord = btn.getText().toString();

                                        if(btnWord.contains(".")){
                                            view.setBackground(getApplicationContext().getDrawable(R.drawable.button1_background3));
                                            String k = btnWord.replace(".","");
                                            String t = txtwords.getText().toString().replace(k,"");
                                            t = t.replace("  "," ");
                                            txtwords.setText(t);
                                            btn.setText(k);
                                        }
                                        else {
                                            view.setBackground(getApplicationContext().getDrawable(R.drawable.button1_background4));
                                            txtwords.setText(txtwords.getText()+" "+btn.getText());
                                            btn.setText(btn.getText()+".");

                                        }

                                    }
                                });

                            }
                        }



                    }

                    //txtsentence.setText(e+" ");
                }
            }
        });

    }

    public void GetQuestion2(int index){

        firebaseFirestore.collection("Questions2").whereEqualTo("index",index).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        String A = (String)  data.get("A");
                        String B = (String)  data.get("B");
                        String C = (String)  data.get("C");
                        String D = (String)  data.get("D");
                        String image2 = (String)  data.get("Image");
                        String answer = (String)  data.get("answer");

                        if(image != null){
                            Glide.with(getApplicationContext()).load(image2).into(image);
                        }

                        ra.setText(A);
                        rb.setText(B);
                        rc.setText(C);
                        rd.setText(D);


                    }

                }
            }
        });

    }

    public void ButtonClick(){

        Button btn;

        for(int i = 1; i<wordsum+1;i++){

        }
    }


    @Override
    public void onInit(int status) {
        if(status == ts.SUCCESS){
            int result = ts.setLanguage(Locale.ENGLISH);
            if(result == ts.LANG_MISSING_DATA || result==ts.LANG_NOT_SUPPORTED){
                Toast.makeText(getApplicationContext(),"This language is not supported",Toast.LENGTH_SHORT).show();

            }
            else {
                //convertTextToSpeech();
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"unsuccessful",Toast.LENGTH_SHORT).show();
        }
    }

    private void convertTextToSpeech(){
        String text = "Hello, my name is Büşra";

        ts.speak(text,ts.QUEUE_FLUSH,null);
    }
}