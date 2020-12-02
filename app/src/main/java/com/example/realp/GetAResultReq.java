package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class GetAResultReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/getAResult.php";
    private Map<String, String> map;

    public GetAResultReq(String k_alarim,String sender,String reciever, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("k_alarim","탈퇴 요청");
        map.put("sender",sender);
        map.put("reciever",reciever);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}