package com.example.realp;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.CustomViewHoldered>{
    private ArrayList<ProjectData> par;

    public ProjectAdapter(ArrayList<ProjectData> par) {
        this.par = par;
    }

    @NonNull
    @Override
    public ProjectAdapter.CustomViewHoldered onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.projectlist,parent,false);
        CustomViewHoldered holder = new CustomViewHoldered(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectAdapter.CustomViewHoldered holder, int position) {
        holder.tv_Pname.setText(par.get(position).getTv_Pname());
        holder.tv_Tname.setText(par.get(position).getTv_Tname());
        holder.iv_tpc.setImageResource(par.get(position).getIv_tpc());
        holder.tv_tpc.setText(String.valueOf(par.get(position).getTv_tpc()));
        holder.tv_sd.setText(par.get(position).getTv_sd());
        holder.tv_fd.setText(par.get(position).getTv_fd());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurProjectinfo cpi=new CurProjectinfo();
                cpi.setProjectName(holder.tv_Pname.getText().toString());
                cpi.setTeamName(holder.tv_Tname.getText().toString());
                Intent i = new Intent(holder.itemView.getContext(),ShowProject.class);
                ((Activity)holder.itemView.getContext()).startActivityForResult(i,9999);
            }
        });
    }


    @Override
    public int getItemCount() {
        return (null!=par?par.size():0);
    }

    public class CustomViewHoldered extends RecyclerView.ViewHolder {
        protected TextView tv_Pname;
        protected TextView tv_Tname;
        protected ImageView iv_tpc;
        protected TextView tv_tpc;
        protected TextView tv_sd;
        protected TextView tv_fd;
        public CustomViewHoldered(@NonNull View itemView) {
            super(itemView);
            tv_Pname=(TextView)itemView.findViewById(R.id.tv_Pname);
            tv_Tname=(TextView)itemView.findViewById(R.id.tv_Tname);
            iv_tpc=(ImageView) itemView.findViewById(R.id.iv_tpc);
            tv_tpc=(TextView)itemView.findViewById(R.id.tv_tpc);
            tv_sd=(TextView)itemView.findViewById(R.id.tv_sd);
            tv_fd=(TextView)itemView.findViewById(R.id.tv_fd);
        }

    }
}
