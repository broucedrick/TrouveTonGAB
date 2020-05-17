package com.example.trouvetongab;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.fragment.app.FragmentManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentAgence extends Fragment implements  OnMapReadyCallback {

    private GoogleMap mMap;
    View v;
    private List latLngList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    //ProgressDialog progressDialog = new ProgressDialog(getActivity());
   DialogFragment  progressDialogs = new DialogFragment();


    int bankid;

    public FragmentAgence(int id){
        this.bankid = id;
    }




    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_gab_map, container, false);



        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        return v;
    }

    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {

        super.onActivityCreated(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

       mMap.animateCamera(CameraUpdateFactory.zoomTo((float) 5.909));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        String URL_AGENCE = "https://digitalfinances.innovstech.com/getAgence.php?id="+bankid;
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
                                final String title = b.getString("title");
                                String location = b.getString("location");
                                for (String data : location.split("!")){
                                    if(data.contains("1s0")){
                                        String code = data.replace("1s", "");
                                        String URL_AGENCE = "https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyDnQuzadpPIOWJSUBgzQVKZ71ODTyADChc&ftid="+code;
                                        StringRequest SRequests = new StringRequest(Request.Method.GET, URL_AGENCE,
                                                new Response.Listener<String>() {

                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            if(response.length() > 0){
                                                            }

                                                            JSONObject obj = new JSONObject(response);
                                                            JSONObject obj_resl = obj.getJSONObject("result");
                                                            JSONObject obj_geo = obj_resl.getJSONObject("geometry");
                                                            JSONObject obj_location = obj_geo.getJSONObject("location");


                                                            String lat = obj_location.getString("lat");
                                                            String lng = obj_location.getString("lng");
                                                            Double Nlat = Double.parseDouble(lat);
                                                            Double Nlng = Double.parseDouble(lng);
                                                            Toast.makeText(getActivity(), title +"  et  "+ lat +" et "+ lng, Toast.LENGTH_SHORT).show();


                                                            if (ContextCompat.checkSelfPermission(getActivity(),
                                                                    Manifest.permission.ACCESS_FINE_LOCATION)
                                                                    == PackageManager.PERMISSION_GRANTED) {



                                                                    LatLng cord = new LatLng(Nlat,Nlng);
                                                                    mMap.addMarker(new MarkerOptions().position(cord).title(title));
                                                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(cord));
                                                                    mMap.setMyLocationEnabled(true);

                                                            }else if (ContextCompat.checkSelfPermission(getActivity(),
                                                                    Manifest.permission.ACCESS_COARSE_LOCATION)
                                                                    == PackageManager.PERMISSION_GRANTED){


                                                                LatLng cord = new LatLng(Nlat,Nlng);
                                                                mMap.addMarker(new MarkerOptions().position(cord).title(title));
                                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(cord));
                                                                mMap.setMyLocationEnabled(true);
                                                            }else{
                                                                Toast.makeText(getActivity(), "impossible de verifier ma position1", Toast.LENGTH_LONG).show();

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
                                        requestQueues.add(SRequests);


                                    }else if (data.contains("1s0?")) {
                                        Toast.makeText(getActivity(), "veillez revoir le liens de geocalisation de l'agence", Toast.LENGTH_SHORT).show();

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

        RequestQueue requestQueuess = Volley.newRequestQueue(getContext());
        requestQueuess.add(SRequest);

    }

}
