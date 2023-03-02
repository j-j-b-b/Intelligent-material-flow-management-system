package com.example.getnet.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetUtil {
    public static String doGet() {
        String result="";
        BufferedReader reader=null;
        String bookJSONString=null;
        try {
            //1.建立连接
            HttpURLConnection httpURLConnection=null;
            String url="http://192.168.43.101:5000/api/way_for";
            URL requestUrl=new URL(url);
            httpURLConnection=(HttpURLConnection)requestUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            //2.获取二进制流
            InputStream inputStream=httpURLConnection.getInputStream();
            //3.将二进制进行包装
            reader=new BufferedReader(new InputStreamReader(inputStream));
            //4.从BufferedReader中读字符串
            String line;
            StringBuilder builder=new StringBuilder();
            while((line=reader.readLine())!=null){
                builder.append(line);
                builder.append("\n");
            }
            if(builder.length()==0){
                return null;

            }
            result=builder.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
