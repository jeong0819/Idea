package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AddTeamUserReq extends StringRequest{

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://whi2020.dothome.co.kr/Project/addTeamUser.php";
    private Map<String, String> map;

    public AddTeamUserReq(int teamNo, String userID, String teamRank, String teamChar, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("teamNo",String.valueOf(teamNo));
        map.put("userID",userID);
        map.put("teamRank",teamRank);
        map.put("teamChar",teamChar);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}