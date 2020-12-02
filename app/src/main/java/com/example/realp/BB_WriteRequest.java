package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BB_WriteRequest extends StringRequest {

    //서버 url 설정(php파일 연동)
    final static  private String URL="http://teamwhi.dothome.co.kr/IdeaMarket/Board/BuyBoardWrite.php";
    private Map<String,String> map;

    public BB_WriteRequest(String bTitle, String price, String userID, String bWrite, String tag, String patent, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송

        map=new HashMap<>();
        map.put("bTitle",bTitle);
        map.put("price",price);
        map.put("userID",userID);
        map.put("bWrite",bWrite);
        map.put("tag",tag);
        map.put("patent",patent);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
