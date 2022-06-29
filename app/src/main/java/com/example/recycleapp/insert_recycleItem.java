package com.example.recycleapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//דף שבעזרתו מוסיפים פריטי מיחזור למסד הנתונים
public class insert_recycleItem extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_FROM_GALLERY = 1;
    EditText name;
    EditText kind;
    EditText value;

    ImageView pic;
    String picName;
    Uri uri;

    StorageReference mStorageRef;

    //ליצירת פריט מיחזור
    FirebaseAuth firebaseAuth;

    //לשם חיבור לבסיס נתונים
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    Button b;
    ProgressDialog p;

    //הצבעה לתת ענף של פריטי המיחזור
    DatabaseReference myref = firebaseDatabase.getReference("Recycle Items");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_recycle_item);

        name = findViewById(R.id.recycletemName);
        kind = findViewById(R.id.recycleItemKind);
        value= findViewById(R.id.recycletemValue);

        b = findViewById(R.id.sent);
        b.setOnClickListener(this);

        pic = findViewById(R.id.recycleItem_PIC);
        pic.setOnClickListener(this);

        //Write a message to the database
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void createRecycleItem()
    {
        p = new ProgressDialog(this);
        p.setMessage("entering recycle items...");
        p.show();

        int Pvalue = Integer.parseInt(value.getText().toString());

        //יצירת פרופיל פריט מיחזור לפי נתונים
        //יצירת אובייקט מסוג recycleItem
        recycleItem recycleItem = new recycleItem(
                name.getText().toString(),
                Pvalue,
                kind.getText().toString(),
                picName);

        myref.child(name.getText().toString()).setValue(recycleItem);
        //שמירת התמונה תתבצע תחת תיקיית
        // Image/recycleItem/recycle item name
        // התמונה נשמרת תחת השם של פריט המיחזור
        handlePermission();

        p.dismiss();
        Toast.makeText(insert_recycleItem.this,
                "הפריט הוכנס בהצלחה למאגר", Toast.LENGTH_LONG).show();
    }
    private void handlePermission()// 'פעולה שמעלה את התמונה לסטורג
    {
            mStorageRef = FirebaseStorage.getInstance()
                    .getReference("Image/recycleItem/" + name.getText().toString());
            mStorageRef = mStorageRef.child(picName);
            mStorageRef.putFile(uri).addOnSuccessListener
                    (new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            Toast.makeText(insert_recycleItem.this, "תמונה הועלתה",
                                    Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(insert_recycleItem.this,
                            "" + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }

    //בדיקה האם הנתונים עומדים בהגדרות הנדרשות – הגדרות המופיעות
    public boolean isValidate()
    {
        if (name.getText().toString() == null)
        {
            name.setError("הכנס שם לפריט מיחזור");
            name.setFocusable(true);
            return false;
        }
        else if (value.getText().toString() == null)
        {
            value.setError("הכנס ערך לפריט מיחזור");
            value.setFocusable(true);
            return false;
        }
        else if((kind.getText().toString() == null))
        {
            kind.setError("הכנס סוג פח");
            kind.setFocusable(true);
            return false;
        }
        return true;
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    @Override
    public void onClick(View view)
    {
        if (view == pic)
        {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            //startActivityForResult(Intent.createChooser(i, "select picture"), 100);
            someActivityResultLauncher.launch(i);
        }

        if (view == b)
        {
            createRecycleItem();
        }
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        // There are no request codes
                        Intent data = result.getData();
                        uri = data.getData();
                        if (uri != null)
                        {
                            pic.setImageURI(uri);

                            //מתן שם לתמונה
                            picName = System.currentTimeMillis() + "." + getFileExtension(uri);
                        }
                    }
                }
            });
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == 100 && data != null && data.getData() != null)
            {
                uri = data.getData();
                if (uri != null)
                {
                    pic.setImageURI(uri);

                    //מתן שם לתמונה
                    picName = System.currentTimeMillis() + "." + getFileExtension(uri);
                    //שמירת התמונה תתבצע תחת תיקיית
                    // Image/recycleItem/recycleItem name
                    // התמונה נשמרת תחת השם של פריט המיחזור
                    mStorageRef = FirebaseStorage.getInstance()
                            .getReference("Image/recycleItem/" + name.getText().toString());
                    mStorageRef = mStorageRef.child(picName);
                    mStorageRef.putFile(uri).addOnSuccessListener
                            (new OnSuccessListener<UploadTask.TaskSnapshot>()
                            {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    Toast.makeText(insert_recycleItem.this, "תמונה הועלתה",
                                            Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(insert_recycleItem.this,
                                    "" + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }
    }
}
