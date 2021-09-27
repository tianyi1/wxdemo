package com.example.weixinjava.entity;

/**
 * 微信通用接口凭证
 *
 */
public class AccessToken {
    // 获取到的凭证
    private String accessToken;
    // 凭证有效时间，单位：秒
    private Long expiresIn;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
