package com.project.ilearncentral.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.project.ilearncentral.Fragment.JobPost;
import com.project.ilearncentral.Fragment.Management;
import com.project.ilearncentral.Fragment.Feed;
import com.project.ilearncentral.Fragment.Profile.EducatorProfile;

import java.util.ArrayList;

public class UserPagesAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> strings = new ArrayList<>();

    public UserPagesAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int i){
        switch (i){
            case 0:
                return new EducatorProfile();
            case 1:
                return new Feed();
            case 2:
                return new JobPost();
            case 3:
                return new Management();
            case 4:
                return new Management();
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String string){
        fragments.add(fragment);
        strings.add(string);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return strings.get(position);
    }
}
