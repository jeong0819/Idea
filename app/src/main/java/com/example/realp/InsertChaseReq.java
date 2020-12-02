package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class InsertChaseReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/insertChase.php";
    private Map<String, String> map;

    public InsertChaseReq(int teamNo,String target,String tLeader,int aCount,String cStatus, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamNo", String.valueOf(teamNo));
        map.put("target",target);
        map.put("tLeader",tLeader);
        map.put("aCount", String.valueOf(aCount));
        map.put("cStatus",cStatus);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
