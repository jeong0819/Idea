package com.example.realp;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RequestSecDialog extends AppCompatActivity {
    TextView req_id,req_date,req_close,req_send;
    EditText req_content;
    CurProjectinfo c=new CurProjectinfo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_req_sec);
        req_id=findViewById(R.id.req_id);
        req_id.setText("테스터3"); //TODO:현재 사용자 닉네임으로 설정
        Calendar calendar=Calendar.getInstance();
        int y=calendar.get(Calendar.YEAR);
        int m =calendar.get(Calendar.MONTH)+1;
        int d=calendar.get(Calendar.DATE);
        int h=calendar.get(Calendar.HOUR_OF_DAY);
        int mn=calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.YEAR,y);
        calendar.set(Calendar.MONTH,m-1);
        calendar.set(Calendar.DATE,d);
        calendar.set(Calendar.HOUR_OF_DAY,h);
        calendar.set(Calendar.MINUTE,mn);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy년 M월 dd일 HH시 mm분");
        req_date=findViewById(R.id.req_date);
        req_date.setText(sdf.format(calendar.getTime()));
        req_content=findViewById(R.id.req_content);
        req_close=findViewById(R.id.req_close);
        req_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        req_send=findViewById(R.id.req_send);
        req_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user=req_id.getText().toString();
                String date=req_date.getText().toString();
                String reciever=getReciever();
                if(reciever.equals("")){
                    Toast.makeText(RequestSecDialog.this, "다시시도해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String contents=req_content.getText().toString();
                Toast.makeText(RequestSecDialog.this, "탈퇴를 요청하였습니다", Toast.LENGTH_SHORT).show();
               insertAlarim(user,reciever,contents,date);

            }
        });

    }

    private String getReciever() {
        for(int i =0;i<c.worker.size();i++){
            if(c.wRank.get(i).equals("팀장")){
                return c.worker.get(i);
            }
        }
        return "";
    }

    private void insertAlarim(String user,String reciever,String contents,String octime){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        finish();
                        fcm(user,octime);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        InsertAlarimReq iar=new InsertAlarimReq(user,reciever,contents,octime,"대기",responseListener);
        RequestQueue queue = Volley.newRequestQueue(RequestSecDialog.this);
        queue.add(iar);
    }
    private void fcm(String sender,String octime){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //TODO:리시버에따른 토큰값을 가져올수있게 ID값 변경
        RequestSecReq rsr = new RequestSecReq(2,"팀원의 탈퇴요청",sender+"님이 팀에서 탈퇴하고 싶어합니다.\n일시:"+octime,responseListener);
        RequestQueue queue = Volley.newRequestQueue(RequestSecDialog.this);
        queue.add(rsr);
    }

}