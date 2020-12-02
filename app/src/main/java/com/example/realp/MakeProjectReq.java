package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MakeProjectReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/makeProject.php";
    private Map<String, String> map;

    public MakeProjectReq(String pName,String teamName,String pTheme,String startDate,String finishDate,int tpc,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamName",teamName);
        map.put("pName",pName);
        map.put("pTheme",pTheme);
        map.put("startDate",startDate);
        map.put("finishDate",finishDate);
        map.put("tpc",String.valueOf(tpc));
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}