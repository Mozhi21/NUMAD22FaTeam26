package edu.northeastern.numad22fateam26.finalProject.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.adapter.RecViewPagerAdapter;
import edu.northeastern.numad22fateam26.finalProject.model.PostImageModel;


public class Recommendation extends Fragment {

    private static final String TAG = "Recommendation";
    private static final String ADMIN_ID = "KAG9Tsfv4rUUtura8CLInb22ckV2";
    private FirebaseUser user;
    private List<PostImageModel> adminPosts;
    private TextView adminDescription;
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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        initDailyPanel(view);
        initTabs(view);
    }

    private void initDailyPanel(View view) {
        adminDescription = view.findViewById(R.id.admin_subtitle);
        adminPic = view.findViewById(R.id.admin_pic);
        loadAdminPost();
    }

    public void loadAdminPost() {
        adminPosts = new ArrayList<>();
        CollectionReference postRef = FirebaseFirestore.getInstance().collection("Users")
                .document(ADMIN_ID).collection("Post Images");
        postRef
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    PostImageModel model = document.toObject(PostImageModel.class);
                                    adminPosts.add(model);
                                }
                            }
                            if (!adminPosts.isEmpty()) {
                                setPostView(adminPosts);
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void setPostView(List<PostImageModel> postsModelList) {
        PostImageModel adminPost = postsModelList.get(0);
        adminDescription.setText(adminPost.getDescription());
        Glide.with(this)
                .load(adminPost.getImageUrl())
                .timeout(6500)
                .into(adminPic);
    }

    private void initTabs(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.recommendation_tab);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.rec_view_pager);
        RecViewPagerAdapter adapter = new RecViewPagerAdapter(getChildFragmentManager(), 0);
        adapter.addFragment(new RecipeFragment(), "Detailed Recipe");
        adapter.addFragment(new RelevantPostsFragment(), "Relevant Posts");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}