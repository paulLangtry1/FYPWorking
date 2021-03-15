package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CurrentContract extends AppCompatActivity {

    Button btnyes,btnno;
    TextView display;
    ArrayList<Contract> allContractsUser = new ArrayList<Contract>();
    private static final String Contract = "ContractHistory";
    private static final String ContractConsideration = "ContractConsideration";
    private FirebaseDatabase database;
    private DatabaseReference dbRef,dbrefAcceptance;
    DatabaseReference ref;
    MyAdapter myAdapter;
    private FirebaseUser user;
    RecyclerView mRecyclerView;
    private String contractuid;
    private String userid;
    private Contract currentcontract;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_contract);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        dbRef= FirebaseDatabase.getInstance().getReference(Contract);
        dbrefAcceptance = FirebaseDatabase.getInstance().getReference(ContractConsideration);


        contractuid = getIntent().getExtras().getString("Value");

        btnyes = findViewById(R.id.btnYes);
        btnno = findViewById(R.id.btnNo);
        display = findViewById(R.id.tvDisplayAccept);

        ref.child("Contract").addValueEventListener(new ValueEventListener() {
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
                            display.setText("Would you like to apply for position: " + positionName);


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
                ref.child("Contract").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        for (DataSnapshot child : children) {
                            Contract contract = child.getValue(Contract.class);
                           if(contract!=null)
                           {
                               if (contract.getContractID().equals(contractuid))
                               {

                                  // ref.child("Contract").child(contractuid).child("userID").setValue(userid);
                                   //contract.setUserID(userid);
                                   Toast.makeText(getApplicationContext(), "Contract Applied To", Toast.LENGTH_LONG).show();
                                   //myAdapter.notifyItemInserted(allContractsUser.size() - 1);

                                   String position = contract.getPosition();
                                   String address = contract.getAddress();
                                   String enddate = contract.getEnddate();
                                   String startdate = contract.getStartdate();
                                   String endtime = contract.getEndtime();
                                   String starttime = contract.getStarttime();
                                   String userID = userid;
                                   String county = contract.getCounty();
                                   String contractID = contract.getContractID();
                                   String companyName = contract.getCompanyName();
                                   String companyID = contract.getCompanyID();

                                   String keyid =  dbRef.push().getKey();


                                   Contract contracthistory = new Contract(position,address,county,startdate,enddate,starttime,endtime,userID,contractID,companyName,companyID);
                                   dbRef.child(keyid).setValue(contracthistory);

                                   Contract contractconsideration = new Contract(position,address,county,startdate,enddate,starttime,endtime,userID,contractID,companyName,companyID);
                                   dbrefAcceptance.child(keyid).setValue(contractconsideration);




                                   Intent intent = new Intent(CurrentContract.this,UserHomeActivity.class);
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
                Intent intent = new Intent(CurrentContract.this,UserHomeActivity.class);
                startActivity(intent);

            }
        });




    }
}