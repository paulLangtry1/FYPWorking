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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserViewContracts extends AppCompatActivity implements MyAdapter.OnContractListener
{
    ArrayList<Contract> allContractsUser = new ArrayList<Contract>();

    private FirebaseDatabase database;
    private DatabaseReference ref;
    MyAdapter myAdapter;
    private FirebaseUser user;
    RecyclerView mRecyclerView;
    private String uid;
    private String enddate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_contracts);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        enddate = getIntent().getExtras().getString("enddate");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        myAdapter = new MyAdapter(allContractsUser,this::onContractClick);
        mRecyclerView.setAdapter(myAdapter);

        ref.child("Contract").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Contract contract = child.getValue(Contract.class);
                    //  if (contract.getUID().equals(uid)) {
                    allContractsUser.add(contract);

                    myAdapter.notifyItemInserted(allContractsUser.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                homeview();
                return true;
            case R.id.item2:
                ViewAll();
                return true;
            case R.id.item4:
                editProfile();
                return true;
            case R.id.item7:
                chatForum();
                return true;
            case R.id.item8:
                maps();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void ViewAll()
    {

        Intent intent = new Intent(UserViewContracts.this, UserViewContracts.class);
       // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }
    public void homeview()
    {

        Intent intent = new Intent(UserViewContracts.this, UserHomeActivity.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }

    public void editProfile()
    {

        Intent intent = new Intent(UserViewContracts.this, Edit_profile.class);
        startActivity(intent);


    }

    public void chatForum()
    {

        Intent intent = new Intent(UserViewContracts.this, chatForum.class);
        startActivity(intent);


    }
    public void maps()
    {

        Intent intent = new Intent(UserViewContracts.this, MapsActivity.class);
        startActivity(intent);


    }


    @Override
    public void onContractClick(int position)
    {
        allContractsUser.get(position);
        String contractID = allContractsUser.get(position).getContractID();
        String startdate = allContractsUser.get(position).getStartdate();


        if(enddate==null)
        {
            Intent intent = new Intent(UserViewContracts.this,CurrentContract.class);
            intent.putExtra( "Value", contractID);
            startActivity(intent);
        }
        else
        {
            //current contracts starting date

            String[] parts = startdate.split("/");
            int startday = Integer.parseInt(parts[0]);
            int startmonth = Integer.parseInt(parts[1]);

            //active contract end date
            String[] parts2 = enddate.split("/");
            int endday = Integer.parseInt(parts2[0]);
            int endmon = Integer.parseInt(parts2[1]);



            if(startmonth>=endmon)
            {
                if(startmonth==endmon)
                {
                    if(endday>startday)
                    {

                    }
                    else
                    {
                        Intent intent = new Intent(UserViewContracts.this,CurrentContract.class);
                        intent.putExtra( "Value", contractID);
                        startActivity(intent);
                    }

                }
                else
                {
                    Intent intent = new Intent(UserViewContracts.this,CurrentContract.class);
                    intent.putExtra( "Value", contractID);
                    startActivity(intent);
                }


            }
            else
            {

            }

        }



    }
}