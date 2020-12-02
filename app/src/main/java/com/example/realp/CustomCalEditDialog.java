package com.example.realp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomCalEditDialog {
    private Context context;
    CurProjectinfo c=new CurProjectinfo();
    CalManage cm= new CalManage();
    int mh,mm,curNotiNum;
    String sDate,ctime,contents;
    Boolean isSw,isSelect=false;
    public CustomCalEditDialog(Context context, String sDate,String time,String contents,Boolean isSw) {
        this.context = context;
        this.sDate=sDate;
        this.ctime=time;
        this.contents=contents;
        this.isSw=isSw;

    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_edit_cal);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();
        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText ed_calctn=(EditText) dlg.findViewById(R.id.ed_calctn);
        ed_calctn.setText(contents);
        final TextView ed_setDate=dlg.findViewById(R.id.ed_setDate);
        ed_setDate.setText(sDate);
        final ImageView ed_date=dlg.findViewById(R.id.ed_date);
        final TextView ed_setTime=dlg.findViewById(R.id.ed_setTime);
        ed_setTime.setText(ctime);
        final ImageView iv_getTime = dlg.findViewById(R.id.ed_getcal);
        final Switch sw=dlg.findViewById(R.id.ed_sw);
        sw.setChecked(isSw);
        final TextView dlgNag=dlg.findViewById(R.id.ed_clearcal);
        final TextView dlgPos=dlg.findViewById(R.id.ed_addcal);
        final SimpleDateFormat sdt=new SimpleDateFormat("HH:mm");
        ed_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar =Calendar.getInstance();
                int y=calendar.get(calendar.YEAR);
                int m=calendar.get(calendar.MONTH)+1;
                int d= calendar.get(calendar.DATE);
                DatePickerDialog dpd=new DatePickerDialog(context, R.style.time, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        SimpleDateFormat tmp=new SimpleDateFormat("yyyy.MM.dd");
                        Calendar calendar2=Calendar.getInstance();
                        Log.e("date",i+"/"+i1+"/"+i2);
                        calendar2.set(i,i1,i2);
                        String d=tmp.format(calendar2.getTime());
                        Log.e("d",d);
                        ed_setDate.setText(d);
                    }
                },y,m-1,d);
                dpd.show();
            }
        });
        iv_getTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] ct=ctime.split(":");
                TimePickerDialog tlg = new TimePickerDialog(context,R.style.time, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        isSelect=true;
                        mh=i;
                        mm=i1;
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(0,0,0,mh,mm);;
                        String t=sdt.format(calendar1.getTime());
                        ed_setTime.setText(t);
                    }
                },Integer.parseInt(ct[0]),Integer.parseInt(ct[1]),false
                );
                if(isSelect){
                    tlg.updateTime(mh,mm);
                }
                tlg.show();
            }
        });
      dlgNag.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              dlg.dismiss();
          }
      });
        dlgPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //생성자에서 초기화한한값들이랑 비교해서 변경된게 있으면 디비갱신 아니면 그냥 닫기
                String newCon,newDate,newTime,newSta;
                newCon=ed_calctn.getText().toString();
                newDate=ed_setDate.getText().toString();
                newTime=ed_setTime.getText().toString();
                newSta=String.valueOf(sw.isChecked());
                if(!sDate.equals(newDate)||!ctime.equals(newTime)||!contents.equals(newCon)||(isSw!=Boolean.valueOf(newSta))){
                    updateDB(newDate,newTime,newCon,newSta);
                }else{
                    dlg.dismiss();
                }
                dlg.dismiss();
            }
        });
    }
    private void updateDB(String newsDate,String newtime,String newcon,String newaSta){
      Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    if(suc){
                        CalData ocd=new CalData(ctime,contents,R.drawable.cancel,isSw);
                        CalData ncd=new CalData(newtime,newcon,R.drawable.cancel,Boolean.valueOf(newaSta));
                        if(!sDate.equals(newsDate)){
                            int index=getIndex(ocd);
                            cm.car.remove(index);
                            cm.calAdapter.notifyDataSetChanged();
                        }
                        else {
                            int index=getIndex(ocd);
                            cm.car.set(index,ncd);
                            cm.calAdapter.notifyDataSetChanged();
                        }
                        if(isSw&&newaSta.equals("false")){
                            //알람제거
                            getNotiNoForDel(newsDate,newtime,newcon,"이정현",0);
                        }
                        if(!isSw&&newaSta.equals("true")){
                            //알람추가
                            getNotiNoForAdd(newsDate,newtime,newcon,"이정현");
                        }
                        if(isSw&&newaSta.equals("true")){
                            //시간확인-그대로면 그냥 지나감,아니면 변경
                            if(!sDate.equals(newsDate)){
                                getNotiNoForDel(newsDate,newtime,newcon,"이정현",2);
                            }else if(!ctime.equals(newtime)){
                                getNotiNoForDel(newsDate,newtime,newcon,"이정현",2);
                            }
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    Log.e("CE0","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        UpdateCalReq ucr = new UpdateCalReq(c.getTeamNo(),"이정현",sDate,ctime,contents,String.valueOf(isSw),
                newsDate,newtime,newcon,newaSta,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(ucr);
    }
    private int getIndex(CalData tcd){
        for(int i=0;i<cm.car.size();i++){
            if(cm.car.get(i).getContents().equals(tcd.getContents())){
                return i;
            }
        }
        return -1;
    }
    private void getNotiNoForDel(String date,String time,String con,String user,int check){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    int noti_i=jsonObject.getInt("noti");
                    if(suc){
                        curNotiNum=noti_i;
                        AlarmManager am = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
                        Intent intent = new Intent(context, ReminderBroadcastReiceiver.class);
                        PendingIntent sender = PendingIntent.getBroadcast(context, curNotiNum, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        if (sender != null) {
                            am.cancel(sender);
                            sender.cancel();
                        }
                        if(check==2){
                            getNotiNoForAdd(date,time,con,"이정현");
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    Log.e("CE0","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        GetNotiNoReq gnnr = new GetNotiNoReq(c.getTeamNo(),date,time,con,user,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(gnnr);

    }
    private void getNotiNoForAdd(String date,String time,String con,String user){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    int noti_i=jsonObject.getInt("noti");
                    if(suc){
                        curNotiNum=noti_i;
                        Intent intent=new Intent(context,ReminderBroadcastReiceiver.class);
                        intent.putExtra("text",con);
                        intent.putExtra("notinum",noti_i);
                        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,noti_i,intent,0);
                        AlarmManager alarmManager= null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
                        }
                        String[] ad=date.split("[.]");
                        String[] at=time.split(":");
                        Calendar cal=Calendar.getInstance();
                        cal.set(Calendar.YEAR,Integer.parseInt(ad[0]));
                        cal.set(Calendar.MONTH,Integer.parseInt(ad[1])-1);
                        cal.set(Calendar.DATE,Integer.parseInt(ad[2]));
                        cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(at[0]));
                        cal.set(Calendar.MINUTE,Integer.parseInt(at[1]));
                        cal.set(Calendar.SECOND, 0);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent);
                        Log.e("알람","설정");
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    Log.e("CE0","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        GetNotiNoReq gnnr = new GetNotiNoReq(c.getTeamNo(),date,time,con,user,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(gnnr);

    }
}