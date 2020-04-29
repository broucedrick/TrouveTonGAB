package com.example.trouvetongab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

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

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView drawerNavView;
    ActionBarDrawerToggle toggle;

    String request_url = "https://trouvetongab.000webhostapp.com/getImage.php";

    LoadingDialog loadingDialog;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();

        drawerLayout = findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        drawerNavView = findViewById(R.id.drawerNavView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        View headerView = drawerNavView.getHeaderView(0);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            //Toast.makeText(this, personPhoto.toString(), Toast.LENGTH_LONG).show();

            ImageView avatar = (ImageView) headerView.findViewById(R.id.avatar);
            TextView avatar_name = (TextView) headerView.findViewById(R.id.avatar_name);


            avatar_name.setText(personName);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.ic_launcher_background);
            requestOptions.error(R.drawable.ic_launcher_background);

            Glide.with(this).load(personPhoto)
                    .apply(requestOptions.circleCrop()).thumbnail(0.5f).into(avatar);
        }

        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        drawerNavView.setNavigationItemSelectedListener(this);

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
                        //Toast.makeText(MainActivity.this, "Connecté | "+response, Toast.LENGTH_LONG).show();
                        try {
                            if(response.length() > 0){
                                loadingDialog.dismissDialog();
                            }
                            JSONArray bank = new JSONArray(response);
                            //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
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
                            //Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this, "Erreur de connexion... "+error.getMessage(), Toast.LENGTH_SHORT).show();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                        loadingDialog.dismissDialog();

                        loadingDialog.startWarningDialog();
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
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i<response.length(); i++){
                    SliderUtils sliderUtils = new SliderUtils();
                    try {
                        if(response.length() > 0){
                            loadingDialog.dismissDialog();
                        }
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                loadingDialog.dismissDialog();

                loadingDialog.startWarningDialog();
            }
        });

        rq.add(jsonArrayRequest);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.apropos:
                //Toast.makeText(this, "A propos", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Apropos.class));
                break;
            case R.id.dnx:
                Toast.makeText(this, "Deconnexion", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, login.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public void checkConnection(){
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if(null != activeNetwork){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
                Toast.makeText(this, "WIFI ENABLE", Toast.LENGTH_SHORT).show();
            }
            else if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                Toast.makeText(this, "DATA NETWORK ENABLE", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }
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
