package com.bygn.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SubjectListStudent extends AppCompatActivity {

    private FirebaseAuth mAuth;
    SubjectAdapterStudent adapter;
    List<String> subjectList;
    RecyclerView subjectRecycler;
    FirebaseFirestore db;
    FloatingActionButton addSubjectButton;
    String subjectNew;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list_student);
        mAuth = FirebaseAuth.getInstance();
        subjectList = new ArrayList<>();
        subjectRecycler = findViewById(R.id.subjectListS);
        addSubjectButton = findViewById(R.id.logOut);
        db =  FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        subjectRecycler.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false);
        subjectRecycler.setLayoutManager(gridLayoutManager);

        database.getReference("Subjects").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        subjectList.clear();
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            String name = ds.getKey();

                            subjectList.add(name);
                        }

                        adapter = new SubjectAdapterStudent(SubjectListStudent.this,subjectList);
                        subjectRecycler.setAdapter(adapter);

                        adapter.setOnItemClickListener(
                                new SubjectAdapterStudent.OnSubjectClick() {
                                    @Override
                                    public void onItemClick(String sub, int position) {
                                        Intent intent = new Intent(SubjectListStudent.this,QuizListStudent.class);
                                        intent.putExtra("Subject", sub);
                                        SubjectListStudent.this.startActivity(intent);
                                    }



                                }
                        );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null){
            Intent intent = new Intent(SubjectListStudent.this, LoginStudent.class);
            startActivity(intent);
            finish();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOutMain:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(SubjectListStudent.this, Login.class);
        startActivity(intent);
    }


}