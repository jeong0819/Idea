package com.example.realp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class TermActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar = null;
    private CheckBox mCheck1 = null;
    private CheckBox mCheck2 = null;
    private CheckBox mCheck3 = null;
    private CheckBox mCheck4 = null;
    private CheckBox mAllCheck = null;
    private Button mOkBtn = null;

    //마케팅 수신여부를 위한 플래그
    private Boolean mActResult = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBarCustomView actionBarCustomView = new ActionBarCustomView(this, this);
        actionBarCustomView.setTitle("이용약관동의");

        mCheck1 = findViewById(R.id.check_1);
        mCheck2 = findViewById(R.id.check_2);
        mCheck3 = findViewById(R.id.check_3);
        mCheck4 = findViewById(R.id.check_4);
        mAllCheck = findViewById(R.id.all_check);
        mOkBtn = findViewById(R.id.ok_btn);

        mAllCheck.setOnClickListener(this);
        mCheck1.setOnClickListener(this);
        mCheck2.setOnClickListener(this);
        mCheck3.setOnClickListener(this);
        mCheck4.setOnClickListener(this);
        mOkBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.back_img: {
                finish();
                break;
            }
            //전체 동의
            case R.id.all_check: {
                if(mAllCheck.isChecked()) {
                    mActResult = true;
                    mCheck1.setChecked(true);
                    mCheck2.setChecked(true);
                    mCheck3.setChecked(true);
                    mCheck4.setChecked(true);
                    mOkBtn.setBackgroundResource(R.drawable.m_border_box6);
                    mOkBtn.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    mActResult = false;
                    mCheck1.setChecked(false);
                    mCheck2.setChecked(false);
                    mCheck3.setChecked(false);
                    mCheck4.setChecked(false);
                    mOkBtn.setBackgroundResource(R.drawable.m_border_box3);
                    mOkBtn.setTextColor(Color.parseColor("#ffb400"));
                }

                break;
            }
            case R.id.check_3:
            case R.id.check_2:
            case R.id.check_1: {

                if(mCheck1.isChecked() && mCheck2.isChecked() && mCheck3.isChecked()) {
                    mOkBtn.setBackgroundResource(R.drawable.m_border_box6);
                    mOkBtn.setTextColor(Color.parseColor("#ffffff"));
                    mAllCheck.setChecked(true);
                }else {
                    mOkBtn.setBackgroundResource(R.drawable.m_border_box3);
                    mOkBtn.setTextColor(Color.parseColor("#ffb400"));
                    mAllCheck.setChecked(false);
                }
                break;
            }
            case R.id.check_4: {

                mActResult = mCheck1.isChecked();

                break;
            }
            case R.id.ok_btn: {

                //카카오톡 시도
                if(getIntent().getBooleanExtra("SNS" , false)) {
                    Intent intent = new Intent(this , SnsJoinActivity.class);
                    //광고 수신여부 전달
                    intent.putExtra("status" , mActResult);
                    intent.putExtra("snsId" , getIntent().getStringExtra("snsId"));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this , JoinActivity.class);
                    //광고 수신여부 전달
                    intent.putExtra("status" , mActResult);
                    startActivity(intent);
                }


                finish();
            }
        }
    }
}