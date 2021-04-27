package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class EnlargeReferenceforcompany extends AppCompatActivity {
    private FirebaseUser user;
    private String uid;
    private ImageView imageView;
    private StorageReference referencepic;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String Passeduid;
    private Button btngoback;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarge_referenceforcompany);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        imageView = findViewById(R.id.imageViewforapprovedissaproveuser);
        btngoback = findViewById(R.id.btnenlargerefgoback);

        Passeduid = getIntent().getExtras().getString("userid");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();




        try
        {
            referencepic = storage.getReferenceFromUrl("gs://fypapp-8ebb3.appspot.com/images/refpicture" + Passeduid);

            File file2 = File.createTempFile("image", "jpeg");

            referencepic.getFile(file2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EnlargeReferenceforcompany.this, "Image failed to load", Toast.LENGTH_LONG).show();
                }
            });
        } catch(
                IOException e)

        {
            e.printStackTrace();
        }



        btngoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.company_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                homeview();
                return true;
            case R.id.item2:
                ViewAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void ViewAll()
    {

        Intent intent = new Intent(EnlargeReferenceforcompany.this, ViewAllContracts.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }
    public void homeview()
    {

        Intent intent = new Intent(EnlargeReferenceforcompany.this, CompanyHomeActivity.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }




}