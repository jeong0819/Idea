package com.example.realp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class CalAdapter extends RecyclerView.Adapter<CalAdapter.CustomViewHolder> {
    private ArrayList<CalData> car;
    CurProjectinfo c= new CurProjectinfo();
    CalManage cm=new CalManage();
    public CalAdapter(ArrayList<CalData> car) {
        this.car = car;
    }

    @NonNull
    @Override
    public CalAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.callist,parent,false);
        CustomViewHolder holder=new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CalAdapter.CustomViewHolder holder, int position) {
            holder.tv_time.setText(car.get(position).getTime());
            holder.tv_calctn.setText(car.get(position).getContents());
            holder.iv_cancal.setImageResource(car.get(position).getIv_cancal());
            holder.iv_cancal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                    builder.setTitle("일정 삭제").setMessage("정말 삭제하시겠습니까?");
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            String sd=cm.getsDate();
                            String[] std=sd.split("[.]");
                            String[] stt=holder.tv_time.getText().toString().split(":");
                            Calendar cur = Calendar.getInstance();
                            cur.set(Calendar.MONTH,cur.get(Calendar.MONTH)+1);
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.set(Integer.parseInt(std[0]),Integer.parseInt(std[1]),Integer.parseInt(std[2]),Integer.parseInt(stt[0]),Integer.parseInt(stt[1]));
                            if(calendar1.before(cur)){
                                DelCal(holder,position);
                            }else{
                                getNotiNoForDel(cm.getsDate(),holder.tv_time.getText().toString(),holder.tv_calctn.getText().toString(),"이정현",holder.itemView.getContext(),holder,position);
                            }


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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sd=cm.getsDate();
                    String[] std=sd.split("[.]");
                    String[] stt=holder.tv_time.getText().toString().split(":");
                    Calendar cur = Calendar.getInstance();
                    cur.set(Calendar.MONTH,cur.get(Calendar.MONTH)+1);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(Integer.parseInt(std[0]),Integer.parseInt(std[1]),Integer.parseInt(std[2]),Integer.parseInt(stt[0]),Integer.parseInt(stt[1]));
                    if(calendar1.before(cur)){
                        Toast.makeText(holder.itemView.getContext(), "기간이 지났습니다", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                            CustomCalEditDialog cced=new CustomCalEditDialog(holder.itemView.getContext(),
                                    cm.curDate.getText().toString(),holder.tv_time.getText().toString(),
                                    holder.tv_calctn.getText().toString(),car.get(position).isTsw());
                            cced.callFunction();
                    }
                }
            });
            if(car.get(position).isTsw()){
                holder.tv_calAsta.setText("On");
            }else{
                holder.tv_calAsta.setText("off");
            }
    }
    private void DelCal(CalAdapter.CustomViewHolder holder, int position){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    if(suc){
                        car.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,car.size());
                        notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    Log.e("CE0","Connect Error");
                    e.printStackTrace();
                }
            }
        };

        DelCalReq dcr = new DelCalReq(c.getTeamNo(),cm.curDate.getText().toString(),holder.tv_time.getText().toString(),holder.tv_calctn.getText().toString(),"이정현"
                ,responseListener);
        RequestQueue queue = Volley.newRequestQueue(holder.itemView.getContext());
        queue.add(dcr);
    }
    private void getNotiNoForDel(String date,String time,String con,String user,Context context,CalAdapter.CustomViewHolder holder, int position){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject( response );
                    boolean suc = jsonObject.getBoolean("success");
                    int noti_i=jsonObject.getInt("noti");
                    if(suc){
                        AlarmManager am = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
                        Intent intent = new Intent(context, ReminderBroadcastReiceiver.class);
                        PendingIntent sender = PendingIntent.getBroadcast(context, noti_i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        if (sender != null) {
                            Log.e("알람제거","del");
                            am.cancel(sender);
                            sender.cancel();
                        }
                        DelCal(holder,position);

                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    Log.e("CE0","Connect Error");
                    e.printStackTrace();
                }
            }
        };
        GetNotiNoReq gnnr = new GetNotiNoReq(c.getTeamNo(),date,time,con,user,responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(gnnr);

    }
    @Override
    public int getItemCount() {
        return (null!=car?car.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected TextView tv_time;
        protected TextView tv_calctn;
        protected TextView tv_calAsta;
        protected ImageView iv_cancal;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_time=itemView.findViewById(R.id.tv_caltime);
            tv_calctn=itemView.findViewById(R.id.tv_calctn);
            tv_calAsta=itemView.findViewById(R.id.tv_calAsta);
            iv_cancal=itemView.findViewById(R.id.iv_cancal);
        }
    }
}
