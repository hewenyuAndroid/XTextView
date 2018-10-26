package com.hwy.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hwy.textview.XTextView;

public class MainActivity extends AppCompatActivity {

    private XTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);

    }

}
