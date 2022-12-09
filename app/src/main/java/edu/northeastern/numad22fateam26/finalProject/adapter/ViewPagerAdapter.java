package edu.northeastern.numad22fateam26.finalProject.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.northeastern.numad22fateam26.finalProject.fragments.Add;
import edu.northeastern.numad22fateam26.finalProject.fragments.HomeParentFragment;
import edu.northeastern.numad22fateam26.finalProject.fragments.Profile;
import edu.northeastern.numad22fateam26.finalProject.fragments.Search;
import edu.northeastern.numad22fateam26.finalProject.fragments.Recommendation;


public class ViewPagerAdapter extends FragmentPagerAdapter {

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
                return new HomeParentFragment();

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
