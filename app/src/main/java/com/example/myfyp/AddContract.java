package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddContract extends AppCompatActivity
{
    private DatabaseReference dbRef,ref;
    private static final String Contract = "Contract";
    private FirebaseUser user;
    private FirebaseDatabase db;
    private String uid;
    private EditText etAddpositon,etAddaddress,etAddenddate,etAddstartdate,etAddendtime,etAddstarttime;
    private Button btnCreateContract;
    private Company currentcompany;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contract);

        db = FirebaseDatabase.getInstance();

        dbRef= FirebaseDatabase.getInstance().getReference(Contract);
        user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        ref=db.getReference();


        etAddpositon = findViewById(R.id.etAddposition);
        etAddaddress = findViewById(R.id.etAddAddress);
        etAddenddate = findViewById(R.id.etAddenddate);
        etAddstartdate = findViewById(R.id.etAddstartdate);
        etAddendtime = findViewById(R.id.etAddcontent);
        etAddstarttime = findViewById(R.id.etAddstarttime);

        btnCreateContract = findViewById(R.id.btnAddComment);

        btnCreateContract.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ref.child("Company").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        for (DataSnapshot child : children) {
                            if (child.getKey().equals(uid)) {
                                currentcompany = child.getValue(Company.class);
                                String currentName = currentcompany.getCompanyName();


                                String position = etAddpositon.getText().toString();
                                String address = etAddaddress.getText().toString();
                                String enddate = etAddenddate.getText().toString();
                                String startdate = etAddstartdate.getText().toString();
                                String endtime = etAddendtime.getText().toString();
                                String starttime = etAddstarttime.getText().toString();
                                String companyID = uid;
                                String userID = "";
                                String companyName = currentName;


                                Toast.makeText(AddContract.this,"Contract Created",Toast.LENGTH_SHORT).show();

                                String contractID = dbRef.push().getKey();
                                Contract contract = new Contract(position,address,startdate,enddate,starttime,endtime,userID,contractID,companyName,companyID);

                                dbRef.child(contractID).setValue(contract);

                                //String keyId = dbRef.push().getKey();
                                //dbRef.child(keyId).setValue(contract);

                                Intent intent = new Intent(AddContract.this,CompanyHomeActivity.class);
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