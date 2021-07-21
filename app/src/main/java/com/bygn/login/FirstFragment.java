package com.bygn.login;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class FirstFragment extends Fragment {


    int maxId;
    QuizDiscribe quizDiscribe;
    EditText titleOfQuiz;
    EditText descriptionOfQuiz;
    FirebaseFirestore db;
    FirebaseDatabase database,databaseSt;
    DatabaseReference ref,refSt;
    String title;
    String userId;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        //sub = getArguments().getString("edttext");
      //  Log.i("fer","Sub is : "+sub);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity2 activity2 = (MainActivity2)getActivity();
        String sub = activity2.getSub();
        Button question_to_Quiz = view.findViewById(R.id.Add_Questions_to_Quiz);
        titleOfQuiz = view.findViewById(R.id.QuizTitle);
        descriptionOfQuiz = view.findViewById(R.id.QuizDescription);
        quizDiscribe = new QuizDiscribe();
        db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseSt = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        ref = database.getReference("Users").child("Admin").child(userId).child(sub);
        refSt = database.getReference("Subjects").child(sub);
       /* ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            maxId = (int) snapshot.getChildrenCount();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );*/

        question_to_Quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleOfQuiz.getText().toString();
                if(TextUtils.isEmpty(titleOfQuiz.getText().toString())){
                    Toast.makeText(getContext(), "Title have to be given", Toast.LENGTH_LONG).show();
                    return;
                }
                ref = ref.child(title);
                refSt = refSt.child(title);
                quizDiscribe.setTitle(titleOfQuiz.getText().toString());
                quizDiscribe.setDescription(descriptionOfQuiz.getText().toString());
                ref.setValue(quizDiscribe);
                refSt.setValue(quizDiscribe);
                FirstFragmentDirections.ActionFirstFragmentToSecondFragment action =
                        FirstFragmentDirections.actionFirstFragmentToSecondFragment();
                action.setArgs(titleOfQuiz.getText().toString());
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(action);

            }
        });


    }



}