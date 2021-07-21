package com.bygn.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QuestionsClick extends AppCompatActivity implements Dialog_opener.DialogListener{

    String id,title,subject;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    private static final  int PICK_IMAGE_REQUEST=1;
    StorageReference storageReferenceX,storageReference,storageReference2;
    EditText Question;
    RadioGroup radioGroup;
    RadioButton radioButtonA;
    RadioButton radioButtonB;
    RadioButton radioButtonC;
    RadioButton radioButtonD;
    Button updateQuestionButton;
    Button updateOptionsButton;
    ImageView imageQuestion;
    Question_Options question_options;
    Uri questionImageUri;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_click);
        id = getIntent().getStringExtra("Idi");
        subject = getIntent().getStringExtra("Sub");
        title = getIntent().getStringExtra("title");
        radioGroup=findViewById(R.id.group_of_radiosB);
        Question = findViewById(R.id.QuestionTextB);
        radioButtonA=findViewById(R.id.radioA_Q);
        radioButtonB=findViewById(R.id.radioB_Q);
        radioButtonC=findViewById(R.id.radioC_Q);
        radioButtonD=findViewById(R.id.radioD_Q);
        imageQuestion = findViewById(R.id.QuestionImageB);
        updateOptionsButton = findViewById(R.id.updateOptions);
        updateQuestionButton = findViewById(R.id.updateQuestion);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        storageReference =  FirebaseStorage.getInstance().getReference().child("ForSolving").child(subject).child(title);
        storageReference2 = FirebaseStorage.getInstance().getReference().child("Admin").child(userId).child(subject).child(title);

        question_options = new Question_Options();
        firestore = FirebaseFirestore.getInstance();
        storageReferenceX = FirebaseStorage.getInstance().getReference().child("Admin").child(userId).child(subject)
                .child(title).child("Question_"+id+".jpg");
        DocumentReference doc= firestore.collection(userId)
                .document(subject)
                .collection(title)
                .document("Question "+id);
        doc.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //Log.d("question", "DocumentSnapshot data: " + document.get("Question"));

                                Question.setText(document.get("Question").toString());
                                radioButtonA.setText(document.get("optionA").toString());
                                radioButtonB.setText(document.get("optionB").toString());
                                radioButtonC.setText(document.get("optionC").toString());
                                radioButtonD.setText(document.get("optionD").toString());

                                if (document.get("optionA").toString().equals(document.get("CorrectOption").toString())) {
                                    radioButtonA.setChecked(true);
                                }
                                else if(document.get("optionB").toString().equals(document.get("CorrectOption").toString())){
                                    radioButtonB.setChecked(true);
                                }
                                else if(document.get("optionC").toString().equals(document.get("CorrectOption").toString())){
                                    radioButtonC.setChecked(true);
                                }
                                else if(document.get("optionD").toString().equals(document.get("CorrectOption").toString())){
                                    radioButtonD.setChecked(true);
                                }

                                if(document.get("ImageUrl")!=null){
                                    try {
                                        File localFile = File.createTempFile("Question_"+id,"jpg");
                                        storageReferenceX.getFile(localFile)
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
                                                        Toast.makeText(QuestionsClick.this,"Error occured",Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                        );
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                });
        updateOptionsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog();
                    }
                }
        );
        updateQuestionButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference docA= firestore.collection("Admin")
                                .document(subject)
                                .collection(title)
                                .document("Question "+id);
                        Map<String, Object> Quiz = new HashMap<>();
                        Quiz.put("Question", Question.getText().toString());
                        Quiz.put("optionA", radioButtonA.getText().toString());
                        Quiz.put("optionB", radioButtonB.getText().toString());
                        Quiz.put("optionC", radioButtonC.getText().toString());
                        Quiz.put("optionD", radioButtonD.getText().toString());
                        if(radioButtonA.isChecked()){
                            Quiz.put("CorrectOption", radioButtonA.getText().toString());
                        }else if(radioButtonB.isChecked()){
                            Quiz.put("CorrectOption", radioButtonB.getText().toString());
                        }else if(radioButtonC.isChecked()){
                            Quiz.put("CorrectOption", radioButtonC.getText().toString());
                        }else if(radioButtonD.isChecked()){
                            Quiz.put("CorrectOption", radioButtonD.getText().toString());
                        }

                        doc.update(Quiz)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(QuestionsClick.this,"Success",Toast.LENGTH_LONG).show();
                                    }


                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(QuestionsClick.this,"fail",Toast.LENGTH_LONG).show();
                                    }
                                });
                        docA.update(Quiz)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                       // Toast.makeText(QuestionsClick.this,"Success",Toast.LENGTH_LONG).show();
                                    }


                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                       // Toast.makeText(QuestionsClick.this,"fail",Toast.LENGTH_LONG).show();
                                    }
                                });
                        uploadImage(doc,docA,"Question_"+id);
                    }
                }
        );

        imageQuestion.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openQuestionImage();
                    }
                }
        );
    }
    public void openDialog(){
        Dialog_opener dialogOptions = new Dialog_opener();
        dialogOptions.show(getSupportFragmentManager(),"options");
    }

    @Override
    public void applyTextChanges(String textA, String textB, String textC, String textD) {
        radioButtonA.setText(textA);
        radioButtonB.setText(textB);
        radioButtonC.setText(textC);
        radioButtonD.setText(textD);
    }
    public void openQuestionImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST || resultCode == RESULT_OK || data!=null ||
                data.getData()!=null){

            questionImageUri = data.getData();
            Picasso.get().load(questionImageUri).into(imageQuestion);

        }
    }
    private String getFileExtinction(Uri uri){
        ContentResolver contentResolver = QuestionsClick.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImage(DocumentReference doc,DocumentReference docA,String str){

        if(questionImageUri!=null){
            StorageReference filereference=  storageReference.child(str+"."+
                    getFileExtinction(questionImageUri));
            StorageReference filereference2=  storageReference2.child(str+"."+
                    getFileExtinction(questionImageUri));
            filereference.putFile(questionImageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //Toast.makeText(getContext(),"Upload Successfull",Toast.LENGTH_LONG).show();
                                    String upload = taskSnapshot.getUploadSessionUri().toString();
                                    Map<String, Object> Quiz = new HashMap<>();
                                    Quiz.put("ImageUrl",str );
                                    //doc.update(Quiz);
                                    docA.update(Quiz);
                                }
                            }
                    )
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                    );
            filereference2.putFile(questionImageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //Toast.makeText(getContext(),"Upload Successfull",Toast.LENGTH_LONG).show();
                                    String upload = taskSnapshot.getUploadSessionUri().toString();
                                    Map<String, Object> Quiz = new HashMap<>();
                                    Quiz.put("ImageUrl",str );
                                    doc.update(Quiz);
                                    //docA.update(Quiz);
                                }
                            }
                    )
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                    );

        }else{
            Toast.makeText(QuestionsClick.this,"File is Not Selected",Toast.LENGTH_LONG).show();

        }

    }
}