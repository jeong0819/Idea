package com.example.realp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class EditProject extends AppCompatActivity {
    public static ArrayList<String> tmpuser=new ArrayList<>();
    public static ArrayList<String> tmpchar=new ArrayList<>();
    public static ArrayList<String> tmprank=new ArrayList<>();
    EditText ed_pname,ed_tname,ed_tsub;
    TextView tv_fd;
    CurProjectinfo c=new CurProjectinfo();
    private RecyclerView recyclerView;
    public static TeamAdapter teamAdapter;
    public static ArrayList<TeamData> tar;
    private LinearLayoutManager linearLayoutManager;
    public static int addCount=0;
    public int getAddCount() {
        return addCount;
    }

    public void setAddCount(int addCount) {
        this.addCount = addCount;
    }

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);
        ed_pname=findViewById(R.id.ed_pName);
        ed_tname=findViewById(R.id.ed_tname);
        ed_tsub=findViewById(R.id.ed_tsub);
        tv_fd=findViewById(R.id.tv_fd);
        ed_pname.setText(c.getProjectName());
        ed_tname.setText(c.getTeamName());
        ed_tsub.setText(c.getpTheme());
        tv_fd.setText(c.getFinishDate());
        findViewById(R.id.ed_fd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar =Calendar.getInstance();
                int y=calendar.get(calendar.YEAR);
                int m=calendar.get(calendar.MONTH)+1;
                int d= calendar.get(calendar.DATE);
                DatePickerDialog dpd=new DatePickerDialog(EditProject.this, R.style.time, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        SimpleDateFormat tmp=new SimpleDateFormat("yyyy.MM.dd");
                        Calendar calendar2=Calendar.getInstance();
                        Log.e("date",i+"/"+i1+"/"+i2);
                        calendar2.set(i,i1,i2);
                        String d=tmp.format(calendar2.getTime());
                        Log.e("d",d);
                        tv_fd.setText(d);
                    }
                },y,m-1,d);
                dpd.show();

            }
        });
        getTeamUserList();
    }
    private void changeDn(String newP,String newT){
        String curDN=c.getProjectName()+"_"+c.getTeamName()+"_"+c.getTeamNo();
        String cDN=newP+"_"+newT+"_"+c.getTeamNo();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        };
        ChangeStorageDirReq upr=new ChangeStorageDirReq(curDN,cDN,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add( upr );
    }
    private void getTeamUserList() {
        recyclerView=(RecyclerView)findViewById(R.id.setProjectView);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        tar=new ArrayList<>();
        teamAdapter=new TeamAdapter(tar);
        recyclerView.setAdapter(teamAdapter);
        for(int i=0;i<c.getTpc();i++){
            TeamData teamData=new TeamData(c.worker.get(i),c.wChar.get(i),c.wRank.get(i),R.drawable.clearmember,1,false);
            tar.add(teamData);
            teamAdapter.notifyDataSetChanged();
        }
    }

    public void backToSetList(View view) {
        setAddCount(0);
        tar.clear();
        tmpuser.clear();
        tmpchar.clear();
        tmprank.clear();
        finish();
    }

    public void addTeamUser(View view) {
        CustomDialog cd= new CustomDialog(this);
        cd.setCount(getAddCount()+1);
        cd.setEdsta(1);
        cd.callFunction();
    }

    public void updateProject(View view) {
        Log.e("결과",getAddCount()+"");
        String newpName=ed_pname.getText().toString();
        String newtName=ed_tname.getText().toString();
        if(!c.getProjectName().equals(newpName)||!c.getTeamName().equals(newtName)){
            changeDn(newpName,newtName);
        }
        String newsub=ed_tsub.getText().toString();
        String newfd=tv_fd.getText().toString();
        if(!c.getProjectName().equals(newpName)||!c.getTeamName().equals(newtName)||!c.getpTheme().equals(newsub)||!c.getFinishDate().equals(newfd)||getAddCount()>=1) {
            updateProjectInfo(newtName, newpName, newsub, newfd);
        } else{
           customFinish();
        }
    }
    private void customFinish(){
        tar.clear();
        tmpuser.clear();
        tmpchar.clear();
        tmprank.clear();
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("status",true);
        this.startActivity(intent);
    }
    private void updateProjectInfo(String newtn,String newpn,String newpt,String newfd) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean success = jsonObject.getBoolean( "success" );
                    if(success){
                        if(getAddCount()>=1){
                            for(int i=0;i<getAddCount();i++){
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject( response );
                                            boolean success = jsonObject.getBoolean( "success" );
                                            if(success){
                                            }
                                        } catch (JSONException e) {
                                            Log.e("CE3","Connect Error");
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                Log.e("adduser",c.getTeamNo()+"/"+tmpuser.get(i)+"/"+tmprank.get(i)+"/"+tmpchar.get(i));
                                AddTeamUserReq atur = new AddTeamUserReq(c.getTeamNo(),tmpuser.get(i),tmprank.get(i),tmpchar.get(i),responseListener);
                                RequestQueue queue = Volley.newRequestQueue(EditProject.this);
                                queue.add( atur );
                            }
                        }
                        customFinish();
                    }
                } catch (JSONException e) {
                    Log.e("CE2","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        Log.e("c.getTpc",c.getTpc()+""); //3
        UpdateProjectReq upr=new UpdateProjectReq(c.getTeamNo(),newtn,c.getTeamName(),newpn,c.getProjectName(),newpt,c.getpTheme(),c.getStartDate(),newfd,c.getFinishDate(),c.getTpc(),responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add( upr );
    }

}
