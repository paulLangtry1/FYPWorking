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
import android.widget.ImageView;
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
import java.util.UUID;

public class verifyActivity extends AppCompatActivity
{
    private ImageView displayImageView;
    private Uri filepath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Button btnconfirmimage;
    private static final String SAFE = "SafePassRequest";
    private FirebaseDatabase database;
    private DatabaseReference dbRef,safepassreq;
    DatabaseReference ref;
    private FirebaseUser user;
    private String uid;
    private User currentUser;
    String picPath;
    private DatabaseReference c1v2;
    private StorageReference safepass;
    private static final String User = "UserVerifiedStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        dbRef = FirebaseDatabase.getInstance().getReference(User);
        safepassreq= FirebaseDatabase.getInstance().getReference(SAFE);

        displayImageView = findViewById(R.id.displayImageView);
        btnconfirmimage = findViewById(R.id.btnconfirmimage);
        uid = user.getUid();
        String currentuid = uid;


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        try
        {
            safepass = storage.getReferenceFromUrl("gs://fypapp-8ebb3.appspot.com/images/safepasspic" + currentuid);

            File file =    File.createTempFile("image","jpeg");

            safepass.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    displayImageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(verifyActivity.this,"Image failed to load",Toast.LENGTH_LONG).show();
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

        displayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }

        });


        btnconfirmimage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                DatabaseReference c1v2= FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("status");
                c1v2.setValue("Pending");

                ref.child("user").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        for (DataSnapshot child : children) {


                                if (child.getKey().equals(uid))
                                {
                                    User user = child.getValue(User.class);
                                    // ref.child("Contract").child(contractuid).child("userID").setValue(userid);
                                    //contract.setUserID(userid);
                                    Toast.makeText(getApplicationContext(), "Contract Accepted", Toast.LENGTH_LONG).show();
                                    //myAdapter.notifyItemInserted(allContractsUser.size() - 1);

                                    String email =user.getEmail();
                                    String username = user.getUsername();
                                    String password = user.getPassword();
                                    String phoneNo = user.getPhoneNo();
                                    String ppurl = user.getPpurl();
                                    String refurl = user.getRefurl();
                                    String safepassurl = user.getSafepassurl();
                                    String status = user.getStatus();
                                    String hasjobaccepted = user.getHasjobaccepted();
                                    String userid = user.getUserID();

                                    String keyid =  dbRef.push().getKey();


                                    User SafePassRequest = new User(email,password,username,phoneNo,ppurl,refurl,safepassurl,status, hasjobaccepted,userid);
                                    safepassreq.child(keyid).setValue(SafePassRequest);



                                    Intent intent = new Intent(verifyActivity.this, UserHomeActivity.class);
                                    // intent.putExtra("pending", pending);

                                    startActivity(intent);

                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });





            }








        });
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
                displayImageView.setImageBitmap(bitmap);
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
        StorageReference ref = storageReference.child("images/safepasspic" + key);
        picPath = ref.getPath();
        DatabaseReference c1v2= FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("safepassurl");
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
