package com.atguigu.common;




import okhttp3.*;

import java.io.IOException;


public class HttpUtil {

    private static OkHttpClient client;

    private HttpUtil(){

    }
    public static OkHttpClient getInstance() {
        if (client == null) {
            synchronized (HttpUtil.class) {
                if (client == null) {
                    client = new OkHttpClient();
                }
            }
        }
        return client;
    }

    public static String get( String url){

        Request request = new Request.Builder()
                .url(url).get().build();
        Call call = HttpUtil.getInstance().newCall(request);
        Response response = null;

        try {
            response = call.execute();
             return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("发送失败...检查网络地址...");

        }


    }


    public static void delete( String url)  {
           Request request = new Request.Builder()
                    .url(url)
                   .delete()
                .build();
            Call call = HttpUtil.getInstance().newCall(request);
          try {
               call.execute();
          } catch (IOException e) {
              e.printStackTrace();
              throw new RuntimeException("发送失败...检查网络地址...");
          }
         }
}
