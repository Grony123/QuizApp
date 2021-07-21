package com.bygn.login;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class FireAdapter extends FirestoreRecyclerAdapter<Question_Options, FireAdapter.myViewHolder> {

    private OnQuestionClick questionClick;

    public FireAdapter(@NonNull FirestoreRecyclerOptions<Question_Options> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Question_Options model) {
        holder.question.setText(model.getQuestion());
       // Log.i("qu","qile is : "+model.getQuestion());
        holder.answer.setText(model.getCorrectOption());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        TextView answer;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.questionCard);
            answer = itemView.findViewById(R.id.ansCard);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION && questionClick!=null ){
                                questionClick.onItemClick(getSnapshots().getSnapshot(position),position);
                            }
                        }
                    }
            );
        }

    }

    public interface OnQuestionClick{
        void onItemClick(DocumentSnapshot snapshot,int position);
    }

    public void setOnItemClickListener(OnQuestionClick onQuestionClick){
        questionClick = onQuestionClick;
    }
}
