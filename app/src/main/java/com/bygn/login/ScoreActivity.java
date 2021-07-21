package com.bygn.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        score = findViewById(R.id.scoreNumber);
        String scorePr = getIntent().getStringExtra("Score");
        score.setText(scorePr);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ScoreActivity.this.finish();
    }
}