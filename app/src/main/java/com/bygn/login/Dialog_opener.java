package com.bygn.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

public class Dialog_opener extends AppCompatDialogFragment {
    private DialogListener listener,listenerA;
    boolean m;
    private EditText optA;
    private EditText optB;
    private EditText optC;
    private EditText optD;
    public Dialog_opener() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.add_options,null);
        builder.setView(view)
                .setTitle("option")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ans1 = optA.getText().toString();
                        String ans2 = optB.getText().toString();
                        String ans3 = optC.getText().toString();
                        String ans4 = optD.getText().toString();
                        if(inflater.getContext().getClass() == QuestionsClick.class) {
                            //listener = (DialogListener) getParentFragment();
                            listener = (DialogListener) getActivity();
                            listener.applyTextChanges(ans1, ans2, ans3, ans4);
                        }else {
                            listener = (DialogListener) getParentFragment();
                            listener.applyTextChanges(ans1, ans2, ans3, ans4);
                        }
                    }
                });
        optA = view.findViewById(R.id.optionA);
        optB = view.findViewById(R.id.optionB);
        optC = view.findViewById(R.id.optionC);
        optD = view.findViewById(R.id.optionD);
        return builder.create();
    }


    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        try {
            listener = (DialogListener) childFragment;
        } catch (ClassCastException e) {
           throw new ClassCastException(childFragment.toString()+
                   " must implement DialogListener");
        }
    }

   /* @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+
                    " must implement DialogListener");
        }
    }*/

    public interface DialogListener{
        void applyTextChanges(String textA,String textB,String textC,String textD);

    }
}
