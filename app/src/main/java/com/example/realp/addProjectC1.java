package com.example.realp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class addProjectC1 extends AppCompatActivity {
    EditText projectName,teamName,start1,start2,start3,finish1,finish2,finish3,teamSubject;
    CheckBox setToday,setnull,check_pt,check_t,check_s;
    Calendar calendar=Calendar.getInstance();
    String startDate="",finishDate="";
    Boolean nofinish=false; //t=미정 f=선택
    Projectinfo p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter1);
        projectName=findViewById(R.id.projectName);
        teamName=findViewById(R.id.teamName);
        teamSubject=findViewById(R.id.teamSubject);
        check_pt=findViewById(R.id.check_pt);
        check_t=findViewById(R.id.check_t);
        check_s=findViewById(R.id.check_s);
        projectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(projectName.getText().toString().equals("미정")){
                    check_pt.setChecked(true);
                }else{
                    check_pt.setChecked(false);
                }
            }
        });
        teamName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(teamName.getText().toString().equals("미정")){
                    check_t.setChecked(true);
                }else{
                    check_t.setChecked(false);
                }
            }
        });

        check_pt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    projectName.setText("미정");
                    projectName.setSelection(projectName.length());
                }else{
                    projectName.setText("");
                    projectName.setSelection(projectName.length());
                }
            }
        });
        check_t.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    teamName.setText("미정");
                    teamName.setSelection(teamName.length());
                }else{
                    teamName.setText("");
                    teamName.setSelection(teamName.length());
                }
            }
        });
        teamSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(teamSubject.getText().toString().equals("미정")){
                    check_s.setChecked(true);
                }else{
                    check_s.setChecked(false);
                }
            }
        });
        check_s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    teamSubject.setText("미정");
                    teamSubject.setSelection(teamSubject.length());
                }else{
                    teamSubject.setText("");
                    teamSubject.setSelection(teamSubject.length());
                }
            }
        });
        start1=(EditText)this.findViewById(R.id.start1);
        start2=(EditText)this.findViewById(R.id.start2);
        start3=(EditText)this.findViewById(R.id.start3);
        setToday=(CheckBox)this.findViewById(R.id.setToday);
        setnull=findViewById(R.id.setnull);
        finish1=(EditText)findViewById(R.id.finish1);
        finish2=(EditText)findViewById(R.id.finish2);
        finish3=(EditText)findViewById(R.id.finish3);
        setToday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    int y=calendar.get(Calendar.YEAR);
                    String m=String.valueOf(calendar.get(Calendar.MONTH)+1);
                    if(m.length()==1){
                        m="0"+m;
                    }
                    String d= String.valueOf(calendar.get(Calendar.DATE));
                    if(d.length()==1){
                        d="0"+d;
                    }
                    startDate=y+m+d;
                    start1.setText(String.valueOf(y));
                    start2.setText(m);
                    start3.setText(d);
                }
                else{
                    start1.setText("");
                    start2.setText("");
                    start3.setText("");
                }
            }
        });
        setnull.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    nofinish=true;
                    finish1.setEnabled(false);
                    finish2.setEnabled(false);
                    finish3.setEnabled(false);
                }else{
                    nofinish=false;
                    finish1.setEnabled(true);
                    finish2.setEnabled(true);
                    finish3.setEnabled(true);
                }
            }
        });


    }

    public void getStartDate() {
        String y = start1.getText().toString();
        if(y.length()!=4||y.equals("")){
            Toast.makeText(this, "년도를 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        String m = start2.getText().toString();
        String d = start3.getText().toString();
        if(d.equals("0")||d.equals("00")||Integer.parseInt(d)>32||d.equals("")){
            Toast.makeText(this, "날짜를 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        startDate=y+"."+m+"."+d;
        p.setStartDate(startDate);
        getFinishDate();
    }

    public void getFinishDate() {
        String y = finish1.getText().toString();
        String m = finish2.getText().toString();
        if(m.length()==1&&!m.equals("0")){
            m="0"+m;
        }
        String d = finish3.getText().toString();
        if(nofinish||(y.equals("")&&m.equals("")&&d.equals(""))){ //종료날짜가 미정인경우
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage("종료날짜를 입력하지 않으셨습니다. 그대로 진행하시겠습니까?");
            builder.setPositiveButton("네",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // 네 클릭
                            Intent i= new Intent(addProjectC1.this,addProjectC2.class);
                            finishDate="미정";
                            p.setFinishDate(finishDate);
                            startActivity(i);
                        }
                    });
                    builder.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // 아니오 클릭. dialog 닫기.
                                    dialog.cancel();
                                    return;
                                }
                            });

            AlertDialog alert = builder.create();
            alert.setTitle("사용자 확인");
            alert.setIcon(R.drawable.mainicon);
            alert.show();
        }else{
           if(y.equals("")||y.length()!=4){
               Toast.makeText(this, "년도를 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
               return;
           }
            if(m.length()==1&&!m.equals("0")){
                m="0"+m;
            }
            if(m.equals("0")||m.equals("")){
                Toast.makeText(this, "월을 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            if(d.equals("0")||d.equals("00")||Integer.parseInt(d)>32||d.equals("")){
                Toast.makeText(this, "날짜를 정확히 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            finishDate=y+"."+m+"."+d;
            p.setFinishDate(finishDate);
            Intent i= new Intent(addProjectC1.this,addProjectC2.class);
            startActivity(i);
        }
    }

    public void backMenu(View view) {
        finish();
    }

    public void nextChapter(View view) {
        p = new Projectinfo();
        String ptName=projectName.getText().toString();
        if(ptName.equals("")){
            Toast.makeText(this, "프로젝트명을 기입해주세요", Toast.LENGTH_SHORT).show();
            return;
        }else{
            p.setProjectName(ptName);
        }

        String tName=teamName.getText().toString();
        if(tName.equals("")){
            Toast.makeText(this, "팀명을 기입해주세요", Toast.LENGTH_SHORT).show();
            return;
        }else{
            p.setTeamName(tName);
        }
        String sName=teamSubject.getText().toString();
        if(sName.equals("")){
            Toast.makeText(this, "주제를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }else{
            p.setPTheme(sName);
        }
        getStartDate();

    }


}
