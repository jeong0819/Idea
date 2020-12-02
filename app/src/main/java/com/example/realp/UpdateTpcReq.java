package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class UpdateTpcReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/updateTpc.php";
    private Map<String, String> map;

    public UpdateTpcReq(int teamNo,int tpc, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamNo", String.valueOf(teamNo));
        map.put("tpc", String.valueOf(tpc));
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}