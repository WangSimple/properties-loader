package com.demo.propertiesloader.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class HttpClientUtil {



    private  static String HTTP_URL ;
    @Value("${message.service.url}")
    public void setURL(String path){
        HTTP_URL=path;
        System.out.println(path);
    }




    /**
     * get请求
     * @param params 接口参数，以&分隔，例：name=darren&sex=1
     * @return
     */
    public static String doGet(String params){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(HTTP_URL + params);
        CloseableHttpResponse response = null;
        try{

            response = httpClient.execute(httpGet);
            httpGet.setConfig(setConfig(60000));
            HttpEntity entity = response.getEntity();
            if(entity != null){
                return EntityUtils.toString(entity, "utf-8");
            }else{
                return "response is null";
            }
        }catch(Exception e){
            return e.getMessage();
        }finally {
            releaseResource(httpClient, response);
        }
    }

    /**
     *  post请求
     * 参数对象：需转换为jsonstring传入
     * @return
     */
    public static String doPost(String params){
    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	HttpPost httpPost = null;
        httpPost = new HttpPost(HTTP_URL);
        //httpPost = new HttpPost(url);
        StringEntity jsonString = new StringEntity(params, "utf-8");
        httpPost.setEntity(jsonString);
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        CloseableHttpResponse response = null;
        try{
            log.debug("url:\t"+httpPost.getURI()+"\tparam:\t"+params);
            response = httpClient.execute(httpPost);
            httpPost.setConfig(HttpClientUtil.setConfig(60000));
            HttpEntity entity = response.getEntity();
            String result;
            if(entity != null){
                result= EntityUtils.toString(entity, "utf-8");
            }else{
                result= HTTP_URL + "?" + params + "response is null";
            }
            log.debug("httpclient return :"+result);
            return result;
        }catch(Exception e){
            return e.getMessage();
        }finally {
            releaseResource(httpClient, response);
        }
    }


    /**
     * 释放资源
     * @param httpClient
     * @param response
     */
    private static void releaseResource(CloseableHttpClient httpClient, CloseableHttpResponse response){
        try {
            // 释放资源
            if (httpClient != null) {
                httpClient.close();
            }
            if (response != null) {
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置超时时间
     * @param time
     * @return
     */
    private static RequestConfig setConfig(int time){
       return RequestConfig.custom()
                // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(time)
                // 设置请求超时时间(单位毫秒)
                .setConnectionRequestTimeout(time)
                // socket读写超时时间(单位毫秒)
                .setSocketTimeout(time).build();
    }

}
