package br.com.hotbmanager.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class AdapterViewPager extends FragmentPagerAdapter {

    private ArrayList<Fragment> frags ;
    private FragmentManager fm;
    private String[] titles = new String[]{"Aguardando","Ativos","Em Fila"};

    public AdapterViewPager(@NonNull FragmentManager fm) {
        super(fm);
        this.fm = fm ;
        this.frags = new ArrayList<>();
    }

    public AdapterViewPager(@NonNull FragmentManager fm, String[] titles) {
        super(fm);
        this.fm = fm ;
        this.titles = titles ;
        this.frags = new ArrayList<>();
    }

    public AdapterViewPager(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.fm = fm ;
        this.frags = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return frags.get(position);
    }

    @Override
    public int getCount() {
        return frags.size();
    }

    public void addFragment(Fragment fragment){
        frags.add(fragment);
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public boolean clear() {
        FragmentTransaction transaction = fm.beginTransaction();
        for (Fragment fragment : frags) {
            transaction.remove(fragment);
        }
        frags.clear();
        fm.getFragments().clear();

        transaction.commitAllowingStateLoss();
        notifyDataSetChanged();

        return true;
    }

    public void remove(int position) {
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.remove(frags.get(position));
        frags.remove(position);

        transaction.commitAllowingStateLoss();
        notifyDataSetChanged();
    }
}
