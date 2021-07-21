package com.bygn.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    private ProgressBar progressBarL;
    private EditText emailL,passwordL,codeword;
    private Button loginL,registerL,loginStudent;
    private FirebaseAuth mAuth;
    private CheckBox checkBoxL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBarL = findViewById(R.id.login_progressBar);
        emailL = findViewById(R.id.login_emailF);
        passwordL = findViewById(R.id.login_passwordF);
        codeword = findViewById(R.id.codeAdminL);
        loginL = findViewById(R.id.login_loginF);
        registerL = findViewById(R.id.login_registerF);
        checkBoxL = findViewById(R.id.checkboxLogin);
        loginStudent = findViewById(R.id.loginAsStudent);
        mAuth = FirebaseAuth.getInstance();


        loginL.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = emailL.getText().toString();
                        String password = passwordL.getText().toString();
                        if(codeword.getText().toString().equals("PICT@PUNE")) {
                            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                                progressBarL.setVisibility(View.VISIBLE);
                                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                                        new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                                    String id = currentUser.getUid();
                                                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users").child("Admin").child(id);
                                                    rootRef.addValueEventListener(
                                                            new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if(snapshot.exists()){
                                                                        Toast.makeText(Login.this,"Logged in",Toast.LENGTH_SHORT).show();
                                                                        sendtoMIan();
                                                                    }
                                                                    else{
                                                                        FirebaseAuth.getInstance().signOut();
                                                                        Toast.makeText(Login.this,"Account Not Found ",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            }
                                                    );
                                                    /*
                                                    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Users").child(id).getParent();
                                                    assert dataRef != null;
                                                    String StrDataRef = dataRef.toString();
                                                    dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            HashMap<String, Object> yourData = (HashMap<String, Object>) dataSnapshot.getValue();
                                                            String userType = (String) yourData.keySet().toArray()[0];
                                                            if (userType.equals("Admin")) {
                                                                sendtoMIan();
                                                            }
                                                            else{
                                                                FirebaseAuth.getInstance().signOut();
                                                                Toast.makeText(Login.this,"Account Not Found",Toast.LENGTH_LONG).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });*/
                                                    //sendtoMIan();
                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(getApplicationContext(), "Error : " + error, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                );

                            }
                        }else{
                            Toast.makeText(Login.this, "College Code is not available", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
        checkBoxL.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            passwordL.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }else{
                            passwordL.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                    }
                }
        );
        registerL.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Login.this, Register.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
        loginStudent.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Login.this, LoginStudent.class);
                        startActivity(intent);
                    }
                }
        );
    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        /*String id = currentUser.getUid();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference(id).getParent();
        String StrDataRef = dataRef.toString();
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userType = (String) dataSnapshot.getValue();
                if(userType == "Admin"){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        if(currentUser!=null){
            String id = currentUser.getUid();
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users").child("Admin").child(id);
            rootRef.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                sendtoMIan();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }
            );
            /*
            DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Users").child(id).getParent();
            assert dataRef != null;
            String StrDataRef = dataRef.toString();
            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //String userType = (String) dataSnapshot.getValue();
                    HashMap<String, Object> yourData = (HashMap<String, Object>) dataSnapshot.getValue();
                    String userType = (String) yourData.keySet().toArray()[0];
                    if(userType.equals("Admin")){
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/

        }
    }

    private void sendtoMIan(){
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}