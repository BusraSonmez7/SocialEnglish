package com.busrasonmez.socialenglish.education.Notebook.Word;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.education.Work.Subject.WorkSubjectModel;
import com.busrasonmez.socialenglish.education.Work.Word.WorkWordAdapter;
import com.busrasonmez.socialenglish.education.Work.Word.WorkWordModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class NotebookWordActivity extends AppCompatActivity {

    ProgressDialog dialog;
    Dialog dialog2;

    Intent intent;
    String subject;

    Uri imageData;

    ImageView imageadd;
    EditText wordadd, edtenglish, edtturkish;

    FloatingActionButton addButton;

    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;

    public NotebookWordAdapter wordAdapter;
    private RecyclerView recylerView;
    ArrayList<NotebookWordModel> wordModelArrayList;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook_word);
        addButton = findViewById(R.id.addbutton);


        recylerView = findViewById(R.id.r_view);

        intent = getIntent();
        subject = intent.getStringExtra("subject");

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        wordModelArrayList = new ArrayList<>();
        recylerView.setHasFixedSize(true);

        recylerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3, RecyclerView.VERTICAL,false));
        wordAdapter = new NotebookWordAdapter(getApplicationContext(), wordModelArrayList);
        recylerView.setAdapter(wordAdapter);

        RegisterLauncher();

        getWords();


        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog2 = new Dialog(NotebookWordActivity.this);
                dialog2.setContentView(R.layout.word_add);
                imageadd = dialog2.findViewById(R.id.imageadd);
                wordadd = dialog2.findViewById(R.id.edtword);
                edtenglish = dialog2.findViewById(R.id.edtenglishmean);
                edtturkish = dialog2.findViewById(R.id.edtturkishmean);

                TextView btnadd = dialog2.findViewById(R.id.btnadd);
                //


                imageadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageAdd(v);
                    }
                });

                btnadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Save(v);
                        imageData = null;

                    }
                });

                dialog2.show();
            }

        });

    }

    public void Save(View v){

        int isvoid = 0;

        if(wordadd.length()==0)
        {
            isvoid++;
            wordadd.requestFocus();
            wordadd.setError("Enter the word!");
        }
        if(edtenglish.length()==0 || edtturkish.length()==0)
        {
            isvoid++;
            edtenglish.requestFocus();
            edtenglish.setError("Please enter English meaning or Turkish meaning!");
        }


        if(isvoid<1){
            dialog = new ProgressDialog(NotebookWordActivity.this);
            dialog.setMessage("Being recorded...");
            dialog.show();

            boolean yok1;
            String word = wordadd.getText().toString();
            String english = wordadd.getText().toString();
            String turkish = wordadd.getText().toString();
            if(this.imageData !=null){

                //VerileriEkle(this.imageData);
                SubjectControl(imageData,word, english,turkish,0);
                //dialog.dismiss();

            }
            else{
                this.imageData = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + getResources().getResourcePackageName(R.drawable.imageadd)
                        + '/' + getResources().getResourceTypeName(R.drawable.imageadd) + '/' + getResources().getResourceEntryName(R.drawable.imageadd) );
                //VerileriEkle(imageData);
                SubjectControl(imageData,word,english,turkish,1);
                dialog.dismiss();
            }
        }
    }

    public void ImageAdd(View view){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(NotebookWordActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //izin istenecek
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                    }
                }).show();
            }else{
                //izin istenecek
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        else{
            //izin verildiyse
            Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentGallery);

        }
    }

    private void RegisterLauncher(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if(intentFromResult != null){
                        imageData = intentFromResult.getData();
                        imageadd.setImageURI(imageData);
                    }
                    else{
                        imageData = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                                "://"+getResources().getResourcePackageName(R.drawable.imageadd)+
                                '/'+getResources().getResourceTypeName(R.drawable.imageadd)+'/'+
                                getResources().getResourceEntryName(R.drawable.imageadd));
                    }
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentGallery);
                }else{
                    Toast.makeText(getApplicationContext(),"Permission needed!",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void SubjectControl(Uri imageData, String subject, String english, String turkish, int resim){
        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();
        firebaseFirestore.collection("Words").whereEqualTo("word",subject).whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String documentt = null;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList<WorkSubjectModel> deneme = new ArrayList<>();
                        DocumentReference docRef = firebaseFirestore.collection("Words").document(document.getId());
                        documentt = docRef.toString();

                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),"You have already added this topic!",Toast.LENGTH_LONG).show();
                        return;


                    }

                    if(documentt == null){
//
                        if(resim == 0){
                            DataAdd(imageData);
                        }
                        else {
                            DataAdd("default");
                        }
                        dialog2.dismiss();
                        dialog.cancel();
                    }
                    else{
                        dialog.cancel();
                    }

                }
            }
        });
    }

    public void DataAdd(String imageData){
        String downloadUrl = "default";

        String word = wordadd.getText().toString();
        String english = edtenglish.getText().toString();
        String turkish = edtturkish.getText().toString();

        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();

        HashMap<String,Object> subjectData = new HashMap<>();
        subjectData.put("word",word);
        subjectData.put("english_mean",english);
        subjectData.put("turkish_mean",turkish);
        subjectData.put("email",email);
        subjectData.put("group","");
        subjectData.put("image",downloadUrl);
        subjectData.put("level","");
        subjectData.put("subject","");


        firebaseFirestore.collection("Words").add(subjectData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(),"Word saved",Toast.LENGTH_LONG).show();
                getWords();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(),"The word could not be registered",Toast.LENGTH_LONG).show();

            }
        });
        this.imageData=null;
    }

    public void DataAdd(Uri imageData){
        UUID uuid = UUID.randomUUID();
        String imageName = "images/"+uuid+".jpg";


        storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference newRefrence = firebaseStorage.getReference(imageName);
                newRefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();

                        String word = wordadd.getText().toString();
                        String english = edtenglish.getText().toString();
                        String turkish = edtturkish.getText().toString();

                        FirebaseUser user = auth.getCurrentUser();
                        String email = user.getEmail();

                        HashMap<String,Object> subjectData = new HashMap<>();
                        subjectData.put("word",word);
                        subjectData.put("english_mean",english);
                        subjectData.put("turkish_mean",turkish);
                        subjectData.put("email",email);
                        subjectData.put("group","");
                        subjectData.put("image",downloadUrl);
                        subjectData.put("level","");
                        subjectData.put("subject","");

                        firebaseFirestore.collection("Words").add(subjectData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(),"Word saved",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(getApplicationContext(),"The word could not be registered!",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });

        this.imageData=null;
    }

    private void getWords(){

        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();

        firebaseFirestore.collection("Words").whereEqualTo("email",email).whereEqualTo("subject",subject).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error !=null){
                    Toast.makeText(getApplicationContext(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
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

//                                profilee = "Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE" +
//                                        "://" + "getResources().getResourcePackageName(R.drawable.profile)"
//                                        + '/' + "getResources().getResourceTypeName(R.drawable.profile)" + '/' + "getResources().getResourceEntryName(R.drawable.profile) )";


                        NotebookWordModel p = new NotebookWordModel(subject,word,email,image);
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