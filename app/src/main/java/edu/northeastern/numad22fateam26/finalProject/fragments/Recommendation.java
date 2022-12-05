package edu.northeastern.numad22fateam26.finalProject.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.northeastern.numad22fateam26.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.adapter.HomeAdapter;
import edu.northeastern.numad22fateam26.finalProject.adapter.StoriesAdapter;
import edu.northeastern.numad22fateam26.finalProject.adapter.UserAdapter;
import edu.northeastern.numad22fateam26.finalProject.model.HomeModel;
import edu.northeastern.numad22fateam26.finalProject.model.PostImageModel;
import edu.northeastern.numad22fateam26.finalProject.model.StoriesModel;

import com.google.firebase.firestore.DocumentReference;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.QuerySnapshot;

import android.widget.ImageView;
import android.widget.TextView;
import javax.annotation.Nullable;


public class Recommendation extends Fragment {

    private static final String TAG = "Recommendation";
    List<PostImageModel> postsModelList;
    DocumentReference userRef;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    Activity activity;
    private TextView adminStory;
    private TextView adminRecipe;
    private ImageView adminPic;


    public Recommendation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommendation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

    }


    private void init(View view) {
        adminStory = view.findViewById(R.id.culture_stories);
        adminRecipe = view.findViewById(R.id.detailed_recipe);
        adminPic = view.findViewById(R.id.admin_pic);

        postsModelList = new ArrayList<>();
        String lastPost = loadAdminPosts();
        adminStory.setText(String.format("Topic for this Week: %s\n", lastPost));

    }


    public String loadAdminPosts() {
        CollectionReference query = FirebaseFirestore.getInstance().collection("Users")
                .document("qUs1aFODabPiCVLakZq1KmG0naJ3").collection("Post Images");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                System.out.println(task);
                System.out.println(task.getResult());
                System.out.println(task.getException());
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getData());
                        if (document.exists()) {
                            PostImageModel model = document.toObject(PostImageModel.class);
                            postsModelList.add(model);
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        int n = postsModelList.size();
        if (n == 0) {
            return null;
        }
        return postsModelList.get(n-1).getDescription();
    }

}