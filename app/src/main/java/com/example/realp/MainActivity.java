package com.example.realp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.common.MyApplication;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager=getSupportFragmentManager();
    private Fragment1 fragment1=new Fragment1();
    private Fragment2 fragment2=new Fragment2();
    private Fragment3 fragment3=new Fragment3(); //프로젝트
    private Fragment4 fragment4=new Fragment4();
    private Fragment5 fragment5=new Fragment5();
    private long backBtn=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChanner();
        permissionCheck();

        Intent i =getIntent();

            FirebaseMessaging.getInstance().subscribeToTopic("test");
            FirebaseInstanceId.getInstance().getToken();
            //기본 프레그먼트 지정
            openFragment(fragment1);
            BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.sell:
                            openFragment(fragment1);
                            break;
                        case R.id.together:
                            openFragment(fragment2);
                            break;
                        case R.id.project:
                            openFragment(fragment3);

                            break;
                        case R.id.chatting:
                            openFragment(fragment4);
                            break;
                        case R.id.set:
                            openFragment(fragment5);
                            break;
                    }
                    return true;
                }
            });
            boolean buy;
            boolean co;
            buy=i.getBooleanExtra("buy",false);
            co=i.getBooleanExtra("co",false);
            if (buy){
                openFragment(fragment1);
                return;
            }
            if (co){
                openFragment(fragment2);
                return;
            }

            boolean s;
            s=i.getBooleanExtra("status",false);
            if(s){
               openFragment(fragment3);
               return;
            }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==100&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
        }else{
            permissionCheck();
        }
    }
    private void permissionCheck() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET}, 100);
                return;
            }
        }
    }

        private void openFragment(final Fragment fragment)   {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        //뒤로가기 두번 눌러야 종료
        long curTime=System.currentTimeMillis();
        long gapTime=curTime-backBtn;
        if( 0<=gapTime && 2000 >= gapTime){
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }else{
            backBtn=curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
    private void createNotificationChanner(){
        CharSequence name="lemubit";
        String des="Channel for lemubit reminder";
        int importance= NotificationManager.IMPORTANCE_DEFAULT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel=new NotificationChannel("noti",name,importance);
            channel.setDescription(des);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

}
