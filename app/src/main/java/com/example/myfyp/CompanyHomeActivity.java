package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompanyHomeActivity extends AppCompatActivity
{

    ArrayList<Contract> allContracts = new ArrayList<Contract>();
    RecyclerView mRecyclerView;
    ActivejobAdapter myAdapter;
    private boolean isAdmin = false;

    private String status;

    private TextView tvWelcome;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private String uid;
    private Company currentcompany;
    private ImageView btnadddcontract, btnrateemployee, btnviewapplicants;
    private Button btnviewcompanyreviews;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_home);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        tvWelcome = findViewById(R.id.tvWelcome);

        db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef=db.getReference();
        uid=user.getUid();

        btnadddcontract = findViewById(R.id.btnaddjob);
        btnrateemployee = findViewById(R.id.btnrateemployee);
        btnviewapplicants = findViewById(R.id.btnapproveapplicant);
        btnviewcompanyreviews = findViewById(R.id.btnviewcompanyreviews);


        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        myAdapter = new ActivejobAdapter(allContracts,this::onContractClick);
        mRecyclerView.setAdapter(myAdapter);

        /*
        dbRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                status = dataSnapshot.child("Company").child(uid).child("isAdmin").getValue(String.class);
                if(status.equals("Yes"))
                {
                    isAdmin = true;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

         */


        dbRef.child("Company").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(uid)) {
                        currentcompany = child.getValue(Company.class);
                        String currentName = currentcompany.getCompanyName();
                        tvWelcome.setText("Welcome " + currentName);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        dbRef.child("ActiveContracts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Contract contract = child.getValue(Contract.class);
                    if (contract.getCompanyID().equals(uid))
                    {
                    allContracts.add(contract);

                    myAdapter.notifyItemInserted(allContracts.size() - 1);
                 }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });

        btnadddcontract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContract();
            }
        });
        btnviewapplicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveUser();
            }
        });
        btnviewcompanyreviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allReviews();
            }
        });

        btnrateemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                rateemployee();


            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.company_menu, menu);
        return true;
    }
    /*
    @Override
    public boolean onPrepareOptionsMenu (Menu menu)
    {
        if (isAdmin==false)
        {
            //menu.getItem(1).setEnabled(false);
            menu.getItem(6).setEnabled(false);
            //menu.getItem(2).setEnabled(false);
            //menu.getItem(4).setEnabled(false);
            // You can also use something like:
            // menu.findItem(R.id.example_foobar).setEnabled(false);
        }

        return true;
    }

     */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                homeCompany();
                return true;
            case R.id.item2:
                viewAllContracts();
                return true;
            case R.id.item4:
                editProfile();
                return true;
            case R.id.item5:
                allReviews();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void addContract()
    {

        Intent intent = new Intent(CompanyHomeActivity.this, AddContract.class);
        startActivity(intent);


    }
    public void rateemployee()
    {

        Intent intent = new Intent(CompanyHomeActivity.this, Company_view_previous_employee.class);
        startActivity(intent);


    }

    public void safepassapproval()
    {

        Intent intent = new Intent(CompanyHomeActivity.this, approve_safe_pass.class);
        startActivity(intent);


    }

    public void viewAllContracts()
    {

        Intent intent = new Intent(CompanyHomeActivity.this, ViewAllContracts.class);
        startActivity(intent);


    }

    public void editProfile()
    {

        Intent intent = new Intent(CompanyHomeActivity.this, company_edit_profile.class);
        startActivity(intent);


    }
    public void allReviews()
    {

        Intent intent = new Intent(CompanyHomeActivity.this, allReviews.class);
        startActivity(intent);


    }
    public void approveUser()
    {

        Intent intent = new Intent(CompanyHomeActivity.this, Company_consider_contract.class);
        startActivity(intent);


    }
    public void homeCompany()
    {

        Intent intent = new Intent(CompanyHomeActivity.this, CompanyHomeActivity.class);
        startActivity(intent);


    }



    public void onContractClick(int position)
    {
        allContracts.get(position);
        Intent intent = new Intent(CompanyHomeActivity.this,CurrentContract.class);
        intent.putExtra( "Position", position);
        //startActivity(intent);
    }



}
