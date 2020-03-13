package com.hisense.test.server;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by tianpengsheng on 2020年03月13日 17时19分.
 */
public class HttpServer extends NanoHTTPD {
    public static final int DEFAULT_SERVER_PORT = 8080;//为8080
    public static final String TAG = HttpServer.class.getSimpleName();
    //根目录
    private static final String REQUEST_ROOT = "/";

    public HttpServer() {
        super(DEFAULT_SERVER_PORT);
    }


    //当接受到连接时会调用此方法
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        switch (method) {
            case GET: {
                Log.e(TAG, "method :  GET ");
                break;
            }
            case POST: {
//                其中的param就是传过来的json数据,或者post过来的任意字符串
//                files.get(postData),中postData 是框架内部自己定义的key  打印的files就可以看见
                Log.e(TAG, "method :  POST ");
                Map<String, String> files = new HashMap<String, String>();
                /*获取header信息，NanoHttp的header不仅仅是HTTP的header，还包括其他信息。*/
                Map<String, String> header = session.getHeaders();

                try {
                    session.parseBody(files);
                    String param = files.get("postData");

                    Log.d(TAG, "header : " + header);
                    Log.d(TAG, "files : " + files);
                    Log.d(TAG, "param : " + param);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ResponseException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        Response response = null;
        try {
            JSONObject o = new JSONObject();
            o.put("result", "gfdgf");
            String resp = o.toString();
            response = newFixedLengthResponse(Response.Status.OK, "application/json", resp);
            System.out.println("result:" + resp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;

//        if (REQUEST_ROOT.equals(session.getUri()) || session.getUri().equals("")) {
//            return responseRootPage(session);
//        }
//        return responseFile(session);
    }
}