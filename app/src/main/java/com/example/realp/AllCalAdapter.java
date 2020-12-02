package com.example.realp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllCalAdapter extends RecyclerView.Adapter<AllCalAdapter.CustomViewHolder> {
    ArrayList<AllCalData> acar;

    public AllCalAdapter(ArrayList<AllCalData> acar) {
        this.acar = acar;
    }

    @NonNull
    @Override
    public AllCalAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_cal_list,parent,false);
        CustomViewHolder holder=new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllCalAdapter.CustomViewHolder holder, int position) {
            holder.tv_date.setText(acar.get(position).getDate());
            holder.tv_con.setText(acar.get(position).getCon());
    }

    @Override
    public int getItemCount() {
        return (null!=acar?acar.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_date;
        protected TextView tv_con;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date=itemView.findViewById(R.id.tv_date);
            tv_con=itemView.findViewById(R.id.tv_con);
        }
    }
}
