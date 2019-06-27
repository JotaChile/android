

package com.JotaSolutions.APC.adaptadores.viewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;



public abstract class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private Map<Integer, Fragment> registeredFragments = new HashMap<>();


    public SmartFragmentStatePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

   
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

   
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

   
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}

