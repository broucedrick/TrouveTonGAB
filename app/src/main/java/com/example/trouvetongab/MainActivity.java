package com.example.trouvetongab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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

    String request_url = "https://digitalfinances.innovstech.com/getImage.php";

    LoadingDialog loadingDialog;


    public String nom;
    public String email;
    final static int REQUEST_LOCATION = 199;


    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    private  static String fine_location = Manifest.permission.ACCESS_FINE_LOCATION;
    private  static String coarse_location = Manifest.permission.ACCESS_COARSE_LOCATION;
    private  static final int requestcode_permission = 1234;

    private Boolean locationAccept = false;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*loadingDialog = new LoadingDialog(this);
        loadingDialog.dismissDialog();*/


        SharedPreferences mSharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);
        nom = mSharedPreferences.getString("nom","");
        email = mSharedPreferences.getString("email", "");
        if(nom.length() <= 0){

            startActivity(new Intent(this, login.class));

        }else{


            requestPermission();
            setContentView(R.layout.activity_main);

            loadingDialog = new LoadingDialog(this);
            loadingDialog.startLoadingDialog();

            drawerLayout = findViewById(R.id.drawer);
            toolbar = (Toolbar) findViewById(R.id.toolbar1);
            drawerNavView = findViewById(R.id.drawerNavView);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

/*
            View headerView = drawerNavView.getHeaderView(0);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
               // Toast.makeText(this, "voici l'email : "+personEmail, Toast.LENGTH_LONG).show();

                ImageView avatar = (ImageView) headerView.findViewById(R.id.avatar);
                TextView avatar_name = (TextView) headerView.findViewById(R.id.avatar_name);


                avatar_name.setText(personName);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.mipmap.avatar_default);
                requestOptions.error(R.mipmap.avatar_default);

                Glide.with(this).load(personPhoto)
                        .apply(requestOptions.circleCrop()).thumbnail(0.5f).into(avatar);
            }else{
                Bundle bundle = getIntent().getExtras();
                String fb_id = bundle.getString("id");
                String fb_name = bundle.getString("name");
                String fb = bundle.getString("fb");


                // Toast.makeText(MainActivity.this, fb, Toast.LENGTH_LONG).show();


                // if(fb.equals("fb") || name_ggle.equals("ggle") ){
                ImageView avatar = (ImageView) headerView.findViewById(R.id.avatar);
                TextView avatar_name = (TextView) headerView.findViewById(R.id.avatar_name);


                avatar_name.setText(fb_name);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.mipmap.avatar_default);
                requestOptions.error(R.mipmap.avatar_default);

                Glide.with(this).load("https://graph.facebook.com/" + fb_id+ "/picture?type=large")
                        .apply(requestOptions.circleCrop()).thumbnail(0.5f).into(avatar);
            }

*/




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
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this,gso);


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


            String URL_BANQUES = "https://digitalfinances.innovstech.com/getBanque.php";
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

                            loadingDialog.dismissDialog();

                            loadingDialog.startWarningDialog();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);



            // specify an adapter (see also next example)


            // getlocation();


        }


    }
    public void requestPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {

// Permission is not granted
// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
               // Toast.makeText(MainActivity.this, "permission deja demande + ", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 1);
              //  Toast.makeText(MainActivity.this, "demande de la permission  ", Toast.LENGTH_LONG).show();

                // MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
         //   Toast.makeText(MainActivity.this, "permission deja activer ", Toast.LENGTH_LONG).show();

// Permission has already been granted
        }
    }



@Override
public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 1) {
       // Toast.makeText(MainActivity.this, "bien recut", Toast.LENGTH_LONG).show();

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted.
            Toast.makeText(MainActivity.this, "permission accepter", Toast.LENGTH_LONG).show();

        } else {
            // User refused to grant permission. You can add AlertDialog here
            requestPermission();
            Toast.makeText(MainActivity.this, "veillez accepter avant de continuer", Toast.LENGTH_LONG).show();
        }
    }
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
        else{
         //  startActivity(new Intent(this, MainActivity.class));
            super.onBackPressed();
            this.finishAffinity();
           // this.finish();
            //startActivity(new Intent(this, MainActivity.class));
            //this.onDestroy();


        }
    }
    public void logout_fb() {
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
            startActivity(new Intent(this, login.class));
           // Toast.makeText(getApplicationContext(),"facebook deconnecter",Toast.LENGTH_LONG).show();
        }
    }

    public void logout_gl() {

       mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              //  Toast.makeText(MainActivity.this, "google deconnecter ... ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
            }
        });

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
                logout_fb();
                logout_gl();
                clearPrefData();
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
    private void clearPrefData(){
        SharedPreferences mSharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear().apply();
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
