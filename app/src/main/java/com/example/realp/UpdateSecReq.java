package com.example.realp;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class UpdateSecReq extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/updateSec.php";
    private Map<String, String> map;

    public UpdateSecReq(String k_alarim,String sender,String reciever,String octime,String contents,String a_result, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        Log.e("update db",k_alarim+"/"+sender+"/"+reciever+"/"+octime+"/"+contents+"/"+a_result);
        map = new HashMap<>();
        map.put("k_alarim",k_alarim);
        map.put("sender",sender);
        map.put("reciever",reciever);
        map.put("octime",octime);
        map.put("contents",contents);
        map.put("oldresult",a_result);
        map.put("newresult","완료");
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}