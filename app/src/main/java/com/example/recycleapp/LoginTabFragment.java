package com.example.recycleapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginTabFragment extends Fragment implements View.OnClickListener {
    EditText LEmail;
    EditText LPass;
    Button login;
    String password;
    String mail;
    float v=0;
    SharedPreferences sp;

    CheckBox rem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);
        LEmail=root.findViewById(R.id.Lmail);
        LPass=root.findViewById(R.id.Lpass);
        login=root.findViewById(R.id.Login);
        rem=root.findViewById(R.id.RememberMe);


        login.setOnClickListener(this);
        LEmail.setTranslationY(800);
        LPass.setTranslationY(800);
        login.setTranslationY(800);

        LEmail.setAlpha(v);
        LPass.setAlpha(v);
        login.setAlpha(v);

        LEmail.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        LPass.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();


        sp=this.getActivity().getSharedPreferences("Users", 0);
        // Inflate the layout for this fragment
        return root;


    }

    public void LogIn(){
        password=LPass.getText().toString();
        mail=LEmail.getText().toString();
        final ProgressDialog pd=ProgressDialog.show(getActivity(), "המתן אנא" ,"...מתחבר");
        pd.show();
        final FirebaseAuth Auth =FirebaseAuth.getInstance();
//בדיקה בפרופיל משתמשים
        Auth.signInWithEmailAndPassword(mail,
                password).addOnCompleteListener(getActivity(), new
                OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult>
                                                   task) {
                        if(task.isSuccessful()){//sign in db
                            Toast.makeText(getActivity(), "משתמש מחובר!",Toast.LENGTH_LONG).show();
                            pd.dismiss();
//מעבר לאקטיביטי אחר
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("Email", LEmail.getText().toString());
                            editor.putString("Pass", LPass.getText().toString());

                            editor.commit();
                            if(rem.isChecked()) //  אם משתמש מסמן זכור אותי ישמר משתנה בוליאני שערכו true
                            {
                                editor.putBoolean("isChecked", true);

                            }
                            editor.commit();
                            Intent in=new Intent(getActivity(), profile.class );
                            in.putExtra("email" , mail);
                            in.putExtra("pass", password);
                            startActivity(in);
//go to next page
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), ""+e.getMessage(),
                        Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });
    }


    @Override
    public void onClick(View view) {
        password=LPass.getText().toString();
        mail=LEmail.getText().toString();
        LogIn();
    }
}

