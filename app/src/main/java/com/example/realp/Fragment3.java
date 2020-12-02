package com.example.realp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class Fragment3 extends Fragment {
    View view; //프로젝트화면의 뷰
    Button btn_add;
    public static Class returnClass;
     RecyclerView f3_rv;
    ProjectAdapter projectAdapter;
     ArrayList<ProjectData> par;
    LinearLayoutManager linearLayoutManager;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.frag3,container,false);
        listPost();
        btn_add=view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Projectinfo p = new Projectinfo();
                 p.clearData();
                Intent i = new Intent(view.getContext(),addProjectC1.class);
                returnClass = getActivity().getClass();
                startActivity(i);
            }
        });
        return view;
    }
    public void listPost(){
        Log.e("수행","수행");
        f3_rv=(RecyclerView)view.findViewById(R.id.f_recylerview);
        linearLayoutManager=new LinearLayoutManager(getActivity());
        f3_rv.setLayoutManager(linearLayoutManager);
        par=new ArrayList<>();
        projectAdapter=new ProjectAdapter(par);
        f3_rv.setAdapter(projectAdapter);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("Team");
                    if(ja.length()==0){return;}
                    for(int i=0;i<ja.length();i++){
                        JSONObject item = ja.getJSONObject(i);
                        String pName=item.getString("pName");
                        String teamName=item.getString("teamName");
                        int tpc =item.getInt("tpc");
                        String sDate = item.getString("startDate");
                        String fDate = item.getString("finishDate");
                        ProjectData pd= new ProjectData(pName,teamName,R.drawable.teampcount,tpc,sDate,fDate);
                        par.add(pd);
                        projectAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e("frag3 listpost","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        getProjectListReq lpr = new getProjectListReq(responseListener);
        RequestQueue queue = Volley.newRequestQueue( view.getContext());
        queue.add( lpr );
    }

}
