package com.example.weixinjava.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.weixinjava.Task.AccessTokenThread;
import com.example.weixinjava.entity.AccessToken;
import com.example.weixinjava.entity.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;


public class CreateMenu {
    private static Logger log = LoggerFactory.getLogger(CreateMenu.class);
    //调用spring里面RestTemplate需要配置bean
    private RestTemplate restTemplate = new RestTemplate();

    public void createMenu() {
        AccessTokenThread tokenThread = new AccessTokenThread();
        tokenThread.getToken();//生成token
        // 调用接口获取access_token
        String token = AccessTokenThread.accessToken.getAccessToken();
        String url = String.format("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s",token);
        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            //定义请求参数类型，这里用json所以是MediaType.APPLICATION_JSON
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(getFirstMenu(), headers);
            // 调用接口创建菜单
            ResponseEntity<String> entity = restTemplate.postForEntity(url, request, String.class);
            String body = entity.getBody();
            //然后把str转换成JSON再通过getJSONObject()方法获取到里面的result对象，因为我想要的数据都在result里面
            //下面的strToJson只是一个str转JSON的一个共用方法；
            JSONObject jsonObj = JSON.parseObject(body);            // 判断菜单创建结果
            if (null != jsonObj) {
                if (0 != jsonObj.getInteger("errcode")) {
                    log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObj.getInteger("errcode"), jsonObj.getString("errmsg"));
                }else {
                    log.info("菜单创建成功！");
                }
            }
        }
    }

    /**
     * 组装菜单数据
     */
    public static Map<String, Object> getFirstMenu(){
        //第一栏菜单
        Menu menu1=new Menu();
        menu1.setId("1");
        menu1.setName("第一栏");
        menu1.setType("click");
        menu1.setKey("1");

        Menu menu11=new Menu();
        menu11.setId("11");
        menu11.setName("第一栏的第一个v3");
        menu11.setType("pic_sysphoto");
        menu11.setKey("11");

        Menu menu12=new Menu();
        menu12.setId("12");
        menu12.setName("第一栏的第二个");
        menu12.setType("click");
        menu12.setKey("12");

        //第二栏
        Menu menu2=new Menu();
        menu2.setId("2");
        menu2.setName("第二栏");
        menu2.setType("click");
        menu2.setKey("2");

        Menu menu21=new Menu();
        menu21.setId("21");
        menu21.setName("第二栏的第一个");
        menu21.setType("click");
        menu21.setKey("21");



        Menu menu3=new Menu();
        menu3.setId("3");
        menu3.setName("第三栏");
        menu3.setType("view");
        menu3.setUrl("http://www.baidu.com");

        //最外一层大括号
        Map<String, Object> wechatMenuMap = new HashMap<String, Object>();

        //包装button的List
        List<Map<String, Object>> wechatMenuMapList = new ArrayList<Map<String, Object>>();

        //包装第一栏
        Map<String, Object> menuMap1 = new HashMap<String, Object>();
        Map<String, Object> menuMap11 = new HashMap<String, Object>();
        Map<String, Object> menuMap12 = new HashMap<String, Object>();
        List<Map<String, Object>> subMenuMapList1 = new ArrayList<Map<String, Object>>();


        //第一栏第一个
        menuMap11.put("name",menu11.getName());
        menuMap11.put("type",menu11.getType());
        menuMap11.put("key",menu11.getKey());
        subMenuMapList1.add(menuMap11);

        //第二栏第二个
        menuMap12.put("name",menu12.getName());
        menuMap12.put("type",menu12.getType());
        menuMap12.put("key",menu12.getKey());
        subMenuMapList1.add(menuMap12);

        menuMap1.put("name",menu1.getName());
        menuMap1.put("sub_button",subMenuMapList1);

        //包装第二栏
        Map<String, Object> menuMap2 = new HashMap<String, Object>();
        Map<String, Object> menuMap21 = new HashMap<String, Object>();
        List<Map<String, Object>> subMenuMapList2 = new ArrayList<Map<String, Object>>();

        //第二栏第一个
        menuMap21.put("name",menu21.getName());
        menuMap21.put("type",menu21.getType());
        menuMap21.put("key",menu21.getKey());
        subMenuMapList2.add(menuMap21);

        menuMap2.put("name",menu2.getName());
        menuMap2.put("sub_button",subMenuMapList2);

        //包装第三栏
        Map<String, Object> menuMap3 = new HashMap<String, Object>();
        List<Map<String, Object>> subMenuMapList3 = new ArrayList<Map<String, Object>>();

        menuMap3.put("name",menu3.getName());
        menuMap3.put("type",menu3.getType());
        menuMap3.put("url",menu3.getUrl());
        menuMap3.put("sub_button",subMenuMapList3);


        wechatMenuMapList.add(menuMap1);
        wechatMenuMapList.add(menuMap2);
        wechatMenuMapList.add(menuMap3);
        wechatMenuMap.put("button",wechatMenuMapList);
        return  wechatMenuMap;
    }

    public static void main(String[] args) {
        CreateMenu createMenu = new CreateMenu();
        createMenu.createMenu();
    }
}
