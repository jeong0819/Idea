package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BB_CommentWriteRequest extends StringRequest {
    //서버 url 설정(php파일 연동)
    final static  private String URL="http://teamwhi.dothome.co.kr/IdeaMarket/Board/BuyBoardCommentWrite.php";
    private Map<String,String> map;

    public BB_CommentWriteRequest(int bNumber, String reWrite, String reMemo, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송

        map=new HashMap<>();
        map.put("bNumber",bNumber+"");
        map.put("reWrite",reWrite);
        map.put("reMemo",reMemo);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
