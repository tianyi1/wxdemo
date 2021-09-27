package com.example.weixinjava.Task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.weixinjava.entity.AccessToken;
import com.example.weixinjava.service.CreateMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

/**
 * 定时获取微信access_token的线程
 *在WechatMpDemoApplication中注解@EnableScheduling，在程序启动时就开启定时任务。
 * 每7200秒执行一次
 */
public class AccessTokenThread {

    private static Logger log = LoggerFactory.getLogger(AccessTokenThread.class);
    private static String appID = "wx2802b62c18482c92";
    private static String appsecret = "cf072934da54150758b8d9698aa7788f";
    // 第三方用户唯一凭证
    public static AccessToken accessToken = null;
    @Scheduled(fixedDelay = 2*3600*1000)
    //7200秒执行一次
    public void getToken(){
        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",appID,appsecret);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String body = exchange.getBody();
        System.out.println("返回的数据是"+body);
        accessToken = JSON.parseObject(body, AccessToken.class);
        if(null!=accessToken){
            log.info("获取成功，accessToken:"+accessToken.getAccessToken());
        }else {
            log.error("获取token失败");
        }
    }
}
