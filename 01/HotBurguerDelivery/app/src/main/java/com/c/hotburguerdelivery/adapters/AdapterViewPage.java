package com.c.hotburguerdelivery.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterViewPage extends FragmentPagerAdapter {

    private List<Fragment> listFrag ;

    public AdapterViewPage(FragmentManager fm) {
        super(fm);
        this.listFrag = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return listFrag.get(position);
    }

    @Override
    public int getCount() {
        return listFrag.size();
    }

    public synchronized void addFrag(Fragment frag){
        listFrag.add(frag);
        notifyDataSetChanged();
    }

    public void removeFrag(int position){
        if(position > 0){
            listFrag.remove(position);
            notifyDataSetChanged();
        }
    }

    public Fragment getFragment(int position){
        return listFrag.get(position);
    }

    public List<Fragment> getListFrag() {
        return listFrag;
    }
}
