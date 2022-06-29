package com.example.recycleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
// דף שמשמש בסיס לדפי הפרגמנט של ההתחברות וההרשמה
public class LoginActivity extends AppCompatActivity {
    TabLayout tablayout;
    ViewPager viewpager;
    float v=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tablayout=findViewById(R.id.tab_layout);
        viewpager=findViewById(R.id.view_pager);

        tablayout.addTab(tablayout.newTab().setText("התחברות"));
        tablayout.addTab(tablayout.newTab().setText("הרשמה"));
        tablayout.setTabGravity(tablayout.GRAVITY_FILL);

        final LoginAndSignupAdapter adapter= new LoginAndSignupAdapter(getSupportFragmentManager(),this,tablayout.getTabCount());
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        tablayout.setTranslationY(300);
        tablayout.setAlpha(v);
        tablayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        if(!isConnectedToInternet())//בדיקת אינטרנט
        {
            Toast.makeText(this, "There is no internet connection", Toast.LENGTH_LONG).show();
            finish();
            System.exit(1);
        }
    }

    // Check  internet connection
    public boolean isConnectedToInternet()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }
}