package com.lzy.org.okhttp3.activity;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.lzy.org.okhttp3.LBaseActivity;
import com.lzy.org.okhttp3.R;
import com.lzy.org.okhttp3.constant.Constants;
import com.lzy.org.okhttp3.http.LOkHttp3Utils;
import com.lzy.org.okhttp3.listener.ProgressListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * POST请求上传单/多文件，上传进度
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 * https://www.jianshu.com/u/e664ba5d0800
 * https://github.com/linzhiyong
 * @time 2018/7/9
 * @desc
 */
public class OkHttpDemoUploadFileActivity extends LBaseActivity {

    @BindView(R.id.progressBar1)
    ProgressBar progressBar;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_okhttp_demo_upload_file;
    }

    @Override
    protected void initView() {
        setTitle(getIntent().getStringExtra("title"));
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1: // 上传单文件file
                postFileAsync();
                break;

            case R.id.button2: // 带进度上传单文件file
                postFileProgressAsync();
                break;

            case R.id.button3: // 上传多文件files
                postFilesAsync();
                break;
        }
    }

    /**
     * 异步POST请求 上传单文件 file
     */
    private void postFileAsync() {
        String url = Constants.BASE_URL + "/request7";

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file, false);
            writer.append("这是文件内容");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        // 方式一
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file1", file.getName(), fileBody)
                .build();

        // 方式二
        RequestBody requestBody1 = new MultipartBody.Builder()
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"file1\"; filename=\"" + file.getName() + "\""),
                        fileBody)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("name", "value")
                .tag("postFileAsync")
                .build();

        final Call call = LOkHttp3Utils.okHttpClient().newCall(request);
        showDialog();
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

    /**
     * 带进度上传单文件file
     */
    private void postFileProgressAsync() {
        String url = Constants.BASE_URL + "/request7";

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file, false);
            writer.append("这是文件内容");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody fileBody = LOkHttp3Utils.createProgressRequestBody(MediaType.parse("application/octet-stream"), file, new ProgressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(final long total, final float progress) {
                Message message = handler.obtainMessage();
                message.what = 2;
                message.obj = progress;
                handler.sendMessage(message);
            }

            @Override
            public void onFinish(File file) {

            }

            @Override
            public void onError(Exception e) {

            }
        });

        // 方式一
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file1", file.getName(), fileBody)
                .build();

        // 方式二
        RequestBody requestBody1 = new MultipartBody.Builder()
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"file1\"; filename=\"" + file.getName() + "\""),
                        fileBody)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("name", "value")
                .tag("postFileAsync")
                .build();

        final Call call = LOkHttp3Utils.okHttpClient().newCall(request);
        showDialog();
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

    /**
     * 异步POST请求 上传多文件 files
     */
    private void postFilesAsync() {
        String url = Constants.BASE_URL + "/request8";

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file, false);
            writer.append("这是文件内容");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        // 方式一
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file1", file.getName(), fileBody)
                .addFormDataPart("file2", file.getName(), fileBody)
                .build();

        // 方式二
        RequestBody requestBody1 = new MultipartBody.Builder()
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"file1\"; filename=\"" + file.getName() + "\""),
                        fileBody)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"file2\"; filename=\"" + file.getName() + "\""),
                        fileBody)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("name", "value")
                .tag("postFilesAsync")
                .build();

        final Call call = LOkHttp3Utils.okHttpClient().newCall(request);
        showDialog();
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
                    showToast("错误：" + msg.obj);
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
