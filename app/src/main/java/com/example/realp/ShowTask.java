package com.example.realp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ShowTask extends AppCompatActivity {
    TabLayout tabLayout;
    public static Spinner spinner;
    private IngTogetherFragment ing = new IngTogetherFragment();
    private IngPersonalFragment ipf=new IngPersonalFragment();
    private FinTogetherFragment fin=new FinTogetherFragment();
    private FinPersonalFragment finp=new FinPersonalFragment();
    ImageView iv_taskBar,iv_addTask;
    TextView findate;
    String cur="진행";
    String curSub="공통";
    public static int num;
    public static void setNum(int num) {
        ShowTask.num = num;
    }
    public static int getNum() {
        return num;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task);
        setNum(1);
        iv_taskBar=findViewById(R.id.iv_taskBack);
        findate=findViewById(R.id.tv_findate);
        iv_addTask=findViewById(R.id.iv_addTask);
        iv_addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (curSub){
                    case "공통":
                        setNum(1);
                        AddTaskDialog ad=new AddTaskDialog(ShowTask.this,1);
                        ad.callFunction();
                        break;
                    case "개인":
                        setNum(2);
                        AddTaskDialog ad2=new AddTaskDialog(ShowTask.this,2);
                        ad2.callFunction();
                        break;
                }

            }
        });
        iv_taskBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        openFragment(ing);
        spinner=findViewById(R.id.spinner);
        final ArrayList<String> ar= new ArrayList<>();
        ar.add("공통");
        ar.add("개인");
        ArrayAdapter adapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,ar);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (ar.get(i)){
                    case "공통":
                        curSub="공통";
                        setNum(1);
                        if(cur.equals("진행")){
                            openFragment(ing);
                        }else if(cur.equals("완료")){
                            openFragment(fin);
                        }
                        break;
                    case "개인":
                        curSub="개인";
                        setNum(2);
                        if(cur.equals("진행")){
                            openFragment(ipf);
                        }else if(cur.equals("완료")){
                            openFragment(finp);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tabLayout=findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString()){
                    case "진행":
                        cur="진행";
                        iv_addTask.setVisibility(View.VISIBLE);
                        findate.setText("완료 기한");
                        spinner.setSelection(0);
                        openFragment(ing);
                        break;
                    case "완료":
                        spinner.setSelection(0);
                        iv_addTask.setVisibility(View.INVISIBLE);
                        findate.setText("완료 날짜");
                        cur="완료";
                        openFragment(fin);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.taskFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
