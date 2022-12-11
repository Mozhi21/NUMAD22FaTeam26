package edu.northeastern.numad22fateam26.finalProject.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import edu.northeastern.numad22fateam26.finalProject.fragments.RecipeFragment;
import edu.northeastern.numad22fateam26.finalProject.fragments.RelevantPostsFragment;


public class RecViewPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public RecViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new RelevantPostsFragment();
            case 0:
            default:
                return new RecipeFragment();
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
