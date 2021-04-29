package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class edit_contract_company extends AppCompatActivity {
    private FirebaseDatabase db;
    private DatabaseReference dbRef,ref;
    private StorageReference profilepic;
    private StorageReference referencepic;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseUser user;
    private User currentuser;
    private static final String pSkills = "Preferred Skills";
    private String uid;
    private Uri filepath;
    private Company currentUser;
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
    private String contractid;

    DatabaseReference c1v2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contract_company);


        //tvName=findViewById(R.id.tvCompanyNameprofile);
        //tvNumber=findViewById(R.id.tvCompanynumberdisplay);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        etChangeName = findViewById(R.id.etedittitle);
        etChangeNumber = findViewById(R.id.etaddressdetail);
        imageView = findViewById(R.id.compprofilepic);
        ratingbar = findViewById(R.id.ratingBarUser);
        reference = findViewById(R.id.imgreference);
        btnaddjob = findViewById(R.id.btnaddextraskills);
        btnenlarge = findViewById(R.id.btnenlarge);

        spinner = findViewById(R.id.spinneraddmoreskills);

        ref= FirebaseDatabase.getInstance().getReference(pSkills);


        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item,paths);

        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(ad);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        profilepic = storage.getReference();




        contractid = getIntent().getExtras().getString("contractid");



        db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef=db.getReference();
        uid=user.getUid();

        String currentuid = uid;





        dbRef.child("Contract").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Contract contract = child.getValue(Contract.class);
                    if (contract.getContractID().equals(contractid))
                    {

                        String currenttitle = contract.getPosition();
                        String currentaddress = contract.getAddress();

                        etChangeName.setHint(currenttitle);
                        etChangeNumber.setHint(currentaddress);







                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) spinner.getSelectedView()).setTextColor(Color.WHITE);
            }
        });








        btnaddjob.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dbRef.child("Contract").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        for (DataSnapshot child : children)
                        {
                            Contract contract =  child.getValue(Contract.class);
                            if (contract.getContractID().equals(contractid))
                            {


                                String userid = contractid;
                                String skill = String.valueOf(spinner.getSelectedItem());
                                String skillid = ref.push().getKey();


                                Toast.makeText(edit_contract_company.this, "Preferred Skill Added", Toast.LENGTH_SHORT).show();

                                ExtraSkills contract22 = new ExtraSkills(userid, skill, skillid);

                                ref.child(skillid).setValue(contract22);

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


                Intent intent = new Intent(edit_contract_company.this, CompanyHomeActivity.class);
                startActivity(intent);


            }
        });


    }




    @Override
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

        Intent intent = new Intent(edit_contract_company.this, ViewAllContracts.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }
    public void homeview()
    {

        Intent intent = new Intent(edit_contract_company.this, CompanyHomeActivity.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }
}