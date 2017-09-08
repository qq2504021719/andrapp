package com.bignerdranch.android.xundian.comm;


import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/8/31.
 * 网络连接类
 */

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";

    private static final String API_KEY = "1e11934c7cb99e10d66cc10822e736a7";

    /**
     * 重指定URL获取原始数据并返回一个字节流数组
     * @param urlSpec
     * @return
     * @throws IOException
     */
    public byte[] getUrlBytes(String urlSpec) throws IOException{
        // 创建url对象
        URL url = new URL(urlSpec);
        // 调用 openConnection() 方法创建一个指向要访问URL的连接对象
        // URL.openConnection() 方法默认返回的是URLConnection对象,但要连接的是http URL,
        // 因此需将其强制类型转换为 HttpURLConnection 对象
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try{

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 如果是 POST 请求,调用 getOutputStream() 方法
            InputStream in = connection.getInputStream();

            // 判断是否连接成功
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage()+": with "+urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0){
                out.write(buffer,0,bytesRead);
            }
            out.close();
            return out.toByteArray();

        }finally {

            connection.disconnect();

        }
    }

    /**
     * 将 getUrlBytes(String)方法返回的结果转换为String
     * @param urlSpec
     * @return
     * @throws IOException
     */
    public String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }

    /**
     * 使用Uri.Builder构建了完整的Flickr API请求URL
     */
    public void fetchItems(){
        try{
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method","flickr.photos.getRecent")
                    .appendQueryParameter("api_key",API_KEY)
                    .appendQueryParameter("format","json")
                    .appendQueryParameter("jsoncallback","1")
                    .appendQueryParameter("extras","url_s")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG,"Received JSON: "+jsonString);

        } catch (IOException ioe){
            Log.i(TAG,"Failed to fetch items",ioe);
        }

    }


}
