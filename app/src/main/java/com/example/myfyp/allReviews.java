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

public class allReviews extends AppCompatActivity implements FeedbackAdapter.OnContractListener
{

    ArrayList<Feedback> allFeedback = new ArrayList<Feedback>();

    private FirebaseDatabase database;
    private DatabaseReference ref;
    FeedbackAdapter feedbackAdapter;
    private FirebaseUser user;
    RecyclerView mRecyclerView;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reviews);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        feedbackAdapter = new FeedbackAdapter(allFeedback,this::onContractClick);
        mRecyclerView.setAdapter(feedbackAdapter);

        ref.child("Feedback").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Feedback feedback = child.getValue(Feedback.class);
                    //  if (contract.getUID().equals(uid)) {
                    allFeedback.add(feedback);

                    feedbackAdapter.notifyItemInserted(allFeedback.size() - 1);
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
        allFeedback.get(position);
        String feedbackID = allFeedback.get(position).getFeedbackid();
        Intent intent = new Intent(allReviews.this,CurrentFeedback.class);
        intent.putExtra( "feedbackID", feedbackID);
        startActivity(intent);
    }
}