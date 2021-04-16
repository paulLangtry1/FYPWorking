package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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

public class Company_rate_employee extends AppCompatActivity
{
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = db.getReference();
    private DatabaseReference ref = db.getReference();
    private DatabaseReference collective = db.getReference();
    //private DatabaseReference first = dbRef.child("Avatar").child("imageUrl");
    private FirebaseUser user;
    private static final String Feedback = "EmployeeFeedback";
    private static final String collectiveFeedback = "CollectiveEmployeeFeedback";
    private String uid;
    private User currentUser;
    private Contract currentcontract;
    private String contractid;
    private TextView tvCompanyName,tvPositionDisplay;
    private EditText etExperience,etPay,etDescribe;
    private Button btnSaveChanges;
    private ImageView imageView;
    private String adminuid,useruid;
    private StorageReference profilepic;
    private StorageReference storageReference;
    private Uri filepath;
    private FirebaseStorage storage;
    private RatingBar rateoveralluser,ratePunctuality,ratecommskills;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_rate_employee);


        tvCompanyName=findViewById(R.id.tvCompanyName);
        tvPositionDisplay=findViewById(R.id.tvPositionDisplay);

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        ref= FirebaseDatabase.getInstance().getReference(Feedback);
        collective = FirebaseDatabase.getInstance().getReference(collectiveFeedback);

        rateoveralluser = findViewById(R.id.rateoveralluser);
        ratePunctuality = findViewById(R.id.ratePunctuality);
        ratecommskills = findViewById(R.id.rateCommskills);

        imageView = findViewById(R.id.userprofilepic);


        contractid = getIntent().getExtras().getString("userid");


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        profilepic = storage.getReference();



        db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef=db.getReference();
        uid=user.getUid();

        dbRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    User contract = child.getValue(User.class);

                    if (contractid.equals(contract.getUserID()))
                    {
                        // tvCompanyName.setText(contract.get);
                        tvPositionDisplay.setText(contract.getEmail());
                        tvCompanyName.setText(contract.getUsername());


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        try
        {
            profilepic = storage.getReferenceFromUrl("gs://fypapp-8ebb3.appspot.com/images/profilepic" + contractid);

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
                    Toast.makeText(Company_rate_employee.this,"Image failed to load",Toast.LENGTH_LONG).show();
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }






        btnSaveChanges.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dbRef.child("user").addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        for (DataSnapshot child : children)
                        {
                            User contract = child.getValue(User.class);

                            if (contractid.equals(contract.getUserID()))
                            {
                                // tvCompanyName.setText(contract.get);

                                String overallrating = String.valueOf(rateoveralluser.getRating());
                                String punctuality = String.valueOf(ratePunctuality.getRating());
                                String communciation = String.valueOf(ratecommskills.getRating());
                                String empfeedbackid = dbRef.push().getKey();
                                String employeeid = contract.getUserID();


                                EmployeeFeedback feedback = new EmployeeFeedback(overallrating,punctuality,communciation,empfeedbackid,employeeid);
                                ref.child(empfeedbackid).setValue(feedback);

                                dbRef.child("CollectiveEmployeeFeedback").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Iterable<DataSnapshot> children = snapshot.getChildren();
                                        for (DataSnapshot child : children)
                                        {
                                            EmployeeFeedback feedbck = child.getValue(EmployeeFeedback.class);

                                            if (feedbck.getEmployeeid().equals(contractid))
                                            {
                                                Toast.makeText(Company_rate_employee.this, "Value already exists", Toast.LENGTH_LONG).show();

                                            }
                                            else
                                            {
                                                EmployeeFeedback feedback1 = new EmployeeFeedback(overallrating,punctuality,communciation,empfeedbackid,employeeid);
                                                collective.child(empfeedbackid).setValue(feedback1);




                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Intent intent = new Intent(Company_rate_employee.this,CompanyHomeActivity.class);
                                startActivity(intent);


                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }


        });

    }
}