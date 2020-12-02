package com.example.realp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowProject extends AppCompatActivity {
    public static RecyclerView recyclerView;
    public static WorkerAdapter workerAdapter;
    public static ArrayList<WorkerData> ar;
    public static LinearLayoutManager linearLayoutManager;
    TextView barName,tv_tName,tv_inTheme,tv_inStorage,tv_inCal;
    ImageButton iv_back;
    ImageView iv_menu,close_draw;
    CurProjectinfo cpi=new CurProjectinfo();
    private DrawerLayout drawerLayout;
    private View drawerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_project);
        recyclerView=(RecyclerView)findViewById(R.id.showTeamList);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ar=new ArrayList<>();
        workerAdapter=new WorkerAdapter(ar);
        recyclerView.setAdapter(workerAdapter);
        getProjectInfo();
        close_draw=findViewById(R.id.close_draw);
        drawerLayout=findViewById(R.id.drawer_layout);
        tv_inTheme=findViewById(R.id.tv_inTheme);
        tv_inStorage=findViewById(R.id.tv_inStrage);
        tv_tName=findViewById(R.id.tv_tName);
        drawerView=findViewById(R.id.show_team);
        barName=findViewById(R.id.barname);
        barName.setText(cpi.getProjectName());
        iv_back=findViewById(R.id.iv_back);
        iv_menu=findViewById(R.id.iv_menu);
        tv_inCal=findViewById(R.id.tv_inCalendar);
        findViewById(R.id.set_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:팀장만 설정가능
                drawerLayout.closeDrawers();
                Intent i =new Intent(ShowProject.this,SettingList.class);
                startActivity(i);
            }
        });
        tv_inCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShowProject.this,CalManage.class);
                startActivity(i);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cpi.worker.clear();
                cpi.wChar.clear();
                cpi.wRank.clear();
                Intent intent = new Intent(ShowProject.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("status",true);
                startActivity(intent);
            }
        });
        tv_inStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(ShowProject.this,ShowFileList.class);
                startActivity(i);
            }
        });
        close_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showWorker();
                drawerLayout.openDrawer(drawerView);

            }
        });
            drawerLayout.setDrawerListener(listener);
            drawerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });

        }

        DrawerLayout.DrawerListener listener= new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        };
    private void getProjectInfo(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    if(suc){
                        int teamNo=jsonObject.getInt("teamNo");
                        String teamName= jsonObject.getString("teamName");
                        tv_tName.setText(teamName);
                        String pName= jsonObject.getString("pName");
                        String pTheme= jsonObject.getString("pTheme");
                        String startDate= jsonObject.getString("startDate");
                        String finishDate= jsonObject.getString("finishDate");
                        int tpc=jsonObject.getInt("tpc");
                        cpi.setTeamNo(teamNo);
                        cpi.setTeamName(teamName);
                        cpi.setProjectName(pName);
                        cpi.setPTheme(pTheme);
                        if(cpi.getpTheme().equals("미정")){
                            tv_inTheme.append(cpi.getpTheme());
                        }else{
                            tv_inTheme.setText(cpi.getpTheme());
                        }
                        cpi.setStartDate(startDate);
                        cpi.setFinishDate(finishDate);
                        cpi.setTpc(tpc);
                        getTeamInfo();
                    }
                } catch (JSONException e) {
                    Log.e("getPInfo in showp","Connect Error");
                    finish();
                    e.printStackTrace();
                }
            }
        };
        getProjectInfoReq gpir = new getProjectInfoReq(cpi.getProjectName(),cpi.getTeamName(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(gpir);
    }
    private void getTeamInfo(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    JSONArray ja = jsonObject.getJSONArray("TeamUser");
                    for(int i=0;i<ja.length();i++) {
                        JSONObject item = ja.getJSONObject(i);
                        String userID = item.getString("userID");
                        cpi.worker.add(userID);
                        String teamChar = item.getString("teamChar");
                        cpi.wChar.add(teamChar);
                        String teamRank = item.getString("teamRank");
                        cpi.wRank.add(teamRank);
                    }
                    showWorker();
                } catch (JSONException e) {
                    Log.e("getTInfo in showp","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        GetTeamInfoReq gtir = new GetTeamInfoReq(cpi.getTeamNo(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(gtir);
    }
    public void showWorker(){
            for(int i=0;i<cpi.getTpc();i++){
                WorkerData wd=new WorkerData(cpi.worker.get(i),cpi.wChar.get(i),cpi.wRank.get(i));
                ar.add(wd);
                workerAdapter.notifyDataSetChanged();
            }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(drawerView)){
            drawerLayout.closeDrawers();
        }else{
            cpi.worker.clear();
            cpi.wChar.clear();
            cpi.wRank.clear();
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("status",true);
            this.startActivity(intent);
        }
    }

    public void openTask(View view) {
        task();
    }
    public void task(){
        Intent i = new Intent(ShowProject.this,ShowTask.class);
        startActivity(i);
    }
}

