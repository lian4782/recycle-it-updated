package com.example.recycleapp;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    BroadCastBattery broadCastBattery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((ProgressBar) findViewById(R.id.progressBar)).setActivated(true);


        SharedPreferences sp;
        FirebaseAuth firebaseAuth;

        sp=getSharedPreferences("Users",0);
        firebaseAuth = FirebaseAuth.getInstance();


        Thread mSplashThread = new Thread() {
            @Override

            public void run() {

                synchronized (this) {
                    try {
                        broadCastBattery = new BroadCastBattery();
                        registerReceiver(broadCastBattery,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                        wait(4000);



                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                finish();

                if (sp.getBoolean("isChecked", true)){
                    Intent i=new Intent(MainActivity.this, profile.class);
                    startActivity(i);
                }
                else {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        mSplashThread.start();

        Animation animation=AnimationUtils.loadAnimation(this,R.anim.fade);
        ((ImageView)findViewById(R.id.ItemImg)).startAnimation(animation);

        if(!isConnectedToInternet())
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setMessage("no internet");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "אישור",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                            System.exit(1);
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }

    }
    // BroadCast receiver - Battery
   private class BroadCastBattery extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            int battery = intent.getIntExtra("level", 0);
            if (battery < 30)
            {
                Toast.makeText(MainActivity.this, "Your battery is low please charge your phone", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadCastBattery);
    }

    // Check  internet connection
    public boolean isConnectedToInternet()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }
}
