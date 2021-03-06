package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.Calendar;

public class AddContract extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    boolean startdateselected = true;
    private DatabaseReference dbRef,ref;
    private static final String Contract = "Contract";
    private FirebaseUser user;
    private FirebaseDatabase db;
    private String uid;
    private DatePicker dpStartdate,dpEnddate;
    private EditText etAddpositon,etAddaddress,etAddenddate,etAddstartdate,etAddendtime,etAddstarttime,etaddCounty;
    private TextView tvstartdate,tvenddate,tvstarttime,tvendtime;
    private Button btnCreateContract;
    private Company currentcompany;
    private String startDate;
    private String endDate;
    private String selected;
    private Spinner spinner;
    private String startTime;
    private String endTime;
    private boolean starttimeistrue=true;
    private static final String[] paths = {"Construction", "Demolition", "Farming","Manufacturing","Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contract);

        db = FirebaseDatabase.getInstance();

        dbRef= FirebaseDatabase.getInstance().getReference(Contract);
        user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        ref=db.getReference();


        etAddpositon = findViewById(R.id.etAddposition);
        etAddaddress = findViewById(R.id.etAddAddress);
        tvstartdate = findViewById(R.id.tvStartdate);
        tvenddate = findViewById(R.id.tvEnddate);
        tvstarttime = findViewById(R.id.tvStarttime);
        tvendtime = findViewById(R.id.tvEndTime);
        etaddCounty = findViewById(R.id.etAddCounty);
        spinner = findViewById(R.id.spinnersectorcontract);

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item,paths);

        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(ad);



        btnCreateContract = findViewById(R.id.btnAddComment);

        tvstartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerStartDate();
                startdateselected=true;
            }
        });
        tvenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDate();
                startdateselected = false;

            }
        });

        tvstarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(),"time picker");
                starttimeistrue=true;

            }
        });
        tvendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker = new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(),"time picker");
                starttimeistrue=false;
            }
        });

        spinner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) spinner.getSelectedView()).setTextColor(Color.WHITE);
            }
        });



        btnCreateContract.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ref.child("Company").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        for (DataSnapshot child : children) {
                            if (child.getKey().equals(uid)) {
                                currentcompany = child.getValue(Company.class);
                                String currentName = currentcompany.getCompanyName();




                                String position = etAddpositon.getText().toString();
                                String address = etAddaddress.getText().toString();
                                String enddate = endDate;
                                String startdate = startDate;
                                String endtime = endTime;
                                String starttime = startTime;
                                String companyID = uid;
                                String county = etaddCounty.getText().toString();
                                String userID = "";
                                String companyName = currentName;
                                String sector = String.valueOf(spinner.getSelectedItem());


                                Toast.makeText(AddContract.this,"Contract Created",Toast.LENGTH_SHORT).show();

                                String contractID = dbRef.push().getKey();
                                Contract contract = new Contract(position,address,county,startdate,enddate,starttime,endtime,userID,contractID,companyName,companyID,sector);

                                dbRef.child(contractID).setValue(contract);

                                //String keyId = dbRef.push().getKey();
                                //dbRef.child(keyId).setValue(contract);

                                Intent intent = new Intent(AddContract.this,CompanyHomeActivity.class);
                                startActivity(intent);




                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






            }
        });
    }
    private void showDatePickerStartDate()
    {
        DatePickerDialog dpd = new DatePickerDialog(this,this,
                Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dpd.show();
    }
    private void showEndDate()
    {
        DatePickerDialog dpd2 = new DatePickerDialog(this,this,
                Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        dpd2.show();
    }

    @Override
    public void onDateSet(DatePicker dpd, int year, int month, int dayOfMonth)
    {
        if(startdateselected==true)
        {
            startDate = dayOfMonth  + "/" + (month + 1)  + "/" + year;
            tvstartdate.setText(startDate);
        }
        else
        {
            endDate = dayOfMonth  + "/" + (month + 1)  + "/" + year;
            tvenddate.setText(endDate);
        }



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

        Intent intent = new Intent(AddContract.this, ViewAllContracts.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }
    public void homeview()
    {

        Intent intent = new Intent(AddContract.this, CompanyHomeActivity.class);
        // intent.putExtra( "enddate", enddate);
        startActivity(intent);


    }






    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        if(starttimeistrue == true)
        {
            tvstarttime.setText("Hours: " +hourOfDay + " Minute: " + minute);
            startTime= hourOfDay + " : " + minute;
            tvstarttime.setText(startTime);

        }
        else
        {
            tvendtime.setText("Hours: " +hourOfDay + " Minute: " + minute);
            endTime=hourOfDay + " : " + minute;
            tvendtime.setText(endTime);
        }

    }
}