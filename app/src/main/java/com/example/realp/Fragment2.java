package com.example.realp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.realp.common.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment2 extends Fragment {
    public ArrayList<CB_ListData> arrayList;
    private ArrayList<CB_ListData2> imageDataList= new ArrayList<>();
    private ArrayList<Integer> fav=new ArrayList<>();

    private RecyclerView recyclerView;
    private CB_ListAdapter CBListAdapter;
    private Button category,btn_new,btn_view;
    private ImageButton write;
    private String ct="전체";
    private TextView battitle;

    CB_ListData CBData;
    CB_ListData2 CBListData2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag2, container, false);
      //  ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        //actionBar.setTitle("아이디어 협업 게시판");
        setHasOptionsMenu(true);

        category=(Button)rootView.findViewById(R.id.categorySearch2);
        btn_new=(Button)rootView.findViewById(R.id.btn_newC);
        btn_view=(Button)rootView.findViewById(R.id.btn_viewC);
        battitle=(TextView)rootView.findViewById(R.id.barnameB);
        battitle.setText("아이디어 협업 게시판");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv2);
        write=(ImageButton)rootView.findViewById(R.id.buy_write);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CB_Write.class);
                startActivity(intent);
            }
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), 1));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());

        arrayList=new ArrayList<>();
        CBListAdapter = new CB_ListAdapter(arrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(CBListAdapter);

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(),v);

                MenuInflater inflater = popupMenu.getMenuInflater();
                Menu menu = popupMenu.getMenu();

                inflater.inflate(R.menu.coboardcategorysearch_menu,menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.menu1:
                                ct="전체";
                                category.setText("전체");
                                listPost();
                                break;
                            case R.id.menu2:
                                ct="음식/외식";
                                category.setText("음식/외식");
                                categorySelect();
                                break;
                            case R.id.menu3:
                                ct="제조";
                                category.setText("제조");
                                categorySelect();
                                break;
                            case R.id.menu4:
                                ct="판매";
                                category.setText("판매");
                                categorySelect();
                                break;
                            case R.id.menu5:
                                ct="서비스";
                                category.setText("서비스");
                                categorySelect();
                                break;
                            case R.id.menu6:
                                ct="시설/대여";
                                category.setText("시설대여");
                                categorySelect();
                                break;
                            case R.id.menu7:
                                ct="기타";
                                category.setText("기타");
                                categorySelect();
                                break;

                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPost();
            }
        });
        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtn_view();
            }
        });



        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePost();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listPost();
    }




    public void favorites(){

        MyApplication qwe =(MyApplication) getActivity().getApplication();
        final String userID = qwe.getGlobalString();
        Response.Listener<String> responseListener=new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("cNum");
                    Log.e("leng in favor",ja.length()+"");
                    if(ja.length()==0){
                        return;}
                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject item = ja.getJSONObject(i);

                        fav.add(item.getInt("cNum"));

                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        };
        CB_FavoritesListRequest cb_favoritesListRequest=new CB_FavoritesListRequest(userID,responseListener);
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        queue.add(cb_favoritesListRequest);

    }
    public void setBtn_view(){

        favorites();
        imagePost();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    arrayList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("Board");

                    Log.e("ja.length",""+ja.length());

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);

                        String cTitle = item.getString("cTitle");
                        String cWriter = item.getString("userID");
                        String cTime = item.getString("cDate");
                        String hPerson = item.getString("hPerson");
                        String cViews = item.getString("cView");

                        int cNumber = item.getInt("cNumber");
                        String url ="http://tmdgus95p.dothome.co.kr/imgFile/mainicon.png";
                        int image=R.drawable.star;


                        for (int z=0;z<imageDataList.size();z++){
                            CB_ListData2 b=imageDataList.get(z);
                            if (cNumber == b.getcNumber()) {
                                url=b.getImageUrl();
                                break;
                            }
                        }
                        for (int f=0;f<fav.size();f++ ){
                            int asd=fav.get(f);
                            if (cNumber==asd){
                                image=R.drawable.yellowstar;
                            }
                        }

                        CBData = new CB_ListData(cNumber,image,url,cTitle,cWriter,cTime.substring(2,16),"조회수",hPerson,cViews);

                        arrayList.add(CBData);

                        CBListAdapter.notifyDataSetChanged(); //추가후새로고침
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        };

        CB_ListViewRequest lpr = new CB_ListViewRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add( lpr );

    }

    public void categorySelect() {

        favorites();
        imagePost();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    arrayList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("Board");

                    Log.e("ja.length",""+ja.length());

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);

                        String cTitle = item.getString("cTitle");
                        String cWriter = item.getString("userID");
                        String cTime = item.getString("cDate");
                        String hPerson = item.getString("hPerson");
                        String cViews = item.getString("cView");

                        int cNumber = item.getInt("cNumber");
                        String url ="http://tmdgus95p.dothome.co.kr/imgFile/mainicon.png";
                        int image=R.drawable.star;

                        for (int z=0;z<imageDataList.size();z++){
                            CB_ListData2 b=imageDataList.get(z);
                            if (cNumber == b.getcNumber()) {
                                url=b.getImageUrl();
                                break;
                            }
                        }
                        for (int f=0;f<fav.size();f++ ){
                            int asd=fav.get(f);
                            if (cNumber==asd){
                                image=R.drawable.yellowstar;
                            }
                        }

                        CBData = new CB_ListData(cNumber,image,url,cTitle,cWriter,cTime.substring(2,16),"조회수",hPerson,cViews);

                        arrayList.add(CBData);

                        CBListAdapter.notifyDataSetChanged(); //추가후새로고침
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "해당 카테고리 글이 없습니다", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        };

        CB_CategoryListRequest lpr = new CB_CategoryListRequest(ct,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add( lpr );

    }
    public void imagePost() {

        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("Board");

                    Log.e("ja.length",""+ja.length());

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);

                        int cNumber=item.getInt("cNumber");
                        String imageUrl = item.getString("imageUrl");

                        CBListData2 =new CB_ListData2(cNumber,imageUrl);
                        imageDataList.add(CBListData2);
                        CBListAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "리스트 불러오기 실패", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        };


        CB_ListImageRequest lpr2 = new CB_ListImageRequest(responseListener2);
        RequestQueue queue2 = Volley.newRequestQueue(getActivity());
        queue2.add( lpr2 );

    }

    public void listPost() {

        favorites();
        imagePost();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    arrayList.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("Board");
                    Log.e("ja.length",""+ja.length());
                    if(ja.length()==0){return;}
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);

                        String cTitle = item.getString("cTitle");
                        String cWriter = item.getString("userID");
                        String cTime = item.getString("cDate");
                        String hPerson = item.getString("hPerson");
                        String cViews = item.getString("cView");

                        int cNumber = item.getInt("cNumber");
                        String url ="http://tmdgus95p.dothome.co.kr/imgFile/mainicon.png";
                        int image=R.drawable.star;

                        for (int z=0;z<imageDataList.size();z++){
                            CB_ListData2 b=imageDataList.get(z);
                            if (cNumber == b.getcNumber()) {
                                url=b.getImageUrl();
                                break;
                            }
                        }
                        for (int f=0;f<fav.size();f++ ){
                            int asd=fav.get(f);
                            if (cNumber==asd){
                                image=R.drawable.yellowstar;
                            }
                        }

                        CBData = new CB_ListData(cNumber,image,url,cTitle,cWriter,cTime.substring(2,16),"조회수",hPerson,cViews);

                        arrayList.add(CBData);

                        CBListAdapter.notifyDataSetChanged(); //추가후새로고침
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "리스트 불러오기 실패", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        };

        CB_ListRequest lpr = new CB_ListRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add( lpr );

    }

}
