package com.example.realp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BB_CommentAdapter extends RecyclerView.Adapter<BB_CommentAdapter.CustomViewHolder> {
    private ArrayList<BB_CommentData> arrayList;




    public BB_CommentAdapter(ArrayList<BB_CommentData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public BB_CommentAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buyboard_comment,parent,false);

        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BB_CommentAdapter.CustomViewHolder holder, final int position) {




        String date=arrayList.get(position).getReDate();
        holder.reWrite.setText(arrayList.get(position).getReWrite());
        holder.reMemo.setText(arrayList.get(position).getReMemo());
        holder.reDate.setText(date.substring(2,16));

        holder.itemView.setTag(position);




    }

    @Override
    public int getItemCount() {
        return (null!=arrayList?arrayList.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {


        protected TextView reWrite,reMemo,reDate;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            reWrite=(TextView) itemView.findViewById(R.id.t_reWrite);
            reMemo=(TextView) itemView.findViewById(R.id.t_reMemo);
            reDate=(TextView) itemView.findViewById(R.id.t_reDate);

        }
    }
}