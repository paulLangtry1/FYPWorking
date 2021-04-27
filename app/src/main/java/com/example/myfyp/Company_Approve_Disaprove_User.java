package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
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
import java.util.ArrayList;

public class Company_Approve_Disaprove_User extends AppCompatActivity {

    Button btnyes,btnno,btnenlargerefforcompany,btnviewskills;
    TextView display,tvusername,tvemail,tvphone,tvstatus;
    ArrayList<Contract> allContractsUser = new ArrayList<Contract>();
    private static final String Contract = "ContractHistory";
    private static final String ContractConsideration = "ContractConsideration";
    private static final String ActiveContracts = "ActiveContracts";
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    ArrayList<ExtraSkills> allFeedback = new ArrayList<ExtraSkills>();
    private  DatabaseReference c1v2;
    private DatabaseReference dbRef,dbrefAcceptance,dbrefactivecontracts;
    DatabaseReference ref;
    MyAdapter myAdapter;
    private StorageReference profilepic;
    private StorageReference referencepic;
    private ArrayList<Float> overallratingList = new ArrayList<Float>();
    private ArrayList<String> skillsarraylist = new ArrayList<String>();
    private FirebaseUser user;
    RecyclerView mRecyclerView;
    private String contractuid;
    private RatingBar ratingbar;
    private String userid;
    private ImageView imgview,referenceimg,imgviewqmark;
    private Contract currentcontract;
    private User currentuser;
    private String usersid;
    private String usersIDtoapply;
    private String contractsidrepresenatation;
    private String tempcontractid;
    private Float overallaverage;
    RecyclerView m2RecyclerView;
    skillsAdapter skillsAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company__approve__disaprove__user);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        dbRef= FirebaseDatabase.getInstance().getReference(Contract);
        dbrefAcceptance = FirebaseDatabase.getInstance().getReference(ContractConsideration);
        dbrefactivecontracts = FirebaseDatabase.getInstance().getReference(ActiveContracts);

        storage = FirebaseStorage.getInstance();
        profilepic = storage.getReference();



        contractuid = getIntent().getExtras().getString("contractID");
        tempcontractid = getIntent().getExtras().getString("tempcontractid");

        btnyes = findViewById(R.id.btnYes);
        btnno = findViewById(R.id.btnNo);
        display = findViewById(R.id.tvDisplayAccept);
        tvemail = findViewById(R.id.tvappremail);
        tvphone = findViewById(R.id.tvapprphone);
        tvstatus = findViewById(R.id.tvapprstatus);
        tvusername = findViewById(R.id.tvsafepassuser);
        imgview = findViewById(R.id.imageViewapprove);
        referenceimg = findViewById(R.id.imagesafepassapprove);
        btnenlargerefforcompany = findViewById(R.id.btnenlargerefcompany);
        btnviewskills = findViewById(R.id.btnviewskillscomp);
        imgviewqmark = findViewById(R.id.imgviewqmark);

        ratingbar = findViewById(R.id.ratingBarcompviewuser);



        ref.child("ContractConsideration").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Contract contract = child.getValue(Contract.class);
                    if(contract!=null)
                    {
                        if (contract.getContractID().equals(contractuid))
                        {
                            //allContractsUser.add(contract);
                            currentcontract = child.getValue(Contract.class);
                            String positionName = contract.getPosition();
                            String userid = contract.getUserID();
                            usersIDtoapply=userid;
                            contractsidrepresenatation = contract.getContractID();


                            ref.child("user").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Iterable<DataSnapshot> children = snapshot.getChildren();
                                    for (DataSnapshot child : children) {
                                        User user = child.getValue(User.class);
                                        if(user!=null)
                                        {
                                            if (child.getKey().equals(userid))
                                            {
                                                try
                                                {
                                                    profilepic = storage.getReferenceFromUrl("gs://fypapp-8ebb3.appspot.com/images/profilepic" + userid);

                                                    File file =    File.createTempFile("image","jpeg");

                                                    profilepic.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
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
                                                            Toast.makeText(Company_Approve_Disaprove_User.this,"Image failed to load",Toast.LENGTH_LONG).show();
                                                        }
                                                    });


                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }


                                                try
                                                {
                                                    referencepic = storage.getReferenceFromUrl("gs://fypapp-8ebb3.appspot.com/images/refpicture" + userid);

                                                    File file2 = File.createTempFile("image","jpeg");

                                                    referencepic.getFile(file2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
                                                    {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                                                        {
                                                            Bitmap bitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                                                            referenceimg.setImageBitmap(bitmap);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(Company_Approve_Disaprove_User.this,"Image failed to load",Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                } catch (IOException e)
                                                {
                                                    e.printStackTrace();
                                                }





                                                //allContractsUser.add(contract);
                                                currentuser = child.getValue(User.class);
                                                String username = currentuser.getUsername();
                                                String email = currentuser.getEmail();
                                                String phoneNo = currentuser.getPhoneNo();
                                                String status = currentuser.getStatus();
                                                usersid = currentuser.getUserID();

                                                tvstatus.setText(status);
                                                tvphone.setText(phoneNo);
                                                tvemail.setText(email);
                                                tvusername.setText(username);


                                                display.setText("Would you like to accept this applicant: " + username);


                                            }
                                        }

                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }


                            });


                        }



                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });
        gettingaverage();










        btnenlargerefforcompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Company_Approve_Disaprove_User.this,EnlargeReferenceforcompany.class);
                intent.putExtra("userid",usersIDtoapply);
                startActivity(intent);
            }
        });

        btnviewskills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Company_Approve_Disaprove_User.this,view_skills.class);
                intent.putExtra("userid",usersIDtoapply);
                startActivity(intent);

            }

        });


        btnyes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DatabaseReference c1v2= FirebaseDatabase.getInstance().getReference().child("Contract").child(contractsidrepresenatation).child("userID");
                c1v2.setValue(usersIDtoapply);

                DatabaseReference c1v3= FirebaseDatabase.getInstance().getReference().child("user").child(usersIDtoapply).child("hasjobaccepted");
                c1v3.setValue("Yes");

                ref.child("Contract").addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        for (DataSnapshot child : children) {
                            Contract contract = child.getValue(Contract.class);
                            if (contract != null) {
                                if (contract.getContractID().equals(contractuid)) {
                                    //allContractsUser.add(contract);
                                    currentcontract = child.getValue(Contract.class);
                                   ref.child("Contract").child(contractuid).removeValue();
                                   ref.child("ContractConsideration").child(tempcontractid).removeValue();
                                   Toast.makeText(Company_Approve_Disaprove_User.this, "Contract removed from marketplace",Toast.LENGTH_LONG).show();

                                    String position = contract.getPosition();
                                    String address = contract.getAddress();
                                    String enddate = contract.getEnddate();
                                    String startdate = contract.getStartdate();
                                    String county = contract.getCounty();
                                    String endtime = contract.getEndtime();
                                    String starttime = contract.getStarttime();
                                    String userID = contract.getUserID();

                                    String companyName = contract.getCompanyName();
                                    String companyID = contract.getCompanyID();
                                    String sector = contract.getSector();


                                    String keyid =  dbRef.push().getKey();
                                    String contractID = keyid;

                                    Contract activecontracts = new Contract(position,address,county,startdate,enddate,starttime,endtime,userID,contractID,companyName,companyID,sector);
                                    dbrefactivecontracts.child(keyid).setValue(activecontracts);

                                    Intent intent = new Intent(Company_Approve_Disaprove_User.this, CompanyHomeActivity.class);
                                    startActivity(intent);

                                }
                            }




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });






    }
});
        btnno.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "Contract Declined", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Company_Approve_Disaprove_User.this,CompanyHomeActivity.class);
                startActivity(intent);

            }
        });


        imgviewqmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(Company_Approve_Disaprove_User.this).setTitle("Average Rating").setMessage("This is the workers current average rating from previous employers").show();

            }
        });


    }
    private void gettingaverage()
    {
        ref.child("EmployeeFeedback").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    EmployeeFeedback feedback = child.getValue(EmployeeFeedback.class);
                    if (feedback.getEmployeeid().equals(usersIDtoapply))
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

        Intent intent = new Intent(Company_Approve_Disaprove_User.this, ViewAllContracts.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }
    public void homeview()
    {

        Intent intent = new Intent(Company_Approve_Disaprove_User.this, CompanyHomeActivity.class);
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