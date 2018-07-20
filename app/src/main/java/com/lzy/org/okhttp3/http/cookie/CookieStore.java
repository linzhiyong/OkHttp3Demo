package com.lzy.org.okhttp3.http.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Cookie缓存接口
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 *       https://github.com/linzhiyong
 *       https://www.jianshu.com/u/e664ba5d0800
 * @time 2018/7/20
 * @desc
 */
public interface CookieStore {

    /**
     * 添加cookie
     *
     * @param httpUrl httpUrl
     * @param cookie cookie
     */
    void add(HttpUrl httpUrl, Cookie cookie);

    /**
     * 添加指定httpurl cookie集合
     *
     * @param httpUrl httpUrl
     * @param cookies cookies
     */
    void add(HttpUrl httpUrl, List<Cookie> cookies);

    /**
     * 根据HttpUrl从缓存中读取cookie集合
     *
     * @param httpUrl httpUrl
     * @return
     */
    List<Cookie> get(HttpUrl httpUrl);

    /**
     * 获取全部缓存cookie
     *
     * @return
     */
    List<Cookie> getCookies();

    /**
     * 移除指定httpurl cookie集合
     *
     * @param httpUrl httpUrl
     * @param cookie cookie
     * @return
     */
    boolean remove(HttpUrl httpUrl, Cookie cookie);

    /**
     * 移除所有cookie
     *
     * @return
     */
    boolean removeAll();

}
