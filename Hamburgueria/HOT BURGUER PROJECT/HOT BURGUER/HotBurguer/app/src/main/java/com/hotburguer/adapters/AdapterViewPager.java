package com.hotburguer.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.hotburguer.acustom.Cart;
import com.hotburguer.acustom.CustomApplication;
import com.hotburguer.fragments.FMT_Product;
import com.hotburguer.itens.HotProduct;

import java.util.ArrayList;
import java.util.List;

public class AdapterViewPager extends FragmentStatePagerAdapter {

    private List<Fragment> frags;
    private FragmentManager fm;

    public AdapterViewPager(FragmentManager fm) {
        super(fm);
        this.fm = fm;
        this.frags = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return frags.get(position);
    }

    @Override
    public int getCount() {
        return frags.size();
    }

    public synchronized void addFrag(Fragment frag) {
        frags.add(frag);
        notifyDataSetChanged();
    }

    public void addAllSliders(ArrayList<HotProduct> products) {

        if (products != null && products.size() > 0) {

            Cart cart = CustomApplication.getCartShopping().getCart();
            HotProduct otherProduct ;

            for (HotProduct product : products) {
                otherProduct = cart.get(product.getId());

                if (otherProduct != null) {
                    product.setCount(otherProduct.getCount());
                    product.setSelected(otherProduct.isSelected());
                }

                addFrag(new FMT_Product(product));

            }
        }
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
}
