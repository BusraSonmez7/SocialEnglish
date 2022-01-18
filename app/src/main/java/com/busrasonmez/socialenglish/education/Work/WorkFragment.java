package com.busrasonmez.socialenglish.education.Work;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.busrasonmez.socialenglish.R;
import com.busrasonmez.socialenglish.education.Work.Subject.WorkSubjectModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class WorkFragment extends Fragment {

    public WorkAdapter workAdapter;
    private RecyclerView recylerView;
    ArrayList<WorkSubjectModel> workSubjectModelArrayList;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.e_work,container,false);

        recylerView = view.findViewById(R.id.r_view);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        workSubjectModelArrayList = new ArrayList<>();
        recylerView.setHasFixedSize(true);

        recylerView.setLayoutManager(new GridLayoutManager(getActivity(),2,RecyclerView.VERTICAL,false));
        workAdapter = new WorkAdapter(getActivity(), workSubjectModelArrayList);
        recylerView.setAdapter(workAdapter);


//        subjectModelArrayList = new ArrayList<>();
//        recylerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recylerView.setLayoutManager(layoutManager);
//        recylerView.setAdapter(workAdapter);
        getSubject();

        return view;
    }

    private void getSubject(){

        firebaseFirestore.collection("Subjects").whereEqualTo("email","admin").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error !=null){
                    Toast.makeText(getActivity(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

                if(value != null){
                    //subjectModelArrayList.clear();
                    for(DocumentSnapshot snapshot : value.getDocuments()){
                        //bildirimlerArrayList.clear();
                        HashMap<String,Object> data = (HashMap<String, Object>) snapshot.getData();
                            String subject = (String)  data.get("subject");
                            String image = (String)  data.get("image");
                            String email = (String)  data.get("email");
//                                profilee = "Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE" +
//                                        "://" + "getResources().getResourcePackageName(R.drawable.profile)"
//                                        + '/' + "getResources().getResourceTypeName(R.drawable.profile)" + '/' + "getResources().getResourceEntryName(R.drawable.profile) )";


                            WorkSubjectModel p = new WorkSubjectModel(subject,image,email);
                            workSubjectModelArrayList.add(p);

                    }
                    if(!workSubjectModelArrayList.isEmpty()){
                        workAdapter.notifyDataSetChanged();
                        // bildirimlerArrayList.clear();
                    }

                }
            }
        });
    }
}
