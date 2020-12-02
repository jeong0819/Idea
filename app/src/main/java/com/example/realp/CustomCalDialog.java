package com.example.realp;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CustomCalDialog {
    static int count=0;
    private Context context;
    CurProjectinfo c=new CurProjectinfo();
    int mh,mm;
    String sDate;
    public static String mc;
    Boolean tsw=false;
    public static int i=0;


    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public CustomCalDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {
        sDate=getsDate();
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_add_cal);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText calctn=(EditText) dlg.findViewById(R.id.calctn);
        final TextView tv_setTime=dlg.findViewById(R.id.tv_setTime);
        final ImageView iv_getTime = dlg.findViewById(R.id.iv_getcal);
        final Switch sw=dlg.findViewById(R.id.sw);

        final TextView dlgNag=dlg.findViewById(R.id.clearcal);
        final TextView dlgPos=dlg.findViewById(R.id.addcal);
        final SimpleDateFormat sdt=new SimpleDateFormat("HH:mm");
        iv_getTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar tmpcal=Calendar.getInstance();
                TimePickerDialog tlg = new TimePickerDialog(context,R.style.time, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String[] td=sDate.split("[.]");
                        Calendar cur = Calendar.getInstance();
                        cur.set(Calendar.MONTH,cur.get(Calendar.MONTH)+1);
                        mh=i;
                        mm=i1;
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Integer.parseInt(td[0]),Integer.parseInt(td[1]),Integer.parseInt(td[2]),mh,mm);
                        if(calendar1.before(cur)){
                            Toast.makeText(context, "현재 시간 이후로 설정해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String t=sdt.format(calendar1.getTime());
                        tv_setTime.setText(t);
                    }
                },tmpcal.get(Calendar.HOUR_OF_DAY),tmpcal.get(Calendar.MINUTE),false
                );
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
                String mName="이정현"; //TODO:현재 사용자 닉네임으로 처리되게 바꾸기
                mc = calctn.getText().toString();
                if(mc.equals("")){
                    Toast.makeText(context, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String mt=tv_setTime.getText().toString();
                if(mt.equals("")||mt==null){
                    Toast.makeText(context, "시간을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(sw.isChecked()){
                    tsw=true;
                }else{
                    tsw=false;
                }
                insertDB(mt,mc,mName, String.valueOf(tsw));
                dlg.dismiss();
            }
        });
    }
    private void insertDB(String mt,String mc,String mName,String aStatus){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    if(suc){
                        CalData cd=new CalData(mt,mc,R.drawable.cancel,tsw);
                        CalManage cm=new CalManage();
                        cm.car.add(cd);
                        cm.calAdapter.notifyDataSetChanged();
                        if(tsw){
                            getNoti_i(getsDate(),mt,mc,mName);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    Log.e("일정추가","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        InsertCalReq icr = new InsertCalReq(c.getTeamNo(),getsDate(),mt,mc,mName,aStatus,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(icr);

    }
    private void getNoti_i(String sDate,String mt,String mc,String mu){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    int noti_i=jsonObject.getInt("noti");
                    if(suc){
                        Intent intent=new Intent(context,ReminderBroadcastReiceiver.class);
                        intent.putExtra("text",mc);
                        intent.putExtra("notinum",noti_i);
                        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,noti_i,intent,0);
                        AlarmManager alarmManager= null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
                        }
                        String[] ad=sDate.split("[.]");
                        String[] at=mt.split(":");
                        Calendar cal=Calendar.getInstance();
                        cal.set(Calendar.YEAR,Integer.parseInt(ad[0]));
                        cal.set(Calendar.MONTH,Integer.parseInt(ad[1])-1);
                        cal.set(Calendar.DATE,Integer.parseInt(ad[2]));
                        cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(at[0]));
                        cal.set(Calendar.MINUTE,Integer.parseInt(at[1]));
                        cal.set(Calendar.SECOND, 0);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent);

                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    Log.e("CE0","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        GetNotiNoReq gnnr = new GetNotiNoReq(c.getTeamNo(),sDate,mt,mc,mu,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(gnnr);
    }
}