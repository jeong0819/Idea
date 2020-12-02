package com.example.realp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.CustomViewHolder> {
    private ArrayList<TeamData> ar;
    Projectinfo p = new Projectinfo();
    public TeamAdapter(ArrayList<TeamData> ar) {
        this.ar = ar;
    }

    @NonNull
    @Override
    public TeamAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.teamlist,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TeamAdapter.CustomViewHolder holder, final int position) {
        holder.mName.setText(ar.get(position).getmName());
        holder.mChar.setText(ar.get(position).getmChar());
        holder.mRank.setText(ar.get(position).getmRank());
        if(!ar.get(position).getApp()) {
            holder.mClear.setVisibility(View.INVISIBLE);
        } else{
            holder.mClear.setImageResource(ar.get(position).getmClear());
        }
        holder.itemView.setTag(position);
        holder.mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ar.get(position).getPsta()==1){
                    CurProjectinfo c =new CurProjectinfo();EditProject ep=new EditProject();
                    remove(holder.getAdapterPosition(),ar.get(position).getPsta());
                    c.worker.remove(position);ep.tmpuser.remove(ep.getAddCount()-1);
                    c.wChar.remove(position); ep.tmpchar.remove(ep.getAddCount()-1);
                    c.wRank.remove(position); ep.tmprank.remove(ep.getAddCount()-1);
                    ep.setAddCount(ep.getAddCount()-1);
                    c.setTpc(c.worker.size());
                    Log.e("삭제후",c.getTpc()+"");
                }else{
                    remove(holder.getAdapterPosition(),ar.get(position).getPsta());
                    p.pWorker.remove(position);
                    p.tChar.remove(position);
                    p.tRank.remove(position);
                    p.setTpc(p.pWorker.size());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null!=ar?ar.size():0);
    }
    private void remove(int position, int psta){
        try {
            if(psta==1){
                EditProject ep=new EditProject();
                ep.tar.remove(position);
                ep.teamAdapter.notifyDataSetChanged();
            }else{
                ar.remove(position);
                notifyDataSetChanged();
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView mName;
        protected TextView mChar;
        protected TextView mRank;
        protected ImageView mClear;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            mName=itemView.findViewById(R.id.mName);
            mChar=itemView.findViewById(R.id.mChar);
            mRank=itemView.findViewById(R.id.mRank);
            mClear=itemView.findViewById(R.id.mClear);
        }
    }
}
