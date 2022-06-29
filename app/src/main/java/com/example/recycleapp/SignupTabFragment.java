package com.example.recycleapp;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executor;
//דף ההרשמה
public class SignupTabFragment extends Fragment implements View.OnClickListener {
    EditText Name;
    EditText Email;
    EditText Pass;
    EditText CPass;
    Button signup;
    float v=0;

    ImageView pic;
    String picName;
    Uri uri;

    StorageReference mStorageRef;


    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    ProgressDialog p;
    DatabaseReference myref=firebaseDatabase.getReference("Users");

    final int FROM_GALLERY =1;
    final int FROM_CAMERA =2;
    int flag=0;
    byte[] bytes;
    boolean f1=false;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    AlertDialog.Builder adb;
    AlertDialog ad;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);
        Name=root.findViewById(R.id.name);
        Email=root.findViewById(R.id.mail);
        Pass=root.findViewById(R.id.pass);
        CPass=root.findViewById(R.id.Cpass);
        signup=root.findViewById(R.id.signup);

        pic = root.findViewById(R.id.uPic);
        pic.setOnClickListener(this);


        Name.setTranslationY(800);
        Email.setTranslationY(800);
        Pass.setTranslationY(800);
        CPass.setTranslationY(800);
        signup.setTranslationY(800);

        firebaseAuth = FirebaseAuth.getInstance();
        signup.setOnClickListener(this);

        Name.setAlpha(v);
        Email.setAlpha(v);
        Pass.setAlpha(v);
        CPass.setAlpha(v);
        signup.setAlpha(v);

        Name.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        Email.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        Pass.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        CPass.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        signup.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();

        // Inflate the layout for this fragment
        return root;

    }

    public void createUser(){

        if(isValidate())

        p= new ProgressDialog(getContext());
        p.setMessage("Registration...");
        p.show();
//יצירת פרופיל משתמש לפי אימייל וסיסמה
            firebaseAuth.createUserWithEmailAndPassword(Email.getText().toString
                    (), Pass.getText().toString()).addOnCompleteListener( getActivity(),
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull
                                                       Task<AuthResult> task) {
                            if (task.isSuccessful()){
//יצירת אובייקט מסוג User
                                User u = new
                                        User(myref.push().getKey(), Name.getText().toString(), Email.getText().toString(), Pass.getText().toString(), picName);

                                myref.child(u.getKey()).setValue(u);


                                handlePermission();
                                p.dismiss();
                                Toast.makeText(getActivity(),"ההרשמה הצליחה", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getActivity(),
                            e.getMessage(), Toast.LENGTH_LONG).show();
                    p.dismiss();
                }
            });
    }




    public boolean isValidate(){//פעולה שבודקת את תקינות הפרטים שהזין המשתמש בהרשמה
        if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()){ //בדיקה האם האימייל עומד בהגדרות הנדרשות – הגדרות המופיעות באנדרואיד
            Email.setError("Invalid email");
            Email.setFocusable(true);
            return false;
        }
        else if (Pass.getText().toString().length()<6){//אם אורך הסיסמה לפחות 6 תווים
            Pass.setError("password length at least 6 characters");
            Pass.setFocusable(true);
            return false;
        }
        else if (! ( (Pass.getText().toString() ).equals( CPass.getText().toString() )) ){//בדיקה האם הסיסמאות שהוזנו זהות- בסיסמה ובאימות סיסמה
            CPass.setError("הסיסמאות שהזנת אינן זהות");
            CPass.setFocusable(true);
            return false;
        }
        else if(Name.getText().toString()==null)//בדיקה האם לא הוזנה סיסמה
        {
            Name.setError("pls enter password");
            Name.setFocusable(true);
            return false;
        }
        else if(picName == null)//בדיקה האם לא נבחרה תמונת פרופיל
        {
            Toast.makeText(getActivity(), "pls enter pic", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view==signup) {
            createUser();
        }
        if (pic == view)
        {
            adb = new AlertDialog.Builder(getActivity());
            adb.setTitle("בחירת תמונה");
            adb.setMessage("אתה הולך לבחור תמונה מהגלריה או מהמצלמה");
            adb.setCancelable(true);
            adb.setPositiveButton("גלריה", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface d, int i) {
                    Intent intent = new Intent();
                    intent.setType("image/*");

                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    activityResultLauncher.launch(intent);
                    //  startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);

                }
            });
            adb.setNeutralButton("מצלמה", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface d, int i) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                            && checkSelfPermission(getActivity(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        if (checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        }
                    } else {
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        activityResultLauncher.launch(intent);
                    }
                }
            });
            ad = adb.create();
            ad.show();
        }

    }

    private void handlePermission()//'פעולה שמעלה את התמונה לסטורג
    {

        mStorageRef = FirebaseStorage.getInstance()
                .getReference("Image/users/" + Email.getText().toString());
        mStorageRef = mStorageRef.child(picName);

        if (flag == FROM_GALLERY) {
            mStorageRef.putFile(uri).addOnSuccessListener
                    (new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "תמונה הועלתה מהגלריה",
                                    Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(),
                            "" + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            StorageTask<UploadTask.TaskSnapshot> uploadTask = mStorageRef.putBytes(bytes).addOnSuccessListener
                    (new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "תמונה הועלתה מהמצלמה",
                                    Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(),
                            "" + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    ActivityResultLauncher activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getData()!=null && result.getData().getAction()==null &&result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                uri = data.getData();
                if(uri != null) {
                    pic.setImageURI(uri);
                    picName = System.currentTimeMillis() + "." + getFileExtension(uri);
                }
                flag=FROM_GALLERY;
                f1= true;
            }

            else if (result.getData()!=null   && result.getResultCode() == Activity.RESULT_OK) {
                Toast.makeText(getActivity(),result.getData().getData()+" ,",Toast.LENGTH_LONG).show();
                Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");

                picName=System.currentTimeMillis() + "."+ "jpg";
                pic.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                bytes = baos.toByteArray();
                flag=FROM_CAMERA;
                f1= true;
            }
        }
    });

}
