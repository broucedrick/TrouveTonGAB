package com.example.trouvetongab;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SlideAdapterVH> {

    private Context context;
    private List<Drawable> mImageSlide = new ArrayList<>();

    public SliderAdapter(Context context, List<Drawable> imageSlide) {
        this.context = context;
        this.mImageSlide = imageSlide;
    }

    @Override
    public SlideAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SlideAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SlideAdapterVH viewHolder, int position) {
        Drawable sliderItem = mImageSlide.get(position);

        viewHolder.imageViewBackground.setBackground(sliderItem);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.digitalefinances.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getCount() {
        return mImageSlide.size();
    }

    class SlideAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SlideAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;
        }
    }


}
