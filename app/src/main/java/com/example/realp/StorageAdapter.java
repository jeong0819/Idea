package com.example.realp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.CustomViewHolder> {
    private ArrayList<StorageData> sar;
    CustomViewHolder holder;
    CurProjectinfo cpi = new CurProjectinfo();
    Handler handler;
    String curPath="/sdcard/"+cpi.getProjectName()+"_"+cpi.getTeamName()+"_"+cpi.getTeamNo();
    public StorageAdapter(ArrayList<StorageData> sar) {
        this.sar = sar;
    }
    ProgressDialog progressDialog;
    boolean isOpen=true;
    @NonNull
    @Override
    public StorageAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.filelist,parent,false);
        holder=new CustomViewHolder(view);
        handler=new Handler();
        progressDialog = new ProgressDialog(holder.itemView.getContext());
        progressDialog.setTitle("Downloading...");
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return holder;
    }
    private void openFile(String filePath){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (filePath.endsWith(".pdf")){
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/pdf"); //연결앱 설정구간
        }else if (filePath.endsWith(".hwp")){
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/hwp");
        }
        else if (filePath.endsWith(".jpg")||filePath.endsWith(".jpeg")){
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "image/*");
        }
        else if (filePath.endsWith(".txt")){
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "text/*");
        }
        else if (filePath.endsWith(".mp3")){
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "audio/*");
        }
        else if (filePath.endsWith(".mp4")){
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "video/*");
        }
        else if (filePath.endsWith(".xls")||filePath.endsWith(".xlsx")){
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.ms-excel");
        }
        else if (filePath.endsWith(".ppt")||filePath.endsWith(".pptx")){
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.ms-powerpoint");
        }
        holder.itemView.getContext().startActivity(intent);
    }
    private void isFile(String fileName){
        String fpath=curPath+"/"+fileName;
        File f=new File(fpath);
        if(f.exists()==true){
            isOpen=true;
            openFile(fpath);
        }else{
            //다운로드
            isOpen=false;
            dowload(fileName);
           // openFile(fpath);
        }

    }
    @Override
    public void onBindViewHolder(@NonNull StorageAdapter.CustomViewHolder holder, int position) {
        holder.tv_fn.setText(sar.get(position).getFileName());
        holder.tv_un.setText(sar.get(position).getUserName());
        holder.btn_fopen.setText(sar.get(position).getOpenText());
        holder.btn_fdown.setText(sar.get(position).getDownText());
        holder.btn_fdel.setText(sar.get(position).getDelText());
        holder.btn_fopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:파일체크다운로드후 내부 저장경로로 열기
                isFile(holder.tv_fn.getText().toString());
            }
        });
        holder.btn_fdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dowload(holder.tv_fn.getText().toString());

            }
        });
        holder.btn_fdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmp_item=holder.tv_fn.getText().toString();
                String tmp_path="Storage/"+cpi.getProjectName()+"_"+cpi.getTeamName()+"_"+cpi.getTeamNo()+"/"+tmp_item;
                com.android.volley.Response.Listener<String> responseListener = new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            boolean suc = jsonObject.getBoolean("suc");
                            if(suc){
                                Toast.makeText(holder.itemView.getContext(), "삭제 완료", Toast.LENGTH_SHORT).show();
                                StorageData sd= new StorageData(tmp_item,"이정현","열기","다운로드","삭제");
                                ShowFileList s= new ShowFileList();
                                s.sar.remove(position);
                                s.far.remove(tmp_item);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,s.sar.size());
                                notifyDataSetChanged();

                                if(s.far.size()==0){
                                    s.tv_isNull.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("CE3","Connect Error");
                            e.printStackTrace();
                        }
                    }
                };
                DelFileReq dfr = new DelFileReq(tmp_path,responseListener);
                RequestQueue queue = Volley.newRequestQueue(holder.itemView.getContext());
                queue.add(dfr);
            }
        });
    }
    private void dowload(String item){
        Log.e("item",item);
        Thread down = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                String selectPath = cpi.getProjectName() + "_" + cpi.getTeamName() + "_" + cpi.getTeamNo();
                Request request = new Request.Builder().url("http://whi2020.dothome.co.kr/Project/Storage/" + selectPath + "/" + item).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    float file_size = response.body().contentLength(); //파일크기
                    BufferedInputStream inputStream = new BufferedInputStream(response.body().byteStream());
                    isFilePathCheck(curPath);
                    OutputStream stream = new FileOutputStream("/sdcard/" + selectPath + "/" + item);
                    byte[] data = new byte[8192];
                    float total = 0;
                    int read_bytes = 0;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.show();
                        }
                    });
                    while ((read_bytes = inputStream.read(data)) != -1) {
                        total = total + read_bytes;
                        stream.write(data, 0, read_bytes);
                        progressDialog.setProgress((int) ((total / file_size) * 100));
                    }
                    progressDialog.dismiss();
                    stream.flush();
                    stream.close();
                    response.body().close();
                    if(!isOpen){
                        openFile("/sdcard/" + selectPath + "/" + item);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(holder.itemView.getContext(), "다운로드완료", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });down.start();
        }

    @Override
    public int getItemCount() {
         return (null!=sar?sar.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_fn;
        protected TextView tv_un;
        protected Button btn_fopen;
        protected Button btn_fdown;
        protected Button btn_fdel;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_fn=itemView.findViewById(R.id.tv_fn);
            tv_un=itemView.findViewById(R.id.tv_un);
            btn_fopen=itemView.findViewById(R.id.btn_fopen);
            btn_fdown=itemView.findViewById(R.id.btn_fdown);
            btn_fdel=itemView.findViewById(R.id.btn_fdel);
        }
    }
    private void isFilePathCheck(String s){
        File f=new File(s);
        if(f.exists()==true){
        }else{
            f.mkdir();
        }
    }

}
