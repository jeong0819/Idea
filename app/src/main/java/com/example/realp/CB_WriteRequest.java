package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CB_WriteRequest extends StringRequest {

    //서버 url 설정(php파일 연동)
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Board/CoBoardWrite.php";
    private Map<String, String> map;

    public CB_WriteRequest(String cTitle, String userID, String cWrite, String cTag, String cPatent, String cPlace, String hPerson, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);//위 url에 post방식으로 값을 전송

        map = new HashMap<>();
        map.put("cTitle", cTitle);
        map.put("userID", userID);
        map.put("cWrite", cWrite);
        map.put("cTag", cTag);
        map.put("cPatent", cPatent);
        map.put("cPlace", cPlace);
        map.put("hPerson", hPerson);



    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
