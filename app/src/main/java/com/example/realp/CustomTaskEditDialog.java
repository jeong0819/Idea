package com.example.realp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomTaskEditDialog {
    private Context context;
    CurProjectinfo c=new CurProjectinfo();
    int userIndex;
    String sta,aos,con,fDate,manager;
    ArrayList<String> mar= new ArrayList<>();
    Spinner sp_manage;
    Dialog dlg;
    public CustomTaskEditDialog(Context context,String sta,String aos,String manage,String con,String fDate) {
        this.context = context;
        this.sta=sta;
        this.aos=aos;
        this.manager=manage;
        this.con=con;
        this.fDate=fDate;
    }
    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        dlg = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_edit_task);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();
        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Spinner sp_status=dlg.findViewById(R.id.sp_status);
        final ArrayList<String> sar= new ArrayList<>();
        sar.add("진행");
        sar.add("완료");
        ArrayAdapter adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,sar);
        sp_status.setAdapter(adapter);
        final Spinner sp_aos=dlg.findViewById(R.id.sp_allOrsol);
        final ArrayList<String> asar= new ArrayList<>();
        asar.add("공통");
        asar.add("개인");
        ArrayAdapter asadapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,asar);
        sp_aos.setAdapter(asadapter);
        if(aos.equals("개인")){
            sp_aos.setSelection(1);
        }
        sp_manage=dlg.findViewById(R.id.sp_manage);
        //팀원가져오기
        mar.add("전체");

        getTeamWorker(c.getTeamNo());
        ArrayAdapter madapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,mar);
        sp_manage.setAdapter(madapter);
        final EditText ed_con=dlg.findViewById(R.id.edit_con);
        ed_con.setText(con);
        final TextView tv_td=dlg.findViewById(R.id.tv_taskDate);
        tv_td.setText(fDate);
        final ImageView ed_td=dlg.findViewById(R.id.ed_taskDate);
        final TextView dlgNag=dlg.findViewById(R.id.ed_clearcal);
        final TextView dlgPos=dlg.findViewById(R.id.ed_addcal);
        ed_td.setOnClickListener(new View.OnClickListener() {
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
                        calendar2.set(i,i1,i2);
                        String d=tmp.format(calendar2.getTime());
                        tv_td.setText(d);
                    }
                },y,m-1,d);
                dpd.show();
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
                String newSta,newTag,newuser,newCon,newDate;
                newSta=sp_status.getSelectedItem().toString();
                newTag=sp_aos.getSelectedItem().toString();
                newuser=sp_manage.getSelectedItem().toString();
                newCon=ed_con.getText().toString();
                if(sta.equals("진행")&&newSta.equals("완료")){
                    Calendar calendar=Calendar.getInstance();
                    int y=calendar.get(Calendar.YEAR);
                    String m=String.valueOf(calendar.get(Calendar.MONTH)+1);
                    if(m.length()==1){
                        m="0"+m;
                    }
                    String d= String.valueOf(calendar.get(Calendar.DATE));
                    if(d.length()==1){
                        d="0"+d;
                    }
                    newDate=y+"."+m+"."+d;
                }else{
                    newDate=tv_td.getText().toString();
                }
                if(!sta.equals(newSta)||!aos.equals(newTag)||!manager.equals(newuser)||!con.equals(newCon)||!fDate.equals(newDate)){
                    updateDB(newSta,newTag,newuser,newCon,newDate);
                }else{
                    dlg.dismiss();
                }
                dlg.dismiss();
            }
        });
    }

    private void getTeamWorker(int teamNo) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject( response );
                        JSONArray ja = jsonObject.getJSONArray("TeamUser");
                        for(int i=0;i<ja.length();i++){
                            JSONObject item = ja.getJSONObject(i);
                            String userID=item.getString("userID");
                            if(manager.equals(userID)){
                                userIndex=i+1;
                            }
                            mar.add(userID);
                        }
                        sp_manage.setSelection(userIndex);
                    } catch (JSONException e) {
                        Log.e("일정편집 팀원가져오기","Connect Error");
                        e.printStackTrace();
                    }
                }
            };
            GetTeamInfoReq gtir = new GetTeamInfoReq(teamNo,responseListener);
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(gtir);
    }



    private void updateDB(String newsta,String newsub,String newuser,String newcon,String newDate){
      Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    if(suc){
                        if(newsta.equals("완료")){
                            if(aos.equals("공통")&&newsub.equals("개인")){
                                IngTogData itd= new IngTogData(manager,con,fDate,R.drawable.clearmember);
                                IngTogetherFragment itf=new IngTogetherFragment();
                                int index=getIndex(itd,0);
                                Log.e("완공개 index",index+"");
                                itf.iar.remove(index);
                                itf.ingTogAdapter.notifyDataSetChanged();
                                FinPersonalFragment fpf=new FinPersonalFragment();
                                IngTogData ftd= new IngTogData(newuser,newcon,newDate,R.drawable.clearmember);
                                fpf.fpar.add(ftd);
                                fpf.finTogAdapter.notifyDataSetChanged();
                            }else if(aos.equals("개인")&&newsub.equals("공통")){
                                IngTogData itd= new IngTogData(manager,con,fDate,R.drawable.clearmember);
                                IngPersonalFragment ipf=new IngPersonalFragment();
                                int index=getIndex(itd,1);
                                Log.e("완개공 index",index+"");
                                ipf.ipar.remove(index);
                                ipf.ingTogAdapter.notifyDataSetChanged();
                                FinTogetherFragment ftf=new FinTogetherFragment();
                                IngTogData ftd= new IngTogData(newuser,newcon,newDate,R.drawable.clearmember);
                                ftf.ftar.add(ftd);
                                ftf.finTogAdapter.notifyDataSetChanged();
                            }else if(aos.equals("공통")){
                                IngTogData itd= new IngTogData(manager,con,fDate,R.drawable.clearmember);
                                IngTogetherFragment itf=new IngTogetherFragment();
                                int index=getIndex(itd,0);
                                Log.e("완공 index",index+"");
                                itf.iar.remove(index);
                                itf.ingTogAdapter.notifyDataSetChanged();
                            }else if(aos.equals("개인")){
                                IngTogData itd= new IngTogData(manager,con,fDate,R.drawable.clearmember);
                                IngPersonalFragment ipf=new IngPersonalFragment();
                                int index=getIndex(itd,1);
                                Log.e("완개 index",index+"");
                                ipf.ipar.remove(index);
                                ipf.ingTogAdapter.notifyDataSetChanged();
                            }
                        }else if(newsta.equals("진행")) {
                            if (aos.equals("공통") && newsub.equals("개인")) {
                                IngTogData itd= new IngTogData(manager,con,fDate,R.drawable.clearmember);
                                IngTogetherFragment itf=new IngTogetherFragment();
                                int index=getIndex(itd,0);
                                Log.e("공개 index",index+"");
                                itf.iar.remove(index);
                                itf.ingTogAdapter.notifyDataSetChanged();

                            } else if (aos.equals("개인") && newsub.equals("공통")) {
                                IngTogData itd= new IngTogData(manager,con,fDate,R.drawable.clearmember);
                                IngPersonalFragment ipf=new IngPersonalFragment();
                                Log.e("수행","겟인덱스전");
                                int index=getIndex(itd,1);
                                Log.e("개공 index",index+"");
                                ipf.ipar.remove(index);
                                ipf.ingTogAdapter.notifyDataSetChanged();

                            }else if(aos.equals("공통")&&newsub.equals("공통")){
                                IngTogData itd= new IngTogData(manager,con,fDate,R.drawable.clearmember);
                                IngTogetherFragment itf=new IngTogetherFragment();
                                int index=getIndex(itd,0);
                                Log.e("공공 index",index+"");
                                itf.iar.remove(index);
                                itf.ingTogAdapter.notifyDataSetChanged();
                                IngTogData ntd= new IngTogData(newuser,newcon,newDate,R.drawable.clearmember);
                                itf.iar.add(index,ntd);
                                itf.ingTogAdapter.notifyDataSetChanged();
                            }else if(aos.equals("개인")&&newsub.equals("개인")){
                                IngTogData itd= new IngTogData(manager,con,fDate,R.drawable.clearmember);
                                IngPersonalFragment ipf=new IngPersonalFragment();
                                int index=getIndex(itd,1);
                                Log.e("개개 index",index+"");
                                ipf.ipar.remove(index);
                                ipf.ingTogAdapter.notifyDataSetChanged();
                                IngTogData ntd= new IngTogData(newuser,newcon,newDate,R.drawable.clearmember);
                                ipf.ipar.add(index,ntd);
                                ipf.ingTogAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    Log.e("할일수정","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        UpdateTaskReq ucr = new UpdateTaskReq(c.getTeamNo(),sta,aos,manager,con,fDate,newsta,newsub,newuser,newcon,newDate,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(ucr);
    }
    private int getIndex(IngTogData tcd,int n){ //n=0-공통, 1-개인
        if(n==0){
            IngTogetherFragment itf=new IngTogetherFragment();
            for(int i=0;i<itf.iar.size();i++){
                if(itf.iar.get(i).getIngTogTask().equals(tcd.getIngTogTask())&&itf.iar.get(i).getIngTogDate().equals(tcd.getIngTogDate())){
                    return i;
                }
            }
        }else if(n==1){
            IngPersonalFragment ipf=new IngPersonalFragment();
            for(int i=0;i<ipf.ipar.size();i++){
                if(ipf.ipar.get(i).getIngTogTask().equals(tcd.getIngTogTask())&&ipf.ipar.get(i).getIngTogDate().equals(tcd.getIngTogDate())){
                    return i;
                }
            }
        }
        return -1;
    }

}