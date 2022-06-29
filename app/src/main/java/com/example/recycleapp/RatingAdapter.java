package com.example.recycleapp;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class RatingAdapter extends FragmentPagerAdapter {
    private Context context;
    int totalTabs;

    public RatingAdapter(FragmentManager fm, Context context, int totalTabs){
        super(fm);
        this.context=context;
        this.totalTabs=totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    public Fragment getItem(int position){
        switch(position){
            case 0:
                OrangeBinRatingFragment orangeBinRatingFragment= new OrangeBinRatingFragment();
                return orangeBinRatingFragment;
            case 1:
                BlueBinRatingFragment blueBinRatingFragment= new BlueBinRatingFragment();
                return blueBinRatingFragment;
            case 2:
            PurpleBinRatingFragment purpleBinRatingFragment= new PurpleBinRatingFragment();
            return purpleBinRatingFragment;
            case 3:
                AllRatingFragment allRatingFragment= new AllRatingFragment();
                return allRatingFragment;

            default:
                return null;
        }
    }
}