package com.example.alilo.alichat;

/**
 * Created by alilo on 21/10/2018.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alilo on 16/09/2018.
 */

public class SectionspagerAdpter extends FragmentPagerAdapter {
    private final List<Fragment> listF = new ArrayList<>();
    private final List<String> listT = new ArrayList<>();
    public SectionspagerAdpter(FragmentManager fm) {
        super(fm);
    }
    protected void AddFragment(Fragment fragment, String  titre){
        listF.add(fragment);
        listT.add(titre);
    }
    @Override
    public Fragment getItem(int position) {
        return listF.get(position);
    }

    @Override
    public int getCount() {
        return listF.size();
    }
    @Override
    public CharSequence getPageTitle(int i){
        return  listT.get(i);
    }
}
