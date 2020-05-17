package com.example.trouvetongab;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragAdapter extends FragmentPagerAdapter {

    private final List<Fragment> lsFragment = new ArrayList<>();
    private final List<String> lsTitles = new ArrayList<>();
    Bundle arguments;

    public ViewPagerFragAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        this.arguments = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return lsFragment.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return lsTitles.get(position);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return lsTitles.size();
    }

    public void AddFragment (Fragment fragment, String title){
        lsFragment.add(fragment);
        lsTitles.add(title);
    }

}
