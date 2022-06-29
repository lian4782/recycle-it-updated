package com.example.recycleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
// דף שמשמש בסיס לדפי הפרגמנט של דפי הדירוגים של -  הפח הכחול, הפח הסגול, הפח הכתו, כל הפחים
public class Rating extends AppCompatActivity {

    BottomNavigationView bottomNav;

    TabLayout tablayout;
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = (Menu) bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_chat:
                        Intent i1 = new Intent(Rating.this, Chat.class);
                        i1.putExtra("from Rating", true);
                        startActivity(i1);
                        break;

                    case R.id.action_ranks:
                        break;

                    case R.id.action_trash:
                        Intent i3 = new Intent(Rating.this, RecyclePage.class);
                        i3.putExtra("from Rating", true);
                        startActivity(i3);
                        break;

                    case R.id.action_profile:
                        Intent i2 = new Intent(Rating.this, profile.class);
                        i2.putExtra("from Rating", true);
                        startActivity(i2);
                        break;
                }
                return true;
            }
        });

        tablayout=findViewById(R.id.tabLayout);
        viewpager=findViewById(R.id.viewPager);

        final RatingAdapter adapter= new RatingAdapter(getSupportFragmentManager(),this,tablayout.getTabCount());
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        getSupportFragmentManager().beginTransaction().replace(R.id.ratingPageMain,new OrangeBinRatingFragment());
        getSupportFragmentManager().beginTransaction().replace(R.id.ratingPageMain,new BlueBinRatingFragment());
        getSupportFragmentManager().beginTransaction().replace(R.id.ratingPageMain,new PurpleBinRatingFragment());
        getSupportFragmentManager().beginTransaction().replace(R.id.ratingPageMain,new AllRatingFragment());
    }
}