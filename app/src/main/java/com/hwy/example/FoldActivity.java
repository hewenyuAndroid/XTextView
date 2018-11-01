package com.hwy.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hwy.example.entity.NoticeBean;
import com.hwy.textview.FoldTextView;

import java.util.ArrayList;
import java.util.List;

public class FoldActivity extends AppCompatActivity {

    private ListView mListView;

    private List<NoticeBean> mDatas;

    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fold);

        mListView = findViewById(R.id.listview);

        mInflater = LayoutInflater.from(this);

        init();

        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mDatas.size();
            }

            @Override
            public Object getItem(int position) {
                return mDatas.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                ViewHolder viewHolder = null;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.adapter_fold, parent, false);
                    viewHolder.tvName = convertView.findViewById(R.id.tvName);
                    viewHolder.tvContent = convertView.findViewById(R.id.tvContent);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                NoticeBean bean = mDatas.get(position);
                viewHolder.tvContent.bindObjState(bean);

                viewHolder.tvName.setText(bean.getName());
                viewHolder.tvContent.setText(bean.getContent());

                return convertView;
            }
        });

    }


    static class ViewHolder {
        TextView tvName;
        FoldTextView tvContent;
    }


    private void init() {
        mDatas = new ArrayList<>();

        int count = 20;

        for (int i = 0; i < count; i++) {
            NoticeBean bean = new NoticeBean();
            mDatas.add(bean);
            bean.setName("position " + i);
            bean.setContent("测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，" +
                    "测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，" +
                    "测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，" +
                    "测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，" +
                    "测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，测试内容，" +
                    "测试内容，测试内容，测试内容，测试内容，");
        }

    }


}
