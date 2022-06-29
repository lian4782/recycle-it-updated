package com.example.recycleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
// דף שבעזרתו משתמש יכול להוסיף פריטי מיחזור לפחים שלו
public class RecyclePage extends AppCompatActivity implements RecyclerViewInterface {
    boolean update = false;
    BottomNavigationView bottomNav;
    ArrayList<recycleItem> ItemsList;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    recycleItem r;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_page);

        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = (Menu) bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_chat:
                        Intent i1 = new Intent(RecyclePage.this, Chat.class);
                        i1.putExtra("from RecyclePage", true);
                        startActivity(i1);
                        break;

                    case R.id.action_ranks:
                        Intent i3 = new Intent(RecyclePage.this, Rating.class);
                        i3.putExtra("from RecyclePage", true);
                        startActivity(i3);
                        break;

                    case R.id.action_trash:
                        break;

                    case R.id.action_profile:
                        Intent i2 = new Intent(RecyclePage.this, profile.class);
                        i2.putExtra("from RecyclePage", true);
                        startActivity(i2);
                        break;
                }
                return true;
            }
        });

        ItemsList = new ArrayList<recycleItem>();
        recyclerView = findViewById(R.id.rw1);
        view_list_of_items();


    }

    public void view_list_of_items() {// פעולה ששולפת את כל הפריטים שניתן למחזר ממסד המתונים ובנוסף מעדכנת לכל הפריטים את התאירך לתאריך הנוכחי ומציגה את הרשימה
        RecyclerViewAdapter1 adapter = new RecyclerViewAdapter1(RecyclePage.this, ItemsList, this);
        databaseReference = firebaseDatabase.getReference("Recycle Items");
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                    if (singleSnapshot.getValue() != null) {

                        r = singleSnapshot.getValue(recycleItem.class);
                        r.setDate(formattedDate);

                        ItemsList.add(r);
                    }
                }
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(RecyclePage.this));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onItemClick(int position) {// פעולה שמופעלת כאשר לוחצים על פריט מיחזור ברשימה, ההפעולה נכנסת למסד הנתונים שולפת את המשתמש שמחובר לפי האימייל ומעדכנת את הפרטים של הפח המתאים לי הפריט שנלחץ
        update = false;// הפריטים שמתעדכנים הם: הניקוד בפח, כמות הפריטים שמוחזרו אליו ןבנוסף הפריט נוסף לרשימת פריטי המיחזור של הפח
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("Users", MODE_PRIVATE);
        String email = (sharedPreferences.getString("Email", "default"));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    User u = singleSnapshot.getValue(User.class);

                    recyclebin Allbin = singleSnapshot.child("allbin").getValue(recyclebin.class);
                    if (u.getEmail().equals(email) && update == false) {
                        Allbin.updateBin(ItemsList.get(position));
                        u.setAllbin(Allbin);

                        if (ItemsList.get(position).getKindOfBin().equals("הפח הכתום")) {
                            recyclebin bin = singleSnapshot.child("orangeBin").getValue(recyclebin.class);
                            bin.updateBin(ItemsList.get(position));
                            u.setOrangeBin(bin);
                        } else if (ItemsList.get(position).getKindOfBin().equals("הפח הסגול")) {
                            recyclebin bin = singleSnapshot.child("purpleBin").getValue(recyclebin.class);
                            bin.updateBin(ItemsList.get(position));
                            u.setPurpleBin(bin);
                        } else if (ItemsList.get(position).getKindOfBin().equals("הפח הכחול")) {
                            recyclebin bin = singleSnapshot.child("blueBin").getValue(recyclebin.class);
                            bin.updateBin(ItemsList.get(position));
                            u.setBlueBin(bin);
                        }

                        databaseReference.child(u.getKey()).setValue(u);
                        update = true;
                        Toast.makeText(RecyclePage.this, "הפריט מוחזר בהצלחה", Toast.LENGTH_LONG).show();

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RecyclePage.this,
                        " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    }





