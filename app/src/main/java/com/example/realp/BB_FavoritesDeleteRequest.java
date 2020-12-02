package com.example.realp;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BB_FavoritesDeleteRequest extends StringRequest {

    final static  private String URL="http://teamwhi.dothome.co.kr/IdeaMarket/Board/BuyBoardFavoritesDelete.php";
    private Map<String,String> map;

    public BB_FavoritesDeleteRequest(int bsNum, String userID, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);//위 url에 post방식으로 값을 전송

        map=new HashMap<>();
        map.put("bsNum",bsNum+"");
        map.put("userID",userID);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }


}
