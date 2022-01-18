package com.busrasonmez.socialenglish.social_media.AddPost;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.databinding.ActivityPostSharingBinding;
import com.busrasonmez.socialenglish.social_media.SocialMedia;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class PostSharing extends Fragment {

    Uri image_data;

    ImageView gallery, vgallery, imageView;
    Button sharebtn;
    MultiAutoCompleteTextView comment;

    ActivityResultLauncher<Intent> activityResultLauncher, activityResultLauncher2;
    ActivityResultLauncher<String> permissionLauncher;

    private ActivityPostSharingBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post_sharing,container,false);

        gallery = view.findViewById(R.id.gallery);
        vgallery = view.findViewById(R.id.videogallery);
        imageView = view.findViewById(R.id.image_view);
        sharebtn = view.findViewById(R.id.share_btn);
        comment = view.findViewById(R.id.comments);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();



        RegisterLauncher();
        return view;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityPostSharingBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);
//
//        auth = FirebaseAuth.getInstance();
//        firebaseFirestore = FirebaseFirestore.getInstance();
//        firebaseStorage = FirebaseStorage.getInstance();
//        storageReference = firebaseStorage.getReference();
//
//        RegisterLauncher();
//    }

    public void ImageShare(View view){
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

    public void RegisterLauncher(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if(intentFromResult != null){
                        image_data = intentFromResult.getData();
                        imageView.setImageURI(image_data);
                        imageView.setVisibility(View.VISIBLE);
                    }
                    else{
                        image_data = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                                "://"+getResources().getResourcePackageName(R.drawable.icon)+
                                '/'+getResources().getResourceTypeName(R.drawable.icon)+'/'+
                                getResources().getResourceEntryName(R.drawable.icon));
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

    public void ShareAdd(View view){
        if(image_data !=null){
            Share(image_data);
        }
        else{

            image_data = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + getResources().getResourcePackageName(R.drawable.profile)
                    + '/' + getResources().getResourceTypeName(R.drawable.profile) + '/' + getResources().getResourceEntryName(R.drawable.profile) );
            //VerileriEkle(imageData);
            Share(image_data);
            //SavePersonDb(image_data);
        }
        Intent intent = new Intent(getActivity(), SocialMedia.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void Share(Uri image_data){
        UUID uuid = UUID.randomUUID();
        String imageName = "shares/"+uuid+".jpg";
        storageReference.child(imageName).putFile(image_data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference newRefrence = firebaseStorage.getReference(imageName);
                newRefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();

                        FirebaseUser user = auth.getCurrentUser();
                        String email = user.getEmail();
                        UUID uuid2 = UUID.randomUUID();
                        String shareName = uuid2+"";
                        HashMap<String,Object> sharee = new HashMap<>();
                        sharee.put("shareID",shareName);
                        sharee.put("comment",comment.getText().toString());
                        sharee.put("email",email);
                        sharee.put("type","image");
                        sharee.put("share_date", FieldValue.serverTimestamp());
                        sharee.put("shareURL",downloadUrl);


                        firebaseFirestore.collection("Shares").add(sharee).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getActivity(),"Successfully saved",Toast.LENGTH_LONG).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(getActivity(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

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
    }
}