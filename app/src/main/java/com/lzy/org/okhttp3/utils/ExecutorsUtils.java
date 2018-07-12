package com.lzy.org.okhttp3.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Executor提供类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 * https://github.com/linzhiyong
 * https://www.jianshu.com/u/e664ba5d0800
 * @time 2018/7/10
 * @desc
 */
public class ExecutorsUtils {

    private static Executor executor = Executors.newFixedThreadPool(5);

    public static Executor executor() {
        return executor;
    }

    public static void execute(Runnable runnable) {
        executor().execute(runnable);
    }

}
