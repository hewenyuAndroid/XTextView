package com.hwy.example;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.TextView;
import android.widget.Toast;

import com.hwy.textview.XEditText;
import com.hwy.textview.listener.OnDrawableClickListener;

public class EditActivity extends AppCompatActivity {

    private XEditText et;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        et = findViewById(R.id.et);

        mContext = this;

        et.setOnDrawableClickListener(new OnDrawableClickListener() {
            @Override
            public void onDrawableLeftClickListener(TextView view) {
                Toast.makeText(mContext, "DrawableLeftClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawableRightClickListener(TextView view) {

            }
        });

//        et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);


    }

}
