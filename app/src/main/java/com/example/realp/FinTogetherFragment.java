package com.example.realp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FinTogetherFragment extends Fragment {
    RecyclerView recyclerView;
     FinTogAdapter finTogAdapter;
    ArrayList<IngTogData> ftar;
    LinearLayoutManager linearLayoutManager;
    private View view;
    private String status="완료";
    private String subject="공통";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.finish_together_frag,container,false);
        finTogList();
        return view;
    }

    private void finTogList() {
        recyclerView=(RecyclerView)view.findViewById(R.id.finTogetherView);
        linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        ftar=new ArrayList<>();
       finTogAdapter=new FinTogAdapter(ftar);
        recyclerView.setAdapter(finTogAdapter);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("TeamTask");
                    if(ja.length()==0){
                        Toast.makeText(view.getContext(), "데이터가 없습니다", Toast.LENGTH_SHORT).show();
                        return;}
                    for(int i=0;i<ja.length();i++){
                        JSONObject item = ja.getJSONObject(i);
                        String userid=item.getString("userID");
                        String contents = item.getString("contents");
                        String gFinish = item.getString("gFinish");
                        IngTogData itd= new IngTogData(userid,contents,gFinish,R.drawable.clearmember);
                        ftar.add(itd);
                        finTogAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e("CE0","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        CurProjectinfo c=new CurProjectinfo();
        GetIngTogDataReq gitdr = new GetIngTogDataReq(c.getTeamNo(),status,subject,responseListener);
        RequestQueue queue = Volley.newRequestQueue( getActivity() );
        queue.add( gitdr );
    }
}

