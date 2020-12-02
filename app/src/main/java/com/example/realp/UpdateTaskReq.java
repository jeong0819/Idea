package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateTaskReq  extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/updatetask.php";
    private Map<String, String> map;

    public UpdateTaskReq(int teamNo,String oldsta,String oldsub,String olduser,String oldcon,String oldfin,
                         String newsta,String newsub,String newuser,String newcon,String newfin, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamNo", String.valueOf(teamNo));
        map.put("oldsta",oldsta);
        map.put("newsta",newsta);
        map.put("oldsub",oldsub);
        map.put("newsub",newsub);
        map.put("olduser",olduser);
        map.put("newuser",newuser);
        map.put("oldcon",oldcon);
        map.put("newcon",newcon);
        map.put("oldfin",oldfin);
        map.put("newfin",newfin);

    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}