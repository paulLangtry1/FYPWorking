package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Approve_current_safepass extends AppCompatActivity {

    Button btnyes,btnno;
    TextView display,tvusername;
    ArrayList<Contract> allContractsUser = new ArrayList<Contract>();
    private FirebaseDatabase database;
    private DatabaseReference dbRef,dbrefAcceptance;
    DatabaseReference ref;
    MyAdapter myAdapter;
    private FirebaseUser user;
    RecyclerView mRecyclerView;
    private StorageReference safepass;
    private ImageView  imgview;
    private FirebaseStorage storage;

    private String userid;
    private User currentuser;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_current_safepass);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        storage = FirebaseStorage.getInstance();


        userid = getIntent().getExtras().getString("Value");
        imgview = findViewById(R.id.imagesafepassapprove);

        btnyes = findViewById(R.id.btnYes);
        btnno = findViewById(R.id.btnNo);
        display = findViewById(R.id.tvDisplayAccept);
        tvusername = findViewById(R.id.tvsafepassuser);

        try
        {
            safepass = storage.getReferenceFromUrl("gs://fypapp-8ebb3.appspot.com/images/safepasspic" + userid);

            File file =    File.createTempFile("image","jpeg");

            safepass.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imgview.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Approve_current_safepass.this,"Image failed to load",Toast.LENGTH_LONG).show();
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

        ref.child("SafePassRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    User user = child.getValue(User.class);
                    if(user!=null)
                    {
                        if (user.getUserID().equals(userid))
                        {
                            //allContractsUser.add(contract);
                            currentuser = child.getValue(User.class);
                            String Name = user.getUsername();
                            tvusername.setText(Name);
                            display.setText("Would you like to approve: " + Name);


                        }
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


        btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                DatabaseReference c1v2= FirebaseDatabase.getInstance().getReference().child("user").child(userid).child("status");
                c1v2.setValue("Approved");




                Intent intent = new Intent(Approve_current_safepass.this,CompanyHomeActivity.class);
                startActivity(intent);

            }







        });


        btnno.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "User Declined", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Approve_current_safepass.this,CompanyHomeActivity.class);
                startActivity(intent);

            }
        });




    }
}