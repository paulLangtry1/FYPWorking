package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CompanyRegister extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private EditText etRegCompEmail,etRegCompName,etRegCompPW,etRegCompConfirmPW,etAddress,etPhone;
    private Button btnRegCompany;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String COMPANY = "Company";
    private static final String TAG = "CompanyRegister";
    private Company company;
    private TextView greeting;
    private String selected;
    private Spinner spinner;
    private static final String[] paths = {"Construction", "Demolition", "Farming","Manufacturing","Other"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_register);

        etRegCompConfirmPW = findViewById(R.id.etRegCompConfirmPW);
        etRegCompPW = findViewById(R.id.etRegCompPW);
        etRegCompEmail = findViewById(R.id.etRegCompEmail);
        etRegCompName = findViewById(R.id.etRegCompName);
        btnRegCompany = findViewById(R.id.btnRegCompany);
        etAddress = findViewById(R.id.etcompanyaddress);
        etPhone = findViewById(R.id.etcompanyPhoneno);
        spinner = findViewById(R.id.spinner);

        greeting = findViewById(R.id.tvGreetingAdmin);

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item,paths);

        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner.setAdapter(ad);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(COMPANY);
        mAuth = FirebaseAuth.getInstance();

        spinner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) spinner.getSelectedView()).setTextColor(Color.WHITE);
            }
        });


        btnRegCompany.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String companyemail = etRegCompEmail.getText().toString();
                String companyname = etRegCompName.getText().toString();
                String password = etRegCompPW.getText().toString();
                String confirmpw = etRegCompConfirmPW.getText().toString();
                String ppurl = "Not Uploaded";
                String address = etAddress.getText().toString();
                String phoneNo= etPhone.getText().toString();
                String isadmin = "No";
                String sector = String.valueOf(spinner.getSelectedItem());

                if(companyemail.isEmpty() || companyname.isEmpty() || password.isEmpty() || confirmpw.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter all details",Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.equals(confirmpw) && password.length()>=6)
                {
                    company = new Company(companyemail,password,companyname,ppurl,address,phoneNo,isadmin,sector);
                    registerUser(companyemail,password);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Passwords do not match Or Password is less than 6 characters",Toast.LENGTH_LONG).show();
                    //return;
                }
            }
        });





    }

    private void registerUser(String companyemail, String password)
    {
        mAuth.createUserWithEmailAndPassword(companyemail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CompanyRegister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    public void updateUI(FirebaseUser currentUser)
    {
        String uid=currentUser.getUid();
        mDatabase.child(uid).setValue(company);
        Intent intent = new Intent(this, CompanyLogin.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        selected = parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}