package com.bygn.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ThirdFragment extends Fragment {

    RecyclerView recyclerView;
    FireAdapter fireAdapter;
    QuizDiscribe quizDiscribe;
    FirebaseFirestore db;
    DocumentReference documentReference;
    Query collectionReference;
    FirebaseDatabase database;
    DatabaseReference ref;
    String title,description,userId;
    String[] array;
    TextView titleText,describeText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity2 activity2 = (MainActivity2)getActivity();
        String sub = activity2.getSub();
        if(getArguments()!=null){
            ThirdFragmentArgs args = ThirdFragmentArgs.fromBundle(getArguments());
            title = args.getArgsT();
           // Log.i("fer","Tile is : "+title);
        }
        titleText = view.findViewById(R.id.QuizOfTitle);
        describeText = view.findViewById(R.id.QuizOfDescription);
        // Log.i("fer","Tile is : "+title);
        array = new String[2];
        // database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        ref =FirebaseDatabase.getInstance().getReference().child("Users").child("Admin").child(userId).child(sub).child(title);
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        description = snapshot.child("description").getValue().toString();
                       // Log.i("fer","description is : "+description);
                        describeText.setText(description);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
        titleText.setText(title);
        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recycle1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirestoreRecyclerOptions<Question_Options> options = new FirestoreRecyclerOptions.Builder<Question_Options>()
                .setQuery(db.collection(userId)
                        .document(sub)
                        .collection(title).orderBy("Id"), Question_Options.class)
                .build();
        fireAdapter = new FireAdapter(options);
        recyclerView.setAdapter(fireAdapter);
        fireAdapter.setOnItemClickListener(
                new FireAdapter.OnQuestionClick() {
                    @Override
                    public void onItemClick(DocumentSnapshot snapshot, int position) {
                        Question_Options question_options = snapshot.toObject(Question_Options.class);
                        String id = String.valueOf(question_options.getId());
                        Intent intent = new Intent(getContext(),QuestionsClick.class);
                        intent.putExtra("Idi",id);
                        intent.putExtra("Sub",sub);
                        intent.putExtra("title",title);
                        getContext().startActivity(intent);

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