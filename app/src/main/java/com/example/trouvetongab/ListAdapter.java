package com.example.trouvetongab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends  RecyclerView.Adapter<ListAdapter.ViewHolder>{
    private ArrayList<String> dataset;
    private Context ctxt;
    public ListAdapter(Context context, ArrayList<String> data){
        dataset = data;
        ctxt = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView img;
        TextView imgname;
        CardView parentLayout;
        public ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.imageView);
            imgname = v.findViewById(R.id.imgName);
            parentLayout = v.findViewById(R.id.parentLayout);
        }
    }


    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, final int position) {
        holder.imgname.setText(dataset.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctxt, dataset.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
