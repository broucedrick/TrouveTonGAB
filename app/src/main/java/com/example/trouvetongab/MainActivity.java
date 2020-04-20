package com.example.trouvetongab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.app.DownloadManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Bank> bk = new ArrayList<>();

    ViewPager slider;
    LinearLayout sliderDotspanels;
    private int dotscount;
    private ImageView[] dots;

    RequestQueue rq;
    List<SliderUtils> sliderImg;
    ViewPagerAdapter viewPagerAdapter;

    String request_url = "https://trouvetongab.000webhostapp.com/getImage.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rq = Volley.newRequestQueue(this);

        sliderImg = new ArrayList<>();

        //int image[] = {R.mipmap.afriland_first_fank_foreground, R.mipmap.allianz_foreground, R.mipmap.axa_foreground};

        slider = (ViewPager) findViewById(R.id.slider); // get the reference of ViewFlipper

        sliderDotspanels = (LinearLayout) findViewById(R.id.slideDot);

        sendRequest();


        slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i=0; i<dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 500, 4000);

        /*for(int imge : image){
            flipperImage(imge);
        }*/


        recyclerView = (RecyclerView) findViewById(R.id.recyclerBank);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        StaggeredGridLayoutManager mlayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mlayoutManager);


        String URL_BANQUES = "https://trouvetongab.000webhostapp.com/getBanque.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_BANQUES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("MainActivity", response);
                        Toast.makeText(MainActivity.this, "Connecté", Toast.LENGTH_LONG).show();
                        try {
                            JSONArray bank = new JSONArray(response);
                            //Toast.makeText(MainActivity.this, bank.toString(), Toast.LENGTH_LONG).show();
                            for(int i=0; i<bank.length(); i++){
                                JSONObject b = bank.getJSONObject(i);

                                int id = b.getInt("id");
                                String title = b.getString("title");
                                String image = b.getString("image");

                                bk.add(new Bank(id, title, image));

                                mAdapter = new ListAdapter(MainActivity.this, bk);
                                recyclerView.setAdapter(mAdapter);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Connection Error... "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



        // specify an adapter (see also next example)

    }

    public class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(slider.getCurrentItem() == 0){
                        slider.setCurrentItem(1);
                    }else if(slider.getCurrentItem() == 1){
                        slider.setCurrentItem(2);
                    }else{
                        slider.setCurrentItem(0);
                    }
                }
            });
        }
    }

    public void sendRequest(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i<response.length(); i++){
                    SliderUtils sliderUtils = new SliderUtils();
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        sliderUtils.setSliderImageUrl(jsonObject.getString("image"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    sliderImg.add(sliderUtils);


                }

                viewPagerAdapter = new ViewPagerAdapter(sliderImg,MainActivity.this);
                slider.setAdapter(viewPagerAdapter);

                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];

                for (int i=0; i<dotscount; i++){
                    dots[i] = new ImageView(MainActivity.this);
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 0, 8, 0);
                    sliderDotspanels.addView(dots[i], params);
                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        rq.add(jsonArrayRequest);
    }



    /*public void flipperImage(int image){
        ImageView img = new ImageView(this);
        img.setBackgroundResource(image);

        slider.addView(img);
        slider.setFlipInterval(1000);
        slider.setAutoStart(true);

        slider.setInAnimation(this, android.R.anim.slide_in_left);
        slider.setOutAnimation(this, android.R.anim.slide_out_right);
    }*/
}
