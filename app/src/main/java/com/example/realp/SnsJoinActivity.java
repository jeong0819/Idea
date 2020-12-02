package com.example.realp;

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

public class SnsJoinActivity extends ActivityBase implements View.OnClickListener, TextWatcher {

    //id값 정의
    private Toolbar mToolbar = null;
    private EditText nick_edit = null;
    private EditText comment_edit = null;
    private EditText email_edit = null;

    private Button nick_check_btn = null;
    private Button ok_btn = null;

    private Spinner work_spinner = null;

    private ImageView nick_check_img = null;
    private ImageView comment_check_img = null;
    private ImageView photo_img = null;
    private ImageView plus_img = null;
    private ImageView email_check_img = null;
    private LinearLayout email_linear = null;
    private TextView comment_size_text = null;

    private RelativeLayout photo_relative = null;

    private ArrayAdapter mArrayAdapter = null;
    private String work = "공무원"; //초기 직업공무원으로 설정

    private Boolean mNickCheckResult = false;

    private String uploadImgName= "";
    private String selectedPhotoImg = "";

    public static final int SELECT_PHOTO_IMG = 20;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_PHOTO = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns_join);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBarCustomView actionBarCustomView = new ActionBarCustomView(this, this);
        actionBarCustomView.setTitle("회원가입");

        //view id찾기
        nick_edit = findViewById(R.id.nick_edit);
        comment_edit = findViewById(R.id.comment_edit);
        nick_check_btn = findViewById(R.id.nick_check_btn);
        ok_btn = findViewById(R.id.ok_btn);
        work_spinner = findViewById(R.id.work_spinner);
        comment_check_img = findViewById(R.id.comment_check_img);
        comment_size_text = findViewById(R.id.comment_size_text);
        email_check_img = findViewById(R.id.email_check_img);
        nick_check_img = findViewById(R.id.nick_check_img);
        photo_relative = findViewById(R.id.photo_relative);
        photo_img = findViewById(R.id.photo_img);
        plus_img = findViewById(R.id.plus_img);
        email_edit = findViewById(R.id.email_edit);
        email_linear = findViewById(R.id.email_linear);


        //직업 스피너 연결
        final String[] items = getResources().getStringArray(R.array.my_array);
        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        work_spinner.setAdapter(mArrayAdapter);
        work_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                work = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        nick_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    nick_edit.setBackgroundResource(R.drawable.m_border_box5);
                } else {
                    nick_edit.setBackgroundResource(R.drawable.m_border_box4);
                }
            }
        });

        comment_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    comment_edit.setBackgroundResource(R.drawable.m_border_box5);
                } else {
                    comment_edit.setBackgroundResource(R.drawable.m_border_box4);
                }
            }
        });

        email_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    email_edit.setBackgroundResource(R.drawable.m_border_box5);
                } else {
                    email_edit.setBackgroundResource(R.drawable.m_border_box4);
                }
            }
        });

        comment_edit.addTextChangedListener(this);
        nick_edit.addTextChangedListener(this);
        email_edit.addTextChangedListener(this);


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
            case R.id.nick_check_btn: {
                if (nick_edit.getText().toString().length() > 0) {
                    nickCheck();
                } else {
                    Toast.makeText(SnsJoinActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.ok_btn: {


                if (!mNickCheckResult) {
                    Toast.makeText(this, "닉네임 중복 체크를 해주세요.", Toast.LENGTH_SHORT).show();

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

    //닉네임 중복확인
    public void nickCheck() {

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, UrlInfo.NICK_CHECK_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //회원가입 서버 결과
                            if (!response.getBoolean("error")) {
                                //사용가능 체크
                                mNickCheckResult = true;
                                nick_check_img.setImageResource(R.drawable.check_tes);
                            } else {
                                mNickCheckResult = false;
                                Toast.makeText(SnsJoinActivity.this, getText(R.string.nick_not), Toast.LENGTH_LONG).show();
                            }
                        } catch (Throwable t) {
                        }
                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mNickCheckResult = false;
                Toast.makeText(SnsJoinActivity.this, getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.hashCode() == comment_edit.getText().hashCode()) {
            comment_size_text.setText(comment_edit.getText().toString().length() + " / 300");

            if (comment_edit.getText().toString().length() > 0) {
                comment_check_img.setImageResource(R.drawable.check_tes);
            } else {
                comment_check_img.setImageResource(R.drawable.check_no);
            }
        } else if (charSequence.hashCode() == nick_edit.getText().hashCode()) {
            mNickCheckResult = false;
            nick_check_img.setImageResource(R.drawable.check_no);
        } else if(charSequence.hashCode() == email_edit.getText().hashCode()) {
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
        if (mNickCheckResult && comment_edit.getText().toString().length() > 0) {
            ok_btn.setBackgroundResource(R.drawable.m_border_box6);
            ok_btn.setTextColor(Color.parseColor("#ffffff"));

        } else {
            ok_btn.setBackgroundResource(R.drawable.m_border_box3);
            ok_btn.setTextColor(Color.parseColor("#ffb400"));
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
                        Intent intent = new Intent(SnsJoinActivity.this , MainActivity.class);
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
    //이미지 있을경우 회원가입
    public Boolean signUp(File file) {

        final OkHttpClient client = new OkHttpClient();
        client.setProtocols(Arrays.asList(Protocol.HTTP_1_1));
        try {

            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("uploaded_file", file.getName(), RequestBody.create(MediaType.parse("text/csv"), file))
                    .addFormDataPart("userid", getIntent().getStringExtra("snsId"))
                    .addFormDataPart("userJob", work)
                    .addFormDataPart("userSelf", comment_edit.getText().toString())
                    .addFormDataPart("userEmail", email_edit.getText().toString())
                    .addFormDataPart("nickname", nick_edit.getText().toString())
                    .addFormDataPart("loginType", String.valueOf(2))
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
                            Intent intent = new Intent(SnsJoinActivity.this, MainActivity.class);
                            startActivity(intent);

                            finish();
                        } else {
                            Handler mHandler = new Handler(Looper.getMainLooper());
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SnsJoinActivity.this, getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();
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
                                Intent intent = new Intent(SnsJoinActivity.this, MainActivity.class);
                                startActivity(intent);

                                finish();
                            } else {
                                Toast.makeText(SnsJoinActivity.this, getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();
                            }
                        } catch (Throwable t) {
                        }
                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SnsJoinActivity.this, getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", getIntent().getStringExtra("snsId"));
                params.put("userJob", work);
                params.put("userSelf", comment_edit.getText().toString());
                params.put("nickname", nick_edit.getText().toString());
                params.put("userEmail", email_edit.getText().toString());
                params.put("loginType", "2");

                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(jsonReq);
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

    //이미지 경로 셋팅
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getImageUrlWithAuthority(Uri uri, String fileName) {

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
}