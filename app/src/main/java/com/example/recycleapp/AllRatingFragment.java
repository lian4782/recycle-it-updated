package com.example.recycleapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
//דף שתפקידו להציג דירוג משתמשים לפי ניקוד שלהם בכל הפחים
public class AllRatingFragment extends Fragment {
    RecyclerView recyclerView;
    UserForRating u;
    ArrayList<UserForRating> UsersList; //רשימת של כל המשתמשים לפי הפריטים הנחוצים לדירוג
    ArrayList<UserForRating> NewUsersList = new ArrayList<UserForRating>();
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    RecyclerViewRatingAdapter adapter;//מתאם שתפקידו להציג רשימה של משתמשים

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_all_rating, container, false);// שם את הלייאוט של הפרגמנט בתוך המיכל בדף של הדירוגים
        super.onCreate(savedInstanceState);

        UsersList = new ArrayList<UserForRating>();

        recyclerView = root.findViewById(R.id.rwAllBin);
        adapter = new RecyclerViewRatingAdapter(this.getContext(), NewUsersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        showAllRatingList();
        return root;
    }

    public void showAllRatingList() { //פעולה ששולפת את כל המשתמשים הקיימים ממסד הנתונים שמה אותם ברשימה ומארגנת אותה מהמשתמש עם הכי הרבה ניקוד עד למשתמש עם הכי פחות ניקוד
        databaseReference = firebaseDatabase.getReference("Users");//הצבעה לענף של המשתמשים במסד הנתונים
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {//נכנס למסד הנתונים ועובר על כל ענף המשתמשים
                    if (singleSnapshot.getValue() != null) {
                        u = new UserForRating(singleSnapshot.child("name").getValue().toString(), singleSnapshot.child("email").getValue().toString() // מחלץ את הפרטים הנחוצים למשתמש בהצגת דירוג
                                , singleSnapshot.child("allbin").getValue(recyclebin.class), singleSnapshot.child("picName").getValue().toString())// במקרה הזה ישלוף את הפח
                        ;                                                                                                                //כי זהו דף המציג את הדירוג הכללי של כל הפחים יחד allBin
                        UsersList.add(u);

                    }
                }

                UserForRating UsersArray[] = new UserForRating[UsersList.size()];//  מערך שבעזרתו נסדר את הרשימה מהמשתמש שלו הכי הרבה נקודות עד למשתמש שלו הכי פחות נקודות
                for (int i = 0; i < UsersArray.length; i++) {
                    UsersArray[i] = UsersList.get(i);
                }

                for (int i = 0; i < UsersArray.length - 1; i++)//מיון בועות
                    for (int j = 0; j < UsersArray.length - i - 1; j++)
                        if (UsersArray[j].getBin().getPoints() < UsersArray[j + 1].getBin().getPoints()) {
                            // swap arr[j+1] and arr[j]
                            UserForRating temp = new UserForRating(UsersArray[j].getName(), UsersArray[j].getEmail(), UsersArray[j].getBin(), UsersArray[j].getPicName());
                            UsersArray[j] = UsersArray[j + 1];
                            UsersArray[j + 1] = temp;
                        }

                for (int i = 0; i < UsersArray.length; i++) {
                    NewUsersList.add(UsersArray[i]);
                }
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}