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

public class AccReciever extends BroadcastReceiver {
    CurProjectinfo c=new CurProjectinfo();
    Context context;
    int notificationId;
    NotificationManager manager;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        String msg=intent.getStringExtra("msg");
        notificationId = intent.getIntExtra("notificationId", 0);
        int index=msg.indexOf("'팀장");
        String 팀장="";
        for(int i=1;i<index;i++) {
            팀장+=msg.charAt(i);
        }
        getACount(c.getTeamNo(),팀장,"진행");
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
        
    }
    private void getACount(int teamNo,String leader,String status){ //sta="진행"
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        int aCount=jsonObject.getInt("aCount");
                        String target=jsonObject.getString("target");
                        if((aCount+1)==c.getTpc()-1){
                            aCount+=1;
                            updateChase(c.getTeamNo(),leader,aCount,"완료");
                            deleteWorker(target);
                        }else{
                            aCount+=1;
                            updateChase(c.getTeamNo(),leader,aCount,"진행");
                        }

                    }
                } catch (JSONException e) {
                    Log.e("ERROR","in getACount");
                    e.printStackTrace();
                }

            }
        };
        GetACountReq gacr=new GetACountReq(teamNo,leader,status,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(gacr);
    }
    private void updateChase(int teamNo,String leader,int aCount,String status){ //sta="진행"
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        Toast.makeText(context, "동의가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.cancel(notificationId);
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
    private void deleteWorker(String target){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        updateTpc(target);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        DeleteWorkerReq dwr = new DeleteWorkerReq(c.getTeamNo(),target,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(dwr);
    }
    private void updateTpc(String target){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        rejectFcm("","'"+target+"' 팀원이 추방되었습니다."); //TODO:팀장에게 전달되도록 설정
                        rejectFcm("",c.getTeamName()+"팀에서 추방되었습니다.");//TODO:대상에게 포문으로묶어서 전달되도록 설정
                        c.setTpc(c.getTpc()-1);
                        int index=c.worker.indexOf(target);
                        c.worker.remove(index);
                        c.wChar.remove(index);
                        c.wRank.remove(index);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //TODO:리시버는 현재사용자
        UpdateTpcReq utr = new UpdateTpcReq(c.getTeamNo(),c.getTpc()-1,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(utr);
    }
}
