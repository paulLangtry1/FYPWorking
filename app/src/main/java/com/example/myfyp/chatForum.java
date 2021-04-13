package com.example.myfyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class chatForum extends AppCompatActivity implements CommentAdapter.OnContractListener {
    private DatabaseReference dbRef,ref;
    private static final String Comment = "Comment";
    private FirebaseUser user;
    private FirebaseDatabase db;
    private String uid;
    private EditText etAddcontent;
    private Button btncreatecomment;
    private User currentuser;
    SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM--dd 'T' HH:mm:sss'Z'");
    private  RecyclerView mRecyclerView;
    private CommentAdapter myAdapter;
    private ArrayList<Comment> allcomments = new ArrayList<Comment>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_forum);

        db = FirebaseDatabase.getInstance();

        dbRef= FirebaseDatabase.getInstance().getReference(Comment);
        user= FirebaseAuth.getInstance().getCurrentUser();
        uid=user.getUid();
        ref=db.getReference();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);



        etAddcontent = findViewById(R.id.etAddcontent);

        btncreatecomment = findViewById(R.id.btnAddComment);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(chatForum.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(chatForum.this, LinearLayoutManager.VERTICAL));
        myAdapter = new CommentAdapter(allcomments, this::onContractClick);
        mRecyclerView.setAdapter(myAdapter);

        ref.child("Comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    Comment comment = child.getValue(Comment.class);
                    //  if (contract.getUID().equals(uid)) {
                    allcomments.add(comment);

                    myAdapter.notifyItemInserted(allcomments.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //   Log.m("DBE Error","Cancel Access DB");
            }
        });


        btncreatecomment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ref.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        for (DataSnapshot child : children) {
                            if (child.getKey().equals(uid)) {
                                currentuser = child.getValue(User.class);

                                String name = currentuser.getUsername();
                                String content = etAddcontent.getText().toString();
                                String datetime = ISO_8601_FORMAT.format(new Date());



                                Toast.makeText(chatForum.this,"Comment Created",Toast.LENGTH_SHORT).show();

                                String commentId = dbRef.push().getKey();
                                Comment comment = new Comment(content,name,datetime,commentId);

                                dbRef.child(commentId).setValue(comment);

                                etAddcontent.setText("");


                                //String keyId = dbRef.push().getKey();
                                //dbRef.child(keyId).setValue(contract);

                                //Intent intent = new Intent(chatForum.this,UserHomeActivity.class);
                               // startActivity(intent);





                            }
                        }
                    }


                    public void onContractClick(int position)
                    {
                        allcomments.get(position);
                        Intent intent = new Intent(chatForum.this,CurrentContract.class);
                        intent.putExtra( "Position", position);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






            }
        });
    }


    @Override
    public void onContractClick(int position)
    {
        allcomments.get(position);
        Intent intent = new Intent(chatForum.this,CurrentContract.class);
        intent.putExtra( "Position", position);
        startActivity(intent);

    }
}