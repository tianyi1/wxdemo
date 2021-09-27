package com.example.weixinjava.contract;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.weixinjava.ResMage.Article;
import com.example.weixinjava.ResMage.NewsMessage;
import com.example.weixinjava.ResMage.TextMessage;
import com.example.weixinjava.commont.MessageUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

@RestController
@RequestMapping("/token")
public class WeiXinCheck {

    @RequestMapping(value ="/login", method = RequestMethod.GET)
    public void get(HttpServletRequest request, HttpServletResponse response){
        System.out.println("进来了嘛？");
        String signature =request.getParameter("signature");
        String timestamp =request.getParameter("timestamp");
        String nonce =request.getParameter("nonce");
        String echostr =request.getParameter("echostr");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
                out.write(echostr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    @RequestMapping(value ="/login", method = RequestMethod.POST)
    public void post(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("进来了嘛？");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 默认返回的文本消息内容
        String respContent = "请求处理异常，请稍候尝试！";
        String respMessage = null;
        // xml请求解析
        Map<String, String> requestMap = MessageUtil.parseXml(request);
        // 发送方帐号（open_id）
        String fromUserName = requestMap.get("FromUserName");
        // 公众帐号
        String toUserName = requestMap.get("ToUserName");
        // 消息类型
        String msgType = requestMap.get("MsgType");

        String event = requestMap.get("Event");
        System.out.println("事件是"+event);
        // 自动回复文本消息
        if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
            // 接收文本消息内容
            String content = requestMap.get("Content");
            if(content.equals("图文")){
                // 创建图文消息
                NewsMessage newsMessage = new NewsMessage();
                newsMessage.setToUserName(fromUserName);
                newsMessage.setFromUserName(toUserName);
                newsMessage.setCreateTime(new Date().getTime());
                newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
                List<Article> articleList = new ArrayList<Article>();
                //测试单图文回复
                Article article = new Article();
                article.setTitle("微信公众帐号开发教程Java版");
                // 图文消息中可以使用QQ表情、符号表情
                article.setDescription("这是测试有没有换行\n\n如果有空行就代表换行成功\n\n点击图文可以跳转到百度首页");
                // 将图片置为空
                article.setPicUrl("http://www.sinaimg.cn/dy/slidenews/31_img/2016_38/28380_733695_698372.jpg");
                article.setUrl("http://www.baidu.com");
                articleList.add(article);
                newsMessage.setArticleCount(articleList.size());
                newsMessage.setArticles(articleList);
                respMessage = MessageUtil.newsMessageToXml(newsMessage);
            }else if(content.equals("登录")){
                // 回复文本消息
                TextMessage textMessage = new TextMessage();
                textMessage.setToUserName(fromUserName);
                textMessage.setFromUserName(toUserName);
                StringBuffer buffer = new StringBuffer();
                String appid = "wx2802b62c18482c92";
                String notyUrl = "http://www.tianzhonghai.top/token/getPageInfo";
                String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect",appid,notyUrl);
                System.out.println(url);
                buffer.append("请点<a href=\"" + url + "\">这里</a>登录");
                respContent = String.valueOf(buffer);
                textMessage.setContent(respContent);
                textMessage.setCreateTime(new Date().getTime());
                textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }else {
                // 回复文本消息
                TextMessage textMessage = new TextMessage();
                textMessage.setToUserName(fromUserName);
                textMessage.setFromUserName(toUserName);
                StringBuffer buffer = new StringBuffer();
                buffer.append("您好，我是小8，请回复数字选择服务：").append("\n\n");
                buffer.append("11 可查看测试单图文").append("\n");
                buffer.append("12  可测试多图文发送").append("\n");
                buffer.append("13  可测试网址").append("\n");

                buffer.append("或者您可以尝试发送表情").append("\n\n");
                buffer.append("回复“1”显示此帮助菜单").append("\n");
                respContent = String.valueOf(buffer);
                textMessage.setContent(respContent);
                textMessage.setCreateTime(new Date().getTime());
                textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }
            PrintWriter out = response.getWriter();
            out.print(respMessage);
            out.flush();
            out.close();
        }

    }

    @RequestMapping(value ="/getPageInfo")
    public JSONObject getPageInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        String state = request.getParameter("/authorizestate");
        String appiD="wx2802b62c18482c92";
        String appsecret = "cf072934da54150758b8d9698aa7788f";
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",appiD,appsecret,code);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> stringResponseEntity = restTemplate.getForEntity(url, String.class);
        String body = stringResponseEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        System.out.println(jsonObject);
        return jsonObject;
    }

}
