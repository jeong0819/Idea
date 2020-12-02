package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChangeStorageDirReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/Storage/changeDN.php";
    private Map<String, String> map;

    public ChangeStorageDirReq(String curDN, String cDN, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
       map.put("curDN",curDN);
        map.put("cDN",cDN);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}