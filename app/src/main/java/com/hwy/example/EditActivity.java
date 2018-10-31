package com.hwy.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.hwy.textview.XEditText;
import com.hwy.textview.listener.OnDrawableClickListener;

public class EditActivity extends AppCompatActivity {

    private XEditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        et = findViewById(R.id.et);

        et.setOnDrawableClickListener(new OnDrawableClickListener() {
            @Override
            public void onDrawableLeftClickListener(TextView view) {
                Log.e("TAG", "onDrawableLeftClick");
            }

            @Override
            public void onDrawableRightClickListener(TextView view) {

            }
        });

    }

}
