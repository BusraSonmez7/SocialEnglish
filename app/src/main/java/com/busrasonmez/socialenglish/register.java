package com.busrasonmez.socialenglish;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.busrasonmez.socialenglish.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class register extends AppCompatActivity {
    EditText name, surname, username, password, r_password, about, email;
    Button btn_register;
    ImageView img_profile;
    Uri image_data;

    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        RegisterLauncher();


    }

    public void ImageAdd(View view){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
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
                        binding.profileIcon.setImageURI(image_data);
                    }
                    else{
                        image_data = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                                "://"+getResources().getResourcePackageName(R.drawable.profile_create)+
                                '/'+getResources().getResourceTypeName(R.drawable.profile_create)+'/'+
                                getResources().getResourceEntryName(R.drawable.profile_create));
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

    public void UserRegister(View view){

        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();

        int bosmu = 0;
        if(binding.name.length()==0)
        {
            bosmu++;
            binding.name.requestFocus();
            binding.name.setError("This field cannot be left blank");
        }

        if(binding.surname.length()==0)
        {
            bosmu++;
            binding.surname.requestFocus();
            binding.surname.setError("This field cannot be left blank");
        }

        if(binding.username.length()==0)
        {
            bosmu++;
            binding.username.requestFocus();
            binding.username.setError("This field cannot be left blank");
        }
        if(email.equals(""))
        {
            bosmu++;
            binding.email.requestFocus();
            binding.email.setError("This field cannot be left blank");
        }
        if(password.equals(""))
        {
            bosmu++;
            binding.password.requestFocus();
            binding.password.setError("This field cannot be left blank");
        }

        if(bosmu<1){

            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    auth.signOut();
                    Intent intent = new Intent(getApplicationContext(),user_login.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });
            if(image_data !=null){
                UserSave(image_data);
            }
            else{

                image_data = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + getResources().getResourcePackageName(R.drawable.profile_n)
                        + '/' + getResources().getResourceTypeName(R.drawable.profile_n) + '/' + getResources().getResourceEntryName(R.drawable.profile_n) );
                //VerileriEkle(imageData);
                UserSave(image_data);
                //SavePersonDb(image_data);
            }

        }
//
//        SavePersonDb();

    }

    public void UserSave(Uri image_data){
        UUID uuid = UUID.randomUUID();
        String imageName = "images/"+uuid+".jpg";
        storageReference.child(imageName).putFile(image_data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference newRefrence = firebaseStorage.getReference(imageName);
                newRefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();

                        FirebaseUser user = auth.getCurrentUser();

                        HashMap<String,Object> bildirimVeri = new HashMap<>();
                        bildirimVeri.put("name",binding.name.getText().toString());
                        bildirimVeri.put("surname",binding.surname.getText().toString());
                        bildirimVeri.put("username",binding.username.getText().toString());
                        bildirimVeri.put("email",binding.email.getText().toString());
                        bildirimVeri.put("password",binding.password.getText().toString());
                        bildirimVeri.put("about", binding.about.getText().toString());
                        bildirimVeri.put("profile",downloadUrl);


                        firebaseFirestore.collection("Users").add(bildirimVeri).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(register.this,"Successfully saved",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), user_login.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(register.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

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
    }
}