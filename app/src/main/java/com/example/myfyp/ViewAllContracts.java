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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllContracts extends AppCompatActivity implements MyAdapter.OnContractListener
{
    ArrayList<Contract> allContracts = new ArrayList<Contract>();

    private FirebaseDatabase database;
    private DatabaseReference ref;
    MyAdapter myAdapter;
    private FirebaseUser user;
    RecyclerView mRecyclerView;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_contracts);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        myAdapter = new MyAdapter(allContracts,this::onContractClick);
        mRecyclerView.setAdapter(myAdapter);

        ref.child("Contract").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Contract contract = child.getValue(Contract.class);
                  //  if (contract.getUID().equals(uid)) {
                        allContracts.add(contract);

                    myAdapter.notifyItemInserted(allContracts.size() - 1);
                }
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

        Intent intent = new Intent(ViewAllContracts.this, ViewAllContracts.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }
    public void homeview()
    {

        Intent intent = new Intent(ViewAllContracts.this, CompanyHomeActivity.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }
    @Override
    public void onContractClick(int position)
    {
        String contractid = allContracts.get(position).getContractID();
        String contractscompid = allContracts.get(position).getCompanyID();

        if(contractscompid.equals(uid))
        {
            Intent intent = new Intent(ViewAllContracts.this, edit_contract_company.class);
            intent.putExtra( "contractid", contractid);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(ViewAllContracts.this,"This Job was not listed by your company!",Toast.LENGTH_LONG).show();
        }

    }
}