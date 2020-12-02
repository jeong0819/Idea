package com.example.realp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.example.realp.common.ActivityBase;
import com.example.realp.common.MyApplication;
import com.example.realp.common.UrlInfo;
import com.example.realp.common.util.CustomRequest;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//베이스 activity 설정
public class JoinActivity extends ActivityBase implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    //view id값 정의
    private Toolbar mToolbar = null;
    private EditText id_edit = null;
    private EditText pass_edit = null;
    private EditText pass_ok_edit = null;
    private EditText nick_edit = null;
    private EditText phone_edit = null;
    private EditText name_edit = null;
    private EditText email_edit = null;
    private EditText comment_edit = null;

    private ImageView id_check_img = null;
    private ImageView pass_check_img = null;
    private ImageView pass_ok_check_img = null;
    private ImageView name_check_img = null;
    private ImageView photo_img = null;
    private ImageView plus_img = null;
    private ImageView comment_check_img = null;
    private ImageView nick_check_img = null;
    private ImageView email_check_img = null;

    private RadioGroup radioGroup = null;

    private TextView comment_size_text = null;
    private TextView pass_result_text = null;
    private RelativeLayout photo_relative = null;

    private Spinner work_spinner = null;
    private Spinner year_spinner = null;
    private Spinner month_spinner = null;
    private Spinner day_spinner = null;

    private Button id_check_btn = null;
    private Button nick_check_btn = null;
    private Button ok_btn = null;

    private LinearLayout email_linear = null;

    private ArrayAdapter mArrayWorkAdapter = null;
    private ArrayAdapter mArrayYearAdapter = null;
    private ArrayAdapter mArrayMonthAdapter = null;
    private ArrayAdapter mArrayDayAdapter = null;
    private String work = "공무원"; //초기 직업공무원으로 설정

    private Boolean mIdCheckResult = false;
    private Boolean mNickCheckResult = false;

    //성별 남자로 초기화 1이면 남자 2이면 여자
    private int mSex = 1;

    //연도 초기화
    private String mYear="2010";
    private String mMonth="1";
    private String mDay="1";

    private String uploadImgName= "";
    private String selectedPhotoImg = "";

    public static final int SELECT_PHOTO_IMG = 20;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBarCustomView actionBarCustomView = new ActionBarCustomView(this, this);
        actionBarCustomView.setTitle("회원가입");

        //view id 찾기
        id_check_btn = findViewById(R.id.id_check_btn);
        nick_check_btn = findViewById(R.id.nick_check_btn);
        ok_btn = findViewById(R.id.ok_btn);

        id_edit = findViewById(R.id.id_edit);
        pass_edit = findViewById(R.id.pass_edit);
        pass_ok_edit = findViewById(R.id.pass_ok_edit);
        nick_edit = findViewById(R.id.nick_edit);
        phone_edit = findViewById(R.id.phone_edit);
        name_edit = findViewById(R.id.name_edit);
        email_edit = findViewById(R.id.email_edit);
        comment_edit = findViewById(R.id.comment_edit);

        id_check_img = findViewById(R.id.id_check_img);
        pass_check_img = findViewById(R.id.pass_check_img);
        pass_ok_check_img = findViewById(R.id.pass_ok_check_img);
        name_check_img = findViewById(R.id.name_check_img);
        photo_img = findViewById(R.id.photo_img);
        plus_img = findViewById(R.id.plus_img);
        comment_check_img = findViewById(R.id.comment_check_img);
        nick_check_img = findViewById(R.id.nick_check_img);
        email_check_img = findViewById(R.id.email_check_img);

        pass_result_text = findViewById(R.id.pass_result_text);
        comment_size_text = findViewById(R.id.comment_size_text);
        photo_relative = findViewById(R.id.photo_relative);

        radioGroup = findViewById(R.id.radioGroup);

        work_spinner = findViewById(R.id.work_spinner);
        year_spinner = findViewById(R.id.year_spinner);
        month_spinner = findViewById(R.id.month_spinner);
        day_spinner = findViewById(R.id.day_spinner);

        email_linear = findViewById(R.id.email_linear);

        //직업 스피너 연결
        final String[] items = getResources().getStringArray(R.array.my_array);
        mArrayWorkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        work_spinner.setAdapter(mArrayWorkAdapter);
        work_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                work = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //년도 스피너
        mArrayYearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);

        for (int i = 1950; i <= 2010; i++) {
            mArrayYearAdapter.add(Integer.toString(i));
        }
        year_spinner.setAdapter(mArrayYearAdapter);
        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mYear =(String)year_spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //월 스피너
        mArrayMonthAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);

        for (int i = 1; i <= 12; i++) {
            mArrayMonthAdapter.add(Integer.toString(i));
        }
        month_spinner.setAdapter(mArrayMonthAdapter);
        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMonth = (String)month_spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //일 스피너
        mArrayDayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);

        for (int i = 1; i <= 31; i++) {
            mArrayDayAdapter.add(Integer.toString(i));
        }
        day_spinner.setAdapter(mArrayDayAdapter);
        day_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDay = (String)day_spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //라디오 그룹 클릭 리스너
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.mail_radio) {
                    mSex = 1;
                } else if (checkedId == R.id.femail_radio) {
                    mSex = 2;
                }
            }
        });

        //입력값변화 리스너
        comment_edit.addTextChangedListener(this);
        pass_edit.addTextChangedListener(this);
        pass_ok_edit.addTextChangedListener(this);
        name_edit.addTextChangedListener(this);
        id_edit.addTextChangedListener(this);
        nick_edit.addTextChangedListener(this);
        email_edit.addTextChangedListener(this);

        //포커스 리스너
        id_edit.setOnFocusChangeListener(this);
        pass_edit.setOnFocusChangeListener(this);
        pass_ok_edit.setOnFocusChangeListener(this);
        nick_edit.setOnFocusChangeListener(this);
        phone_edit.setOnFocusChangeListener(this);
        name_edit.setOnFocusChangeListener(this);
        email_edit.setOnFocusChangeListener(this);
        comment_edit.setOnFocusChangeListener(this);

        //클릭 리스너 찾기
        id_check_btn.setOnClickListener(this);
        nick_check_btn.setOnClickListener(this);
        ok_btn.setOnClickListener(this);
        photo_relative.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_img: {
                finish();
                break;
            }
            case R.id.id_check_btn: {
                if(id_edit.getText().toString().length() > 0) {
                    idCheck();
                }else {
                    Toast.makeText(JoinActivity.this  , "아이디를 입력해주세요." , Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.nick_check_btn: {
                if(nick_edit.getText().toString().length() > 0) {
                    nickCheck();
                }else {
                    Toast.makeText(JoinActivity.this  , "닉네임을 입력해주세요." , Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.ok_btn: {

                if (!mIdCheckResult) {
                    Toast.makeText(this, "아이디 중복 체크를 해주세요.", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(pass_edit.getText().toString().length() < 6 || pass_ok_edit.getText().toString().length() < 6) {
                    Toast.makeText(this, "비밀번호는 6자리로 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pass_edit.getText().toString().equals(pass_ok_edit.getText().toString())) {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!mNickCheckResult) {
                    Toast.makeText(this, "닉네임 중복 체크를 해주세요.", Toast.LENGTH_SHORT).show();

                    return;
                }

                if(name_edit.getText().toString().length() < 2) {

                    Toast.makeText(this, "이름은 2자리이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isValidEmail(email_edit.getText().toString())) {
                    email_linear.setVisibility(View.VISIBLE);
                    return;
                }

                if(comment_edit.getText().toString().length()  <= 0) {

                    Toast.makeText(this, "자기소개를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!selectedPhotoImg.isEmpty()) {

                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + "idea_market", uploadImgName);
                    signUp(f);
                } else {
                    signin();
                }

                break;
            }
            case R.id.photo_relative: {

                //이미지 선택
                selectPhoto();
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //이미지 결과처리
        if (requestCode == SELECT_PHOTO_IMG && resultCode == RESULT_OK && null != data) {

            uploadImgName = System.currentTimeMillis()+"photo.jpg";
            selectedPhotoImg = getImageUrlWithAuthority( data.getData(), uploadImgName);

            try {
                save(selectedPhotoImg,  uploadImgName);
                selectedPhotoImg = Environment.getExternalStorageDirectory() + File.separator + "idea_market" + File.separator + uploadImgName;
                plus_img.setVisibility(View.GONE);
                photo_img.setImageURI(null);
                photo_img.setImageURI(FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", new File(selectedPhotoImg)));

            } catch (Exception e) {
                photo_img.setImageURI(null);
                selectedPhotoImg = "";
                plus_img.setVisibility(View.VISIBLE);
            }
        }
    }

    //포커스 변환에 따른 배경전환
    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.id_edit: {
                if (b) {
                    id_edit.setBackgroundResource(R.drawable.m_border_box5);
                } else {
                    id_edit.setBackgroundResource(R.drawable.m_border_box4);
                }
                break;
            }
            case R.id.pass_edit: {
                if (b) {
                    pass_edit.setBackgroundResource(R.drawable.m_border_box5);
                } else {
                    pass_edit.setBackgroundResource(R.drawable.m_border_box4);
                }
                break;
            }
            case R.id.pass_ok_edit: {
                if (b) {
                    pass_ok_edit.setBackgroundResource(R.drawable.m_border_box5);
                } else {
                    pass_ok_edit.setBackgroundResource(R.drawable.m_border_box4);
                }
                break;
            }
            case R.id.nick_edit: {
                if (b) {
                    nick_edit.setBackgroundResource(R.drawable.m_border_box5);
                } else {
                    nick_edit.setBackgroundResource(R.drawable.m_border_box4);
                }
                break;
            }
            case R.id.comment_edit: {
                if (b) {
                    comment_edit.setBackgroundResource(R.drawable.m_border_box5);
                } else {
                    comment_edit.setBackgroundResource(R.drawable.m_border_box4);
                }
                break;
            }
            case R.id.phone_edit: {
                if (b) {
                    phone_edit.setBackgroundResource(R.drawable.m_border_box5);
                } else {
                    phone_edit.setBackgroundResource(R.drawable.m_border_box4);
                }
                break;
            }
            case R.id.name_edit: {
                if (b) {
                    name_edit.setBackgroundResource(R.drawable.m_border_box5);
                } else {
                    name_edit.setBackgroundResource(R.drawable.m_border_box4);
                }
                break;
            }
            case R.id.email_edit: {
                if (b) {
                    email_edit.setBackgroundResource(R.drawable.m_border_box5);
                } else {
                    email_edit.setBackgroundResource(R.drawable.m_border_box4);
                }
                break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.hashCode() == comment_edit.getText().hashCode()) {
            comment_size_text.setText(comment_edit.getText().toString().length() + " / 300");

            if (comment_edit.getText().toString().length() > 0) {
                comment_check_img.setImageResource(R.drawable.check_tes);
            } else {
                comment_check_img.setImageResource(R.drawable.check_no);
            }
        } else if (charSequence.hashCode() == pass_edit.getText().hashCode()) {
            if (pass_edit.getText().toString().length() >= 6) {
                pass_check_img.setImageResource(R.drawable.check_tes);
            } else {
                pass_check_img.setImageResource(R.drawable.check_no);
            }

        } else if (charSequence.hashCode() == pass_ok_edit.getText().hashCode()) {
            if (pass_ok_edit.getText().toString().length() >= 6) {
                if (pass_ok_edit.getText().toString().equals(pass_edit.getText().toString())) {
                    pass_ok_check_img.setImageResource(R.drawable.check_tes);
                }
            } else {
                pass_ok_check_img.setImageResource(R.drawable.check_no);
            }
        } else if (charSequence.hashCode() == name_edit.getText().hashCode()) {
            if (name_edit.getText().toString().length() > 0) {
                name_check_img.setImageResource(R.drawable.check_tes);
            } else {
                name_check_img.setImageResource(R.drawable.check_no);
            }

        }else if(charSequence.hashCode() == id_edit.getText().hashCode()) {
            mIdCheckResult = false;
            id_check_img.setImageResource(R.drawable.check_no);
        }else if(charSequence.hashCode() == nick_edit.getText().hashCode()) {
            mNickCheckResult = false;
            nick_check_img.setImageResource(R.drawable.check_no);
        }else if(charSequence.hashCode() == email_edit.getText().hashCode()) {
            if(isValidEmail(email_edit.getText().toString())) {
                email_check_img.setImageResource(R.drawable.check_tes);
                email_linear.setVisibility(View.GONE);
            } else {
                email_check_img.setImageResource(R.drawable.check_no);
            }
        }
        checkSign();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    //필수항목 완료시 완료 버튼 활성화
    private void checkSign() {
        if (mIdCheckResult && mNickCheckResult && name_edit.getText().toString().length() > 0 && comment_edit.getText().toString().length() > 0
                && (pass_edit.getText().toString().length() >= 6 && pass_edit.getText().toString().equals(pass_ok_edit.getText().toString()))) {
            ok_btn.setBackgroundResource(R.drawable.m_border_box6);
            ok_btn.setTextColor(Color.parseColor("#ffffff"));

        } else {
            ok_btn.setBackgroundResource(R.drawable.m_border_box3);
            ok_btn.setTextColor(Color.parseColor("#ffb400"));
        }
    }

    //이미지 있을경우 회원가입
    public Boolean signUp(File file) {

        final OkHttpClient client = new OkHttpClient();
        client.setProtocols(Arrays.asList(Protocol.HTTP_1_1));
        try {

            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("uploaded_file", file.getName(), RequestBody.create(MediaType.parse("text/csv"), file))
                    .addFormDataPart("userid", id_edit.getText().toString())
                    .addFormDataPart("userpw", pass_edit.getText().toString())
                    .addFormDataPart("userName", name_edit.getText().toString())
                    .addFormDataPart("userSex", String.valueOf(mSex))
                    .addFormDataPart("userJob", work)
                    .addFormDataPart("userSelf", comment_edit.getText().toString())
                    .addFormDataPart("userEmail", email_edit.getText().toString())
                    .addFormDataPart("userPnum", phone_edit.getText().toString())
                    .addFormDataPart("birthday", mYear +"-" + mMonth + "-" + mDay)
                    .addFormDataPart("nickname", nick_edit.getText().toString())
                    .addFormDataPart("loginType", String.valueOf(1))
                    .build();
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                    .url(UrlInfo.SIGN_UP_URL)
                    .addHeader("Accept", "application/json;")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(com.squareup.okhttp.Request request, IOException e) {
                    hidepDialog();
                }

                @Override
                public void onResponse(com.squareup.okhttp.Response response) throws IOException {

                    String jsonData = response.body().string();
                    try {
                        JSONObject result = new JSONObject(jsonData);
                        //회원가입 서버 결과
                        if (!result.getBoolean("error")) {
                            //회원가입 완료시, 메인으로 이동
                            Intent intent = new Intent(JoinActivity.this, MainActivity.class);
                            startActivity(intent);

                            finish();
                        } else {
                            Handler mHandler = new Handler(Looper.getMainLooper());
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(JoinActivity.this, getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();
                                }
                            }, 0);
                        }
                    } catch (Throwable t) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + t.getMessage() + "\"");
                    }
                }
            });
            return true;
        } catch (Exception ex) {
            hidepDialog();
        }

        return false;
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
                        Intent intent = new Intent(JoinActivity.this , MainActivity.class);
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
    //이미지 없을시 , 회원가입
    public void signin() {
        //서버통신
        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, UrlInfo.SIGN_UP_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //회원가입 서버 결과
                            if (!response.getBoolean("error")) {
                                //회원가입 완료시, 메인으로 이동
                               getNick(nick_edit.getText().toString());
                            } else {
                                Toast.makeText(JoinActivity.this, getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();
                            }
                        } catch (Throwable t) {
                        }
                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(JoinActivity.this, getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", id_edit.getText().toString());
                params.put("userpw", pass_edit.getText().toString());
                params.put("userName", name_edit.getText().toString());
                params.put("userSex", String.valueOf(mSex));
                params.put("userJob", work);
                params.put("userSelf", comment_edit.getText().toString());
                params.put("userEmail", email_edit.getText().toString());
                params.put("userPnum", phone_edit.getText().toString());
                params.put("birthday", mYear +"-" + mMonth + "-" + mDay);
                params.put("nickname", nick_edit.getText().toString());
                params.put("loginType", "1");

                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(jsonReq);
    }

    //id 중복확인
    public void idCheck() {
        //서버통신
        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, UrlInfo.ID_CHECK_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //ID 중복확인 결과
                            if (!response.getBoolean("error")) {
                                //사용가능
                                mIdCheckResult = true;
                                id_check_img.setImageResource(R.drawable.check_tes);
                            } else {
                                mIdCheckResult = false;
                                Toast.makeText(JoinActivity.this, getText(R.string.id_not), Toast.LENGTH_LONG).show();
                            }
                        } catch (Throwable t) {
                        }
                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mIdCheckResult = false;
                Toast.makeText(JoinActivity.this, getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", id_edit.getText().toString());

                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(jsonReq);
    }

    //닉네임 중복확인
    public void nickCheck() {
        //서버통신
        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, UrlInfo.NICK_CHECK_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //회원가입 서버 결과
                            if (!response.getBoolean("error")) {
                                //사용가능
                                mNickCheckResult = true;
                                nick_check_img.setImageResource(R.drawable.check_tes);
                            } else {
                                mNickCheckResult = false;
                                Toast.makeText(JoinActivity.this, getText(R.string.nick_not), Toast.LENGTH_LONG).show();
                            }
                        } catch (Throwable t) {
                        }
                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mNickCheckResult = false;
                Toast.makeText(JoinActivity.this, getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nick", nick_edit.getText().toString());

                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(jsonReq);
    }

    //이미지 경로 셋팅
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getImageUrlWithAuthority( Uri uri, String fileName) {

        InputStream is = null;

        if (uri.getAuthority() != null) {

            try {
                String imagePath = getRealPathFromURI(uri); // path 경로
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = exifOrientationToDegrees(exifOrientation);

                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                bitmap = rotate(bitmap, exifDegree);


                return writeToTempImageAndGetPathUri( bitmap, fileName).toString();

            } catch (Exception e) {
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public int exifOrientationToDegrees(int exifOrientation)
    {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
        {
            return 90;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
        {
            return 180;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
        {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }


    //앱 폴더 생성 및 이미지 저장
    public static String writeToTempImageAndGetPathUri( Bitmap inImage, String fileName) {

        String file_path = Environment.getExternalStorageDirectory() + File.separator + "idea_market";
        File dir = new File(file_path);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, fileName);

        try {

            FileOutputStream fos = new FileOutputStream(file);
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Environment.getExternalStorageDirectory() + File.separator + "idea_market" + File.separator + fileName;
    }

    public void save(String outFile, String inFile) {

        try {

            Bitmap bmp = resize(outFile);


            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "idea_market", inFile);
            FileOutputStream fOut = new FileOutputStream(file);

            bmp.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
            fOut.flush();
            fOut.close();

        } catch (Exception ex) {

            Log.e("Error", ex.getMessage());
        }
    }


    private Bitmap resize(String path){

        int exifDegree = -111;
        try {

            ExifInterface exif = new ExifInterface(path);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        }catch (Exception e) {

        }

        int maxWidth = 512;
        int maxHeight = 512;

        // create the options
        BitmapFactory.Options opts = new BitmapFactory.Options();

        //just decode the file
        opts.inJustDecodeBounds = true;
        Bitmap bp = BitmapFactory.decodeFile(path, opts);

        //get the original size
        int orignalHeight = opts.outHeight;
        int orignalWidth = opts.outWidth;

        //initialization of the scale
        int resizeScale = 1;

        //get the good scale
        if (orignalWidth > maxWidth || orignalHeight > maxHeight) {

            final int heightRatio = Math.round((float) orignalHeight / (float) maxHeight);
            final int widthRatio = Math.round((float) orignalWidth / (float) maxWidth);
            resizeScale = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        //put the scale instruction (1 -> scale to (1/1); 8-> scale to 1/8)
        opts.inSampleSize = resizeScale;
        opts.inJustDecodeBounds = false;

        //get the futur size of the bitmap
        int bmSize = (orignalWidth / resizeScale) * (orignalHeight / resizeScale) * 4;

        //check if it's possible to store into the vm java the picture
        if (Runtime.getRuntime().freeMemory() > bmSize) {

            //decode the file
            bp = BitmapFactory.decodeFile(path, opts);

        } else {

            return null;
        }

        if(exifDegree != -111) {
            bp = rotate(bp, exifDegree);
        }

        return bp;
    }

    //이메일 형식확인
    public boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    //이미지 사용하기 위한 권한확인
    private void selectPhoto() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO);
            }
        } else {
            choiceImage();
        }
    }

    private void choiceImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "이미지 선택"), SELECT_PHOTO_IMG);
    }


    //요청 권한 확인
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choiceImage();
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {

                    if (!ActivityCompat.shouldShowRequestPermissionRationale(JoinActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //권환 재요청
                        showNoStoragePermissionSnackbar();
                    }
                }
                return;
            }
        }
    }


    public void showNoStoragePermissionSnackbar() {

        Snackbar.make(findViewById(R.id.content), getString(R.string.label_no_storage_permission) , Snackbar.LENGTH_LONG).setAction(getString(R.string.action_settings), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        }).show();
    }
}