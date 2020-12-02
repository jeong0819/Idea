package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetTeamNoReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/getTeamNo.php";
    private Map<String, String> map;

    public GetTeamNoReq(String teamName,String pName, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamName",teamName);
        map.put("pName",pName);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}