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
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAdapter extends  RecyclerView.Adapter<ListAdapter.ViewHolder>{
    private List<Bank> dataset;
    private Context ctxt;

    public ListAdapter(Context context, List<Bank> data){
        dataset = data;
        ctxt = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView img;
        //TextView imgname;
        CardView parentLayout;
        public ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.imageView);
            //imgname = v.findViewById(R.id.imgName);
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
        final Bank bk = dataset.get(position);
        //holder.imgname.setText(bk.getTitle());

        Bitmap image = StringToBitMap(bk.getImage());

        holder.img.setImageResource(getImageSrc(bk.getImage()));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(ctxt, ListGab.class);
                Intent i = new Intent(ctxt, AgenceGab.class);
                Bundle bundle = new Bundle();
                bundle.putInt("bank_id", bk.getId());
                bundle.putString("bank_name", bk.getTitle());
                i.putExtras(bundle);
                //ctxt.startActivity(i, bundle);*/

                ctxt.startActivity(i, bundle);
            }
        });
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

    public int getImageSrc(String image){
        int img = R.drawable.ic_launcher_foreground; // blank image

// testimage.png is the image name from the database
        if(image.equals("Afriland-First-Bank.jpg"))
            img = R.mipmap.afriland_first_fank_foreground;
        else if(image.equals("banque-atlantique.jpg"))
            img = R.mipmap.banque_atlantique_foreground;
        else if(image.equals("banque-populaire.jpg"))
            img = R.mipmap.banque_populaire_foreground;
        else if(image.equals("Axa.jpg"))
            img = R.mipmap.axa_foreground;
        else if(image.equals("Allianz.jpg"))
            img = R.mipmap.allianz_foreground;
        else if(image.equals("bda.jpg"))
            img = R.mipmap.bda_foreground;
        else if(image.equals("bdu-ci.jpg"))
            img = R.mipmap.bdu_ci_foreground;
        else if(image.equals("bgfibank.jpg"))
            img = R.mipmap.bgfibank_foreground;
        else if(image.equals("bhci.png"))
            img = R.mipmap.bhci_foreground;
        else if(image.equals("bicici.jpg"))
            img = R.mipmap.bicici_foreground;
        else if(image.equals("bms-ci.jpg"))
            img = R.mipmap.bms_ci_foreground;
        else if(image.equals("bni.jpg"))
            img = R.mipmap.bni_foreground;
        else if(image.equals("bridge-bank-group.jpg"))
            img = R.mipmap.bridge_bank_group_foreground;
        else if(image.equals("boa.jpg"))
            img = R.mipmap.boa_foreground;
        else if(image.equals("bsic.jpg"))
            img = R.mipmap.bsic_foreground;
        else if(image.equals("citi.jpg"))
            img = R.mipmap.citi_foreground;
        else if(image.equals("corisbank.jpg"))
            img = R.mipmap.corisbank_foreground;
        else if(image.equals("diamond.jpg"))
            img = R.mipmap.diamond_foreground;
        else if(image.equals("ecobank.jpg"))
            img = R.mipmap.ecobank_foreground;
        else if(image.equals("gtbank.jpg"))
            img = R.mipmap.gtbank_foreground;
        else if(image.equals("nsia-banque.jpg"))
            img = R.mipmap.nsia_banque_foreground;
        else if(image.equals("orabank.jpg"))
            img = R.mipmap.orabank_foreground;
        else if(image.equals("sib.jpg"))
            img = R.mipmap.sib_foreground;
        else if(image.equals("societe-generale.jpg"))
            img = R.mipmap.societe_generale_foreground;
        else if(image.equals("stanbic-bank.jpg"))
            img = R.mipmap.stanbic_bank_foreground;
        else if(image.equals("stantard-chartered.jpg"))
            img = R.mipmap.stantard_chartered_foreground;
        else if(image.equals("uba.jpg"))
            img = R.mipmap.uba_foreground;
        else if(image.equals("versus-bank.jpg"))
            img = R.mipmap.versus_bank_foreground;


        return img;
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
