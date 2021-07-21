package com.bygn.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;

public class QuizAdapterStudent extends FirebaseRecyclerAdapter<QuizDiscribe,QuizAdapterStudent.myViewHolder> {

    OnQuizClick quizClick;

    public QuizAdapterStudent(@NonNull FirebaseRecyclerOptions<QuizDiscribe> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull QuizDiscribe model) {
        holder.title.setText(model.getTitle());

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quizcardstudent,parent,false);
        return new QuizAdapterStudent.myViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView title;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.quizNameSt);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION && quizClick!=null ){
                                quizClick.onItemClick(getSnapshots().getSnapshot(position),position);
                            }
                        }
                    }
            );
        }
    }
    public interface OnQuizClick{
        void onItemClick(DataSnapshot quizTitle, int position);

    }
    public void setOnItemClickListener(QuizAdapterStudent.OnQuizClick onQuestionClick){
        quizClick = onQuestionClick;
    }
}

