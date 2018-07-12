package com.lzy.org.okhttp3.http;

import com.lzy.org.okhttp3.listener.ProgressListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 文件下载回调
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 * https://github.com/linzhiyong
 * https://www.jianshu.com/u/e664ba5d0800
 * @time 2018/7/11
 * @desc
 */
public abstract class FileCallback implements Callback, ProgressListener {

    private String destFileDir;

    private String destFileName;

    public FileCallback(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        onError(e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        this.saveFile(response);
    }

    private void saveFile(Response response) throws IOException {
        onStart();
        InputStream is = null;
        byte[] buf = new byte[2048];
        FileOutputStream fos = null;

        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0L;
            File dir = new File(this.destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, this.destFileName);
            fos = new FileOutputStream(file);

            int len = 0;
            while ((len = is.read(buf)) != -1) {
                sum += (long) len;
                fos.write(buf, 0, len);
                onProgress(total, (float) sum * 1.0F / (float) total);
            }

            fos.flush();
            onFinish(file);
        } catch (Exception e) {
            onError(e);
        } finally {
            try {
                response.body().close();
                if (is != null) {
                    is.close();
                }
            } catch (IOException var23) {
            }

            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException var22) {
            }
        }
    }

}
