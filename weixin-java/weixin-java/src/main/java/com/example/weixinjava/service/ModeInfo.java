package com.example.weixinjava.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.weixinjava.Task.AccessTokenThread;
import com.example.weixinjava.entity.ValueType;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 模板信息开发
 * 1 设置所属行业
 * 2 获取设置的行业信息
 * 3 获得模板ID
 * 4 获取模板列表
 * 5 删除模板
 * 6 发送模板消息
 * 7 事件推送
 */
public class ModeInfo {


    /**
     * 设置 1 设置所属行业
     */
    public void set(){
        AccessTokenThread tokenThread = new AccessTokenThread();
        tokenThread.getToken();//生成token
        // 调用接口获取access_token
        String token = AccessTokenThread.accessToken.getAccessToken();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=%s",token);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //定义请求参数类型，这里用json所以是MediaType.APPLICATION_JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        //最外一层大括号
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("industry_id1","1");
        map.put("industry_id2","4");
        System.out.println(map);
        HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(map, headers);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = stringResponseEntity.getBody();
        //然后把str转换成JSON再通过getJSONObject()方法获取到里面的result对象，因为我想要的数据都在result里面
        //下面的strToJson只是一个str转JSON的一个共用方法；
        JSONObject jsonObj = JSON.parseObject(body);
        System.out.println(jsonObj);
    }

    /**
     * 获取行业设置
     */
    public void getModeInfo(){
        AccessTokenThread tokenThread = new AccessTokenThread();
        tokenThread.getToken();//生成token
        String token = AccessTokenThread.accessToken.getAccessToken();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=%s",token);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        String body = forEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        System.out.println(jsonObject);
    }

    /**
     * 发模板
     */
    public void sent(){
        AccessTokenThread tokenThread = new AccessTokenThread();
        tokenThread.getToken();//生成token
        String token = AccessTokenThread.accessToken.getAccessToken();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s",token);
        ValueType valueType1 = new ValueType();
        valueType1.setValue("您有新的反馈信息啦！");
        valueType1.setColor("#abcdef");
        ValueType valueType2 = new ValueType();
        valueType2.setValue("罗天使");
        valueType2.setColor("#abcdef");
        ValueType valueType3 = new ValueType();
        valueType3.setValue("2020年10月1日");
        valueType3.setColor("#abcdef");
        ValueType valueType4 = new ValueType();
        valueType4.setValue("面试通过！");
        valueType4.setColor("#abcdef");
        ValueType valueType5 = new ValueType();
        valueType5.setValue("请查看！");
        valueType5.setColor("#abcdef");

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("value",valueType1.getValue());
        map1.put("color",valueType1.getColor());

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("value",valueType2.getValue());
        map2.put("color",valueType2.getColor());

        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("value",valueType3.getValue());
        map3.put("color",valueType3.getColor());

        HashMap<String, Object> map4 = new HashMap<>();
        map4.put("value",valueType4.getValue());
        map4.put("color",valueType4.getColor());

        HashMap<String, Object> map5 = new HashMap<>();
        map5.put("value",valueType5.getValue());
        map5.put("color",valueType5.getColor());

        HashMap<String, Object> stringMap = new HashMap<>();
        stringMap.put("first",map1);
        stringMap.put("company",map2);
        stringMap.put("time",map3);
        stringMap.put("result",map4);
        stringMap.put("remark",map5);

        HashMap<String, Object> value = new HashMap<>();
        value.put("touser","op61o5vpo4pJE6tUVwn1KE2rxOAE");
        value.put("template_id","aHgryOMBpAl0hHVmOI8T0obRVrD-ZHwsQiQWU9_5dgI");
        value.put("url","https://www.baidu.com/");
        value.put("miniprogram","");
        value.put("data",stringMap);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //定义请求参数类型，这里用json所以是MediaType.APPLICATION_JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(value, headers);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, request, String.class);
        String body = stringResponseEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        System.out.println(jsonObject);

    }

    public static void main(String[] args) {
        ModeInfo modeInfo = new ModeInfo();
//        modeInfo.set();//设置行业
//        modeInfo.getModeInfo();//获取行业信息
            modeInfo.sent();
    }
}
