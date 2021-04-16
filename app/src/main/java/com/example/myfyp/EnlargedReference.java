package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

public class EnlargedReference extends AppCompatActivity {
    private FirebaseUser user;
    private String uid;
    private ImageView imageView;
    private StorageReference referencepic;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarged_reference);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        imageView = findViewById(R.id.imageViewforapprovedissaproveuser);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();




        try
        {
            referencepic = storage.getReferenceFromUrl("gs://fypapp-8ebb3.appspot.com/images/refpicture" + uid);

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
                    Toast.makeText(EnlargedReference.this, "Image failed to load", Toast.LENGTH_LONG).show();
                }
            });
        } catch(
                IOException e)

        {
            e.printStackTrace();
        }


    }



}