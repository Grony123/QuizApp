package com.bygn.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddSubjectDialog.SubjectDialogListener {

    //private Button addSubject;
    private FirebaseAuth mAuth;
    SubjectAdapter adapter;
    List<String> subjectList;
    RecyclerView subjectRecycler;
    FirebaseFirestore db;
    FloatingActionButton addSubjectButton;
    String subjectNew;
    FirebaseDatabase database,databaseSt;
    String userId;
    private Dialog loadingDialogue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Subjects");
        //addSubject = findViewById(R.id.logOut);
        mAuth = FirebaseAuth.getInstance();
        subjectList = new ArrayList<>();
        subjectRecycler = findViewById(R.id.subjectList);
        addSubjectButton = findViewById(R.id.logOut);
        db =  FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseSt = FirebaseDatabase.getInstance();

        loadingDialogue = new Dialog(MainActivity.this);
        loadingDialogue.setContentView(R.layout.loading);
        loadingDialogue.setCancelable(false);
        loadingDialogue.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialogue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialogue.show();

        subjectRecycler.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false);
        subjectRecycler.setLayoutManager(gridLayoutManager);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){
            userId = user.getUid();

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users").child("Student").child(userId);
            rootRef.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                loadingDialogue.cancel();
                                Intent intent = new Intent(MainActivity.this, SubjectListStudent.class);
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
            DatabaseReference dtRef = FirebaseDatabase.getInstance().getReference("Users").child("Admin").child(userId);
            dtRef.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                loadingDialogue.cancel();
                                subjectList.clear();
                                for(DataSnapshot ds : snapshot.getChildren()) {
                                    String name = ds.getKey();

                                    subjectList.add(name);
                                }

                                adapter = new SubjectAdapter(MainActivity.this,subjectList);
                                subjectRecycler.setAdapter(adapter);

                                adapter.setOnItemClickListener(
                                        new SubjectAdapter.OnSubjectClick() {
                                            @Override
                                            public void onItemClick(String sub, int position) {
                                                Intent intent = new Intent(MainActivity.this,QuizList.class);
                                                intent.putExtra("Subject", sub);
                                                MainActivity.this.startActivity(intent);
                                            }

                                            @Override
                                            public void onDeleteClick(String sub, int position) {
                                                database.getReference().child("Users").child("Admin").child(userId).child(sub).removeValue();
                                                db.collection(userId).document(sub).delete();
                                                db.collection("Admin").document(sub).delete();
                                                removeItem(position);
                                            }
                                        }
                                );
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }
            );
            /*database.getReference("Users").child("Admin").child(userId).addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            subjectList.clear();
                            for(DataSnapshot ds : snapshot.getChildren()) {
                                String name = ds.getKey();

                                subjectList.add(name);
                            }

                            adapter = new SubjectAdapter(MainActivity.this,subjectList);
                            subjectRecycler.setAdapter(adapter);

                            adapter.setOnItemClickListener(
                                    new SubjectAdapter.OnSubjectClick() {
                                        @Override
                                        public void onItemClick(String sub, int position) {
                                            Intent intent = new Intent(MainActivity.this,QuizList.class);
                                            intent.putExtra("Subject", sub);
                                            MainActivity.this.startActivity(intent);
                                        }

                                        @Override
                                        public void onDeleteClick(String sub, int position) {
                                            database.getReference().child("Users").child("Admin").child(userId).child(sub).removeValue();
                                            db.collection(userId).document(sub).delete();
                                            db.collection("Admin").document(sub).delete();
                                            removeItem(position);
                                        }
                                    }
                            );
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }
            );*/
        }else{
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
        //database = FirebaseDatabase.getInstance().getReference("Admin").child(userId);


        addSubjectButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openSubjectDialog();
                        //subjectRecycler.removeAllViewsInLayout();
                    }
                }
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null){
            loadingDialogue.cancel();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
        else {
            String id = currentUser.getUid();
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Users").child("Student").child(id);
            rootRef.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                loadingDialogue.cancel();
                                Intent intent = new Intent(MainActivity.this, SubjectListStudent.class);
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
        }

    }

    public void openSubjectDialog(){
        AddSubjectDialog subjectDialog = new AddSubjectDialog();
        subjectDialog.show(getSupportFragmentManager(),"Subject");
    }
    @Override
    public void applyChanges(String text) {
        subjectNew = text;
        database.getReference("Users").child("Admin").child(userId).child(subjectNew).setValue("null");
        databaseSt.getReference("Subjects").child(subjectNew).setValue("null");
        //db.collection("Admin").document(text);
    }
    public void removeItem(int position){
        subjectList.remove(position);
        adapter.notifyItemRemoved(position);
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
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }


}