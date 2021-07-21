package com.bygn.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class QuizList extends  AppCompatActivity {

    //AppCompatActivity,
    FirebaseDatabase database,databaseSt;
    FirebaseFirestore db;
    FloatingActionButton addQuizButton;
    RecyclerView listOfQuiz;
    QuizAdapter adapter;
    String subject;
    String title;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);
        subject = getIntent().getStringExtra("Subject");
        Log.i("sub","subb is : "+subject);
        database = FirebaseDatabase.getInstance();
        databaseSt = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        addQuizButton = findViewById(R.id.addQuiz);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        listOfQuiz = findViewById(R.id.quizList);
        listOfQuiz.setHasFixedSize(true);
        listOfQuiz.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<QuizDiscribe> options = new FirebaseRecyclerOptions.Builder<QuizDiscribe>()
                .setQuery(database.getReference("Users").child("Admin").child(userId).child(subject),QuizDiscribe.class)
                .build();
        adapter = new QuizAdapter(options);
        listOfQuiz.setAdapter(adapter);
        adapter.setOnItemClickListener(
                new QuizAdapter.OnQuizClick() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onItemClick(DataSnapshot quizTitle, int position) {
                        QuizDiscribe quizDiscribe = quizTitle.getValue(QuizDiscribe.class);
                        title = quizDiscribe.getTitle();
                        Toast.makeText(QuizList.this,"Thu is "+title,Toast.LENGTH_LONG).show();
                        Intent intent= new Intent(QuizList.this,QuizListToQuiz.class);
                        intent.putExtra("Subject",subject);
                        intent.putExtra("Title",title);
                        startActivity(intent);
                    }

                }
        );
        addQuizButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(QuizList.this, MainActivity2.class);
                        intent.putExtra("subjectName",subject);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("sub","title is : "+subject);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void removeItem(int position){
        //subjectList.remove(position);

        adapter.notifyItemRemoved(position);
    }
}