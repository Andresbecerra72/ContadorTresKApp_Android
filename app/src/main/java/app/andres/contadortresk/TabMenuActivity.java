package app.andres.contadortresk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import app.andres.contadortresk.adapters.ViewPagerAdapter;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class TabMenuActivity extends AppCompatActivity {

    // Objetos
    TabLayout tabs;
    ViewPager2 viewPager;

    private String[] titles = new String[]{"Intro", "Home", "Data"};
    private int[] icons = new int[]{R.drawable.ic_note, R.drawable.ic_home, R.drawable.ic_cloud};




    // ------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_menu);
        //deja el icono en el action Bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        //esta linea de codigo evita la rotacion de la patanlla
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // relacio con los Objetos
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);





        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabs, viewPager,
                (tab, position) -> {
                    // Log.d("TAG Position", String.valueOf(tab.getPosition()));
                    tab.setIcon(icons[position]);
                    // tab.setText(titles[position]);
                    // tabs.selectTab(tabs.getTabAt(1)); // abre la app en el tab Home
                }
        ).attach();




    } // End OnCreate



} // END class