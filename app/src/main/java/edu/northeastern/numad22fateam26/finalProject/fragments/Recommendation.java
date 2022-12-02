package edu.northeastern.numad22fateam26.finalProject.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;


public class Recommendation extends Fragment {

    static List<PostImageModel> postsModelList;
    DocumentReference userRef;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    Activity activity;


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


    }


    private void init(View view) {

    }


    public static String loadAdminPosts() {
        Query query = FirebaseFirestore.getInstance().collection("Users")
                .document("qUs1aFODabPiCVLakZq1KmG0naJ3").collection("Post Images");
        query.addSnapshotListener((value, error) -> {

            if (error != null) {
                Log.d("Error: ", error.getMessage());
            }

            if (value == null)
                return;

            postsModelList.clear();

            for (QueryDocumentSnapshot snapshot : value) {

                if (!value.isEmpty()) {
                    PostImageModel model = snapshot.toObject(PostImageModel.class);
                    postsModelList.add(model);
                }

            }

        });
        int n = postsModelList.size();
        return postsModelList.get(n-1).getDescription();
    }

}