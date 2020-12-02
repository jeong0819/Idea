package com.example.realp;

import android.content.Intent;
import android.media.Image;
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

public class BB_Read extends AppCompatActivity {

   private int bNumber,bView;
   private EditText et_that;
   private Button post;
   private ImageButton fave,back;
   public String userID,bDate,bTitle,bWrite,tag,patent,price;
   private TextView t_userID,t_bDate,t_bTitle,t_bWrite,t_tag,t_bView,t_patent,t_price,titleBar;
   com.example.realp.imageSlideAdapter imageSlideAdapter;
   ArrayList<String> imageUrlList;
   ViewPager viewPager;

    private RecyclerView recyclerView;
    private BB_CommentAdapter BBCommentAdapter;

    ArrayList<BB_CommentData> arrayList;
    BB_CommentData BBCommentData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyboard_read);
       // getSupportActionBar().setTitle("아이디어 판매 글");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        t_userID=findViewById(R.id.userId);
        t_bDate=findViewById(R.id.bDate);
        t_bTitle=findViewById(R.id.ideaTitle);
        t_bWrite=findViewById(R.id.read_content);
        t_tag=findViewById(R.id.read_category);
        t_bView=findViewById(R.id.vNum);
        t_patent=findViewById(R.id.patent);
        t_price=findViewById(R.id.priceReadBoard);
        et_that=findViewById(R.id.et_that);
        post=findViewById(R.id.post);
        fave=findViewById(R.id.fav);
        titleBar=findViewById(R.id.barname);
        titleBar.setText("아이디어 판매 글");
        viewPager=(ViewPager)findViewById(R.id.ReadViewPager);
        back=(ImageButton)findViewById(R.id.backFromSet);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication qwe =(MyApplication) getApplication();
                final String userID = qwe.getGlobalString();
                Intent intent =new Intent(BB_Read.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("buy",true);
                intent.putExtra("userID",userID);
                startActivity(intent);

            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.rvComment);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);

        arrayList = new ArrayList<>();
        BBCommentAdapter = new BB_CommentAdapter(arrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(BBCommentAdapter);

        imageUrlList=new ArrayList<>();

        Intent intent = getIntent(); /*데이터 수신*/
        bNumber = intent.getExtras().getInt("bNumber");

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

                    BB_FavoritesPlusRequest bb_favoritesPlusRequest = new BB_FavoritesPlusRequest(bNumber,userID,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(BB_Read.this);
                    queue.add(bb_favoritesPlusRequest);

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

                    BB_FavoritesDeleteRequest bb_favoritesdeleteRequest = new BB_FavoritesDeleteRequest(bNumber,userID,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(BB_Read.this);
                    queue.add(bb_favoritesdeleteRequest);

                }
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication qwe =(MyApplication) getApplication();
                final String userID = qwe.getGlobalString();
                final String reMemo = et_that.getText().toString();
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

                BB_CommentWriteRequest BBCommentWriteRequest = new BB_CommentWriteRequest(bNumber,userID,reMemo,responseListener);
                RequestQueue queue = Volley.newRequestQueue(BB_Read.this);
                queue.add(BBCommentWriteRequest);


            }
        });



        bViewPlus();
        imageLIst();
        readBoard();
        commentList();

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
                        int bsNum = jasonObject.getInt("bsNum");
                    }
                    else
                        fave.setImageResource(R.drawable.star);

                } catch (JSONException e) {
                    Toast.makeText( getApplicationContext(), "즐찾 실패", Toast.LENGTH_SHORT ).show();
                    e.printStackTrace();
                }
            }
        };
        BB_FavoritesSelectRequest bb_favoritesSelectRequest=new BB_FavoritesSelectRequest(bNumber,userID,responseListener);
        RequestQueue queue= Volley.newRequestQueue(BB_Read.this);
        queue.add(bb_favoritesSelectRequest);

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
                        String reWrite = item.getString("reWrite");
                        String reMemo = item.getString("reMemo");
                        String reDate = item.getString("reDate");

                        if (reWrite.equals(userID)){
                            reWrite=" "+reWrite+" (작성자)";
                        }

                        BBCommentData = new BB_CommentData(reWrite,reMemo,reDate);

                        arrayList.add(BBCommentData);

                        BBCommentAdapter.notifyDataSetChanged(); //추가후새로고침
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        BB_CommentListRequest lpr = new BB_CommentListRequest(bNumber,responseListener);
        RequestQueue queue = Volley.newRequestQueue(BB_Read.this);
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
    bViewPlusRequest bViewPlusRequest=new bViewPlusRequest(bNumber,responseListener);
    RequestQueue queue= Volley.newRequestQueue(BB_Read.this);
    queue.add(bViewPlusRequest);

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
        BB_ImageUrlListRequest BBImageUrlListRequest =new BB_ImageUrlListRequest(bNumber,responseListener);
        RequestQueue queue= Volley.newRequestQueue(BB_Read.this);
        queue.add(BBImageUrlListRequest);
    }


    public void readBoard(){


        Response.Listener<String> responseListener=new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jasonObject=new JSONObject(response);
                    boolean success=jasonObject.getBoolean("success");

                    if (success) {

                        userID=jasonObject.getString("userID");
                        bDate = jasonObject.getString("bDate");
                        bTitle = jasonObject.getString("bTitle");
                        bWrite = jasonObject.getString("bWrite");
                        tag = jasonObject.getString("tag");
                        bView = jasonObject.getInt("bView");
                        patent = jasonObject.getString("patent");
                        price=jasonObject.getString("price");
                        t_userID.setText(userID);
                        t_bDate.setText(bDate.substring(2,16));
                        t_bTitle.setText(bTitle);
                        t_bWrite.setText(bWrite);
                        t_tag.setText("카테고리 "+tag);
                        t_bView.setText("조회수 "+bView);
                        t_patent.setText("특허등록"+patent);

                        t_price.setText(price);

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
        BB_ReadRequest BB_readRequest =new BB_ReadRequest(bNumber,responseListener);
        RequestQueue queue= Volley.newRequestQueue(BB_Read.this);
        queue.add(BB_readRequest);
    }



}
