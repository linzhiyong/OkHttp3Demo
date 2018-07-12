package com.lzy.org.okhttp3.activity;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.lzy.org.okhttp3.LBaseActivity;
import com.lzy.org.okhttp3.R;
import com.lzy.org.okhttp3.http.FileCallback;
import com.lzy.org.okhttp3.http.FileNoProgressCallback;
import com.lzy.org.okhttp3.http.LOkHttp3Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 普通GET/POST的同步/异步请求实例
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 * https://www.jianshu.com/u/e664ba5d0800
 * https://github.com/linzhiyong
 * @time 2018/7/9
 * @desc
 */
public class OkHttpDemoDownloadFileActivity extends LBaseActivity {

    @BindView(R.id.progressBar1)
    ProgressBar progressBar;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_okhttp_demo_download_file;
    }

    @Override
    protected void initView() {
        setTitle(getIntent().getStringExtra("title"));
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.button1, R.id.button2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1: // 普通下载
                getFileAsync();
                break;

            case R.id.button2: // 带进度下载
                getFileProgressAsync();
                break;
        }
    }

    /**
     * 普通下载
     */
    private void getFileAsync() {
        String url = "http://172.20.32.19:8000/oa/userfiles/image/xushijie/061901.png";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("name", "value")
                .tag("getFileAsync")
                .build();

        final Call call = LOkHttp3Utils.okHttpClient().newCall(request);
        showDialog();
        call.enqueue(new FileNoProgressCallback(Environment.getExternalStorageDirectory().getAbsolutePath(), "test.png") {
            @Override
            public void onFinish(File file) {
                Message message = handler.obtainMessage();
                message.what = 0;
                message.obj = "下载成功：" + file.getName();
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                Message message = handler.obtainMessage();
                message.what = 0;
                message.obj = "错误：" + e.getMessage();
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 带进度下载
     */
    private void getFileProgressAsync() {
        String url = "http://172.20.32.19:8000/oa/userfiles/image/xushijie/061901.png";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("name", "value")
                .tag("getFileProgressAsync")
                .build();

        final Call call = LOkHttp3Utils.okHttpClient().newCall(request);
        showDialog();
        call.enqueue(new FileCallback(Environment.getExternalStorageDirectory().getAbsolutePath(), "test.png") {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(long total, float progress) {
                Message message = handler.obtainMessage();
                message.what = 2;
                message.obj = progress;
                handler.sendMessage(message);
            }

            @Override
            public void onFinish(File file) {
                Message message = handler.obtainMessage();
                message.what = 0;
                message.obj = "下载成功：" + file.getName();
                handler.sendMessage(message);
            }

            @Override
            public void onError(Exception e) {
                Message message = handler.obtainMessage();
                message.what = 0;
                message.obj = "错误：" + e.getMessage();
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
                    showToast(msg.obj.toString());
                    break;

                case 1:
                    try {
                        dealResponse((Response) msg.obj);
                    } catch (Exception e) {
                        showToast("错误：" + e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    progressBar.setProgress((int) ((float) msg.obj * 100));
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
            showToast(result);
            Log.i("r", result);
        } else {
            showToast("错误：" + response.message());
            Log.e("e", response.message());
        }
    }

}
