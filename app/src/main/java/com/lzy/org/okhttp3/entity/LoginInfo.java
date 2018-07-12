package com.lzy.org.okhttp3.entity;

import java.io.Serializable;

/**
 * 登录信息实体类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 *       https://www.jianshu.com/u/e664ba5d0800
 * @time 2018/7/9
 * @desc
 */
public class LoginInfo implements Serializable {

    private String username;

    private String password;

    private String signcode;

    public LoginInfo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSigncode() {
        return signcode;
    }

    public void setSigncode(String signcode) {
        this.signcode = signcode;
    }

    @Override
    public String toString() {
        return "username=" + getUsername() + "&password=" + getPassword() + "&signcode=" + getSigncode();
    }
}
