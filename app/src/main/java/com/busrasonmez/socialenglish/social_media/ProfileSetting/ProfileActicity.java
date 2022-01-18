package com.busrasonmez.socialenglish.social_media.ProfileSetting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.social_media.Messages.MessagePage;
import com.busrasonmez.socialenglish.social_media.ProfileSetting.Persons.Person;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActicity extends AppCompatActivity {

    String username;
    TextView usernamee, nameandsurname, about;
    ImageView profileee;
    TextView btnfollow;
    CardView cardView;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<Person> personArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acticity);

        usernamee = findViewById(R.id.username);
        nameandsurname = findViewById(R.id.nameandsurname);
        about = findViewById(R.id.about);
        profileee = findViewById(R.id.profileee);
        cardView = findViewById(R.id.cardViewUye2);
        btnfollow = findViewById(R.id.btnfollow);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        personArrayList = new ArrayList<>();

        getData();
        LinkControl(username);

    }

    public void Follow(View view) {
        // verileri filtreleme
        FirebaseUser user = auth.getCurrentUser();
        String email1 = user.getEmail();

        firebaseFirestore.collection("Users").whereEqualTo("email", email1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                }
                if (value != null) {
                    HashMap<String, Object> personList = new HashMap<>();
                    int i = 0;
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Toast.makeText(getApplicationContext(), username, Toast.LENGTH_LONG).show();

                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();
                        personList.put("usernameA", data.get("username"));
                        personList.put("usernameB", username);
                        personList.put("date", FieldValue.serverTimestamp());
                    }
                    if (!personList.equals("")) {

                        firebaseFirestore.collection("Persons").add(personList).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(), "Connection successful", Toast.LENGTH_LONG).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(getApplicationContext(), personList.get("usernameA") + "Connection failed!", Toast.LENGTH_LONG).show();

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Boşşşş", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        // verileri kaydetme
    }

    public void WriteMessage(View view) {
        String username = usernamee.getText().toString();


        firebaseFirestore.collection("Users").whereEqualTo("username", username).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                }

                if (value != null) {

                    int i = 0;
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();
                        String email = (String) data.get("email");

                        Intent intent = new Intent(getApplicationContext(), MessagePage.class);
                        intent.putExtra("email_share",email);
                        startActivity(intent);

                    }

                }
            }
        });



    }

    public void ListPost(View view) {

    }

    public void getData() {
        firebaseFirestore.collection("Users").whereEqualTo("username", username).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                }

                if (value != null) {

                    int i = 0;
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();
                        String name = (String) data.get("name");
                        String surname = (String) data.get("surname");
                        String username = (String) data.get("username");
                        String email = (String) data.get("email");
                        String password = (String) data.get("password");
                        String about = (String) data.get("about");
                        String profile = (String) data.get("profile");


                        Person person = new Person(name, surname, username, email, password, about, profile);
                        personArrayList.add(person);

                    }
                    if (!personArrayList.isEmpty()) {
                        String imageURL = personArrayList.get(0).getImage();
                        Glide.with(getApplicationContext()).load(imageURL).placeholder(R.drawable.profile_n).into(profileee);
                        nameandsurname.setText(personArrayList.get(0).getName() + " " + personArrayList.get(0).getSurname());
                        about.setText(personArrayList.get(0).getAbout());
                        usernamee.setText(personArrayList.get(0).getUsername());

                    } else {
                        Toast.makeText(getApplicationContext(), "A general error has occurred!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    public void LinkControl(String username) {

        FirebaseUser user = auth.getCurrentUser();
        String email1 = user.getEmail();

        firebaseFirestore.collection("Users").whereEqualTo("email",email1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                }

                if (value != null) {

                    String usernameAB;

                    int i = 0;
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();
                        usernameAB = (String) data.get("username");

                        String finalUsernameAB = usernameAB;
                        firebaseFirestore.collection("Persons").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                }

                                if (value != null) {

                                    int i = 0;
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        HashMap<String, Object> data = (HashMap<String, Object>) snapshot.getData();

                                        if ((data.get("usernameA").equals(finalUsernameAB) && data.get("usernameB").equals(username)) ||
                                                (data.get("usernameA").equals(username) && data.get("usernameB").equals(finalUsernameAB))) {

                                            btnfollow.setText("You are following");
                                            cardView.setBackgroundResource(R.color.darkgray);
                                        } else {

                                        }

                                    }
                                }
                            }
                        });
//                        if ((data.get("usernameA").equals(email1) && data.get("usernameB").equals(username)) || (data.get("usernameA").equals(username) && data.get("usernameB").equals(email1))) {
//                            Toast.makeText(getApplicationContext(), "Takip ediyorsuuuuun", Toast.LENGTH_LONG).show();
//
//                            btnfollow.setText("You are following");
//                            cardView.setBackgroundResource(R.color.background);
//                        } else {
//                            Toast.makeText(getApplicationContext(), ":(", Toast.LENGTH_LONG).show();
//
//                        }

                    }
                }
            }
        });
    }
}