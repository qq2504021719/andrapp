package com.bignerdranch.android.photogallery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/8/31.
 * 网络连接类
 */

public class FilckrFetchr {

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

}
