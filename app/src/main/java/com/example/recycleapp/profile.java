package com.example.recycleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class profile extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sp;
    BottomNavigationView bottomNav;
    FirebaseAuth firebaseAuth;
    ImageView pic;
    StorageReference storageReference;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    User u;
    TextView BlueBinCnt,OrangeBinCnt,PurpleBinCnt,AllBinCnt;
    TextView BlueBinPoints,OrangeBinPoints,PurpleBinPoints,AllBinPoints, username;
    Button BlueBinButton, PurpleBinButton, OrangeBinButton, AllBinButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BlueBinCnt=findViewById(R.id.BlueBinCnt);
        BlueBinPoints=findViewById(R.id.BlueBinPoints);
        OrangeBinCnt=findViewById(R.id.OrangeBinCnt);
        OrangeBinPoints=findViewById(R.id.OrangeBinPoints);
        PurpleBinCnt=findViewById(R.id.PurpleBinCnt);
        PurpleBinPoints=findViewById(R.id.PurpleBinPoints);
        AllBinCnt=findViewById(R.id.AllBinCnt);
        AllBinPoints=findViewById(R.id.AllBinPoints);
        username=findViewById(R.id.username);

        pic=findViewById(R.id.uPic2);

        BlueBinButton= findViewById(R.id.blueBinButton);
        OrangeBinButton= findViewById(R.id.OrangeBinButton);
        PurpleBinButton= findViewById(R.id.PurpleBinButton);
        AllBinButton= findViewById(R.id.AllBinButton);

        BlueBinButton.setOnClickListener(this);
        OrangeBinButton.setOnClickListener(this);
        PurpleBinButton.setOnClickListener(this);
        AllBinButton.setOnClickListener(this);

        sp=getSharedPreferences("Users",0);
        firebaseAuth = FirebaseAuth.getInstance();
        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = (Menu) bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_profile:
                        break;
                    case R.id.action_chat:
                        Intent i1 = new Intent(profile.this, Chat.class);
                        i1.putExtra("from Profile", true);
                        startActivity(i1);
                        break;

                    case R.id.action_ranks:
                        Intent i2 = new Intent(profile.this, Rating.class);
                        i2.putExtra("from Profile", true);
                        startActivity(i2);
                        break;

                    case R.id.action_trash:
                        Intent i3 = new Intent(profile.this, RecyclePage.class);
                        i3.putExtra("from Profile", true);
                        startActivity(i3);
                        break;


                }
                return true;
            }
        });

        showUserInfo();
    }

    public void showUserInfo(){// פעולה ששולפת את פרטי המשתמש ומציגה אותם בדף
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("Users", MODE_PRIVATE);
        String email = (sharedPreferences.getString("Email","default"));

        databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {

                    if (singleSnapshot.getValue() != null) {
                        u = singleSnapshot.getValue(User.class);
                        u.setAllbin(singleSnapshot.child("allbin").getValue(recyclebin.class));
                        u.setOrangeBin(singleSnapshot.child("orangeBin").getValue(recyclebin.class));
                        u.setBlueBin(singleSnapshot.child("blueBin").getValue(recyclebin.class));
                        u.setPurpleBin(singleSnapshot.child("purpleBin").getValue(recyclebin.class));


                        if (u.getEmail().equals(email)) {

                            BlueBinPoints.setText("נק' שצברתי:"+ u.getBlueBin().getPoints());
                            BlueBinCnt.setText("נאספו "+ u.getBlueBin().getCnt()+" פריטים");
                            OrangeBinPoints.setText("נק' שצברתי:"+ u.getOrangeBin().getPoints());
                            OrangeBinCnt.setText("נאספו "+ u.getOrangeBin().getCnt()+" פריטים");
                            PurpleBinPoints.setText("נק' שצברתי:"+ u.getPurpleBin().getPoints());
                            PurpleBinCnt.setText("נאספו "+ u.getPurpleBin().getCnt()+" פריטים");
                            AllBinPoints.setText("נק' שצברתי: "+ u.getAllbin().getPoints());
                            AllBinCnt.setText("נאספו "+u.getAllbin().getCnt() +" פריטים");
                            username.setText(u.getName());
                            storageReference = FirebaseStorage.getInstance().getReference("Image/users/" + u.getEmail());
                            storageReference = storageReference.child(u.getPicName());
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.with(profile.this).load(uri).into(pic);
                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



     public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.after_login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if(id==R.id.logout)
        {
            if (sp.getBoolean("isChecked", true))
            {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isChecked", false);
                editor.commit();

            }

                firebaseAuth.signOut();
                finish();
                Intent go = new Intent(this, LoginActivity.class);
                go.putExtra("from Profile", true);
                startActivity(go);

        }
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent goToList = new Intent(profile.this, userItemsList.class);

        if (view== BlueBinButton){
            goToList.putExtra("kind of bin", "פח כחול");
            startActivity(goToList);
        }
        else if (view== PurpleBinButton){
            goToList.putExtra("kind of bin", "פח סגול");
            startActivity(goToList);
        }
        else if (view== OrangeBinButton){
            goToList.putExtra("kind of bin", "פח כתום");
            startActivity(goToList);
        }
        if (view== AllBinButton){
            goToList.putExtra("kind of bin", "כל הפחים");
            startActivity(goToList);
        }



    }
}

