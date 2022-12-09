package edu.northeastern.numad22fateam26.finalProject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.adapter.PreferenceTabViewPagerAdapter;
import edu.northeastern.numad22fateam26.finalProject.chat.ChatUsersActivity;


public class HomeParentFragment extends Fragment {
    TabLayout preferenceTabLayout;
    ViewPager preferenceViewPager;

    public HomeParentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        addFragment(view);

        view.findViewById(R.id.sendBtn).setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), ChatUsersActivity.class);
            startActivity(intent);

        });
        return view;
    }

    private void addFragment(View view) {
        preferenceTabLayout = view.findViewById(R.id.preferenceTabLayout);
        preferenceViewPager = view.findViewById(R.id.preferenceViewPager);
        PreferenceTabViewPagerAdapter adapter = new PreferenceTabViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new HomeFollowFragment(), "Follow");
        adapter.addFragment(new HomeLikeFragment(), "Like");
        adapter.addFragment(new HomeForYouFragment(), "For You");
        preferenceViewPager.setAdapter(adapter);
        preferenceTabLayout.setupWithViewPager(preferenceViewPager);
    }

}
