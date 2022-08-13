package com.prox.appsleep.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.prox.appsleep.fragment.ASMRFragment;
import com.prox.appsleep.fragment.CityFragment;
import com.prox.appsleep.fragment.NatureForestFragment;
import com.prox.appsleep.fragment.RainFragment;
import com.prox.appsleep.fragment.RelaxFragment;
import com.prox.appsleep.ui.home.HomeFragment;

import org.jetbrains.annotations.NotNull;

public class CategoryAdapter extends FragmentStateAdapter {

    public CategoryAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new RainFragment();
            case 1:
                return new NatureForestFragment();
            case 2:
                return new CityFragment();
            case 3:
                return new RelaxFragment();
            case 4:
                return new ASMRFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
