package com.example.trouvetongab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentGab extends Fragment {

    private RecyclerView recyclerView;
    private GabListAdapter mAdapter;
    private EditText searchBar;

    private List<Gab> gabs = new ArrayList<>();

    LoadingDialog loadingDialog;

    View v;
    int bankid;

    public FragmentGab(int bankID) {
        this.bankid = bankID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.gab_frag, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.gab_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        StaggeredGridLayoutManager mlayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mlayoutManager);

        int bank_id = bankid;

        String URL_GAB = "https://digitalfinances.innovstech.com/getGab.php?id="+bank_id;
        //Toast.makeText(ListGab.this, URL_GAB, Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GAB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(ListGab.this, "Connecté", Toast.LENGTH_LONG).show();
                        try {
                            if(response.length() > 0){
                            }
                            JSONArray gab = new JSONArray(response);
                            //Toast.makeText(ListGab.this, gab.toString(), Toast.LENGTH_LONG).show();
                            for (int i = 0; i < gab.length(); i++) {
                                JSONObject b = gab.getJSONObject(i);

                                String title = b.getString("title");
                                String location = b.getString("location");
                                int posted = b.getInt("posted");

                                gabs.add(new Gab(title, location, posted));

                            }
                            mAdapter = new GabListAdapter(getContext(), gabs);
                            recyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ListGab.this, "Connection Error... "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);



        String URL_AGENCE = "https://digitalfinances.innovstech.com/getAgence.php?id="+bank_id;
        //Toast.makeText(ListGab.this, URL_GAB, Toast.LENGTH_LONG).show();
        StringRequest SRequest = new StringRequest(Request.Method.GET, URL_AGENCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.length() > 0){
                            }
                            JSONArray agence = new JSONArray(response);
                            for (int i = 0; i < agence.length(); i++) {
                                JSONObject b = agence.getJSONObject(i);
                                String title = b.getString("title");
                                String location = b.getString("location");
                                for (String data : location.split("!")){
                                    if(data.contains("1s0")){
                                        String code = data.replace("1s", "");
                                        //Toast.makeText(getActivity(), code, Toast.LENGTH_LONG).show();
                                        extract(code);
                                    }
                                }
                            }
                           // mAdapter = new GabListAdapter(getContext(), gabs);
                           // recyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ListGab.this, "Connection Error... "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueues = Volley.newRequestQueue(getContext());
        requestQueues.add(SRequest);
        return v;
    }
    public String extract(String code){
        Places.initialize(getApplicationContext(), "AIzaSyA1VRrkzXGFNAd1xh32eVePw3tgv9FKqJU");
        PlacesClient placesClient = Places.createClient(getActivity());
        //placesClient.fetchPlace()
        String URL_AGENCE = "https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyA1VRrkzXGFNAd1xh32eVePw3tgv9FKqJU&ftid="+code;
        //Toast.makeText(ListGab.this, URL_GAB, Toast.LENGTH_LONG).show();
        StringRequest SRequest = new StringRequest(Request.Method.GET, URL_AGENCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.length() > 0){
                            }
                            Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();

                            JSONArray mapLatLong = new JSONArray(response);
                            for (int i = 0; i < mapLatLong.length(); i++) {
                                JSONObject b = mapLatLong.getJSONObject(i);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(ListGab.this, "Connection Error... "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueues = Volley.newRequestQueue(getContext());
        requestQueues.add(SRequest);

        return code;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
