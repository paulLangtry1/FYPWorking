package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserHomeActivity extends AppCompatActivity
{
    private EditText etSearch;
    private Button btnlaunchmaps,btncalender;
    private TextView tvWelcome,tvstatus,tvtodaysdate;
    private static final String Contract = "ContractHistory";
    private FirebaseDatabase db;
    private DatabaseReference dbRef,dbhistory;
    private FirebaseUser user;
    private String status;
    private String uid;
    private String jobaccepted;
    private Contract currentcontract;
    Boolean pending = false;
    Boolean verified = false;
    Boolean virginAccount = false;
    Boolean accepted = false;
    private User currentuser;
    ArrayList<Contract> allContracts = new ArrayList<Contract>();
    RecyclerView mRecyclerView;
    ActivejobAdapter myAdapter;
    private String currentdate;
    private String address;
    private String county,startdate,enddate,starttime,endtime,useremail,title;
    private Button locationlauncher;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        //buttons
        btncalender =findViewById(R.id.btncalender);



        Date c = Calendar.getInstance().getTime();


        dbhistory= FirebaseDatabase.getInstance().getReference(Contract);





        tvWelcome = findViewById(R.id.tvWelcome);
        tvstatus = findViewById(R.id.tvPending);
        tvtodaysdate = findViewById(R.id.tvTodaysDate);


        db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef=db.getReference();
        uid=user.getUid();

        DatabaseReference c1v2= FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("userID");
        c1v2.setValue(uid);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        myAdapter = new ActivejobAdapter(allContracts,this::onContractClick);
        mRecyclerView.setAdapter(myAdapter);

        //todays date

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        currentdate = df.format(c);
        tvtodaysdate.setText(currentdate);



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

        dbRef.child("ActiveContracts").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    Contract contract = child.getValue(Contract.class);
                    if(contract.getUserID().equals(uid)) {
                        address = contract.getAddress();
                        county = contract.getCounty();
                        startdate = contract.getStartdate();
                        enddate = contract.getEnddate();
                        starttime = contract.getStarttime();
                        endtime = contract.getEndtime();
                        title = contract.getPosition();

                        String contractid = contract.getContractID();

                        //contracts ending date
                        String contractenddate = contract.getEnddate();
                        String[] parts = contractenddate.split("/");
                        int endday = Integer.parseInt(parts[0]);
                        int endmonth = Integer.parseInt(parts[1]);

                        //current date
                        String[] parts2 = currentdate.split("/");
                        int currday = Integer.parseInt(parts2[0]);
                        int currmon = Integer.parseInt(parts2[1]);
                        allContracts.add(contract);

                        myAdapter.notifyItemInserted(allContracts.size() - 1);


                        if (currday<=endday && currmon<=endmonth)
                        {

                        }
                        else {


                            dbRef.child("ActiveContracts").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Iterable<DataSnapshot> children = snapshot.getChildren();
                                    for (DataSnapshot child : children)
                                    {
                                        Contract contract = child.getValue(Contract.class);
                                        if(contract.getUserID().equals(uid))
                                        {
                                            String position = contract.getPosition();
                                            String address = contract.getAddress();
                                            String enddate = contract.getEnddate();
                                            String startdate = contract.getStartdate();
                                            String endtime = contract.getEndtime();
                                            String starttime = contract.getStarttime();
                                            String userID = uid;
                                            String county = contract.getCounty();
                                            String contractID = contract.getContractID();
                                            String companyName = contract.getCompanyName();
                                            String companyID = contract.getCompanyID();




                                            String keyid =  dbhistory.push().getKey();


                                            Contract contracthistory = new Contract(position,address,county,startdate,enddate,starttime,endtime,userID,contractID,companyName,companyID);
                                            dbhistory.child(keyid).setValue(contracthistory);
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    //   Log.m("DBE Error","Cancel Access DB");
                                }
                            });



                            dbRef.child("ActiveContracts").removeValue();
                        }


                    }





                }
            }





           @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
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
                        useremail = currentuser.getEmail();
                        tvWelcome.setText("Welcome " + currentName + "!");
                        tvstatus.setText(currentuser.getStatus());


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btncalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               // Calendar beginCal = Calendar.getInstance();
                //beginCal.set(year, mnth, day, hrs, min);
                //startTime = beginCal.getTimeInMillis();
                SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy");
                Calendar begincal = Calendar.getInstance();
                Calendar endcal = Calendar.getInstance();
                Date enDate = new Date();
                String[] startTime = starttime.split(":");
                String hour = startTime[0];
                String min = startTime[1];

                String[] endTime = endtime.split(":");
                String hourend = endTime[0];
                String minend = endTime[1];

               // Integer.parseInt(hour),Integer.parseInt(min);
                try
                {
                    Date stDate = new Date();
                    stDate= sdf.parse(startdate + " " + startTime);
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(stDate);



                    begincal.set(cal1.get(Calendar.YEAR),cal1.get(Calendar.MONTH),cal1.get(Calendar.DAY_OF_MONTH),cal1.get(Calendar.HOUR_OF_DAY),cal1.get(Calendar.MINUTE));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    enDate= sdf.parse(enddate + " " + endTime);
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(enDate);
                    endcal.set(cal2.get(Calendar.YEAR),cal2.get(Calendar.MONTH),cal2.get(Calendar.DAY_OF_MONTH),cal2.get(Calendar.HOUR_OF_DAY),cal2.get(Calendar.MINUTE));
                } catch (ParseException e) {
                    e.printStackTrace();
                }









                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE, title);
               // intent.putExtra(CalendarContract.Events.ALL_DAY,"True")
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION,address + "\n"  + county);
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begincal.getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endcal.getTimeInMillis());
                intent.putExtra(Intent.EXTRA_EMAIL, useremail);

                startActivity(intent);



            }
        });

/*

 */







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
            case R.id.item8:
                maps();
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

        Intent intent = new Intent(UserHomeActivity.this, MapsActivity.class);
        startActivity(intent);


    }
    public void onContractClick(int position)
    {
        allContracts.get(position);



        Intent intent = new Intent(UserHomeActivity.this,MapsSiteLocation.class);
        intent.putExtra( "address", address);
        intent.putExtra( "county", county);
        startActivity(intent);
        //startActivity(intent);
    }
}