package com.example.realp;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestSecReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/pushNoti.php";
    private Map<String, String> map;

    public RequestSecReq(int id,String title,String msg, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("id", String.valueOf(id));
        map.put("title",title);
        map.put("msg", String.valueOf(msg));
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}