package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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

public class User_Feedback extends AppCompatActivity {


    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = db.getReference();
    private DatabaseReference ref = db.getReference();
    private DatabaseReference collective = db.getReference();
    //private DatabaseReference first = dbRef.child("Avatar").child("imageUrl");
    private FirebaseUser user;
    private static final String Feedback = "Feedback";
    private static final String collectiveFeedback = "CollectiveFeedback";
    private String uid;
    private User currentUser;
    private Contract currentcontract;
    private String contractid;
    private TextView tvCompanyName,tvPositionDisplay;
    private EditText etExperience,etPay,etDescribe;
    private Button btnSaveChanges;
    private ImageView imageView;
    private String adminuid,useruid;
    private RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__feedback);


        tvCompanyName=findViewById(R.id.tvCompanyName);
        tvPositionDisplay=findViewById(R.id.tvPositionDisplay);

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        ref= FirebaseDatabase.getInstance().getReference(Feedback);
        collective = FirebaseDatabase.getInstance().getReference(collectiveFeedback);

        //etExperience = findViewById(R.id.etExperience);
        ratingBar = findViewById(R.id.ratingBar);
        etPay = findViewById(R.id.etPay);
        etDescribe = findViewById(R.id.etDescribe);


        contractid = getIntent().getExtras().getString("contractID");



        db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef=db.getReference();
        uid=user.getUid();

        dbRef.child("ContractHistory").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    Contract contract = child.getValue(Contract.class);

                    if (contractid.equals(contract.getContractID()))
                    {
                       // tvCompanyName.setText(contract.get);
                        tvPositionDisplay.setText(contract.getPosition());
                        tvCompanyName.setText(contract.getCompanyName());


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        btnSaveChanges.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dbRef.child("ContractHistory").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        for (DataSnapshot child : children)
                        {
                            Contract contract = child.getValue(Contract.class);

                            if (contractid.equals(contract.getContractID()))
                            {
                                // tvCompanyName.setText(contract.get);

                                String experience = String.valueOf(ratingBar.getRating());
                                String pay = etPay.getText().toString();
                                String description = etDescribe.getText().toString();
                                String userid = uid;
                                String companyName = contract.getCompanyName();
                                String position = contract.getPosition();
                                String feedbackid = dbRef.push().getKey();
                                String companyID = contract.getCompanyID();

                                Feedback feedback = new Feedback(experience,pay,description,userid,companyName,position,feedbackid,companyID);
                                ref.child(feedbackid).setValue(feedback);

                                dbRef.child("CollectiveFeedback").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Iterable<DataSnapshot> children = snapshot.getChildren();
                                        for (DataSnapshot child : children)
                                        {
                                            Feedback feedbck = child.getValue(Feedback.class);

                                            if (feedbck.getCompanyID().equals(companyID))
                                            {
                                                Toast.makeText(User_Feedback.this, "Value already exists", Toast.LENGTH_LONG).show();

                                            }
                                            else
                                            {
                                                Feedback feedback1 = new Feedback(experience,pay,description,userid,companyName,position,feedbackid,companyID);
                                                collective.child(feedbackid).setValue(feedback1);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });





                                Intent intent = new Intent(User_Feedback.this,UserHomeActivity.class);
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

}