package com.escience.weather.Network;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;

/**
 * Created by Stark on 2017/2/11.
 */
public class Net {
    public static String request(String Url,String JsonStr) throws Exception{
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Url);
        httpPost.addHeader(HTTP.CONTENT_TYPE,"application/x-www-form-urlencoded;application/json;charset=utf-8");
        StringEntity se = new StringEntity(JsonStr,"UTF-8");
        se.setContentEncoding("UTF-8");
        httpPost.setEntity(se);
        HttpResponse response = httpclient.execute(httpPost);
        if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, "UTF-8");
            }
        }
        return null;
    }
    public static String request(String Url) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(Url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
            if (entity != null) {
                return EntityUtils.toString(entity,"UTF-8");
            }
        }
        return null;
    }
    public static boolean UpLoad(String Url,File file) throws Exception{
        HttpClient client=new DefaultHttpClient();// 开启一个客户端 HTTP 请求
        HttpPost post = new HttpPost(Url);//创建 HTTP POST 请求
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//      builder.setCharset(Charset.forName("uft-8"));//设置请求的编码格式
//        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
//        int count=0;
//        for (File file : files) {
          FileBody fileBody = new FileBody(file);//把文件转换成流对象FileBody
          builder.addPart("upload_file", fileBody);
//        builder.addBinaryBody("file"+count, file);
//            count++;
//        }
//        builder.addTextBody("method", params.get("method"));//设置请求参数
//        builder.addTextBody("fileTypes", params.get("fileTypes"));//设置请求参数
        HttpEntity entity = builder.build();// 生成 HTTP POST 实体
        post.setEntity(entity);//设置请求参数
        HttpResponse response = client.execute(post);// 发起请求 并返回请求的响应
        if (response.getStatusLine().getStatusCode()==200) {
            return true;
        }
        return false;
    }
}
