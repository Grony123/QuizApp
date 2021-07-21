package com.bygn.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubjectAdapterStudent extends RecyclerView.Adapter<SubjectAdapterStudent.myViewHolder> {
    private SubjectAdapterStudent.OnSubjectClick subjectClick;
    List<String> subjects;
    LayoutInflater inflater;
    public SubjectAdapterStudent(Context ctx, List<String> subjects){
        this.subjects = subjects;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.subjectcardstudent,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapterStudent.myViewHolder holder, int position) {
        holder.subject.setText(subjects.get(position));
        // Log.i("qu","qile is : "+subjects.get(position));

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView subject;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.subjectNameS);
            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION && subjectClick!=null ){
                                subjectClick.onItemClick(subjects.get(position),position);
                            }
                        }
                    }
            );

        }
    }
    public interface OnSubjectClick{
        void onItemClick(String sub, int position);
    }
    public void setOnItemClickListener(SubjectAdapterStudent.OnSubjectClick onQuestionClick){
        subjectClick = onQuestionClick;
    }
}
