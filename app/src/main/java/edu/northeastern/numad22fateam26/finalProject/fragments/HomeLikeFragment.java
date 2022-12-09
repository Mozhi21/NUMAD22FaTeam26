package edu.northeastern.numad22fateam26.finalProject.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.adapter.HomeAdapter;
import edu.northeastern.numad22fateam26.finalProject.adapter.StoriesAdapter;
import edu.northeastern.numad22fateam26.finalProject.chat.ChatUsersActivity;
import edu.northeastern.numad22fateam26.finalProject.model.HomeModel;
import edu.northeastern.numad22fateam26.finalProject.model.StoriesModel;

public class HomeLikeFragment extends Fragment {
//
//    TabLayout preferenceTabLayout;
//    ViewPager preferenceViewPager;
//    private final MutableLiveData<Integer> commentCount = new MutableLiveData<>();
//    HomeAdapter adapter;
//    RecyclerView storiesRecyclerView;
//    StoriesAdapter storiesAdapter;
//    List<StoriesModel> storiesModelList;
//    private RecyclerView recyclerView;
//    private List<HomeModel> list;
//    private FirebaseUser user;
//    private FirebaseFirestore fireStore;
//    Activity activity;
//    ImageView circle;
//
//
//
    public HomeLikeFragment() {
        // Required empty public constructor
    }
//
//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home_like, null);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

}