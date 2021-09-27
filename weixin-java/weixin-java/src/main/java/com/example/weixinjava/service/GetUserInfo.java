package com.example.weixinjava.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.weixinjava.Task.AccessTokenThread;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * 获取已经关注的用户信息
 */
public class GetUserInfo {
    /**
     * 获取用户信息
     */
    public void getUserInfo() {
        AccessTokenThread tokenThread = new AccessTokenThread();
        tokenThread.getToken();//生成token
        String token = AccessTokenThread.accessToken.getAccessToken();
        String openid = "op61o5vpo4pJE6tUVwn1KE2rxOAE";
        String url = String.format("https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN", token, openid);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> stringResponseEntity = restTemplate.getForEntity(url, String.class);
        String body = stringResponseEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        System.out.println(jsonObject);
    }

    public static void main(String[] args) {
        GetUserInfo getUserInfo = new GetUserInfo();
        getUserInfo.getUserInfo();
    }
}
