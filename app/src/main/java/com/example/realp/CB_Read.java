package com.example.realp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.common.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CB_Read extends AppCompatActivity {

    private int cNumber;
    private EditText et_that;
    private Button post;
    private ImageButton fave,back;
    public String userID,cDate,cTitle,cWrite,tag,cView,patent,hPerson,cPlace;
    private TextView t_userID,t_cDate,t_cTitle,t_cWrite,t_tag,t_cView,t_patent,t_hPerson,t_cPlace,bartitle;
    com.example.realp.imageSlideAdapter imageSlideAdapter;
    ArrayList<String> imageUrlList;
    ViewPager viewPager;

    private RecyclerView recyclerView;
    private BB_CommentAdapter BBCommentAdapter;

    ArrayList<BB_CommentData> arrayList;
    BB_CommentData BBCommentData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coboard_read);
       // getSupportActionBar().setTitle("아이디어 협업 글");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        t_userID=findViewById(R.id.cUserId);
        t_cDate=findViewById(R.id.cDate);
        t_cTitle=findViewById(R.id.cReadTitle);
        t_cWrite=findViewById(R.id.cRead_content);
        t_tag=findViewById(R.id.cReadCategory);
        t_cView=findViewById(R.id.cvNum);
        t_patent=findViewById(R.id.cPatent);
        t_hPerson=findViewById(R.id.personReadBoard);
        t_cPlace=findViewById(R.id.cPlace);
        et_that=findViewById(R.id.et_thatC);
        viewPager=(ViewPager)findViewById(R.id.cReadViewPager);
        post=findViewById(R.id.postC);
        fave=findViewById(R.id.cStar);
        back=findViewById(R.id.backFromSet);
        bartitle=findViewById(R.id.barname);
        bartitle.setText("아이디어 협업 글");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication qwe =(MyApplication) getApplication();
                final String userID = qwe.getGlobalString();
                Intent intent =new Intent(CB_Read.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("co",true);
                intent.putExtra("userID",userID);

                startActivity(intent);

            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.rvC);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);

        arrayList = new ArrayList<>();
        BBCommentAdapter = new BB_CommentAdapter(arrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(BBCommentAdapter);

        imageUrlList=new ArrayList<>();

        Intent intent = getIntent(); /*데이터 수신*/
        cNumber = intent.getExtras().getInt("cNumber");

        imageSlideAdapter = new imageSlideAdapter(this,imageUrlList);
        viewPager.setAdapter(imageSlideAdapter);
        favorites();

        fave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MyApplication qwe =(MyApplication) getApplication();
                final String userID = qwe.getGlobalString();
                if (fave.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.star).getConstantState())){
                    fave.setImageResource(R.drawable.yellowstar);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jasonObject = new JSONObject(response);

                                boolean success = jasonObject.getBoolean("success");

                                if (success){

                                    Toast.makeText( getApplicationContext(), "즐겨찾기에 추가 되었습니다.", Toast.LENGTH_SHORT ).show();

                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"즐겨찾기 등록 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }catch (JSONException e){
                                Toast.makeText(getApplicationContext(),"즐겨찾기 등록 실패", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    };

                    CB_FavoritesPlusRequest cb_favoritesPlusRequest = new CB_FavoritesPlusRequest(cNumber,userID,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(CB_Read.this);
                    queue.add(cb_favoritesPlusRequest);

                }else
                {
                    fave.setImageResource(R.drawable.star);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jasonObject = new JSONObject(response);

                                boolean success = jasonObject.getBoolean("success");

                                if (success){

                                    Toast.makeText( getApplicationContext(), "즐겨찾기에서 삭제 되었습니다.", Toast.LENGTH_SHORT ).show();

                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"즐겨찾기 삭제 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }catch (JSONException e){
                                Toast.makeText(getApplicationContext(),"즐겨찾기 삭제 실패", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    };

                    CB_FavoritesDeleteRequest cb_favoritesdeleteRequest = new CB_FavoritesDeleteRequest(cNumber,userID,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(CB_Read.this);
                    queue.add(cb_favoritesdeleteRequest);

                }
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication qwe =(MyApplication) getApplication();
                final String userID = qwe.getGlobalString();
                final String ceMemo = et_that.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jasonObject = new JSONObject(response);

                            boolean success = jasonObject.getBoolean("success");

                            if (success){

                                Toast.makeText( getApplicationContext(), "댓글을 입력하였습니다", Toast.LENGTH_SHORT ).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"댓글 등록 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(),"댓글 등록 실패", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                };

                CB_CommentWriteRequest CBCommentWriteRequest = new CB_CommentWriteRequest(cNumber,userID,ceMemo,responseListener);
                RequestQueue queue = Volley.newRequestQueue(CB_Read.this);
                queue.add(CBCommentWriteRequest);


            }
        });

        commentList();
        bViewPlus();
        imageLIst();
        readBoard();

    }





    public void favorites(){

        MyApplication qwe =(MyApplication) getApplication();
        final String userID = qwe.getGlobalString();
        Response.Listener<String> responseListener=new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jasonObject=new JSONObject(response);
                    boolean success=jasonObject.getBoolean("success");

                    if (success) {
                        fave.setImageResource(R.drawable.yellowstar);
                        int sNum = jasonObject.getInt("sNum");
                    }
                    else
                        fave.setImageResource(R.drawable.star);

                } catch (JSONException e) {
                    Toast.makeText( getApplicationContext(), "즐찾 실패", Toast.LENGTH_SHORT ).show();
                    e.printStackTrace();
                }
            }
        };
        CB_FavoritesSelectRequest cb_favoritesSelectRequest=new CB_FavoritesSelectRequest(cNumber,userID,responseListener);
        RequestQueue queue= Volley.newRequestQueue(CB_Read.this);
        queue.add(cb_favoritesSelectRequest);

    }
    public void commentList(){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("Board");

                    Log.e("ja.length",""+ja.length());

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);

                        String ceWrite = item.getString("ceWrite");
                        String ceMemo = item.getString("ceMemo");
                        String ceDate = item.getString("ceDate");


                        if (ceWrite.equals(userID)){
                            ceWrite=""+ceWrite+" (작성자)";
                        }
                        BBCommentData = new BB_CommentData(ceWrite,ceMemo,ceDate);

                        arrayList.add(BBCommentData);

                        BBCommentAdapter.notifyDataSetChanged(); //추가후새로고침
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        CB_CommentListRequest lpr = new CB_CommentListRequest(cNumber,responseListener);
        RequestQueue queue = Volley.newRequestQueue(CB_Read.this);
        queue.add( lpr );
    }

    public void bViewPlus(){

        Response.Listener<String> responseListener=new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jasonObject=new JSONObject(response);
                    boolean success=jasonObject.getBoolean("success");


                } catch (JSONException e) {
                    Toast.makeText( getApplicationContext(), "조회수 증가 실패", Toast.LENGTH_SHORT ).show();
                    e.printStackTrace();
                }
            }
        };
        cViewPlusRequest cViewPlusRequest=new cViewPlusRequest(cNumber,responseListener);
        RequestQueue queue= Volley.newRequestQueue(CB_Read.this);
        queue.add(cViewPlusRequest);

    }

    public void imageLIst(){

        Response.Listener<String> responseListener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("imageUrlList");
                    Log.e("ja.length",""+ja.length());

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject item = ja.getJSONObject(i);
                        String imageUrl = item.getString("imageUrl");


                        imageUrlList.add(imageUrl);
                        imageSlideAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    Toast.makeText( getApplicationContext(), "등록된 사진 없음", Toast.LENGTH_SHORT ).show();
                    e.printStackTrace();
                }
            }
        };
        CB_ImageUrlListRequest cimageUrlListRequest=new CB_ImageUrlListRequest(cNumber,responseListener);
        RequestQueue queue= Volley.newRequestQueue(CB_Read.this);
        queue.add(cimageUrlListRequest);
    }

    public void readBoard(){


        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jasonObject=new JSONObject(response);
                    boolean success=jasonObject.getBoolean("success");

                    if (success) {

                        userID=jasonObject.getString("userID");
                        cDate = jasonObject.getString("cDate");
                        cTitle = jasonObject.getString("cTitle");
                        cWrite = jasonObject.getString("cWrite");
                        tag = jasonObject.getString("cTag");
                        cView = jasonObject.getString("cView");
                        patent = jasonObject.getString("cPatent");
                        hPerson=jasonObject.getString("hPerson");
                        cPlace=jasonObject.getString("cPlace");



                        t_userID.setText(userID);
                        t_cDate.setText(cDate.substring(2,16));
                        t_cTitle.setText(cTitle);
                        t_cWrite.setText(cWrite);
                        t_tag.setText(tag);
                        t_cView.setText("조회수 "+cView);
                        t_patent.setText("특허등록"+patent);
                        t_hPerson.setText(hPerson);
                        t_cPlace.setText(cPlace);


                    }
                    else{
                        Toast.makeText(getApplicationContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
                        return;

                    }
                } catch (JSONException e) {
                    Toast.makeText( getApplicationContext(), "불러오기 실패2", Toast.LENGTH_SHORT ).show();
                    e.printStackTrace();
                }
            }
        };
        CB_ReadRequest CB_readRequest =new CB_ReadRequest(cNumber,responseListener);
        RequestQueue queue= Volley.newRequestQueue(CB_Read.this);
        queue.add(CB_readRequest);
    }

}
