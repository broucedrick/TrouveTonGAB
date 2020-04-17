package com.example.trouvetongab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Bank> bk = new ArrayList<>();

    ViewFlipper slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int image[] = {R.mipmap.afriland_first_fank_foreground, R.mipmap.allianz_foreground, R.mipmap.axa_foreground};

        slider = (ViewFlipper) findViewById(R.id.slider); // get the reference of ViewFlipper

        for(int imge : image){
            flipperImage(imge);
        }


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

    public void flipperImage(int image){
        ImageView img = new ImageView(this);
        img.setBackgroundResource(image);

        slider.addView(img);
        slider.setFlipInterval(1000);
        slider.setAutoStart(true);

        slider.setInAnimation(this, android.R.anim.slide_in_left);
        slider.setOutAnimation(this, android.R.anim.slide_out_right);
    }
}
