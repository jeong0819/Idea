package com.example.realp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTaskDialog {
    private Context context;
    CurProjectinfo c=new CurProjectinfo();
    private int number; //1=진행/공통, 2=진행/개인
    public AddTaskDialog(Context context,int number)
    {
        this.context = context;
        this.number=number;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_add_task);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Spinner sp_user=dlg.findViewById(R.id.sp_user);
        //TODO:팀장일시 전체+모든팀원,팀원일시 본인(현재사용자)만
        final ArrayList<String> ar= new ArrayList<>();
        ar.add("이정현");
        ArrayAdapter adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,ar);
        sp_user.setAdapter(adapter);
        final EditText ed_contents=dlg.findViewById(R.id.ed_contents);
        final TextView tv_addt=dlg.findViewById(R.id.tv_addfd);
        final ImageView iv_addt=dlg.findViewById(R.id.iv_addt);
        iv_addt.setOnClickListener(new View.OnClickListener() {
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
                        tv_addt.setText(d);
                    }
                },y,m-1,d);
                dpd.show();
            }
        });

        final TextView dlgNag=dlg.findViewById(R.id.clearTask);
        final TextView dlgPos=dlg.findViewById(R.id.addTask);

      dlgNag.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              dlg.dismiss();
          }
      });
        dlgPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tpic=sp_user.getSelectedItem().toString();
                String contents=ed_contents.getText().toString();
                if(contents.equals("")){
                    Toast.makeText(context, "업무를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String date=tv_addt.getText().toString();
                if(date.equals("")){
                    Toast.makeText(context, "날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] ad=date.split("[.]");
                Calendar calendar =Calendar.getInstance();
                int y=calendar.get(calendar.YEAR);
                int m=calendar.get(calendar.MONTH)+1;
                int d= calendar.get(calendar.DATE);
                if(y>Integer.parseInt(ad[0])){
                    Toast.makeText(context, "년도가 과거년도입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(Integer.parseInt(ad[0])==y&&m>Integer.parseInt(ad[1])){
                    Toast.makeText(context, "월수가 과거형입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(Integer.parseInt(ad[0])==y&&m==Integer.parseInt(ad[1])&&d>Integer.parseInt(ad[2])){
                    Toast.makeText(context, "일자가 과거형입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
               switch (number){
                   case 1:
                       insertDB("진행","공통",tpic,contents,date);
                       break;
                   case 2:
                       insertDB("진행","개인",tpic,contents,date);
                       break;
               }
               dlg.dismiss();
            }
        });
    }
    private void insertDB(String status,String subject,String user,String contents,String gFinish){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    if(suc){
                        IngTogData itd=new IngTogData(user,contents,gFinish,R.drawable.clearmember);
                        if(number==1){
                            IngTogetherFragment it=new IngTogetherFragment();
                            it.iar.add(itd);
                            it.ingTogAdapter.notifyDataSetChanged();
                        }else{
                            IngPersonalFragment ip=new IngPersonalFragment();
                            ip.ipar.add(itd);
                            ip.ingTogAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    Log.e("CE0","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        InsertTaskReq itr = new InsertTaskReq(c.getTeamNo(),status,subject,user,contents,gFinish,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(itr);

    }

}