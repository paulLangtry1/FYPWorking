package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class view_skills_edit_prof extends AppCompatActivity implements skillsAdapter.OnContractListener {

    ArrayList<ExtraSkills> allFeedback = new ArrayList<ExtraSkills>();



    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String userid;
    skillsAdapter skillsAdapter;
    private FirebaseUser user;
    RecyclerView mRecyclerView;
    private String uid;
    private TextView tv;
    private String currentcompany;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_skills_edit_prof);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerviewcontract2);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();





        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        skillsAdapter = new skillsAdapter(allFeedback,this::onContractClick);
        mRecyclerView.setAdapter(skillsAdapter);

        ref.child("Skills").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    ExtraSkills feedback = child.getValue(ExtraSkills.class);

                    if(feedback.getUserid().equals(uid))
                    {

                        allFeedback.add(feedback);

                        skillsAdapter.notifyItemInserted(allFeedback.size() - 1);
                    }

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
        String contractid = allFeedback.get(position).getSkillid();

        Intent intent = new Intent(view_skills_edit_prof.this, delete_skill.class);
        intent.putExtra( "contractid", contractid);
        startActivity(intent);

    }


}