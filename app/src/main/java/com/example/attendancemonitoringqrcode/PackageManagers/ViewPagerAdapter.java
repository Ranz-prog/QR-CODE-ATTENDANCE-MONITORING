package com.example.attendancemonitoringqrcode.PackageManagers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {


    private final List<String> fragmentTitleList = new ArrayList<>();
    private final List<Fragment> fragmentList = new ArrayList<>();


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


    @Override
    public int getCount() {
        return fragmentTitleList.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }


    public void addFragment(String title, Fragment fragment) {

        fragmentTitleList.add(title);
        fragmentList.add(fragment);

    }


}
