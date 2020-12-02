package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class InsertAlarimReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/InsertAlarim.php";
    private Map<String, String> map;

    public InsertAlarimReq(String sender,String reciever,String contents,String octime,String a_result,Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("k_alarim","탈퇴 요청");
        map.put("sender",sender);
        map.put("reciever",reciever);
        map.put("contents",contents);
        map.put("octime",octime);
        map.put("a_result",a_result);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
