package com.busrasonmez.socialenglish.social_media.Messages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.register;
import com.busrasonmez.socialenglish.social_media.ProfileSetting.Persons.Person;
import com.busrasonmez.socialenglish.social_media.Search.SearchAdapter;
import com.busrasonmez.socialenglish.user_login;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagePage extends AppCompatActivity {

    CircleImageView profile_buyer;
    TextView txt_username;
    ImageButton btn_sender, btn_camera;
    EditText edtmessage;

    private Intent intent;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<Person> personArrayList;
    private FirebaseUser firebaseUser;

    private MessageAdapter messageAdapter;
    private RecyclerView recylerView;
    private ArrayList<MessageModel> messageModelArrayList;

    String buyer_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_page);

        profile_buyer = findViewById(R.id.profile_buyer);
        txt_username = findViewById(R.id.txt_username);
        btn_camera = findViewById(R.id.btn_camera);
        btn_sender = findViewById(R.id.btn_sender);
        edtmessage = findViewById(R.id.edtmessage);
        recylerView = findViewById(R.id.recycler_viewmessage);

        intent = getIntent();
        buyer_email = intent.getStringExtra("email_share");

        recylerView.setHasFixedSize(true);
        recylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        messageModelArrayList = new ArrayList<>();
        messageAdapter = new MessageAdapter(getApplicationContext(),messageModelArrayList);
        recylerView.setAdapter(messageAdapter);


        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        personArrayList = new ArrayList<>();
        firebaseUser = auth.getCurrentUser();

        UserInformation();

    }

    private void UserInformation(){

        firebaseFirestore.collection("Users").whereEqualTo("email", buyer_email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                }

                if (value != null) {

                    int i = 0;
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();
                        String profile = (String) data.get("profile");
                        String buyer_username = (String) data.get("username");

                        Person person = new Person(buyer_username,profile,2);
                        personArrayList.add(person);

                    }
                    if (!personArrayList.isEmpty()) {
                        String imageURL = personArrayList.get(0).getImage();
                        Glide.with(getApplicationContext()).load(imageURL).placeholder(R.drawable.profile_n).into(profile_buyer);
                        txt_username.setText(personArrayList.get(0).getUsername());

                    } else {
                        Toast.makeText(getApplicationContext(), "A general error has occurred!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        FirebaseUser user = auth.getCurrentUser();
        String sender_email = user.getEmail();

        MessageRead(sender_email,buyer_email);
    }

    public void Sender(View view){
        String message = edtmessage.getText().toString();
        FirebaseUser user = auth.getCurrentUser();
        String sender_email = user.getEmail();

        if(!message.equals("")){
            MessageSender(sender_email, buyer_email, message);
            //MessageBuyer(buyer_email, buyer_email, message);

        }
        else {
            Toast.makeText(this,"Message is blank..",Toast.LENGTH_LONG).show();
        }

        edtmessage.setText("");
    }

    private void MessageRead(String sender_email, String buyer_username){


        firebaseFirestore.collection("Messages").orderBy("date", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                }

                if (value != null) {


                    int i = 0;
                    messageModelArrayList.clear();

                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();
                        String sender_emailx = (String) data.get("sender_email");
                        String buyer_emailx = (String) data.get("buyer_email");
                        String messagex = (String) data.get("message");
                        String message_image = (String) data.get("message_image");
                        Timestamp time = (Timestamp) data.get("date");


                        if(buyer_emailx.equals(sender_email) && sender_emailx.equals(buyer_email) ||
                        buyer_emailx.equals(buyer_email) && sender_emailx.equals(sender_email)){

                            if(time != null){
                                Date date = time.toDate();
                                String date2 = date.toString();
                                String pattern = "kk:mm";
                                SimpleDateFormat format = new SimpleDateFormat(pattern);

                                MessageModel p = new MessageModel(sender_emailx,buyer_emailx,messagex,message_image,format.format(date),true);
                                messageModelArrayList.add(p);
                            }
                            else{

                            }

                        }


                    }

                    if(!messageModelArrayList.isEmpty()){
                        messageAdapter.notifyDataSetChanged();
                        // bildirimlerArrayList.clear();

                    }

                }
            }
        });
    }

    private void MessageSender(String sender_email, String buyer_email, String message) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender_email", sender_email);
        hashMap.put("buyer_email", buyer_email);
        hashMap.put("message", message);
        hashMap.put("message_image", "");
        hashMap.put("date",FieldValue.serverTimestamp());
        Date now = new Date();


        firebaseFirestore.collection("Messages").add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(MessagePage.this, "Message sent..", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });
    }

    private void MessageBuyer(String sender_email, String buyer_email, String message) {

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender_email", sender_email);
        hashMap.put("buyer_email", buyer_email);
        hashMap.put("message", message);
        hashMap.put("message_image", "");

        Date now = new Date();

        firebaseFirestore.document("Messages/"+buyer_email).collection(now.getTime()+"").document().set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MessagePage.this, "Message sent..", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });
    }


}
