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

public class AdminHome extends AppCompatActivity {

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
    private Admin currentcompany;
    private ImageView btnadddcontract, btnrateemployee, btnviewapplicants;
    private Button btnviewcompanyreviews;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

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






        dbRef.child("Admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(uid)) {
                        currentcompany = child.getValue(Admin.class);
                        String currentName = currentcompany.getUsername();
                        tvWelcome.setText("Welcome " + currentName);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
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
                safepassapproval();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    public void safepassapproval()
    {

        Intent intent = new Intent(AdminHome.this, approve_safe_pass.class);
        startActivity(intent);


    }




}