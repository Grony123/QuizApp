package com.bygn.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddSubjectDialog extends AppCompatDialogFragment {

    private EditText subjectName;
    SubjectDialogListener listener;
    public AddSubjectDialog(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.addsubject,null);
        builder.setView(view)
                .setTitle("Subject")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = subjectName.getText().toString();
                        listener = (AddSubjectDialog.SubjectDialogListener) getActivity();
                        assert listener != null;
                        listener.applyChanges(name);
                        Toast.makeText(getContext(),"Subject is "+name,Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        subjectName = view.findViewById(R.id.nameofsubject);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
           listener = (AddSubjectDialog.SubjectDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+
                    " must implement DialogListener");
        }
    }

    public interface SubjectDialogListener{
        void applyChanges(String text);

    }
}
