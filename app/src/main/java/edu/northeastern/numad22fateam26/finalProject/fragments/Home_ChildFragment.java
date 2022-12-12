package edu.northeastern.numad22fateam26.finalProject.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.adapter.HomeAdapter;
import edu.northeastern.numad22fateam26.finalProject.adapter.HomeViewPagerAdapter;
import edu.northeastern.numad22fateam26.finalProject.adapter.RecViewPagerAdapter;
import edu.northeastern.numad22fateam26.finalProject.adapter.StoriesAdapter;
import edu.northeastern.numad22fateam26.finalProject.chat.ChatUsersActivity;
import edu.northeastern.numad22fateam26.finalProject.model.HomeModel;
import edu.northeastern.numad22fateam26.finalProject.model.PostImageModel;
import edu.northeastern.numad22fateam26.finalProject.model.StoriesModel;

public class Home_ChildFragment extends Fragment {
    private FirebaseUser user;
    RecyclerView storiesRecyclerView;
    StoriesAdapter storiesAdapter;
    List<StoriesModel> storiesModelList;
    private FirebaseFirestore fireStore;

    ImageButton messageBtn11;
    Activity activity;
    ImageView circle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_child_frag, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        init(view);
        initTabs(view);

        checkIfUnreadMessages();

        final DocumentReference reference = fireStore.collection("Users")
                .document(user.getUid());

        messageBtn11.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), ChatUsersActivity.class);
            startActivity(intent);
        });

        reference.addSnapshotListener((value, error) -> {

                    if (error != null) {
                        Log.d("Error: ", error.getMessage());
                        return;
                    }

                    if (value == null)
                        return;

                    List<String> uidList = (List<String>) value.get("following");

                    if (uidList == null || uidList.isEmpty())
                        return;

                    loadStories(uidList);
                });
    }

    private void init(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null)
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        storiesRecyclerView = view.findViewById(R.id.storiesRecyclerView);
        storiesRecyclerView.setHasFixedSize(true);
        storiesRecyclerView
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        storiesModelList = new ArrayList<>();
        storiesAdapter = new StoriesAdapter(storiesModelList, getActivity());
        storiesRecyclerView.setAdapter(storiesAdapter);

        circle = view.findViewById(R.id.circle);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();

        messageBtn11 = view.findViewById(R.id.messageBtn11);
    }
    private void initTabs(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.ContentTabLayout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.rec_view_pager);
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getChildFragmentManager(), 0);
        adapter.addFragment(new Home_explore(), "Explore");
        adapter.addFragment(new Home_likes(), "Likes");
        adapter.addFragment(new Home_follow(), "Follow");
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
                adapter.notifyDataSetChanged();
            }
        });
    }

    void loadStories(List<String> followingList) {

        Query query = FirebaseFirestore.getInstance().collection("Stories");
        query.whereIn("uid", followingList).addSnapshotListener((value, error) -> {

            if (error != null) {
                Log.d("Error: ", error.getMessage());
            }

            if (value == null)
                return;

            storiesModelList.clear();
            storiesModelList.add(new StoriesModel("", "", "", "", ""));

            for (QueryDocumentSnapshot snapshot : value) {

                if (!value.isEmpty()) {
                    StoriesModel model = snapshot.toObject(StoriesModel.class);
                    storiesModelList.add(model);
                }

            }
            storiesAdapter.notifyDataSetChanged();

        });

    }

    private void checkIfUnreadMessages() {
        Query query = FirebaseFirestore.getInstance().collection("Messages");
        query.whereEqualTo("unread", true).whereEqualTo("lastMessageTo", user.getUid()).addSnapshotListener(((value, error) -> {
            if (error != null) {
                Log.d("Error: ", error.getMessage());
            }

            if (value == null)
                return;

            if (value.isEmpty()) {
                circle.setVisibility(View.GONE);
            } else {
                circle.setVisibility(View.VISIBLE);
            }
        }));
    }

}
