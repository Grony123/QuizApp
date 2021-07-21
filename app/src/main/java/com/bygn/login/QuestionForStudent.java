package com.bygn.login;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuestionForStudent extends AppCompatActivity implements View.OnClickListener {

    TextView question,timer;
    Button optA,optB,optC,optD;
    ImageView imageQuestion;
    private List<Question_Options> questionList;
    private int questionNo,score;
    private CountDownTimer countDownTimer;
    StorageReference storageReference;
    String subject,title;
    private FirebaseFirestore db;
    private Dialog loadingDialogue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_for_student);
        subject = getIntent().getStringExtra("Subject");
        title = getIntent().getStringExtra("Title");
        question = findViewById(R.id.questionForStudent);
        timer = findViewById(R.id.timer);
        imageQuestion = findViewById(R.id.QuizImageStudent);

        optA = findViewById(R.id.optionA_student);
        optB = findViewById(R.id.optionB_student);
        optC = findViewById(R.id.optionC_student);
        optD = findViewById(R.id.optionD_student);

        loadingDialogue = new Dialog(QuestionForStudent.this);
        loadingDialogue.setContentView(R.layout.loading);
        loadingDialogue.setCancelable(false);
        loadingDialogue.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialogue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialogue.show();

        db =  FirebaseFirestore.getInstance();
        optA.setOnClickListener(this);
        optB.setOnClickListener(this);
        optC.setOnClickListener(this);
        optD.setOnClickListener(this);

        getQuestionList();
    }

    private void getQuestionList(){
        questionList = new ArrayList<>();
        db.collection("Admin").document(subject).collection(title)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            int i=1;
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String imageurla = "null";
                                if(document.get("ImageUrl")!=null){
                                    imageurla = "present";
                                }
                               questionList.add(new Question_Options(document.getString("Question"),
                                       document.getString("optionA"),
                                       document.getString("optionB"),
                                       document.getString("optionC"),
                                       document.getString("optionD"),
                                       document.getString("CorrectOption"),
                                       imageurla,
                                       i//Integer.valueOf(document.getString("Id"))
                               ));
                               i++;
                            }
                            setQuestion();
                        } else {
                            Toast.makeText(QuestionForStudent.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                        loadingDialogue.cancel();
                    }
                });
       /* questionList.add(new Question_Options("Q1","A","B","C","D","A","",1));
        questionList.add(new Question_Options("Q2","B","B","C","D","C","",1));
        questionList.add(new Question_Options("Q3","C","B","C","D","D","",1));
        questionList.add(new Question_Options("Q4","D","B","C","D","B","",1));
        setQuestion();*/
    }

    private void setQuestion(){
        timer.setText(String.valueOf(20));
        question.setText(questionList.get(0).getQuestion());
        optA.setText(questionList.get(0).getOptionA());
        optB.setText(questionList.get(0).getOptionB());
        optC.setText(questionList.get(0).getOptionC());
        optD.setText(questionList.get(0).getOptionD());
        if(questionList.get(0).getImageUrl().equals("present")) {
            try {
                storageReference = FirebaseStorage.getInstance().getReference().child("ForSolving").child(subject)
                        .child(title).child("Question_" + String.valueOf(questionList.get(0).getId()) + ".jpg");
                File localFile = File.createTempFile("Question_" + String.valueOf(questionList.get(0).getId()), "jpg");
                storageReference.getFile(localFile)
                        .addOnSuccessListener(
                                new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        imageQuestion.setImageBitmap(bitmap);
                                    }
                                }
                        ).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(QuestionForStudent.this, "Error occurred", Toast.LENGTH_LONG).show();
                            }
                        }
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        questionNo = 0;
        score = 0;
        startTimer();
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(22000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished<20000)
                    timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                changeQuestion();
            }
        };
        countDownTimer.start();
    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        String selectectOption = "";
        switch (v.getId()){
            case R.id.optionA_student:
                selectectOption = optA.getText().toString();
                break;
            case R.id.optionB_student:
                selectectOption = optB.getText().toString();
                break;
            case R.id.optionC_student:
                selectectOption = optC.getText().toString();
                break;
            case R.id.optionD_student:
                selectectOption = optD.getText().toString();
                break;
            default:
        }
        countDownTimer.cancel();
        checkAnser(selectectOption,v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnser(String selectedOption, View view){
        if(selectedOption.equals(questionList.get(questionNo).CorrectOption)){
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        }else{
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            if (optA.getText().toString().equals(questionList.get(questionNo).CorrectOption)) {
                optA.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            }
            else if(optB.getText().toString().equals(questionList.get(questionNo).CorrectOption)){
                optB.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            }
            else if(optC.getText().toString().equals(questionList.get(questionNo).CorrectOption)){
                optC.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            }
            else if(optD.getText().toString().equals(questionList.get(questionNo).CorrectOption)){
                optD.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            }
            //wrong
        }
        Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        changeQuestion();
                    }
                },2000
        );

    }

    private void changeQuestion(){
        if(questionNo < questionList.size() - 1){

            questionNo++;
            playAnimation(question,0,0);
            playAnimation(optA,0,1);
            playAnimation(optB,0,2);
            playAnimation(optC,0,3);
            playAnimation(optD,0,4);
            playAnimation(imageQuestion,0,5);
            timer.setText(String.valueOf(20));
            startTimer();

        }else{
            Intent intent = new Intent(QuestionForStudent.this,ScoreActivity.class);
            intent.putExtra("Score",String.valueOf(score)+"/"+String.valueOf(questionNo+1));
            startActivity(intent);
            QuestionForStudent.this.finish();
        }
    }

    private void playAnimation(View view,final int value,int viewNo){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if(value == 0){
                                    switch (viewNo){
                                        case 0:
                                            ((TextView)view).setText(questionList.get(questionNo).getQuestion());
                                            break;
                                        case 1:
                                            ((Button)view).setText(questionList.get(questionNo).getOptionA());
                                            break;
                                        case 2:
                                            ((Button)view).setText(questionList.get(questionNo).getOptionB());
                                            break;
                                        case 3:
                                            ((Button)view).setText(questionList.get(questionNo).getOptionC());
                                            break;
                                        case 4:
                                            ((Button)view).setText(questionList.get(questionNo).getOptionD());
                                            break;
                                        case 5:
                                            ((ImageView)view).setImageBitmap(null);
                                            if(questionList.get(questionNo).getImageUrl().equals("present")) {
                                                try {
                                                    storageReference = FirebaseStorage.getInstance().getReference().child("ForSolving").child(subject)
                                                            .child(title).child("Question_" + String.valueOf(questionList.get(questionNo).getId()) + ".jpg");
                                                    File localFile = File.createTempFile("Question_" + String.valueOf(questionList.get(questionNo).getId()), "jpg");
                                                    storageReference.getFile(localFile)
                                                            .addOnSuccessListener(
                                                                    new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                                            ((ImageView)view).setImageBitmap(bitmap);
                                                                        }
                                                                    }
                                                            ).addOnFailureListener(
                                                            new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(QuestionForStudent.this, "Error occurred", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                    );
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            break;
                                    }
                                    if(viewNo!=0 && viewNo!=5){

                                        ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4169E1")));
                                    }
                                    playAnimation(view,1,viewNo);
                                }

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }
                );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
    }
}