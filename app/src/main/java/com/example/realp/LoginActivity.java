package com.example.realp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.realp.common.ActivityBase;
import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.example.realp.common.ActivityBase;
import com.example.realp.common.MyApplication;
import com.example.realp.common.UrlInfo;
import com.example.realp.common.util.CustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends ActivityBase implements View.OnClickListener {

    private EditText mIdEdit = null;
    private EditText mpassword_edit = null;
    private TextView mJoinText = null;
    private Button mLoginBtn = null;
    private ImageView mKakaoImg = null;

    private LoginButton com_kakao_login;

    private SessionCallback sessionCallback;

    private String mLoginId = "";
    private String mLoginPass = "";
    //1이면 일반로그인 , 2이면 카카오톡 로그인
    private int mLoginType = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getHashKey();

        mIdEdit = findViewById(R.id.id_edit);
        mpassword_edit = findViewById(R.id.password_edit);
        mJoinText = findViewById(R.id.join_text);
        mLoginBtn = findViewById(R.id.login_btn);
        mKakaoImg = findViewById(R.id.kakao_img);
        com_kakao_login = findViewById(R.id.com_kakao_login);


        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);

        //클릭 리스너
        mJoinText.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mKakaoImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.join_text: {

                //약관페이지 이동
                Intent intent = new Intent(this , TermActivity.class);
                startActivity(intent);

                break;
            }
            case R.id.login_btn: {
                //아이디 입력 체크
                if(mIdEdit.getText().toString().length() <= 0) {
                    Toast.makeText(this , "아이디를 입력해주세요." , Toast.LENGTH_SHORT).show();
                    return;
                }
                //비밀번호 입력 체크
                if(mpassword_edit.getText().toString().length() <= 0) {
                    Toast.makeText(this , "비밀번호를 입력해주세요." , Toast.LENGTH_SHORT).show();
                    return;
                }

                //로그인 시도 및 변수저장

                mLoginId = mIdEdit.getText().toString();
                mLoginPass = mpassword_edit.getText().toString();
                signin();
                break;
            }
            case R.id.kakao_img: {
                //카카오톡 버튼 클릭
                com_kakao_login.performClick();
                break;
            }
        }
    }

    //카카오톡 로그인시 , 성공값 전달받기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) { //카카오 로그인 액티비티에서 넘어온 경우일 때 실행
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    //화면종료시 , 콜백 제거
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    //카카오톡 가입여부 확인
                    mLoginId = String.valueOf(result.getId());
                    mLoginType = 2;
                    signin();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getHashKey() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    private void getNick(String userID) {
        Log.e("수행","여기");
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    Log.e("suc getnick in login",suc+"");
                    if(suc){
                        String userNick=jsonObject.getString("userNick");
                        Log.e("userNick",userNick);
                        MyApplication my = (MyApplication) getApplication();
                        my.setGlobalString(userNick);
                        Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                } catch (JSONException e) {
                    Log.e("getPInfo in showp","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        Log.e("send",userID);
        getUserNickReq gunr = new getUserNickReq(userID,responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(gunr);
    }
    public void signin() {
        //서버통신 다이얼로그 표시
        showpDialog();
        //서버 통신
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, UrlInfo.LOGIN_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //로그인 서버 결과
                            if (!response.getBoolean("error")) {
                                //로그인 완료시, 메인으로 이동
                                getNick(mLoginId);
                            } else {
                                if(mLoginType == 2) {
                                    //카카오톡 로그인 시도시 , 비회원일경우 , 가입페이지 전달
                                    Intent intent = new Intent(LoginActivity.this , TermActivity.class);
                                    intent.putExtra("SNS" , true);
                                    intent.putExtra("snsId" , mLoginId);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, getText(R.string.not_member), Toast.LENGTH_LONG).show();
                                }

                            }
                        } catch (Throwable t) {
                        }
                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginActivity.this, getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", mLoginId);
                params.put("userpw", mLoginPass);
                params.put("loginType", String.valueOf(mLoginType));

                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(jsonReq);
    }
}
