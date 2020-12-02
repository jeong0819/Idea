package com.example.realp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CalManage extends AppCompatActivity {
    private CalendarView calendarView;
    public static TextView curDate;
    public static String sDate;
    private RecyclerView recyclerView;
    public static CalAdapter calAdapter;
    public static ArrayList<CalData> car;
    CurProjectinfo c =new CurProjectinfo();
    private LinearLayoutManager linearLayoutManager;
    public String getsDate() {
        return sDate;
    }
    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_manage);
        findViewById(R.id.iv_callist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CalManage.this,CalList.class);
                startActivity(i);
            }
        });
        findViewById(R.id.iv_calback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        calendarView=findViewById(R.id.calendarView);
        curDate=findViewById(R.id.curDate);
        Calendar calendar =Calendar.getInstance();
        int y=calendar.get(calendar.YEAR);
        int m=calendar.get(calendar.MONTH)+1;
        int d= calendar.get(calendar.DATE);
        calendar.set(y,m-1,d);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd");
        sDate=sdf.format(calendar.getTime());
        final SimpleDateFormat sdt=new SimpleDateFormat("HH:mm");
        curDate.setText(sDate);
        getCalList(sDate);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                calendar.set(i,i1,i2);
                sDate=sdf.format(calendar.getTime());
                curDate.setText(sDate);
                setsDate(sDate);
                getCalList(sDate);
            }
        });
        findViewById(R.id.addCal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] ad=sDate.split("[.]");
                if(y>Integer.parseInt(ad[0])){
                    Toast.makeText(CalManage.this, "년도가 과거년도입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(Integer.parseInt(ad[0])==y&&m>Integer.parseInt(ad[1])){
                    Toast.makeText(CalManage.this, "월수가 과거형입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(Integer.parseInt(ad[0])==y&&m==Integer.parseInt(ad[1])&&d>Integer.parseInt(ad[2])){
                    Toast.makeText(CalManage.this, "일자가 과거형입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                CustomCalDialog ccd=new CustomCalDialog(CalManage.this);
                ccd.setsDate(sDate);
                ccd.callFunction();
            }
        });
    }
    public void getCalList(String date){
        recyclerView=(RecyclerView)findViewById(R.id.calView);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        car=new ArrayList<>();
        calAdapter=new CalAdapter(car);
        recyclerView.setAdapter(calAdapter);

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
                        String time=item.getString("time");
                        String contents=item.getString("contents");
                        Boolean sta=item.getBoolean("aStatus");
                        CalData cd =new CalData(time,contents,R.drawable.cancel,sta);
                        car.add(cd);
                        calAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e("CE0","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        GetCalReq gcr = new GetCalReq(c.getTeamNo(),date,"이정현",responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add( gcr);
    }
}
