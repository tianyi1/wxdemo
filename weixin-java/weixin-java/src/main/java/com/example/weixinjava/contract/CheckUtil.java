package com.example.weixinjava.contract;

import java.util.Arrays;

/**
 * 校验代码
 */
public class CheckUtil {
    private static final String token = "123456";
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        //        1）将token、timestamp、nonce三个参数进行字典序排序
//        2）将三个参数字符串拼接成一个字符串进行sha1加密
//        3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        String[] str = new String[]{token, timestamp, nonce};
        //排序
        Arrays.sort(str);
        //拼接字符串
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            buffer.append(str[i]);
        }
        //进行sha1加密
        String temp = SHA1.encode(buffer.toString());
        //与微信提供的signature进行匹对
        return signature.equals(temp);
    }
}
