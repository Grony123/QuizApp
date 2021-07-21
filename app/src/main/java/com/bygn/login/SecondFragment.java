
package com.bygn.login;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class SecondFragment extends Fragment implements Dialog_opener.DialogListener{

    final static String DATA_RECEIVE = "data_receive";
    private static final  int PICK_IMAGE_REQUEST=1;
    private ImageButton btnAddQuestion;
    private Button saveQuestionBtn,saveQuizButton;
    private EditText Question;
    private RadioGroup radioGroup;
    private RadioButton radioButtonA;
    private RadioButton radioButtonB;
    private RadioButton radioButtonC;
    private RadioButton radioButtonD;
    private Button button;
    private ImageView imageQuestion;
    private Uri questionImageUri;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private DatabaseReference refQ;
    private StorageReference storageReference,storageReference2;
    int QuestionId;
    int QuizId;
    int questionNo = 1;
    Question_Options question_options;
    FirebaseFirestore db,db2;
    FirstFragment firstFragment;
    String userId;
    private String title;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity2 activity2 = (MainActivity2)getActivity();
        String sub = activity2.getSub();
        if(getArguments()!=null){
            SecondFragmentArgs args = SecondFragmentArgs.fromBundle(getArguments());
            title = args.getArgs();
            Log.i("title","Tile is : "+title);
        }
        radioGroup=view.findViewById(R.id.group_of_radios);
        btnAddQuestion=view.findViewById(R.id.addQuestion);
        saveQuestionBtn = view.findViewById(R.id.saveQuestion);
        saveQuizButton = view.findViewById(R.id.saveQuiz);
        Question = view.findViewById(R.id.QuestionText);
        radioButtonA=view.findViewById(R.id.radioA);
        radioButtonB=view.findViewById(R.id.radioB);
        radioButtonC=view.findViewById(R.id.radioC);
        radioButtonD=view.findViewById(R.id.radioD);
        imageQuestion = view.findViewById(R.id.QuestionImage);
        button = view.findViewById(R.id.add_ansers_to_question);
        question_options = new Question_Options();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
       // ref = firebaseDatabase.getInstance().getReference().child("Quiz");
        storageReference =  FirebaseStorage.getInstance().getReference().child("ForSolving").child(sub).child(title);
        storageReference2 = FirebaseStorage.getInstance().getReference().child("Admin").child(userId).child(sub).child(title);
        db =  FirebaseFirestore.getInstance();

        saveQuestionBtn.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        DocumentReference doc= db.collection(userId)
                                .document(sub)
                                .collection(title)
                                .document("Question "+String.valueOf(questionNo));
                        DocumentReference docA= db.collection("Admin")
                                .document(sub)
                                .collection(title)
                                .document("Question "+String.valueOf(questionNo));
                        Map<String, Object> Quiz = new HashMap<>();
                        Quiz.put("Question", Question.getText().toString());
                        Quiz.put("optionA", radioButtonA.getText().toString());
                        Quiz.put("optionB", radioButtonB.getText().toString());
                        Quiz.put("optionC", radioButtonC.getText().toString());
                        Quiz.put("optionD", radioButtonD.getText().toString());
                        Quiz.put("Id",questionNo);
                        // Quiz.put("ImageUri",uri);
                        if(radioButtonA.isChecked()){
                            Quiz.put("CorrectOption", radioButtonA.getText().toString());
                        }else if(radioButtonB.isChecked()){
                            Quiz.put("CorrectOption", radioButtonB.getText().toString());
                        }else if(radioButtonC.isChecked()){
                            Quiz.put("CorrectOption", radioButtonC.getText().toString());
                        }else if(radioButtonD.isChecked()){
                            Quiz.put("CorrectOption", radioButtonD.getText().toString());
                        }

                        doc.set(Quiz)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(),"Success",Toast.LENGTH_LONG).show();
                                    }


                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(),"fail",Toast.LENGTH_LONG).show();
                                    }
                                });
                        docA.set(Quiz)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Toast.makeText(getContext(),"Success",Toast.LENGTH_LONG).show();
                                    }


                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                       // Toast.makeText(getContext(),"fail",Toast.LENGTH_LONG).show();
                                    }
                                });
                        uploadImage(doc,docA,"Question_"+String.valueOf(questionNo));
                        questionNo++;
                    }
                }
        );
        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Question.getText().clear();
                radioGroup.clearCheck();
                radioButtonA.setText("");
                radioButtonB.setText("");
                radioButtonC.setText("");
                radioButtonD.setText("");
                imageQuestion.setImageBitmap(null);
                imageQuestion.destroyDrawingCache();
                // NavHostFragment.findNavController(SecondFragment.this)
                //       .navigate(R.id.action_SecondFragment_self);
            }
        });
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog();
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
        saveQuizButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SecondFragmentDirections.ActionSecondFragmentToThirdFragment2 action =
                                SecondFragmentDirections.actionSecondFragmentToThirdFragment2();
                        action.setArgsT(title);
                         NavHostFragment.findNavController(SecondFragment.this)
                             .navigate(action);
                    }
                }
        );
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
    public void openDialog(){
        Dialog_opener dialogOptions = new Dialog_opener();
        dialogOptions.show(getChildFragmentManager(),"options");
    }

    @Override
    public void applyTextChanges(String textA, String textB, String textC, String textD) {
        radioButtonA.setText(textA);
        radioButtonB.setText(textB);
        radioButtonC.setText(textC);
        radioButtonD.setText(textD);
    }

    private String getFileExtinction(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
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
            Toast.makeText(getContext(),"File is Not Selected",Toast.LENGTH_LONG).show();

        }

    }

}