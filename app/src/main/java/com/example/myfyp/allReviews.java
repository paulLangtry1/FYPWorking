package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
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

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class allReviews extends AppCompatActivity implements FeedbackAdapter.OnContractListener
{

    ArrayList<Feedback> allFeedback = new ArrayList<Feedback>();

    private float experienceavg;
    private float payavg;
    private float worklifeavg;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    FeedbackAdapter feedbackAdapter;
    private FirebaseUser user;
    RecyclerView mRecyclerView;
    private String uid;
    private TextView tv;
    private ArrayList<Float> experienceList = new ArrayList<Float>();
    private ArrayList<Float> payList = new ArrayList<Float>();
    private ArrayList<Float> worklifeList = new ArrayList<Float>();
    private String currentcompany;
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

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

        ref.child("CollectiveFeedback").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
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
        String companyID = allFeedback.get(position).getCompanyID();
       // Intent intent = new Intent(allReviews.this,CurrentFeedback.class);
       // intent.putExtra( "feedbackID", feedbackID);
       // startActivity(intent);

        ref.child("Feedback").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    Feedback feedback = child.getValue(Feedback.class);
                    if(feedback.getCompanyID().equals(companyID))
                    {
                        currentcompany = feedback.getCompanyName();
                        Float experience = Float.valueOf(feedback.getExperience());
                        Float pay = Float.valueOf(feedback.getPay());
                        Float worklife = Float.valueOf(feedback.getDescription());

                        experienceList.add(experience);
                        payList.add(pay);
                        worklifeList.add(worklife);



                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });


        avererageExperience();
        avereragePay();
        avererageworklife();


        displayAlert();




    }



    private void avererageExperience()
    {
        float total = 0;
        float average;
        for(int i = 0; i<experienceList.size(); i++)
        {
            float currentNo = experienceList.get(i);
            total = currentNo + total;
            average = total / experienceList.size();
            experienceavg = average;

        }



    }
    private void avereragePay()
    {
        float total = 0;
        float average;
        for(int i = 0; i<payList.size(); i++)
        {
            float currentNo = payList.get(i);
            total = currentNo + total;
            average = total / payList.size();
            payavg = average;

        }



    }
    private void avererageworklife()
    {
        float total = 0;
        float average;
        for(int i = 0; i<worklifeList.size(); i++)
        {
            float currentNo = worklifeList.get(i);
            total = currentNo + total;
            average = total / worklifeList.size();
            worklifeavg = average;

        }



    }
    private void displayAlert()
    {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(allReviews.this);
        builder1.setMessage("Company Selected: " + currentcompany + "\n" + "User Average Experience: " +  decimalFormat.format(experienceavg) + " Stars"+ "\n" + "User Average Pay: " +  decimalFormat.format(payavg) + " Stars" + "\n" + "User Average Work-Life: " +  decimalFormat.format(worklifeavg) + " Stars");
        builder1.setCancelable(true);

        AlertDialog alert11 = builder1.create();
        alert11.show();


    }




}