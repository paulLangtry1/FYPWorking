package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class RecommendedJobs extends AppCompatActivity implements MyAdapter.OnContractListener {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = db.getReference();
    private DatabaseReference ref = db.getReference();
    ArrayList<Contract> recommendedcontracts = new ArrayList<Contract>();
    ArrayList<Contract> allcontracts = new ArrayList<Contract>();
    private DatabaseReference collective = db.getReference();
    //private DatabaseReference first = dbRef.child("Avatar").child("imageUrl");
    private FirebaseUser user;
    private static final String Feedback = "Feedback";
    private static final String collectiveFeedback = "CollectiveFeedback";
    private String uid;
    MyAdapter myAdapter;
    RecyclerView mRecyclerView;
    private User currentUser;
    private Contract currentcontract;
    private String contractid;
    private TextView tvbasedonprevious;
    private EditText etExperience,etPay,etDescribe;
    private Button btnSaveChanges,btnsort;
    ArrayList<String> allsectors = new ArrayList<String>();
    private String bestString = null;
    private ImageView imageView;
    private String adminuid,useruid;
    private RatingBar rateExperience,ratePay,rateWorklife;
    private static final String[] paths = {"Based On Previous Jobs", "Skills", "Length"};
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_jobs);


        tvbasedonprevious = findViewById(R.id.tvbasedonprevioustext);

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        ref = FirebaseDatabase.getInstance().getReference(Feedback);

        spinner = findViewById(R.id.spinnerfilter);
        btnsort = findViewById(R.id.btnsort);


        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item,paths);

        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(ad);

        db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = db.getReference();
        uid = user.getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        db = FirebaseDatabase.getInstance();
        ref = db.getReference();

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        myAdapter = new MyAdapter(recommendedcontracts, this::onContractClick);
        //myAdapter = new MyAdapter(allcontracts,this::onContractClick);
        mRecyclerView.setAdapter(myAdapter);

        spinner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) spinner.getSelectedView()).setTextColor(Color.WHITE);
            }
        });

        btnsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(spinner.getSelectedItem().equals("Based On Previous Jobs"))
                {
                    clear();

                    ref.child("ContractHistory").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Iterable<DataSnapshot> children = snapshot.getChildren();
                            for (DataSnapshot child : children) {
                                Contract contract = child.getValue(Contract.class);

                                if (contract.getUserID().equals(uid))
                                {
                                    String sector = contract.getSector();

                                    allsectors.add(sector);


                                }
                            }

                            String previous = null;
                            int bestCount = 0;
                            int count = 1;


                            for (int i = 0; i < allsectors.size(); i++) {
                                if (allsectors.get(i).equals(previous)) {
                                    count++;
                                } else {
                                    count = 1;
                                }

                                if (count > bestCount) {
                                    bestCount = count;
                                    bestString = allsectors.get(i);
                                }

                                previous = allsectors.get(i);
                            }

                            myAdapter.notifyDataSetChanged();

                            displayrecommendedjob();



                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else if(spinner.getSelectedItem().equals("Skills"))
                {
                    clear();
                    tvbasedonprevious.setText("Based On Skills");

                    ref.child("Skills").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Iterable<DataSnapshot> children = snapshot.getChildren();
                            for (DataSnapshot child : children) {
                                ExtraSkills contract = child.getValue(ExtraSkills.class);

                                if (contract.getUserid().equals(uid))
                                {
                                    String skill = contract.getSkill();
                                    if(skill.equalsIgnoreCase("General Labourer"))
                                    {
                                        displayrecommendedjobsall();
                                        myAdapter.notifyDataSetChanged();
                                    }
                                    else if(skill.equalsIgnoreCase("Plumber") || skill.equalsIgnoreCase("Electrician") || skill.equalsIgnoreCase("Block Layer") || skill.equalsIgnoreCase("Builder"))
                                    {
                                        displayrecommendedjobconstruction();
                                        myAdapter.notifyDataSetChanged();
                                    }
                                    else if(skill.equalsIgnoreCase("W Tractor License Holder"))
                                    {
                                        displayrecommendedjobsfarming();
                                        myAdapter.notifyDataSetChanged();
                                    }
                                    else if(skill.equalsIgnoreCase("Excavator Driver"))
                                    {
                                        displayrecommendedjobsdemo();
                                        myAdapter.notifyDataSetChanged();
                                    }
                                    else if(skill.equalsIgnoreCase("Machine Operator"))
                                    {
                                        displayrecommendedjobsmanu();
                                        myAdapter.notifyDataSetChanged();
                                    }
                                    else if(skill.equalsIgnoreCase("D1 Truck License Holder"))
                                    {
                                        displayrecommendedjobsOther();
                                        myAdapter.notifyDataSetChanged();
                                    }







                                }
                            }



                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                if(spinner.getSelectedItem().equals("Length"))
                {
                    clear();

                    ref.child("Contract").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Iterable<DataSnapshot> children = snapshot.getChildren();
                            for (DataSnapshot child : children)
                            {
                                Contract contract = child.getValue(Contract.class);

                               // if (contract.getUserID().equals(uid))
                                recommendedcontracts.add(contract);

                                myAdapter.notifyItemInserted(recommendedcontracts.size() - 1);




                                    //allsectors.add(sector);



                            }
                            tvbasedonprevious.setText("Based On Start Date");
                            ascending();





                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });









        //  getmostpopularsector();

        //displayrecommendedjob();


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

        Intent intent = new Intent(RecommendedJobs.this, UserViewContracts.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }
    public void homeview()
    {

        Intent intent = new Intent(RecommendedJobs.this, UserHomeActivity.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }

    public void displayrecommendedjob()
    {
        dbRef.child("Contract").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    Contract contract = child.getValue(Contract.class);

                    if (contract.getSector().equals(bestString))
                    {
                        Contract recom = child.getValue(Contract.class);
                        recommendedcontracts.add(recom);

                        myAdapter.notifyItemInserted(recommendedcontracts.size() - 1);



                    }
                    tvbasedonprevious.setText("Based On Previous Jobs");
                }
                myAdapter.notifyDataSetChanged();

                if (myAdapter.getItemCount() == 0) {

                    tvbasedonprevious.setText("No Previous Related Jobs Found");
                    myAdapter.notifyDataSetChanged();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void displayrecommendedjobconstruction()
    {
        dbRef.child("Contract").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    Contract contract = child.getValue(Contract.class);

                    if (contract.getSector().equals("Construction"))
                    {
                        Contract recom = child.getValue(Contract.class);
                        recommendedcontracts.add(recom);

                        myAdapter.notifyItemInserted(recommendedcontracts.size() - 1);


                    }
                    tvbasedonprevious.setText("Based On Your Skills");
                }
                if (myAdapter.getItemCount() == 0) {

                    tvbasedonprevious.setText("No Previous Related Jobs Found");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void displayrecommendedjobsall()
    {
        dbRef.child("Contract").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    Contract contract = child.getValue(Contract.class);

                    if (contract.getSector().equals("Construction") ||contract.getSector().equals("Demolition") || contract.getSector().equals("Farming") ||contract.getSector().equals("Manufacturing") ||contract.getSector().equals("Other"))
                    {
                        Contract recom = child.getValue(Contract.class);
                        recommendedcontracts.add(recom);


                        myAdapter.notifyItemInserted(recommendedcontracts.size() - 1);


                    }
                    tvbasedonprevious.setText("Based On Your Skills");
                }
                if (myAdapter.getItemCount() == 0) {

                    tvbasedonprevious.setText("No Previous Related Jobs Found");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void displayrecommendedjobsfarming()
    {
        dbRef.child("Contract").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    Contract contract = child.getValue(Contract.class);

                    if (contract.getSector().equals("Farming"))
                    {
                        Contract recom = child.getValue(Contract.class);
                        recommendedcontracts.add(recom);

                        myAdapter.notifyItemInserted(recommendedcontracts.size() - 1);


                    }
                    tvbasedonprevious.setText("Based On Your Skills");
                }
                if (myAdapter.getItemCount() == 0) {

                    tvbasedonprevious.setText("No Previous Related Jobs Found");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void displayrecommendedjobsmanu()
    {
        dbRef.child("Contract").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    Contract contract = child.getValue(Contract.class);

                    if (contract.getSector().equals("Manufacturing"))
                    {
                        Contract recom = child.getValue(Contract.class);
                        recommendedcontracts.add(recom);

                        myAdapter.notifyItemInserted(recommendedcontracts.size() - 1);


                    }
                    tvbasedonprevious.setText("Based On Your Skills");
                }
                if (myAdapter.getItemCount() == 0) {

                    tvbasedonprevious.setText("No Previous Related Jobs Found");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void displayrecommendedjobsdemo()
    {
        dbRef.child("Contract").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    Contract contract = child.getValue(Contract.class);

                    if (contract.getSector().equals("Demolition"))
                    {
                        Contract recom = child.getValue(Contract.class);
                        recommendedcontracts.add(recom);

                        myAdapter.notifyItemInserted(recommendedcontracts.size() - 1);


                    }
                    tvbasedonprevious.setText("Based On Your Skills");
                }
                if (myAdapter.getItemCount() == 0) {

                    tvbasedonprevious.setText("No Previous Related Jobs Found");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void displayrecommendedjobsOther()
    {
        dbRef.child("Contract").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    Contract contract = child.getValue(Contract.class);

                    if (contract.getSector().equals("Other"))
                    {
                        Contract recom = child.getValue(Contract.class);
                        recommendedcontracts.add(recom);

                        myAdapter.notifyItemInserted(recommendedcontracts.size() - 1);


                    }
                    tvbasedonprevious.setText("Based On Your Skills");
                }
                if (myAdapter.getItemCount() == 0) {

                    tvbasedonprevious.setText("No Previous Related Jobs Found");

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onContractClick(int position)
    {
        recommendedcontracts.get(position);
        String contractID = recommendedcontracts.get(position).getContractID();
       // String startdate = allContractsUser.get(position).getStartdate();



        Intent intent = new Intent(RecommendedJobs.this,CurrentContract.class);
        intent.putExtra( "Value", contractID);
        startActivity(intent);






    }

    public void clear()
    {
        int size = recommendedcontracts.size();
        recommendedcontracts.clear();
        myAdapter.notifyItemRangeRemoved(0,size);
    }
    public void ascending()
    {
        Collections.sort(recommendedcontracts, new Comparator<Contract>()
        {
            @Override
            public int compare(Contract o1, Contract o2)
            {
                return o1.getStartdate().compareTo(o2.getStartdate());
            }

        });
    }


    public void descending()
    {
        Collections.sort(recommendedcontracts, new Comparator<Contract>()
        {
            @Override
            public int compare(Contract o1, Contract o2)
            {
                return o2.getStartdate().compareTo(o1.getEnddate());
            }

        });


    }

}