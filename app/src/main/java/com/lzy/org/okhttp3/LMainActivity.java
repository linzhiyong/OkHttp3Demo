package com.lzy.org.okhttp3;

import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.BindView;

/**
 * 主页面
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 *       https://github.com/linzhiyong
 *       https://www.jianshu.com/u/e664ba5d0800
 * @time 2018/7/9
 * @desc
 */
public class LMainActivity extends LBaseActivity {

    private String url = "";

    @BindView(R.id.listView1)
    protected ListView listView;

    private String[][] activities = {{
            "GET请求（同步/异步）",
            "POST请求（同步/异步）",
            "POST请求提交String",
            "POST请求提交实体/JSON",
            "POST请求提交普通Form表单",
            "POST请求提交混合Form表单",
            "POST请求提交单/多文件（带进度条）",
            "GET请求下载文件（带进度条）",
    }, {
            "com.lzy.org.okhttp3.activity.OkHttpDemoActivity",
            "com.lzy.org.okhttp3.activity.OkHttpDemoActivity",
            "com.lzy.org.okhttp3.activity.OkHttpDemoActivity",
            "com.lzy.org.okhttp3.activity.OkHttpDemoActivity",
            "com.lzy.org.okhttp3.activity.OkHttpDemoActivity",
            "com.lzy.org.okhttp3.activity.OkHttpDemoActivity",
            "com.lzy.org.okhttp3.activity.OkHttpDemoUploadFileActivity",
            "com.lzy.org.okhttp3.activity.OkHttpDemoDownloadFileActivity",
    }};

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String target = activities[1][position];
                ComponentName name = new ComponentName(getPackageName(), target);
                Intent intent = new Intent();
                intent.putExtra("title", activities[0][position]);
                intent.setComponent(name);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activities[0]);
        listView.setAdapter(adapter);
    }
}
