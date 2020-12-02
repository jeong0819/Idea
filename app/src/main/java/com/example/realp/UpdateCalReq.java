package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateCalReq extends StringRequest{

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/updateCal.php";
    private Map<String, String> map;

    public UpdateCalReq(int teamNo,String userid,String oldsDate,String oldtime,String oldcon,String oldaSta,
                        String newsDate,String newtime,String newcontents,String newaSta,Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamNo", String.valueOf(teamNo));
        map.put("userid",userid);
        map.put("oldsDate",oldsDate);
        map.put("oldtime",oldtime);
        map.put("oldcontents",oldcon);
        map.put("oldaSta",oldaSta);
        map.put("newsDate",newsDate);
        map.put("newtime",newtime);
        map.put("newcontents",newcontents);
        map.put("newaSta",newaSta);

    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}