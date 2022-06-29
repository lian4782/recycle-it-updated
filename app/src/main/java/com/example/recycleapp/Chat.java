package com.example.recycleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chat extends AppCompatActivity //implements View.OnClickListener
{
    ImageView sendMessege;
    EditText text;
    ArrayAdapter<String> adapter;
    ArrayList<String> list;
    ArrayList<Message> listpost;
    ArrayAdapter<Message> lpadapter;
    ListView myListView;
    DatabaseReference myref;

    ProgressDialog p;
    BottomNavigationView bottomNav;

    Message message, m;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_chat:
                        break;

                    case R.id.action_ranks:
                        Intent i3 = new Intent(Chat.this, Rating.class);
                        i3.putExtra("from Chat", true);
                        startActivity(i3);
                        break;

                    case R.id.action_trash:
                        Intent i1 = new Intent(Chat.this, RecyclePage.class);
                        i1.putExtra("from Chat", true);
                        startActivity(i1);

                        break;

                    case R.id.action_profile:
                        Intent i2 = new Intent(Chat.this, profile.class);
                        i2.putExtra("from Chat", true);
                        startActivity(i2);
                        break;
                }
                return true;
            }
        });

        message = new Message();
        myref = firebaseDatabase.getReference("Messages");
        list = new ArrayList<>();
        listpost = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        sendMessege = (ImageView) findViewById(R.id.send_image);
        sendMessege.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createMSG();
            }
        });
        text = (EditText) findViewById(R.id.user_message);
        myListView = (ListView) findViewById(R.id.list);
        lpadapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1, listpost);

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    message = ds.getValue(Message.class);
                    list.add( message.getFromUser() + ": " + message.getText());
                    listpost.add(message);
                }
                myListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createMSG()
    {
        if (isValidate())
        {
            p = new ProgressDialog(this);
            p.setMessage("שולח..");
            p.show();
            //לשם חיבור לבסיס הנתונים
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            //הצבעה לתת ענף של משתמשים ("טבלה")
            DatabaseReference myref = firebaseDatabase.getReference("Messages");

            m = new Message(text.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail().substring(0,FirebaseAuth.getInstance().getCurrentUser().getEmail().indexOf("@")));
            Toast.makeText(Chat.this, "נשלח", Toast.LENGTH_SHORT).show();
            myref.push().setValue(m);

            this.recreate();
            text.setText("");
        }
    }

    public boolean isValidate()
    {
        if (text.getText().toString().length() == 0)
        {
            text.setError("אי אפשר לשלוח הודעות ריקות");
            text.setFocusable(true);
            return false;
        }

        if (firebaseAuth.getInstance().getCurrentUser()==null)
            return false;
        return true;
    }
}