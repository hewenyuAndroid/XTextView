package com.hwy.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hwy.textview.XTextView;
import com.hwy.textview.listener.OnDrawableClickListener;

public class MainActivity extends AppCompatActivity {

    private XTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);

        tv.setOnDrawableClickListener(new OnDrawableClickListener() {
            @Override
            public void onDrawableLeftClickListener(TextView view) {
                Log.e("TAG", "onLeftClick");
            }

            @Override
            public void onDrawableRightClickListener(TextView view) {
                Log.e("TAG", "onRightClick");
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "click");
            }
        });

    }

    public void startEdit(View view) {
        startActivity(new Intent(this, EditActivity.class));
    }
}
