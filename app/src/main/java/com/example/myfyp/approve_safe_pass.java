package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class approve_safe_pass extends AppCompatActivity implements userAdpater.OnContractListener
{
    ArrayList<User> allusers = new ArrayList<User>();

    private FirebaseDatabase database;
    private DatabaseReference ref;
    userAdpater myAdapter;
    private FirebaseUser user;
    RecyclerView mRecyclerView;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_safe_pass);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        myAdapter = new userAdpater(allusers,this::onContractClick);
        mRecyclerView.setAdapter(myAdapter);

        ref.child("SafePassRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    User user = child.getValue(User.class);
                    //  if (contract.getUID().equals(uid)) {
                    allusers.add(user);

                    myAdapter.notifyItemInserted(allusers.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });
    }

    @Override
    public void onContractClick(int position)
    {
        allusers.get(position);
        String userID = allusers.get(position).getUserID();

        Intent intent = new Intent(approve_safe_pass.this,Approve_current_safepass.class);
        intent.putExtra( "Value", userID);
        startActivity(intent);
    }

}