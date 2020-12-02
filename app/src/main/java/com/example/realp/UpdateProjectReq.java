package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateProjectReq extends StringRequest{

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/updateProject.php";
    private Map<String, String> map;

    public UpdateProjectReq(int teamNo,String newtn,String oldtn,String newpn,String oldpn,
                            String newpt,String oldpt,String oldsd,String newfd,String oldfd,int newtpc ,Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamNo", String.valueOf(teamNo));
        map.put("newtn",newtn);
        map.put("oldtn",oldtn);
        map.put("newpn",newpn);
        map.put("oldpn",oldpn);
        map.put("newpt",newpt);
        map.put("oldpt",oldpt);
        map.put("oldsd",oldsd);
        map.put("newfd",newfd);
        map.put("oldfd",oldfd);
        map.put("newtpc", String.valueOf(newtpc));

    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}