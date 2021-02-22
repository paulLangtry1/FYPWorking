package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;

public class UserHomeActivity extends AppCompatActivity
{
    private EditText etSearch;
    private Button btnSearch,btnViewall,btnAddContract,btnViewUpdate,btnverify,btnmaps;
    private TextView tvWelcome,tvstatus;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private String status;
    private String uid;
    private String jobaccepted;
    Boolean pending = false;
    Boolean verified = false;
    Boolean virginAccount = false;
    Boolean accepted = false;
    private User currentuser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);




        btnAddContract = findViewById(R.id.btnAddComment);

        tvWelcome = findViewById(R.id.tvWelcome);
        tvstatus = findViewById(R.id.tvPending);


        db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef=db.getReference();
        uid=user.getUid();

        DatabaseReference c1v2= FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("userID");
        c1v2.setValue(uid);




        dbRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                status = dataSnapshot.child("user").child(uid).child("status").getValue(String.class);
                if(status.equals("Pending"))
                {
                    pending = true;
                }

                jobaccepted = dataSnapshot.child("user").child(uid).child("hasjobaccepted").getValue(String.class);
                if(jobaccepted.equals("Yes"))
                {
                    accepted = true;
                }
                if(status.equals("Approved"))
                {
                    verified=true;
                }
                if(status.equals("Not Verified"))
                {
                    virginAccount = true;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        dbRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    if (child.getKey().equals(uid)) {
                        currentuser = child.getValue(User.class);
                        String currentName = currentuser.getUsername();
                        tvWelcome.setText("Welcome " + currentName + "!");
                        tvstatus.setText(currentuser.getStatus());

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
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if (accepted==true)
        {
            //menu.getItem(1).setEnabled(false);
            menu.getItem(2).setEnabled(false);
            //menu.getItem(2).setEnabled(false);
            //menu.getItem(4).setEnabled(false);
            // You can also use something like:
            // menu.findItem(R.id.example_foobar).setEnabled(false);
        }
        else if (pending == true)
        {
            menu.getItem(1).setEnabled(false);
            menu.getItem(2).setEnabled(false);

        }
        else if (verified == true)
        {
            menu.getItem(2).setEnabled(false);
        }
        else if(virginAccount == true)
        {
            menu.getItem(1).setEnabled(false);
            menu.getItem(4).setEnabled(false);
        }
        return true;

        //verified==false || pending==true ||

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                return true;
            case R.id.item2:
                ViewAll();
                return true;
            case R.id.item3:
                verify();
                return true;
            case R.id.item4:
                editProfile();
                return true;
            case R.id.item5:
                jobHistory();
                return true;
            case R.id.item6:
                allReviews();
                return true;
            case R.id.item7:
                chatForum();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void ViewAll()
    {

        Intent intent = new Intent(UserHomeActivity.this, UserViewContracts.class);
        startActivity(intent);


    }
    public void allReviews()
    {

        Intent intent = new Intent(UserHomeActivity.this, allReviews.class);
        startActivity(intent);


    }

    public void verify()
    {

        Intent intent = new Intent(UserHomeActivity.this, verifyActivity.class);
        startActivity(intent);


    }
    public void editProfile()
    {

        Intent intent = new Intent(UserHomeActivity.this, Edit_profile.class);
        startActivity(intent);


    }
    public void jobHistory()
    {

        Intent intent = new Intent(UserHomeActivity.this, UserContractHistory.class);
        startActivity(intent);


    }
    public void chatForum()
    {

        Intent intent = new Intent(UserHomeActivity.this, chatForum.class);
        startActivity(intent);


    }
    public void maps()
    {

        Intent intent = new Intent(UserHomeActivity.this, verifyActivity.class);
        startActivity(intent);


    }
}