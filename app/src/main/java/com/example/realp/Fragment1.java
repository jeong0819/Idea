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

public class Fragment1 extends Fragment {

    private ArrayList<BB_ListData> arrayList=new ArrayList<>();
    private ArrayList<BB_ListData2> imageDataList=new ArrayList<>();
    private ArrayList<Integer> fav=new ArrayList<>();

    private RecyclerView recyclerView;
    private BB_ListAdapter BBListAdapter;
    private Button category,btn_new,btn_view;
    private ImageButton buyWrite;
    private String ct="전체";
    BB_ListData BBListData;
    BB_ListData2 BBListData2;

    View rootView;
    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag1, container, false);
       // ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();

       // actionBar.setTitle("아이디어 판매 게시판");

        setHasOptionsMenu(true);

        buyWrite =(ImageButton)rootView.findViewById(R.id.buy_write);
        buyWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BB_Write.class);
                startActivity(intent);
            }
        });
        category=(Button)rootView.findViewById(R.id.categorySearch);
        btn_new=(Button)rootView.findViewById(R.id.btn_new);
        btn_view=(Button)rootView.findViewById(R.id.btn_view);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        recyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), 1));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());


        BBListAdapter = new BB_ListAdapter(arrayList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(BBListAdapter);

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(),v);

                MenuInflater inflater = popupMenu.getMenuInflater();
                Menu menu = popupMenu.getMenu();

                inflater.inflate(R.menu.buyboardcategorysearch_menu,menu);

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
                                ct="기계장치";
                                category.setText("기계장치");
                                categorySelect();
                                break;
                            case R.id.menu3:
                                ct="전기전자";
                                category.setText("전기전자");
                                categorySelect();
                                break;
                            case R.id.menu4:
                                ct="정보통신";
                                category.setText("정보통신");
                                categorySelect();
                                break;
                            case R.id.menu5:
                                ct="토목건축";
                                category.setText("토목건축");
                                categorySelect();
                                break;
                            case R.id.menu6:
                                ct="생활도구";
                                category.setText("생활도구");
                                categorySelect();
                                break;
                            case R.id.menu7:
                                ct="식품음식";
                                category.setText("식품음식");
                                categorySelect();
                                break;
                            case R.id.menu8:
                                ct="농수산";
                                category.setText("농수산");
                                categorySelect();
                                break;
                            case R.id.menu9:
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
        Log.e("userNick in favor first",userID);
        Response.Listener<String> responseListener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("수행","수");
                    JSONArray ja = jsonObject.getJSONArray("bsNum");
                    Log.e("leng in favor",ja.length()+"");
                    Log.e("수행","행");
                    if(ja.length()==0){return;}
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);
                        fav.add(item.getInt("bsNum"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        BB_FavoritesListRequest bb_favoritesListRequest=new BB_FavoritesListRequest(userID,responseListener);
        RequestQueue queue= Volley.newRequestQueue(rootView.getContext());
        queue.add(bb_favoritesListRequest);

    }

    public void setBtn_view(){
        imagePost();
        favorites();
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

                        String rvTitle = item.getString("bTitle");
                        String rvWriter = item.getString("userID");
                        String rvTime = item.getString("bDate");
                        String rvPrice = item.getString("price");
                        String rvViews = item.getString("bView");

                        int bNumber = item.getInt("bNumber");
                        String url = "http://tmdgus95p.dothome.co.kr/imgFile/mainicon.png";
                        int image=R.drawable.star;

                        for (int z = 0; z < imageDataList.size(); z++) {
                            BB_ListData2 b = imageDataList.get(z);
                            if (bNumber == b.getbNumber()) {
                                url = b.getImageUrl();
                                break;
                            }
                        }
                        for (int f=0;f<fav.size();f++ ){
                            int asd=fav.get(f);
                            if (bNumber==asd){
                                image=R.drawable.yellowstar;
                            }
                        }
                        BBListData = new BB_ListData(bNumber, rvPrice, rvViews, url, image, rvTitle, rvWriter, rvTime.substring(2, 16), "조회수");

                        arrayList.add(BBListData);
                        BBListAdapter.notifyDataSetChanged(); //추가후새로고침
                    }



                } catch (JSONException e) {
                    Toast.makeText( getActivity(), "", Toast.LENGTH_SHORT ).show();
                    e.printStackTrace();
                }
            }

        };

        BB_ListViewRequest qwe  = new BB_ListViewRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(qwe);

    }

    public void categorySelect(){

        imagePost();
        favorites();
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

                        String rvTitle = item.getString("bTitle");
                        String rvWriter = item.getString("userID");
                        String rvTime = item.getString("bDate");
                        String rvPrice = item.getString("price");
                        String rvViews = item.getString("bView");

                        int bNumber = item.getInt("bNumber");
                        String url = "http://tmdgus95p.dothome.co.kr/imgFile/mainicon.png";
                        int image=R.drawable.star;

                        for (int z = 0; z < imageDataList.size(); z++) {
                            BB_ListData2 b = imageDataList.get(z);
                            if (bNumber == b.getbNumber()) {
                                url = b.getImageUrl();
                                break;
                            }
                        }
                        for (int f=0;f<fav.size();f++ ){
                            int asd=fav.get(f);
                            if (bNumber==asd){
                                image=R.drawable.yellowstar;
                            }
                        }
                        BBListData = new BB_ListData(bNumber, rvPrice, rvViews, url, image, rvTitle, rvWriter, rvTime.substring(2, 16), "조회수");

                        arrayList.add(BBListData);
                        BBListAdapter.notifyDataSetChanged(); //추가후새로고침
                    }



                } catch (JSONException e) {
                    Toast.makeText( getActivity(), "해당 카테고리 글이 없습니다.", Toast.LENGTH_SHORT ).show();
                    e.printStackTrace();
                }
            }

        };

        BB_CategoryListRequest qwe  = new BB_CategoryListRequest(ct, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(qwe);
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

                        int bNumber=item.getInt("bNumber");
                        String imageUrl = item.getString("imageUrl");

                        BBListData2 =new BB_ListData2(bNumber,imageUrl);
                        imageDataList.add(BBListData2);
                        BBListAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };


        BB_ListImageRequest lpr2 = new BB_ListImageRequest(responseListener2);
        RequestQueue queue2 = Volley.newRequestQueue(getActivity());
        queue2.add( lpr2 );


    }



    public void listPost() {

        imagePost();
        favorites();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    arrayList.clear();


                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray ja = jsonObject.getJSONArray("Board");

                    Log.d("ja.length",""+ja.length());

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject item = ja.getJSONObject(i);

                        String rvTitle = item.getString("bTitle");
                        String rvWriter = item.getString("userID");
                        String rvTime = item.getString("bDate");
                        String rvPrice = item.getString("price");
                        String rvViews = item.getString("bView");

                        int bNumber=item.getInt("bNumber");
                        String url="http://tmdgus95p.dothome.co.kr/imgFile/mainicon.png";
                        int image=R.drawable.star;
                        for (int z=0;z<imageDataList.size();z++){
                            BB_ListData2 b=imageDataList.get(z);
                            if (bNumber == b.getbNumber()) {
                                url=b.getImageUrl();
                                break;
                            }
                        }

                        for (int f=0;f<fav.size();f++ ){
                            int asd=fav.get(f);
                            if (bNumber==asd){
                                image=R.drawable.yellowstar;
                            }
                        }


                        BBListData = new BB_ListData(bNumber,rvPrice,rvViews,url,image,rvTitle,rvWriter,rvTime.substring(2,16),"조회수");

                        arrayList.add(BBListData);

                        BBListAdapter.notifyDataSetChanged(); //추가후새로고침
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        BB_ListRequest lpr = new BB_ListRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add( lpr );

    }




}