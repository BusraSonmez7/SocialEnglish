package com.busrasonmez.socialenglish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.busrasonmez.socialenglish.databinding.ActivityRegisterBinding;
import com.busrasonmez.socialenglish.databinding.ActivityUserLoginBinding;
import com.busrasonmez.socialenglish.social_media.SocialMedia;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

public class user_login extends AppCompatActivity {

    private FirebaseAuth auth;
    private ActivityUserLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(getApplicationContext(),main_page.class);
            startActivity(intent);
            finish();
        }
    }


    public void Login(View view){
        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();

        int bosmu = 0;

        if(email.equals(""))
        {
            bosmu++;
            binding.email.requestFocus();
            binding.email.setError("Bu alan boş geçilemez");
        }
        if(password.equals(""))
        {
            bosmu++;
            binding.password.requestFocus();
            binding.password.setError("Bu alan boş geçilemez");
        }

        if(bosmu<1){
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent intent = new Intent(getApplicationContext(), main_page.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                }
            });

//            if(image_data !=null){
//                SavePersonDb(image_data);
//            }
//            else{
//
//                image_data = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
//                        "://" + getResources().getResourcePackageName(R.drawable.profile)
//                        + '/' + getResources().getResourceTypeName(R.drawable.profile) + '/' + getResources().getResourceEntryName(R.drawable.profile) );
//                //VerileriEkle(imageData);
//                SavePersonDb(image_data);
//            }

        }

    }

    public void Register(View view){
        Intent intent = new Intent(getApplicationContext(),register.class);
        startActivity(intent);

    }
}