package com.example.realp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.CustomViewHolder> {
    private ArrayList<WorkerData> war;

    public WorkerAdapter(ArrayList<WorkerData> war) {
        this.war = war;
    }

    @NonNull
    @Override
    public WorkerAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.workerlist,parent,false);
        CustomViewHolder holder=new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerAdapter.CustomViewHolder holder, int position) {
        holder.wName.setText(war.get(position).getwName());
        holder.wChar.setText(war.get(position).getwChar());
        holder.wRank.setText(war.get(position).getwRank());
    }

    @Override
    public int getItemCount() {
        return (null!=war?war.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView wName;
        protected TextView wChar;
        protected TextView wRank;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            wName=itemView.findViewById(R.id.wName);
            wChar=itemView.findViewById(R.id.wChar);
            wRank=itemView.findViewById(R.id.wRank);
        }
    }
}
