package com.hwy.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hwy.textview.FoldTextView;

public class FoldActivity extends AppCompatActivity {

    private FoldTextView ftv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fold);

        ftv = findViewById(R.id.ftv);


    }

    public void open(View view) {
        ftv.setState(true, 200);
    }

    public void fold(View view) {
        ftv.setState(false, 2000);
    }
}
