package com.example.weixinjava.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.weixinjava.Task.AccessTokenThread;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 生成二维码
 */
public class CreateRCode {

    /**
     * 获取ticket
     */
    public void getTicket() {
        AccessTokenThread tokenThread = new AccessTokenThread();
        tokenThread.getToken();//生成token
        String token = AccessTokenThread.accessToken.getAccessToken();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s", token);
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("scene_id", 123);
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("expire_seconds", 604800);
        stringObjectHashMap.put("action_name", "QR_SCENE");
        stringObjectHashMap.put("action_info", map1);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //定义请求参数类型，这里用json所以是MediaType.APPLICATION_JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(stringObjectHashMap, headers);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = stringResponseEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        System.out.println(jsonObject);
        //gQEY8jwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyVmNMWEZKREpkRUMxeDMxRjF3MTMAAgTDxp9gAwSAOgkA
    }

    /**
     * 通过tickit生成二维码
     */
    public void createRcode() {
        String tickit = "gQEY8jwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyVmNMWEZKREpkRUMxeDMxRjF3MTMAAgTDxp9gAwSAOgkA";
        String url = String.format("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s", tickit);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> stringResponseEntity = restTemplate.getForEntity(url, String.class);
        String body = stringResponseEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        System.out.println(jsonObject);
    }

    public static void main(String[] args) {
        CreateRCode code = new CreateRCode();
//        code.getTicket();
        code.createRcode();
    }
}
