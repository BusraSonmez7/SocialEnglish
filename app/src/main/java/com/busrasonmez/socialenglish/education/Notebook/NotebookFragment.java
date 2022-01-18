package com.busrasonmez.socialenglish.education.Notebook;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.education.Work.Subject.WorkSubjectModel;
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

import static android.app.Activity.RESULT_OK;

public class NotebookFragment extends Fragment {

    ProgressDialog dialog;
    Dialog dialog2;

    Uri imageData;

    ImageView imageadd;
    EditText subjectadd;

    FloatingActionButton addButton;

    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;

    private RecyclerView recylerView;
    public WorkSubjectModel subjectModel;
    public NotebookAdapter notebookAdapter;
    ArrayList<WorkSubjectModel> subjectModelArrayList;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;



    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.e_notebook,container,false);
        addButton = view.findViewById(R.id.addbutton);
        recylerView = view.findViewById(R.id.r_view);


        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        subjectModelArrayList = new ArrayList<>();
        recylerView.setHasFixedSize(true);

        getData();

        recylerView.setLayoutManager(new GridLayoutManager(getActivity(),2,RecyclerView.VERTICAL,false));
        notebookAdapter = new NotebookAdapter(getActivity(), subjectModelArrayList);
        recylerView.setAdapter(notebookAdapter);

        RegisterLauncher();


        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog2 = new Dialog(getActivity());
                dialog2.setContentView(R.layout.subject_add);
                imageadd = dialog2.findViewById(R.id.imageadd);
                subjectadd = dialog2.findViewById(R.id.edtsubject);
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

        return view;
    }

    public void Save(View v){

        int isvoid = 0;

        if(subjectadd.length()==0)
        {
            isvoid++;
            subjectadd.requestFocus();
            subjectadd.setError("Enter the subject!");
        }

        if(isvoid<1){
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Being recorded...");
            dialog.show();

            boolean yok1;
            String subject = subjectadd.getText().toString();
            if(this.imageData !=null){

                //VerileriEkle(this.imageData);
                SubjectControl(imageData,subject,0);
                //dialog.dismiss();

            }
            else{
                this.imageData = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + getResources().getResourcePackageName(R.drawable.imageadd)
                        + '/' + getResources().getResourceTypeName(R.drawable.imageadd) + '/' + getResources().getResourceEntryName(R.drawable.imageadd) );
                //VerileriEkle(imageData);
                SubjectControl(imageData,subject,1);
                dialog.dismiss();
            }
        }
    }

    public void ImageAdd(View view){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
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
                    Toast.makeText(getActivity(),"Permission needed!",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void SubjectControl(Uri imageData, String subject, int resim){
        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();
        firebaseFirestore.collection("Subjects").whereEqualTo("subject", subject).whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String documentt = null;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList<WorkSubjectModel> deneme = new ArrayList<>();
                        DocumentReference docRef = firebaseFirestore.collection("Subjects").document(document.getId());
                        documentt = docRef.toString();

                        dialog.cancel();
                        Toast.makeText(getActivity(),"You have already added this topic!",Toast.LENGTH_LONG).show();
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

        String subject = subjectadd.getText().toString();

        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();

        HashMap<String,Object> subjectData = new HashMap<>();
        subjectData.put("subject",subject);
        subjectData.put("image",downloadUrl);
        subjectData.put("email",email);


        firebaseFirestore.collection("Subjects").add(subjectData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getActivity(),"Subject saved",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getActivity(),"The subject could not be registered",Toast.LENGTH_LONG).show();

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

                        String subject = subjectadd.getText().toString();

                        FirebaseUser user = auth.getCurrentUser();
                        String email = user.getEmail();

                        HashMap<String,Object> subjectData = new HashMap<>();
                        subjectData.put("subject",subject);
                        subjectData.put("image",downloadUrl);
                        subjectData.put("email",email);


                        firebaseFirestore.collection("Subjects").add(subjectData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getActivity(),"Subject saved",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(getActivity(),"The subject could not be registered!",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getActivity(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });

        this.imageData=null;
    }

    private void getData(){

        firebaseFirestore.collection("Subjects").orderBy("subject", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error !=null){
                    Toast.makeText(getActivity(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

                if(value != null){
                    FirebaseUser user = auth.getCurrentUser();
                    String email = user.getEmail();
                    subjectModelArrayList.clear();
                    for(DocumentSnapshot snapshot : value.getDocuments()){
                        //bildirimlerArrayList.clear();
                        HashMap<String,Object> data = (HashMap<String, Object>) snapshot.getData();
                        if(email.equals(data.get("email").toString())){
                            String subject = (String)  data.get("subject");
                            String image = (String)  data.get("image");
                            if(data.get("image")=="default"){
                                image = "Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE" +
                                        "://" + "getResources().getResourcePackageName(R.drawable.imageadd)"
                                        + '/' + "getResources().getResourceTypeName(R.drawable.imageadd)" + '/' + "getResources().getResourceEntryName(R.drawable.imageadd) )";
                            }
                            else{

                            }


                            WorkSubjectModel bildirimler = new WorkSubjectModel(subject, image, email);
                            subjectModelArrayList.add(bildirimler);
                        }

                    }
                    if(!subjectModelArrayList.isEmpty()){
                        notebookAdapter.notifyDataSetChanged();
                        // bildirimlerArrayList.clear();

                    }

                }
            }
        });
    }
}
