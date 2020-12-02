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


public class BB_ListAdapter extends RecyclerView.Adapter<BB_ListAdapter.CustomViewHolder> {

    private ArrayList<BB_ListData> arrayList;


    String str;


    public BB_ListAdapter(ArrayList<BB_ListData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public BB_ListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.buyboard_listview,parent,false);

        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BB_ListAdapter.CustomViewHolder holder, final int position) {

        str=arrayList.get(position).getMainImage();

        holder.bNumber=arrayList.get(position).getbNumber();
        Glide.with(holder.itemView.getContext()).load(str).into(holder.mainImage);
        holder.star.setImageResource(arrayList.get(position).getStar());
        holder.rvTitle.setText(arrayList.get(position).getRvTitle());
        holder.rvWriter.setText(arrayList.get(position).getRvWriter());
        holder.rvTime.setText(arrayList.get(position).getRvTime());
        holder.rvPrice.setText(arrayList.get(position).getRvPrice());
        holder.rvViews.setText(arrayList.get(position).getRvViews());
        holder.view1.setText(arrayList.get(position).getView1());




        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context=view.getContext();

                Intent i = new Intent(view.getContext(), BB_Read.class);
                i.putExtra("bNumber",holder.bNumber);

                context.startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return (null!=arrayList?arrayList.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mainImage;
        protected ImageView star;
        protected TextView rvTitle,rvWriter,rvTime,view1,rvPrice,rvViews;
        protected int bNumber;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            mainImage =(ImageView) itemView.findViewById(R.id.mainImage);
            star =(ImageView) itemView.findViewById(R.id.star);
            rvTitle=(TextView) itemView.findViewById(R.id.rvTitle);
            rvWriter=(TextView) itemView.findViewById(R.id.rvWriter);
            rvTime=(TextView) itemView.findViewById(R.id.rvTime);
            view1=(TextView) itemView.findViewById(R.id.view1);
            rvPrice=(TextView) itemView.findViewById(R.id.rvPrice);
            rvViews=(TextView) itemView.findViewById(R.id.rvViews);
        }
    }
}
