package com.example.realp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FinTogAdapter  extends RecyclerView.Adapter<FinTogAdapter.CustomViewHolderr> {
    private ArrayList<IngTogData> ftar;
    CurProjectinfo c = new CurProjectinfo();
    ShowTask s = new ShowTask();
    int num;
    String subject="";
    public FinTogAdapter(ArrayList<IngTogData> ftar) {
        this.ftar = ftar;
    }

    @NonNull
    @Override
    public FinTogAdapter.CustomViewHolderr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ingtoglist,parent,false);
        FinTogAdapter.CustomViewHolderr holder=new FinTogAdapter.CustomViewHolderr(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FinTogAdapter.CustomViewHolderr holder, int position) {
        holder.ingtogUser.setText(ftar.get(position).getIngTogUser());
        holder.ingtogTask.setText(ftar.get(position).getIngTogTask());
        holder.ingtogDate.setText(ftar.get(position).getIngTogDate());
        holder.ingTogDel.setImageResource(ftar.get(position).getIngTogDel());
        holder.ingTogDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=s.getNum();
                if(num==1){
                    subject="공통";
                }else if(num==2){
                    subject="개인";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("데이터 삭제").setMessage("정말 삭제하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject( response );
                                    boolean suc = jsonObject.getBoolean("success");
                                    if(suc){
                                        ftar.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position,ftar.size());
                                        notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    Log.e("CE0","Connect Error");
                                    e.printStackTrace();
                                }
                            }
                        };
                        DelTaskReq dtr = new DelTaskReq(c.getTeamNo(),"완료",subject,holder.ingtogUser.getText().toString(),holder.ingtogTask.getText().toString(),holder.ingtogDate.getText().toString()
                                ,responseListener);
                        RequestQueue queue = Volley.newRequestQueue(holder.itemView.getContext());
                        queue.add(dtr);
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null!=ftar?ftar.size():0);
    }

    public class CustomViewHolderr extends RecyclerView.ViewHolder {
        protected TextView ingtogUser;
        protected TextView ingtogTask;
        protected TextView ingtogDate;
        protected ImageView ingTogDel;
        public CustomViewHolderr(@NonNull View itemView) {
            super(itemView);
            ingtogUser=itemView.findViewById(R.id.ingtogUser);
            ingtogTask=itemView.findViewById(R.id.ingtogTak);
            ingtogDate=itemView.findViewById(R.id.ingtogDate);
            ingTogDel=itemView.findViewById(R.id.ingtogDel);
        }
    }
}
