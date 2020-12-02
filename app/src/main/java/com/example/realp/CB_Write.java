package com.example.realp;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.common.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class CB_Write extends AppCompatActivity {



    private ImageButton pictureAdd,fileAdd;
    private EditText cTitle,hPerson,cPlace,cWrite;
    private TextView bartitle;
    private RadioGroup radioGroup;
    private Button finish,save,cTag;
    private ImageButton back;
    private String patent;

    String imageName;
    String ho;
    String ct="미정";
    ListView list;
    ArrayList<Bitmap> bar = new ArrayList<Bitmap>();
    ArrayList<String> ar=new ArrayList<String>();
    ArrayList<String> imageNameList=new ArrayList<String>();
    ArrayList<String> imageData = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    public int cNumber,cNumber2;

    int sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coboard_write);
       // getSupportActionBar().setTitle("아이디어 협업 글쓰기");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        list =(ListView)findViewById(R.id.imList2);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ar);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,View view,int i, long l){
                View dv =(View)View.inflate(CB_Write.this, R.layout.dialog_imagelist,null);
                AlertDialog.Builder dlg=new AlertDialog.Builder(CB_Write.this);
                ImageView iv=(ImageView)dv.findViewById(R.id.iv);
                iv.setImageBitmap(bar.get(i));
                dlg.setTitle("이미지");
                dlg.setView(dv);
                dlg.setNegativeButton("닫기",null);
                dlg.show();
            }
        });

        bartitle=(TextView)findViewById(R.id.barname2);
        bartitle.setText("아이디어 협업 글쓰기");
        cTag=(Button)findViewById(R.id.category2);

        cTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);

                MenuInflater inflater = popupMenu.getMenuInflater();
                Menu menu = popupMenu.getMenu();
                inflater.inflate(R.menu.coboardcategory_menu,menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.menu1:
                                ct="음식/외식";
                                cTag.setText("음식/외식 업종");
                                break;
                            case R.id.menu2:
                                ct="제조";
                                cTag.setText("제조 업종");
                                break;
                            case R.id.menu3:
                                ct="판매";
                                cTag.setText("판매 업종");
                                break;
                            case R.id.menu4:
                                ct="서비스";
                                cTag.setText("서비스 업종");
                                break;
                            case R.id.menu5:
                                ct="시설/대여";
                                cTag.setText("시설/대여 업종");
                                break;
                            case R.id.menu6:
                                ct="기타";
                                cTag.setText("기타 업종");
                                break;

                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        cTitle=findViewById(R.id.title2);
        hPerson=findViewById(R.id.people);
        cPlace=findViewById(R.id.place);
        cWrite=findViewById(R.id.edit2);
        cTag=findViewById(R.id.category2);
        back=findViewById(R.id.backFromSetWrite);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        radioGroup = (RadioGroup) findViewById(R.id.radioGroup2);
        radioGroup.setOnCheckedChangeListener(m);


        pictureAdd=findViewById(R.id.pictureAdd2);
        fileAdd=findViewById(R.id.fileAdd2);
        pictureAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        CB_Write.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        3000
                );
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            imageName=jsonObject.getString("imageName");

                            boolean success = jsonObject.getBoolean( "success" );
                            if(success) {
                                Toast.makeText( getApplicationContext(), "getImageName", Toast.LENGTH_SHORT ).show();
                                Log.e("getimageName",imageName);
                                String tmp="";
                                for(int i=4;i<imageName.length();i++) {
                                    tmp=tmp+(imageName.charAt(i));
                                }
                                sum=Integer.parseInt(tmp);

                                Log.e("imageName2",imageName);
                            } else {
                                Toast.makeText( getApplicationContext(), "can't getImageName", Toast.LENGTH_SHORT ).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText( getApplicationContext(), "can't getImageName", Toast.LENGTH_SHORT ).show();
                            e.printStackTrace();
                        }

                    }
                };
                Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject( response );

                            cNumber=jsonObject.getInt("cNumber");
                            boolean success = jsonObject.getBoolean( "success" );
                            if(success) {
                                Toast.makeText( getApplicationContext(), cNumber+"getImageName", Toast.LENGTH_SHORT ).show();

                                cNumber2=cNumber+1;

                            } else {
                                Toast.makeText( getApplicationContext(), "", Toast.LENGTH_SHORT ).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText( getApplicationContext(), "", Toast.LENGTH_SHORT ).show();
                            e.printStackTrace();
                        }
                    }
                };
                cNumberRequest z = new cNumberRequest(responseListener2);
                CB_GetImageRequest g = new CB_GetImageRequest(responseListener);
                RequestQueue q = Volley.newRequestQueue(CB_Write.this);
                RequestQueue qw = Volley.newRequestQueue(CB_Write.this);
                q.add(g);
                qw.add(z);
            }
        });
        fileAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        CB_Write.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        4000
                );//퍼미션권한 허용 묻기

            }
        });


        finish = findViewById(R.id.finish2);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                MyApplication qwe =(MyApplication) getApplication();
                final String CTitle =cTitle.getText().toString();
                final String CPlace = cPlace.getText().toString();
                final String userID = qwe.getGlobalString();
                final  String HPerson=hPerson.getText().toString();
                final  String CWrite = cWrite.getText().toString();
                final String CTag=ct;
                final String CPatent=patent;

                for (int i =0;i < bar.size();i++) {
                    ho = BitMapToString(bar.get(i));
                    imageData.add(ho);
                }
                for (int i=0;i<bar.size();i++){
                    sum=sum+1;
                    imageName = "img_"+String.valueOf(sum);
                    imageNameList.add(imageName);
                }


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jasonObject = new JSONObject(response);

                            boolean success = jasonObject.getBoolean("success");

                            if (success){
                                int bn=jasonObject.getInt("cNumber");
                                Log.e("getbn",bn+"");
                                if(bar.size()>=1){
                                    for (int i=0;i<bar.size();i++) {
                                        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jasonObject = new JSONObject(response);
                                                    boolean success = jasonObject.getBoolean("success");
                                                    Log.e("sucin image",success+"");
                                                    if(success){
                                                        Log.e("포문","수행끝");
                                                        MyApplication qwe =(MyApplication) getApplication();
                                                        final String userID = qwe.getGlobalString();
                                                        Intent intent =new Intent(CB_Write.this, MainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.putExtra("co",true);
                                                        intent.putExtra("userID",userID);
                                                        Toast.makeText(getApplicationContext(), "게시글 등록 성공", Toast.LENGTH_SHORT).show();
                                                        startActivity(intent);
                                                    }

                                                }catch (JSONException e){
                                                    Toast.makeText(getApplicationContext(),"이미지 등록 실패2", Toast.LENGTH_SHORT).show();
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        CB_ImageUploadRequest r = new CB_ImageUploadRequest(bn, imageNameList.get(i), imageData.get(i), responseListener2);
                                        RequestQueue queue = Volley.newRequestQueue(CB_Write.this);
                                        queue.add(r);
                                    }
                                }else{
                                    MyApplication qwe =(MyApplication) getApplication();
                                    final String userID = qwe.getGlobalString();
                                    Intent intent =new Intent(CB_Write.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("co",true);
                                    intent.putExtra("userID",userID);
                                    Toast.makeText(getApplicationContext(), "게시글 등록 성공", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"게시글 등록 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(),"게시글 등록 실패2", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                };

               CB_WriteRequest CBWriteRequest = new CB_WriteRequest(CTitle,userID,CWrite,CTag,CPatent,CPlace,HPerson,responseListener);
                RequestQueue queue = Volley.newRequestQueue(CB_Write.this);
                queue.add(CBWriteRequest);


            }
        });
    }


    private void addImageName(String name){
        ar.add(name);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //갤러리열기
        if(requestCode==3000){
            if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent i =new Intent(Intent.ACTION_PICK);
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                i.setType("image/*");
                startActivityForResult(i,3000);
            }else{
                Toast.makeText(this, "퍼미션 허용 클릭", Toast.LENGTH_SHORT).show();
            }
            return;
        } else if (requestCode==4000){
            if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent i =new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                startActivityForResult(i,4000);
            }else{
                Toast.makeText(this, "퍼미션 허용 클릭", Toast.LENGTH_SHORT).show();
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==3000&&resultCode==RESULT_OK&&data!=null){
            Uri path=data.getData();
            ClipData clipData = data.getClipData();
            //사용자가 선택한 파일의 이름/사이즈/경로 등을 얻을때 사용
            if (clipData!=null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri urion = clipData.getItemAt(i).getUri();
                    String[] proj = {MediaStore.Images.Media.DISPLAY_NAME}; //TITLE=파일이름만,DISPLAY_NAME=확장자까지
                    Cursor cursor = managedQuery(urion, proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                    cursor.moveToFirst();
                    String imgPath = cursor.getString(column_index);
                    //--
                    addImageName(imgPath);
                    try {

                        InputStream inputStream = getContentResolver().openInputStream(urion);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bar.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }else if (path != null){
                String[] proj = { MediaStore.Images.Media.DISPLAY_NAME}; //TITLE=파일이름만,DISPLAY_NAME=확장자까지
                Cursor cursor = managedQuery(path, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                cursor.moveToFirst();
                String imgPath = cursor.getString(column_index);
                //--
                addImageName(imgPath);
                try{
                    InputStream inputStream = getContentResolver().openInputStream(path);
                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                    bar.add(bitmap);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }

            }
        }else if(requestCode==4000&&resultCode==RESULT_OK&&data!=null){
            Uri path=data.getData();
            //사용자가 선택한 파일의 이름/사이즈/경로 등을 얻을때 사용
            String[] proj = { MediaStore.Images.Media.DISPLAY_NAME}; //TITLE=파일이름만,DISPLAY_NAME=확장자까지
            Cursor cursor = managedQuery(path, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            cursor.moveToFirst();
            String imgPath = cursor.getString(column_index);
            //--
            Log.e("imgpath","="+imgPath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    RadioGroup.OnCheckedChangeListener m = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (i == R.id.cRg_btn1) {
                patent = "유";
            } else if (i == R.id.cRg_btn2) {
                patent = "무";
            }
        }
    };

    public String BitMapToString(Bitmap bitmap){

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);

        return temp;

    }
}
