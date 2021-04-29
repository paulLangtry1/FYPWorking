package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class delete_skill extends AppCompatActivity {

    Button btnyes,btnno;
    ArrayList<ExtraSkills> allFeedback = new ArrayList<ExtraSkills>();
    TextView display;
    ArrayList<Contract> allContractsUser = new ArrayList<Contract>();
    private FirebaseDatabase database;
    private DatabaseReference dbRef,dbrefAcceptance;
    DatabaseReference ref;
    private FirebaseUser user;
    skillsAdapter skillsAdapter;
    RecyclerView mRecyclerView;
    private String contractuid;
    private String address,county;
    private String userid;
    private Contract currentcontract;
    private Button btncontractlocation;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_skill);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerviewcontract2);


        contractuid = getIntent().getExtras().getString("contractid");

        btnyes = findViewById(R.id.btnYes);
        btnno = findViewById(R.id.btnNo);
        display = findViewById(R.id.tvDisplayAccept);
        btncontractlocation = findViewById(R.id.btnviewlocation);




        ref.child("Skills").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    ExtraSkills contract = child.getValue(ExtraSkills.class);
                    if(contract!=null)
                    {
                        if (contract.getSkillid().equals(contractuid))
                        {
                            //allContractsUser.add(contract);
                            currentcontract = child.getValue(Contract.class);
                            String skill = contract.getSkill();

                            display.setText("Would you like to delete this skill: " + skill);


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

                ref.child("Skills").child(contractuid).removeValue();
                Toast.makeText(getApplicationContext(), "Skill deleted", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(delete_skill.this,Edit_profile.class);
                startActivity(intent);


            }
        });


        btnno.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "Skill not deleted", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(delete_skill.this,Edit_profile.class);
                startActivity(intent);

            }
        });





    }

    private void onContractClick(int i)
    {

    }

    @Override
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

        Intent intent = new Intent(delete_skill.this, UserViewContracts.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }
    public void homeview()
    {

        Intent intent = new Intent(delete_skill.this, UserHomeActivity.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }
}