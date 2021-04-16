package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class company_edit_profile extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private StorageReference profilepic;
    private StorageReference referencepic;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser user;
    private String uid;
    private Uri filepath;
    private Company currentUser;
    private TextView tvName,tvNumber,tvAddress;
    private EditText etChangeName,etChangeNumber,etChangeaddress;
    private Button btnSaveChanges;
    private ImageView imageView;
    private String picPath;
    DatabaseReference c1v2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_edit_profile);


        tvName=findViewById(R.id.tvCompanyNameprofile);
        tvNumber=findViewById(R.id.tvCompanynumberdisplay);
        tvAddress=findViewById(R.id.tvcompanyaddress);

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        etChangeName = findViewById(R.id.etChnageName);
        etChangeNumber = findViewById(R.id.etChangeNumber);
        etChangeaddress = findViewById(R.id.etchangeaddress);
        imageView = findViewById(R.id.userprofilepic);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        profilepic = storage.getReference();







        db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef=db.getReference();
        uid=user.getUid();

        String currentuid = uid;




        dbRef.child("Company").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(uid)) {
                        currentUser = child.getValue(Company.class);
                        String currentName = currentUser.getCompanyName();
                        String currentaddress = currentUser.getAddress();
                        String phoneNo = currentUser.getPhoneNo();
                        //String link = currentUser.getPpurl();
                        tvName.setText(currentName);
                        tvNumber.setText(phoneNo);
                        tvAddress.setText(currentaddress);






                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        try
        {
            profilepic = storage.getReferenceFromUrl("gs://fypapp-8ebb3.appspot.com/images/Companyprofilepic" + currentuid);

            File file =    File.createTempFile("image","jpeg");

            profilepic.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(company_edit_profile.this,"Image failed to load",Toast.LENGTH_LONG).show();
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }








        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });



        btnSaveChanges.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String newaddress = etChangeaddress.getText().toString();
                String newnumber = etChangeNumber.getText().toString();

                if(newaddress!=null)
                {
                    updateAddress(newaddress);
                }
                if (newnumber!=null)
                {
                    updateNumber(newnumber);
                }

                Intent intent = new Intent(company_edit_profile.this, CompanyHomeActivity.class);
                startActivity(intent);


            }
        });


    }

    public void updateNumber(String newnumber){
        dbRef.child("Company").child(uid).child("phoneNo").setValue(newnumber);
        tvNumber.setText(newnumber);
        Toast.makeText(getApplicationContext(), "Number updated", Toast.LENGTH_SHORT).show();

    }
    public void updateAddress(String newaddress){
        dbRef.child("Company").child(uid).child("address").setValue(newaddress);
        tvAddress.setText(newaddress);
        Toast.makeText(getApplicationContext(), "Address updated", Toast.LENGTH_SHORT).show();

    }
    private void choosePicture()
    {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            filepath = data.getData();



            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


            uploadpicture();
        }

    }


    private void uploadpicture()
    {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uplaoading Image..");
        pd.show();

        final String key = user.getUid();
        StorageReference ref = storageReference.child("images/Companyprofilepic" + key);
        picPath = ref.getPath();
        DatabaseReference c1v2= FirebaseDatabase.getInstance().getReference().child("Company").child(uid).child("ppurl");
        c1v2.setValue("gs://fypapp-8ebb3.appspot.com" + picPath);

        ref.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Snackbar.make(findViewById(android.R.id.content),"Image Uploaded",Snackbar.LENGTH_LONG).show();


                        pd.dismiss();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        pd.dismiss();
                        // ...
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progresspercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Percentage: " + (int)progresspercent + "%");
            }
        });




    }


}