package com.bygn.login;

import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuizListStudent extends AppCompatActivity {

    FirebaseDatabase database,databaseSt;
    FirebaseFirestore db;
    RecyclerView listOfQuiz;
    QuizAdapterStudent adapter;
    String subject;
    String title;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list_student);
        subject = getIntent().getStringExtra("Subject");
        Log.i("sub","subb is : "+subject);
        database = FirebaseDatabase.getInstance();
        databaseSt = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        listOfQuiz = findViewById(R.id.quizListStudent);
        listOfQuiz.setHasFixedSize(true);
        listOfQuiz.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<QuizDiscribe> options = new FirebaseRecyclerOptions.Builder<QuizDiscribe>()
                .setQuery(database.getReference("Subjects").child(subject),QuizDiscribe.class)
                .build();
        adapter = new QuizAdapterStudent(options);
        listOfQuiz.setAdapter(adapter);
        adapter.setOnItemClickListener(
                new QuizAdapterStudent.OnQuizClick() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onItemClick(DataSnapshot quizTitle, int position) {
                        QuizDiscribe quizDiscribe = quizTitle.getValue(QuizDiscribe.class);
                        title = quizDiscribe.getTitle();
                        Intent intent= new Intent(QuizListStudent.this,QuestionForStudent.class);
                        intent.putExtra("Subject",subject);
                        intent.putExtra("Title",title);
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

}