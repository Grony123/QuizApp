package com.bygn.login;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.myViewHolder> {

    private OnSubjectClick subjectClick;
    List<String> subjects;
    LayoutInflater inflater;
    public SubjectAdapter(Context ctx,List<String> subjects){
        this.subjects = subjects;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.subjectcard,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.subject.setText(subjects.get(position));
       // Log.i("qu","qile is : "+subjects.get(position));

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView subject;
        ImageView deleteSubIcon;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.subjectName);
            deleteSubIcon = itemView.findViewById(R.id.deleteSubjectIcon);
            deleteSubIcon.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION && subjectClick!=null ){
                                subjectClick.onDeleteClick(subjects.get(position),position);
                            }
                        }
                    }
            );
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
        void onDeleteClick(String sub, int position);
    }
    public void setOnItemClickListener(OnSubjectClick onQuestionClick){
        subjectClick = onQuestionClick;
    }
}
