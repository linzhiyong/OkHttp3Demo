package com.lzy.org.okhttp3.http;

import android.content.Context;
import android.util.Log;

import com.lzy.org.okhttp3.http.cookie.CookieJarImpl;
import com.lzy.org.okhttp3.http.cookie.MemoryCookieStore;
import com.lzy.org.okhttp3.listener.ProgressListener;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * 提供OkHttpClient单例，同时列举okhttp基本使用示例
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 *       https://github.com/linzhiyong
 *       https://www.jianshu.com/u/e664ba5d0800
 * @time 2018/7/9
 * @desc
 */
public class LOkHttp3Utils {

    private static final int HTTP_TIME_OUT = 30;

    private static OkHttpClient okHttpClient = null;

    /**
     * 构建OkHttpClient对象，设置相应全局参数
     *
     * @return OkHttpClient
     */
    public static OkHttpClient okHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .readTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
                    .connectTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            Response response = chain.proceed(request);
                            return response;

                        }
                    })
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            Response response = chain.proceed(request);
                            return response;
                        }
                    });
            okHttpClient = builder.build();
        }

        return okHttpClient;
    }

    /**
     * 构建OkHttpClient对象，设置相应全局参数，持久化cookie
     *
     * @return OkHttpClient
     */
    public static OkHttpClient okHttpClient1(Context context) {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .readTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
                    .connectTimeout(HTTP_TIME_OUT, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            Response response = chain.proceed(request);
                            return response;

                        }
                    })
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            Response response = chain.proceed(request);
                            return response;
                        }
                    })
//                    .cookieJar(new CookieJarImpl(new PersistentCookieStore(context)));
                    .cookieJar(new CookieJarImpl(new MemoryCookieStore()));
            okHttpClient = builder.build();
        }

        return okHttpClient;
    }

    /**
     * 清除所有缓存cookie
     */
    public static void clearCookies() {
        if (okHttpClient != null && okHttpClient.cookieJar() instanceof CookieJarImpl) {
            CookieJarImpl cookieJar = (CookieJarImpl) okHttpClient.cookieJar();
            cookieJar.getCookieStore().removeAll();
        }
    }

    /**
     * 创建文件requestbody，自定义进度监听器
     *
     * @param mediaType
     * @param file
     * @param listener
     * @return
     */
    public static RequestBody createProgressRequestBody(final MediaType mediaType, final File file, final ProgressListener listener) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() throws IOException {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                listener.onStart();
                Source source;
                try {
                    source = Okio.source(file);
                    //sink.writeAll(source);
                    Buffer buf = new Buffer();
                    Long remaining = contentLength();
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        listener.onProgress(contentLength(), 1 - (float)(remaining -= readCount) / contentLength());
                    }
                    listener.onFinish(file);
                } catch (Exception e) {
                    listener.onError(e);
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 异步GET请求
     * async get example
     */
    public void getAsync() {
        Request request = new Request.Builder()
                .url("")
                .get()
                .addHeader("", "")
                .tag("tag")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("", "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    String result = response.body().string();
                    Log.i("r", result);
                }
                else {
                    Log.e("e", response.message());
                }
            }
        });
    }

    /**
     * 同步GET请求
     * sync get example
     *
     * @return response
     * @throws IOException io
     */
    public Response getSync() throws IOException {
        Request request = new Request.Builder()
                .url("")
                .get()
                .addHeader("", "")
                .tag("tag")
                .build();
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    /**
     * 异步POST string请求
     * async post string example
     */
    public void postStringAsync() {
        String bodyStr = "content";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyStr);
        Request request = new Request.Builder()
                .url("")
                .post(requestBody)
                .addHeader("", "")
                .tag("tag")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("", "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    String result = response.body().string();
                    Log.i("r", result);
                }
                else {
                    Log.e("e", response.message());
                }
            }
        });
    }

    /**
     * 同步POST string请求
     * sync post string example
     *
     * @throws IOException io
     */
    public Response postStringSync() throws IOException {
        String bodyStr = "content";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyStr);
        Request request = new Request.Builder()
                .url("")
                .post(requestBody)
                .addHeader("", "")
                .tag("tag")
                .build();
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    /**
     * 异步方式提交流，同步不在陈列
     * async post stream example
     */
    public void postStreamAsync() {
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse("application/octet-stream; charset=utf-8");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("提交流");
            }
        };
        Request request = new Request.Builder()
                .url("")
                .post(requestBody)
                .addHeader("", "")
                .tag("tag")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("", "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    String result = response.body().string();
                    Log.i("r", result);
                }
                else {
                    Log.e("e", response.message());
                }
            }
        });
    }

    /**
     * 异步方式提交form表单，同步不在陈列
     * async post form example
     */
    public void postFormAsync() {
        FormBody formBody = new FormBody.Builder()
                .add("key1", "value1")
                .add("key2", "value2")
                .add("key3", "value3")
                .build();
        Request request = new Request.Builder()
                .url("")
                .post(formBody)
                .addHeader("", "")
                .tag("tag")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("", "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    String result = response.body().string();
                    Log.i("r", result);
                }
                else {
                    Log.e("e", response.message());
                }
            }
        });
    }

    /**
     * 异步方式提交文件，同步不在陈列
     * async post file example
     */
    public void postFileAsync() {
        File file = new File("filePath");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream; charset=utf-8"), file);
        Request request = new Request.Builder()
                .url("")
                .post(requestBody)
                .addHeader("", "")
                .tag("tag")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("", "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    String result = response.body().string();
                    Log.i("r", result);
                }
                else {
                    Log.e("e", response.message());
                }
            }
        });
    }

    /**
     * 异步方式提交混合form表单（文本参数+文件），同步不在陈列
     * async post multipart form example
     */
    public void postMultipartFormAsync() {
        File file = new File("filePath");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream; charset=utf-8"), file);

        // 方式一
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key1", "value1")
                .addFormDataPart("key2", "value2")
                .addFormDataPart("key3", "value3")
                .addFormDataPart("file1", "name1", fileBody)
                .build();

        // 方式二
        FormBody formBody = new FormBody.Builder()
                .add("key1", "value1")
                .add("key2", "value2")
                .add("key3", "value3")
                .build();
        RequestBody requestBody1 = new MultipartBody.Builder()
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"params\""),
                        formBody)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"file\"; filename=\"plans.xml\""),
                        fileBody)
                .build();

        Request request = new Request.Builder()
                .url("")
                .post(requestBody)
                .addHeader("", "")
                .tag("tag")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("", "");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    String result = response.body().string();
                    Log.i("r", result);
                }
                else {
                    Log.e("e", response.message());
                }
            }
        });
    }

    /**
     * 根据Tag取消请求
     *
     * @param tag tag
     */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : okHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : okHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 根据Tag取消请求
     *
     * @param client OkHttpClient
     * @param tag tag
     */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有请求请求
     */
    public void cancelAll() {
        for (Call call : okHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : okHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /**
     * 取消所有请求请求
     *
     * @param client OkHttpClient
     */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }


}
