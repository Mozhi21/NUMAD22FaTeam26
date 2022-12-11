package edu.northeastern.numad22fateam26.finalProject.adapter;

import android.support.v4.media.RatingCompat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import edu.northeastern.numad22fateam26.finalProject.fragments.Add;
import edu.northeastern.numad22fateam26.finalProject.fragments.Home;
import edu.northeastern.numad22fateam26.finalProject.fragments.Home_ChildFragment;
import edu.northeastern.numad22fateam26.finalProject.fragments.Notification;
import edu.northeastern.numad22fateam26.finalProject.fragments.Profile;
import edu.northeastern.numad22fateam26.finalProject.fragments.Search;
import edu.northeastern.numad22fateam26.finalProject.fragments.Recommendation;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int noOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.noOfTabs = noOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            default:
            case 0:
                return new Home_ChildFragment();

            case 1:
                return new Search();

            case 2:
                return new Add();

            case 3:
                return new Recommendation();

            case 4:
                return new Profile();


        }

    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
