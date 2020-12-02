package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DelTaskReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/delTask.php";
    private Map<String, String> map;

    public DelTaskReq(int teamNo,String status,String subject,String userid,String contents,String gFinish, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamNo", String.valueOf(teamNo));
        map.put("status",status);
        map.put("subject",subject);
        map.put("userid",userid);
        map.put("contents",contents);
        map.put("gFinish",gFinish);

    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}