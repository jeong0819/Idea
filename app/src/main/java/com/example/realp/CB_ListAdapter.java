package com.example.realp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CB_ListAdapter extends RecyclerView.Adapter<CB_ListAdapter.CustomViewHolder> {

    private ArrayList<CB_ListData> arrayList;
    String str;

    public CB_ListAdapter(ArrayList<CB_ListData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CB_ListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coboard_listview,parent,false);

        CB_ListAdapter.CustomViewHolder holder = new CB_ListAdapter.CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CB_ListAdapter.CustomViewHolder holder, int position) {

        str=arrayList.get(position).getcMainImage();

        holder.cNumber=arrayList.get(position).getcNumber();
        Glide.with(holder.itemView.getContext()).load(str).into(holder.cMainImage);
        holder.star2.setImageResource(arrayList.get(position).getStar2());
        holder.cTitle.setText(arrayList.get(position).getcTitle());
        holder.cWriter.setText(arrayList.get(position).getcWriter());
        holder.cTime.setText(arrayList.get(position).getcTime());
        holder.hPerson.setText(arrayList.get(position).getHperson());
        holder.cViews.setText(arrayList.get(position).getcViews());
        holder.view2.setText(arrayList.get(position).getView2());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context=view.getContext();

                Intent i = new Intent(view.getContext(), CB_Read.class);

                i.putExtra("cNumber",holder.cNumber);

                context.startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return (null!=arrayList?arrayList.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView cMainImage;
        protected ImageView star2;
        protected TextView cTitle,cWriter,cTime,view2,hPerson,cViews;
        protected int cNumber;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            cMainImage =(ImageView) itemView.findViewById(R.id.cMainImage);
            star2 =(ImageView) itemView.findViewById(R.id.star2);
            cTitle=(TextView) itemView.findViewById(R.id.cTitle);
            cWriter=(TextView) itemView.findViewById(R.id.cWriter);
            cTime=(TextView) itemView.findViewById(R.id.cTime);
            view2=(TextView) itemView.findViewById(R.id.view2);
            hPerson=(TextView) itemView.findViewById(R.id.cHperson);
            cViews=(TextView) itemView.findViewById(R.id.cViews);
        }
    }



}
