package com.bygn.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class QuizListToQuiz extends AppCompatActivity {

    RecyclerView recyclerView;
    FireAdapter fireAdapter;
    QuizDiscribe quizDiscribe;
    FirebaseFirestore db;
    DocumentReference documentReference;
    Query collectionReference;
    FirebaseDatabase database;
    DatabaseReference ref;
    String description;
    String[] array;
    TextView titleText,describeText;
    String title,subject,userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list_to_quiz);
        subject = getIntent().getStringExtra("Subject");
        title = getIntent().getStringExtra("Title");
        titleText = findViewById(R.id.QuizOfTitleA);
        describeText = findViewById(R.id.QuizOfDescriptionA);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        ref =FirebaseDatabase.getInstance().getReference().child("Users").child("Admin").child(userId).child(subject).child(title);
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("description").getValue()!=null) {
                            description = snapshot.child("description").getValue().toString();
                            describeText.setText(description);
                        }
                        // Log.i("fer","description is : "+description);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
        titleText.setText(title);
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recycleQuestions);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(QuizListToQuiz.this));

        FirestoreRecyclerOptions<Question_Options> options = new FirestoreRecyclerOptions.Builder<Question_Options>()
                .setQuery(db.collection(userId)
                        .document(subject)
                        .collection(title).orderBy("Id"), Question_Options.class)
                .build();
        fireAdapter = new FireAdapter(options);
        recyclerView.setAdapter(fireAdapter);
        fireAdapter.setOnItemClickListener(
                new FireAdapter.OnQuestionClick() {
                    @Override
                    public void onItemClick(DocumentSnapshot snapshot, int position) {
                        Question_Options question_options = snapshot.toObject(Question_Options.class);
                        String question = question_options.getQuestion();
                        String ans = question_options.getCorrectOption();
                        String id = String.valueOf(question_options.getId());
                        Intent intent = new Intent(QuizListToQuiz.this,QuestionsClick.class);
                        intent.putExtra("Idi",id);
                        intent.putExtra("Sub",subject);
                        intent.putExtra("title",title);
                        QuizListToQuiz.this.startActivity(intent);

                    }
                }
        );
    }
    @Override
    public void onStart() {
        super.onStart();
        fireAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        fireAdapter.stopListening();
    }
}