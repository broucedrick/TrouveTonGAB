package com.example.trouvetongab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class ScreenAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public ScreenAdapter(Context context) {
        this.context = context;
    }

    public int[] Screen_id = {1, 2, 3};

    public String[] Desc_title = {
            "Trouve ton GAB",
            "Déclarer un incident\nbancaire",
            "Market Place\nDigitale Finances"
    };

    public String[] description = {
        "Retrouvez l'ensemble des\nguichets automatiques de toutes\nles banques avec leurs statuts\nde disponibilité",
        "Déclarer un incident rencontré\nlors d'une opération dans un\nguichet automatique",
        "Se rendre sur la première Marketplace financière en ligne\nwww.digitalefinances.com"
    };

    public String[] btn_nav_text = {
            "suivant",
            "suivant",
            "commencer"
    };

    @Override
    public int getCount() {
        return Screen_id.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.screen_layout, container, false);

        TextView screen_id = (TextView) view.findViewById(R.id.screen_id);
        TextView desc_title = (TextView) view.findViewById(R.id.desc_title);
        TextView desc = (TextView) view.findViewById(R.id.desc);

        screen_id.setText(String.valueOf(Screen_id[position]));
        desc_title.setText(Desc_title[position]);
        desc.setText(description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
