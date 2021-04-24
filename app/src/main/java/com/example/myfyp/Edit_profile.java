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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
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
import java.util.ArrayList;

public class Edit_profile extends AppCompatActivity {

    private FirebaseDatabase db;
    private DatabaseReference dbRef,ref;
    private StorageReference profilepic;
    private StorageReference referencepic;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser user;
    private User currentuser;
    private static final String Skills = "Skills";
    private String uid;
    private Uri filepath;
    private User currentUser;
    private TextView tvName,tvNumber;
    private EditText etChangeName,etChangeNumber;
    private Button btnSaveChanges,btnenlarge,btnaddjob;
    private ImageView imageView,reference;
    private String picPath;
    private ArrayList<Float> overallratingList = new ArrayList<Float>();
    private Float overallaverage;
    private static final String[] paths = {"Plumber", "Electrician", "General Labourer","Block Layer","Builder","W Tractor License Holder","D1 Truck License Holder","Machine Operator","Excavator Driver"};
    private Spinner spinner;
    private RatingBar ratingbar;

    DatabaseReference c1v2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        //tvName=findViewById(R.id.tvCompanyNameprofile);
        //tvNumber=findViewById(R.id.tvCompanynumberdisplay);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        etChangeName = findViewById(R.id.etChnageName);
        etChangeNumber = findViewById(R.id.etChangeNumber);
        imageView = findViewById(R.id.userprofilepic);
        ratingbar = findViewById(R.id.ratingBarUser);
        reference = findViewById(R.id.imgreference);
        btnaddjob = findViewById(R.id.btnaddextraskills);
        btnenlarge = findViewById(R.id.btnenlarge);

        spinner = findViewById(R.id.spinneraddmoreskills);

        ref= FirebaseDatabase.getInstance().getReference(Skills);


        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item,paths);

        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(ad);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        profilepic = storage.getReference();








        db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef=db.getReference();
        uid=user.getUid();

        String currentuid = uid;

        gettingaverage();




        dbRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(uid)) {
                        currentUser = child.getValue(User.class);
                        String currentName = currentUser.getUsername();
                        String currentNumber = currentUser.getPhoneNo();
                        String link = currentUser.getPpurl();
                        etChangeName.setHint(currentName);
                        etChangeNumber.setHint(currentNumber);







                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        try
        {
            profilepic = storage.getReferenceFromUrl("gs://fypapp-8ebb3.appspot.com/images/profilepic" + currentuid);

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
                    Toast.makeText(Edit_profile.this,"Image failed to load",Toast.LENGTH_LONG).show();
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }




        try
        {
            referencepic = storage.getReferenceFromUrl("gs://fypapp-8ebb3.appspot.com/images/refpicture" + currentuid);

            File file2 = File.createTempFile("image","jpeg");

            referencepic.getFile(file2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                    reference.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Edit_profile.this,"Image failed to load",Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e)
        {
            e.printStackTrace();
        }




        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
        reference.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                choosePictureref();
            }
        });

        btnenlarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Edit_profile.this, EnlargedReference.class);
                startActivity(intent);


            }
        });

        btnaddjob.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dbRef.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        for (DataSnapshot child : children)
                        {
                            if (child.getKey().equals(uid)) {

                                currentUser = child.getValue(User.class);

                                String userid = uid;
                                String skill = String.valueOf(spinner.getSelectedItem());
                                String skillid = ref.push().getKey();


                                Toast.makeText(Edit_profile.this, "Skill Added", Toast.LENGTH_SHORT).show();

                                ExtraSkills contract = new ExtraSkills(userid, skill, skillid);

                                ref.child(skillid).setValue(contract);

                                //String keyId = dbRef.push().getKey();
                                //dbRef.child(keyId).setValue(contract);


                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });


        btnSaveChanges.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String newname = etChangeName.getText().toString();
                String newnumber = etChangeNumber.getText().toString();

                if(newname!=null)
                {
                    updateName(newname);
                }
                if (newnumber!=null)
                {
                    updateNumber(newnumber);
                }

                Intent intent = new Intent(Edit_profile.this, UserHomeActivity.class);
                startActivity(intent);


            }
        });


    }

    public void updateNumber(String newnumber){
        dbRef.child("user").child(uid).child("phoneNo").setValue(newnumber);
        etChangeNumber.setHint(newnumber);
        Toast.makeText(getApplicationContext(), "Number updated", Toast.LENGTH_SHORT).show();

    }
    public void updateName(String newname){
        dbRef.child("user").child(uid).child("username").setValue(newname);
        etChangeName.setHint(newname);
        Toast.makeText(getApplicationContext(), "Name updated", Toast.LENGTH_SHORT).show();

    }
    private void choosePicture()
    {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    private void choosePictureref()
    {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,2);
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
        if(requestCode==2 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            filepath = data.getData();


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                reference.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


            uploadrefpicture();
        }
    }


    private void uploadpicture()
    {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uplaoading Image..");
        pd.show();

        final String key = user.getUid();
        StorageReference ref = storageReference.child("images/profilepic" + key);
        picPath = ref.getPath();
        DatabaseReference c1v2= FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("ppurl");
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

    private void uploadrefpicture()
    {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uplaoading Image..");
        pd.show();

        final String key = user.getUid();
        StorageReference ref = storageReference.child("images/refpicture" + key);
        picPath = ref.getPath();
        DatabaseReference c1v2= FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("refurl");
        c1v2.setValue("gs://fypapp-8ebb3.appspot.com/" + picPath);

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
    public void loadpp() {

    }
    private void gettingaverage()
    {
        dbRef.child("EmployeeFeedback").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    EmployeeFeedback feedback = child.getValue(EmployeeFeedback.class);
                    if (feedback.getEmployeeid().equals(uid))
                    {

                        Float overall = Float.valueOf(feedback.getOverallrating());

                        overallratingList.add(overall);


                    }
                }

                averageoverall();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
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

        Intent intent = new Intent(Edit_profile.this, UserViewContracts.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }
    public void homeview()
    {

        Intent intent = new Intent(Edit_profile.this, UserHomeActivity.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }



    private void averageoverall()
    {
        float total = 0;
        float average;

        for(int i = 0; i<overallratingList.size(); i++)
        {
            float currentNo = overallratingList.get(i);
            total = currentNo + total;
            average = total / overallratingList.size();
            overallaverage = average;

        }

        ratingbar.setRating(overallaverage);



    }
}