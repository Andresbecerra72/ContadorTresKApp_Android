package app.andres.contadortresk.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import app.andres.contadortresk.fragments.DataFragment;
import app.andres.contadortresk.fragments.HomeFragment;
import app.andres.contadortresk.fragments.IntroFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

   // ----------contructor---------------
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // -----------------------------------
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new IntroFragment();
            case 1:
                return  new HomeFragment();
            case 2:
                return  new DataFragment();
            default:
                return null;
        }


    }

    @Override
    public int getItemCount() {
        return 3; // retorna la cantidad de Paginas / Fragments
    }
} // END class
