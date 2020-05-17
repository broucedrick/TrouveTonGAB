package com.example.trouvetongab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListGabAdapter extends  RecyclerView.Adapter<ListGabAdapter.ViewHolder> implements Filterable {
    private List<Gab> dataset;
    private List<Gab> mFilteredList;
    private Context ctxt;



    public ListGabAdapter(Context context, List<Gab> data){
        dataset = data;
        mFilteredList = new ArrayList<>(dataset);
        ctxt = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        Button btniti;
        TextView gabname;
        ImageView ic;
        CardView gabLayout;
        TextView aucun;
        public ViewHolder(View v) {
            super(v);
            aucun = v.findViewById(R.id.aucun);
            btniti = v.findViewById(R.id.btniti);
            gabname = v.findViewById(R.id.gabName);
            gabLayout = v.findViewById(R.id.gabLayout);
            ic = v.findViewById(R.id.gabimage);
        }
    }


    @NonNull
    @Override
    public ListGabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listgabitem_layout, parent, false);
        ListGabAdapter.ViewHolder holder = new ListGabAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListGabAdapter.ViewHolder holder, final int position) {
        Gab gb = dataset.get(position);
        holder.gabname.setText(gb.getTitle());

        if (gb.getPosted() == 0)
            holder.ic.setImageResource(R.mipmap.ic_unavailable_gab);
        else
            holder.ic.setImageResource(R.mipmap.gab_dispo);

        holder.btniti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctxt, ItineActivity.class);
               // Intent i = new Intent(ctxt, Map.class);

                Toast.makeText(ctxt, "go to itineraire", Toast.LENGTH_LONG).show();

                Bundle bundle = new Bundle();
                bundle.putString("url",dataset.get(position).getLocation());
                bundle.putString("gabName", dataset.get(position).getTitle());
                i.putExtras(bundle);
                ctxt.startActivity(i, bundle);
            }

        });

//        if(gabname.size() <= 0){
//            holder.aucun.setVisibility(View.VISIBLE);
//        }else{
//            holder.aucun.setVisibility(View.GONE);
//        }
    }

    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte = Base64.decode(image,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                List<Gab> filteredList = new ArrayList<>();

                if (charString.isEmpty()) {

                    filteredList.addAll(mFilteredList);
                } else {

                    for (Gab gab : dataset) {

                        if (gab.getTitle().toLowerCase().contains(charString.trim()) || gab.getTitle().contains(charString.trim())) {

                            filteredList.add(gab);
                        }

                    }

                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataset.clear();
                dataset.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

}
