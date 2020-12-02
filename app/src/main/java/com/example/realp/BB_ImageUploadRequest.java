package com.example.realp;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BB_ImageUploadRequest extends StringRequest {
    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Board/BuyImageUpload.php";
    private Map<String,String> map;
    public BB_ImageUploadRequest(int bNumber, String imageName, String imageData , Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        map=new HashMap<>();
        map.put("bNumber", String.valueOf(bNumber));
        map.put("imageName",imageName);
        map.put("image",imageData);

        //url은 php에서 처리

    }

    protected Map<String,String> getParams() throws AuthFailureError {
        return  map;
    }
}
