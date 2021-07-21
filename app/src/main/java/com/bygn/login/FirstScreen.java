package com.bygn.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FirstScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String id = user.getUid();
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users").child("Student").child(id);
            rootRef.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(FirstScreen.this,"To Students ",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(FirstScreen.this, SubjectListStudent.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                //finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }
            );
            DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Users").child("Admin").child(id);
            dataRef.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(FirstScreen.this,"To admins ",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(FirstScreen.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                //finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }
            );
            /*DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Users").child(id).getParent();
            assert dataRef != null;
            String StrDataRef = dataRef.toString();
            dataRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //HashMap<String, Object> yourData = (HashMap<String, Object>) dataSnapshot.getValue();
                    HashMap<String, Object> yourData = (HashMap<String, Object>) dataSnapshot.getValue();
                    String userType = (String) yourData.keySet().toArray()[0];
                    Toast.makeText(FirstScreen.this, "here is " + userType, Toast.LENGTH_SHORT).show();
                    if (userType == null) {


                    } else if (userType.equals("Student")) {
                        Intent intent = new Intent(FirstScreen.this, SubjectListStudent.class);
                        startActivity(intent);
                        finish();

                    } else if (userType.equals("Admin")) {
                        Intent intent = new Intent(FirstScreen.this, SubjectListStudent.class);
                        startActivity(intent);
                        finish();
                    }
                    Intent intent = new Intent(FirstScreen.this, SubjectListStudent.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/
        }
        else{
            Intent intent = new Intent(FirstScreen.this, Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}