package com.bygn.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterStudent extends AppCompatActivity {

    private ProgressBar progressBarL;
    private EditText emailL,passwordL1,passwordL2;
    private Button loginL,registerL;
    private FirebaseAuth mAuth;
    private CheckBox checkBoxL;
    private FirebaseDatabase database;
    private String userId;
    Task<Void> ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);
        progressBarL = findViewById(R.id.register_progressBarS);
        emailL = findViewById(R.id.register_emailFS);
        passwordL1 = findViewById(R.id.register_passwordFS);
        passwordL2 = findViewById(R.id.register_passwordSS);
        loginL = findViewById(R.id.register_loginFS);
        registerL = findViewById(R.id.register_registerFS);
        checkBoxL = findViewById(R.id.checkboxRegisterS);
        mAuth = FirebaseAuth.getInstance();

        checkBoxL.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            passwordL1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordL2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }else{
                            passwordL1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordL2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                    }
                }
        );

        registerL.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = emailL.getText().toString();
                        String password1 = passwordL1.getText().toString();
                        String password2 = passwordL2.getText().toString();
                        if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password1) || !TextUtils.isEmpty(password2)){

                            if(password1.equals(password2)){
                                progressBarL.setVisibility(View.VISIBLE);

                                mAuth.createUserWithEmailAndPassword(email,password1).addOnCompleteListener(
                                        new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()){
                                                    database = FirebaseDatabase.getInstance();
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    assert user != null;
                                                    userId = user.getUid();
                                                    ref = database.getReference("Users").child("Student").child(userId).setValue("null");
                                                    sendtoMain();

                                                }
                                                else{
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(getApplicationContext(),"Error : "+error,Toast.LENGTH_LONG).show();
                                                }
                                                progressBarL.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                );
                            }else{
                                Toast.makeText(RegisterStudent.this,"Password Fields are not matching",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );
        loginL.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RegisterStudent.this, LoginStudent.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //if(currentUser!=null){
           // sendtoMain();
        //}
    }

    private void sendtoMain(){
        Intent intent = new Intent(RegisterStudent.this, SubjectListStudent.class);
        startActivity(intent);
        finish();
    }
}