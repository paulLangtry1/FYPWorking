package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminLogin extends AppCompatActivity {

    private EditText etCompEmail, etCompPassword;
    private Button btnCompLogin, btnCompReg,btngoback;
    private FirebaseAuth mAuth;
    private static final String USER = "Admin";
    private static final String TAG = "AdminLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        btnCompLogin = findViewById(R.id.btnadminLogin);
        btnCompReg = findViewById(R.id.btnadminReg);
        btngoback = findViewById(R.id.btnadminbackbutton);
        etCompEmail = findViewById(R.id.etadminEmail);
        etCompPassword = findViewById(R.id.etadminPassword);
        mAuth = FirebaseAuth.getInstance();

        btnCompLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String companyEmail = etCompEmail.getText().toString();
                String password = etCompPassword.getText().toString();
                if (companyEmail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter credentials", Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(companyEmail, password)
                        .addOnCompleteListener(AdminLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(AdminLogin.this, "Successfully Logged In",Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user
                                    Toast.makeText(AdminLogin.this, "Incorrect Username Or Password",Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());


                                }

                                // ...
                            }
                        });

            }
        });

        btngoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent compreg = new Intent(AdminLogin.this, CompanyLogin.class);
                startActivity(compreg);
            }
        });





        //bring user to company register page
        btnCompReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent compreg = new Intent(AdminLogin.this, AdminRegister.class);
                startActivity(compreg);
            }
        });

    }

    public void updateUI(FirebaseUser currentUser)
    {

        Intent homeIntent = new Intent(AdminLogin.this, AdminHome.class);
        homeIntent.putExtra("companyEmail",currentUser.getEmail());
        startActivity(homeIntent);
    }
}