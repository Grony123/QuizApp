package com.bygn.login;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

public class MainActivity2 extends AppCompatActivity {

    String sub;

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sub = getIntent().getStringExtra("subjectName");
        Bundle bundle = new Bundle();
        bundle.putString("edttext", sub);
        FirstFragment fragobj = new FirstFragment();
        fragobj.setArguments(bundle);

    }
}