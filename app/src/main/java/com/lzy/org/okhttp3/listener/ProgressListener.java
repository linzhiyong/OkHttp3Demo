package com.lzy.org.okhttp3.listener;

import java.io.File;

/**
 * 进度监听器
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 * https://github.com/linzhiyong
 * https://www.jianshu.com/u/e664ba5d0800
 * @time 2018/7/11
 * @desc
 */
public interface ProgressListener {

    void onStart();

    void onProgress(long total, float progress);

    void onFinish(File file);

    void onError(Exception e);

}
