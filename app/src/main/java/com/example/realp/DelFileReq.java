package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DelFileReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/rmFile.php";
    private Map<String, String> map;

    public DelFileReq(String path, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("path",path);

    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}