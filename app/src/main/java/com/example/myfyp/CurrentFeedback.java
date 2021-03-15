package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CurrentFeedback extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = db.getReference();
    private DatabaseReference ref = db.getReference();
    private FirebaseUser user;
    private static final String Feedback = "Feedback";
    private String uid;
    private User currentUser;
    private Contract currentcontract;
    private String feedbackID;
    private TextView tvCompanyName,tvPositionDisplay,tvrating,tvpay,tvdescription;
    private EditText etExperience,etPay,etDescribe;
    private Button btnSaveChanges;
    private ImageView imageView;
    private String adminuid,useruid;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_feedback);


        tvCompanyName=findViewById(R.id.tvCompanyName);
      //  tvPositionDisplay=findViewById(R.id.tvPositionDisplay);
        tvrating = findViewById(R.id.tvrating);
        tvpay = findViewById(R.id.tvpay);
       // tvdescription = findViewById(R.id.tvdescription);


        //ref= FirebaseDatabase.getInstance().getReference(Feedback);




        feedbackID = getIntent().getExtras().getString("feedbackID");



        db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef=db.getReference();
        uid=user.getUid();

        dbRef.child("Feedback").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children)
                {
                    Feedback fb = child.getValue(Feedback.class);

                    if (feedbackID.equals(fb.getFeedbackid()))
                    {
                        // tvCompanyName.setText(contract.get);
                       // tvPositionDisplay.setText(fb.getPosition());
                        tvCompanyName.setText(fb.getCompanyName());
                       // tvdescription.setText(fb.getDescription());
                        tvpay.setText(fb.getPay());
                        tvrating.setText(fb.getExperience());


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









    }
}