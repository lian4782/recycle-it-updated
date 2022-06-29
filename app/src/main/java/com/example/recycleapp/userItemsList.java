package com.example.recycleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
//דף המציג רשימה של פריטי מיחזור של פח של משתמש

public class userItemsList extends AppCompatActivity {
    ArrayList<recycleItem> ItemsList;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Intent goToList;
    String kind;
    TextView headline;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_items_list);

        headline=findViewById(R.id.headline);
        ItemsList = new ArrayList<recycleItem>();
        recyclerView = findViewById(R.id.rwForUser);
        goToList=getIntent();
        kind=goToList.getExtras().getString("kind of bin");//במעבר לדף הזה נשלח אינטנט ובו ערך של סוג הפח
        view_list_of_items_of_user();

    }

    public void view_list_of_items_of_user() {// פעולה שמציגה רשימה של פריטי מיחזור של פח של משתמש

        databaseReference = firebaseDatabase.getReference("Users");
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("Users", MODE_PRIVATE);
        String email = (sharedPreferences.getString("Email", "default"));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recyclebin bin;
                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                    if (singleSnapshot.getValue() != null) {
                        User u = singleSnapshot.getValue(User.class);
                        if (u.getEmail().equals(email)) {

                            if (kind.equals("פח כתום")) {// לפי הערך של סוג האינטנט ידע איזה פח לשלוף ממסד הנתונים
                                bin = singleSnapshot.child("orangeBin").getValue(recyclebin.class);//"אם עברו אל הדף הזה מהכפתור של הפח הכתום האינטנט יכיל "פח כתום
                                if (bin.getBinItemsList() != null) {
                                    for(int i=bin.getBinItemsList().size()-1;i>=0;i--)
                                    ItemsList.add(bin.getBinItemsList().get(i));

                                    headline.setText("פריטים שמוחזרו לפח הכתום");
                                }
                            }
                            else if (kind.equals("פח כחול")) {// לפי הערך של סוג האינטנט ידע איזה פח לשלוף ממסד הנתונים
                                bin = singleSnapshot.child("blueBin").getValue(recyclebin.class);//"אם עברו אל הדף הזה מהכפתור של הפח הכחול האינטנט יכיל "פח כחול
                                if (bin.getBinItemsList() != null) {
                                    for(int i=bin.getBinItemsList().size()-1;i>=0;i--)
                                        ItemsList.add(bin.getBinItemsList().get(i));
                                    headline.setText("פריטים שמוחזרו לפח הכחול");
                                }
                            }
                            else if (kind.equals("פח סגול")) {// לפי הערך של סוג האינטנט ידע איזה פח לשלוף ממסד הנתונים
                                bin = singleSnapshot.child("purpleBin").getValue(recyclebin.class);//"אם עברו אל הדף הזה מהכפתור של הפח הסגול האינטנט יכיל "פח סגול
                                if (bin.getBinItemsList() != null) {
                                    for(int i=bin.getBinItemsList().size()-1;i>=0;i--)
                                        ItemsList.add(bin.getBinItemsList().get(i));
                                    headline.setText("פריטים שמוחזרו לפח הסגול");
                                }
                            }
                            else if (kind.equals("כל הפחים")) {// לפי הערך של סוג האינטנט ידע איזה פח לשלוף ממסד הנתונים
                                bin = singleSnapshot.child("allbin").getValue(recyclebin.class);//"אם עברו אל הדף הזה מהכפתור של כל הפחים האינטנט יכיל "כל הפחים
                                if (bin.getBinItemsList() != null) {
                                    for(int i=bin.getBinItemsList().size()-1;i>=0;i--)
                                        ItemsList.add(bin.getBinItemsList().get(i));
                                    headline.setText("פריטים שמוחזרו לכל הפחים");
                                }
                            }

                        }
                    }
                }
                RecyclerViewAdapterUserItems adapter = new RecyclerViewAdapterUserItems(userItemsList.this, ItemsList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(userItemsList.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}