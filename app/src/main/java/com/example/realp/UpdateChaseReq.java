package com.example.realp;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class UpdateChaseReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/updateChase.php";
    private Map<String, String> map;

    public UpdateChaseReq(int teamNo,String tLeader,int aCount,String cStatus, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamNo", String.valueOf(teamNo));
        map.put("tLeader",tLeader);
        map.put("aCount", String.valueOf(aCount));
        map.put("newsta",cStatus);
        map.put("oldsta","진행");
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
