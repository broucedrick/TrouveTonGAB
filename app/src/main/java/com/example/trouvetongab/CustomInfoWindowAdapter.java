package com.example.trouvetongab;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private String horaire;
    private String contact;
    private FragmentAgence context;

    public CustomInfoWindowAdapter(FragmentAgence context, String horaire, String contact){
        this.context = context;
        this.horaire = horaire;
        this.contact = contact;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.custominfowindow, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvHoraire = (TextView) view.findViewById(R.id.tv_horaire);
        TextView tvContact = (TextView) view.findViewById(R.id.tv_contact);
        ImageView ic_contact = (ImageView) view.findViewById(R.id.im_contact);

        if(contact.contentEquals("null")){
            tvContact.setVisibility(View.GONE);
            ic_contact.setVisibility(View.GONE);
        }else{
            tvContact.setText(contact);
        }

        tvTitle.setText(marker.getTitle());
        tvHoraire.setText(horaire);


        return view;
    }
}
