package com.example.realp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CalList extends AppCompatActivity {
    private RecyclerView recyclerView;
    public static AllCalAdapter allcalAdapter;
    public static ArrayList<AllCalData> acar;
    CurProjectinfo c =new CurProjectinfo();
    private LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_list);
        findViewById(R.id.iv_backTocal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getAllList();
    }

    private void getAllList() {
        recyclerView=(RecyclerView)findViewById(R.id.allCalList);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        acar=new ArrayList<>();
        allcalAdapter=new AllCalAdapter(acar);
        recyclerView.setAdapter(allcalAdapter);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("TeamCal");
                    if(ja.length()==0){
                        return;}
                    for(int i=0;i<ja.length();i++){
                        JSONObject item = ja.getJSONObject(i);
                        String sDate=item.getString("sDate");
                        String contents=item.getString("contents");
                        AllCalData cd =new AllCalData(sDate,contents);
                        acar.add(cd);
                        allcalAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e("CE0","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        GetAllCalReq gacr = new GetAllCalReq(c.getTeamNo(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add( gacr);
    }
}
