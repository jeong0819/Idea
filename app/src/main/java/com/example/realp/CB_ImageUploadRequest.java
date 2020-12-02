package com.example.realp;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CB_ImageUploadRequest extends StringRequest {

    final static private String URL = "http://teamwhi.dothome.co.kr/IdeaMarket/Board/CoImageUpload.php";
    private Map<String,String> map;
    public CB_ImageUploadRequest(int cNumber, String imageName, String imageData , Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        map=new HashMap<>();

        map.put("cNumber",Integer.toString(cNumber));
        map.put("imageName",imageName);
        map.put("image",imageData);

        if(imageData==null){
            Log.e("image","null in request");
        }else{
            Log.e("image","not null in request");
        }
        //url은 php에서 처리

    }

    protected Map<String,String> getParams() throws AuthFailureError {
        return  map;
    }
}
