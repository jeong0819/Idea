package com.example.realp;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DismissReciever extends BroadcastReceiver {
    Context context;
    CurProjectinfo c = new CurProjectinfo();
    NotificationManager manager;
    int notificationId;
    String 팀장;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        Log.e("거부","수행");
        String msg=intent.getStringExtra("msg");
        notificationId = intent.getIntExtra("notificationId", 0);
        int index=msg.indexOf("'팀장");
        팀장="";
        for(int i=1;i<index;i++) {
            팀장+=msg.charAt(i);
        }
        getStatus();

    }
    private void updateChase(int teamNo,String leader,int aCount,String status){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        Toast.makeText(context, "거부가 처리되었습니다.", Toast.LENGTH_SHORT).show();
                        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.cancel(notificationId);
                        rejectFcm("","팀원 중 거부자가 있어 해당 팀원은 팀에서 추방되지 않았습니다.");
                    }
                } catch (JSONException e) {
                    Log.e("ERROR","in updateChase");
                    e.printStackTrace();
                }

            }
        };
        UpdateChaseReq ucr=new UpdateChaseReq(teamNo,leader,aCount,status,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(ucr);
    }
    private void rejectFcm(String title,String msg){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        };
        //TODO:리시버가 센더에게 보낼수있도록 토큰값을 가져올수있게 ID값 변경
        RequestSecReq rsr = new RequestSecReq(2,title,msg,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(rsr);
    }
    private void getStatus(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        String cStatus=jsonObject.getString("cStatus");
                        if(cStatus.equals("진행")){
                            updateChase(c.getTeamNo(),팀장,0,"완료");
                        }else{
                            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.cancel(notificationId);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        //현재 사용자로 바꾸기
        GetCStatusReq gcsr=new GetCStatusReq(c.getTeamNo(),"이정현","진행",responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(gcsr);
    }
}
