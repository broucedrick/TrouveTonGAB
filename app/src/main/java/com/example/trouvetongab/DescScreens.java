package com.example.trouvetongab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DescScreens extends AppCompatActivity {
    private ViewPager screen_viewpage;
    private LinearLayout dots_layout;
    private ScreenAdapter screenAdapter;
    private TextView[] dots;
    private Button screen_nav;
    private LinearLayout back_btn;
    private int mCurrentPage;
    private boolean desc_viewed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc_screens);

        screen_viewpage = (ViewPager) findViewById(R.id.screen_viewpage);
        dots_layout = (LinearLayout) findViewById(R.id.dots_layout);


        screen_nav = (Button) findViewById(R.id.btn_screen_nav);

        screenAdapter = new ScreenAdapter(this);

        screen_viewpage.setAdapter(screenAdapter);

        screen_viewpage.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });

        addDotsIndicator(0);

        back_btn = (LinearLayout) findViewById(R.id.back);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen_viewpage.setCurrentItem(mCurrentPage-1);
            }
        });

        screen_viewpage.addOnPageChangeListener(viewListener);

        screen_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentPage == dots.length-1){
                    startActivity(new Intent(DescScreens.this, Home.class));
                    finish();
                }else{
                    desc_viewed = true;
                    SharedPreferences mSharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                    mEditor.putBoolean("desc_viewed", desc_viewed);
                    mEditor.apply();
                    screen_viewpage.setCurrentItem(mCurrentPage+1);
                }
            }
        });

    }

    public void addDotsIndicator(int pos){
        dots = new TextView[3];
        dots_layout.removeAllViews();

        for(int i=0; i<dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.quantum_grey));

            dots_layout.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[pos].setTextColor(getResources().getColor(R.color.quantum_white_100));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);

            mCurrentPage = position;

            if(position == 0){
                back_btn.setVisibility(View.INVISIBLE);
                screen_nav.setText("suivant");
            }else if(position == dots.length-1){
                back_btn.setVisibility(View.VISIBLE);
                screen_nav.setText("commencer");
            }else{
                back_btn.setVisibility(View.VISIBLE);
                screen_nav.setText("suivant");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
