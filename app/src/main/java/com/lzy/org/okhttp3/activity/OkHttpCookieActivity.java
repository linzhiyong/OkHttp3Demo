package com.lzy.org.okhttp3.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lzy.org.okhttp3.LBaseActivity;
import com.lzy.org.okhttp3.R;
import com.lzy.org.okhttp3.http.LOkHttp3Utils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Cookie持久化
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 * https://github.com/linzhiyong
 * https://www.jianshu.com/u/e664ba5d0800
 * @time 2018/7/20
 * @desc
 */
public class OkHttpCookieActivity extends LBaseActivity {

    @BindView(R.id.textView)
    TextView textView1;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_okhttp_demo_cookie;
    }

    @Override
    protected void initView() {
        setTitle(getIntent().getStringExtra("title"));
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.button, R.id.button1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                requestWithCookie();
                break;

            case R.id.button1:
                LOkHttp3Utils.clearCookies();
                break;
        }
    }

    private void requestWithCookie() {
        Request request = new Request.Builder()
                .get()
                .url("https://blog.csdn.net/u012527802/article/details/81013772")
                .tag("requestWithCookie")
                .build();

        final Call call = LOkHttp3Utils.okHttpClient1(this).newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = handler.obtainMessage();
                message.what = 0;
                message.obj = e.getMessage();
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = response;
                handler.sendMessage(message);
            }
        });
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            hideDialog();
            switch (msg.what) {
                case 0:
                    textView1.setText("错误：" + msg.obj);
                    break;

                case 1:
                    try {
                        dealResponse((Response) msg.obj);
                    } catch (Exception e) {
                        textView1.setText("错误：" + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
            }
            return false;
        }
    });

    /**
     * 处理返回结果
     *
     * @param response response
     */
    private void dealResponse(Response response) throws Exception {
        int code = response.code();
        if (code == 200) {
            String result = response.body().string();
            textView1.setText(result);
            Log.i("r", result);
        } else {
            textView1.setText("错误：" + response.message());
            Log.e("e", response.message());
        }
    }

}
