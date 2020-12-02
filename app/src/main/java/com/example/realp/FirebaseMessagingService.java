package com.example.realp;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    String contents,title,message,user,sendtime;
    int noti_id=0;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage)
    {
        //TODO:알림이 여러개일경우 TITLE을 IF문으로 탈퇴요청인지 EQUALS를 사용하여 구분해서 수행하기
        title=remoteMessage.getData().get("title");
        message= remoteMessage.getData().get("message");
        if (remoteMessage.getData().size() > 0)
        {//todo:노티피케이션 requestcode랑 notify id값을 테이블의 no로해서 알림이 다르게되도록만들어 중복일경우의 상황을 방지하자
            if(title.contains("탈퇴요청")){
                getSecData("탈퇴 요청",getSender( remoteMessage.getData().get("message")),getDate( remoteMessage.getData().get("message")));
            }else{
                showNotification(title,message,"");
            }
        }
        if (remoteMessage.getNotification() != null)
        {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),"");
        }

    }
    private void getSecData(String k_alarim,String sender,String date){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        contents=jsonObject.getString("contents");
                        getNo(sender,date,contents);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        //TODO:curuserinfo클래스에서 curuser로 리시버를 where문에 추가해서 테스트해보기
        GetSecDataReq gsdr=new GetSecDataReq(k_alarim,sender,date,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(gsdr);
    }
    private void getNo(String sender,String date,String contents){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        noti_id=jsonObject.getInt("no");
                        showNotification(title,message,contents);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        //TODO:curuserinfo클래스에서 curuser로 리시버를 where문에 추가해서 테스트해보기
        GetSecNoReq gsnr=new GetSecNoReq("탈퇴 요청",sender,date,contents,"대기",responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(gsnr);
    }
    private RemoteViews getCustomDesign(String title, String message) {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_expand);
        if(title.equals("")){
            remoteViews.setViewVisibility(R.id.tv_c_t, View.GONE);
        }
        remoteViews.setTextViewText(R.id.tv_c_t, title);
        remoteViews.setTextViewText(R.id.tv_c_c, message);
        return remoteViews;
    }
    private String getDate(String message){
        Calendar c=Calendar.getInstance();
        int y=c.get(Calendar.YEAR);
        int d_s=message.indexOf(":"+y+"년");
        String octime="";
        for(int i=d_s+1;i<message.length();i++) {
            octime+=String.valueOf(message.charAt(i));
        }
        sendtime=octime;
        return octime;
    }
    private String getSender(String message) {
        int finish=message.indexOf("님이");
        String sender="";
        for(int i=0;i<finish;i++) {
            sender+=String.valueOf(message.charAt(i));
        }
        user=sender;
        return sender;
    }


    public void showNotification(String title, String message,String mc) {
        String channel_id = "pushtest";
        PendingIntent pendingIntent = null;
        PendingIntent btPendingIntent = null;
        PendingIntent acPendingIntent = null;
        if(title.contains("탈퇴요청")){
            Intent intent = new Intent(this, ResultSecDialog.class);
            intent.putExtra("sender",user);
            intent.putExtra("sendtime",sendtime);
            intent.putExtra("contents",mc);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, noti_id, intent, 0);
        }else if(title.equals("팀원 추방")){
            SharedPreferences sp=getSharedPreferences("noti_id",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            noti_id=sp.getInt("noti_id",0);
            noti_id+=1;
            editor.putInt("noti_id",noti_id);
            editor.commit();
            Intent disIntent = new Intent(this, DismissReciever.class);
            disIntent.putExtra("notificationId",noti_id);
            disIntent.putExtra("msg",message);
            btPendingIntent = PendingIntent.getBroadcast(this, noti_id, disIntent,0);
            Intent accIntent = new Intent(this, AccReciever.class);
            accIntent.putExtra("notificationId",noti_id);
            accIntent.putExtra("msg",message);
            acPendingIntent = PendingIntent.getBroadcast(this, noti_id, accIntent,0);
        }else{
            SharedPreferences sp=getSharedPreferences("noti_id",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            noti_id=sp.getInt("noti_id",0);
            noti_id+=1;
            editor.putInt("noti_id",noti_id);
            editor.commit();
        }
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.mainicon)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true);
        if(title.contains("탈퇴요청")){
            builder.setContentIntent(pendingIntent);
        }else if(title.contains("팀원 추방")){
            builder.setOngoing(true);
            builder.addAction(R.mipmap.ic_launcher_round,"거부",btPendingIntent);
            builder.addAction(R.mipmap.ic_launcher_round,"동의",acPendingIntent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            builder = builder.setContent(getCustomDesign(title, message));
        }
        else
        {
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.mainicon);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(noti_id, builder.build());
    }

}