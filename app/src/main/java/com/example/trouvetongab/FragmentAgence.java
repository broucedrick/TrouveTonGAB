package com.example.trouvetongab;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableResource;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.fragment.app.FragmentManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentAgence extends Fragment implements  OnMapReadyCallback {

    private static final int MY_PERMISSIONS_LOCATION = 1;
    private GoogleMap mMap;
    View v;
    private List latLngList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    //ProgressDialog progressDialog = new ProgressDialog(getActivity());
   DialogFragment  progressDialogs = new DialogFragment();
    private  static String fine_location = Manifest.permission.ACCESS_FINE_LOCATION;
    private  static String coarse_location = Manifest.permission.ACCESS_COARSE_LOCATION;
    private  static final int requestcode_permission = 1234;
    private Boolean locationAccept = false;
   AlertDialog alertDialog;
    Context context;


    int bankid;

    public FragmentAgence(int id, Context context){
        this.bankid = id;
        this.context = context;
    }




    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_gab_map, container, false);

       /*String URL_AGENCE = "https://digitalfinances.innovstech.com/getAgence.php?id="+bankid;
        //Toast.makeText(ListGab.this, URL_GAB, Toast.LENGTH_LONG).show();
        StringRequest SRequest = new StringRequest(Request.Method.GET, URL_AGENCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.length() > 0){
                            }
                            //Toast.makeText(getActivity(), response.length(), Toast.LENGTH_SHORT).show();

                            JSONArray agence = new JSONArray(response);
                            for (int i = 0; i < agence.length(); i++) {
                                JSONObject b = agence.getJSONObject(i);
                                final String title = b.getString("title");
                                String location = b.getString("location");
                                Toast.makeText(getActivity(),title, Toast.LENGTH_SHORT).show();

                               for (String data : location.split("!")){
                                    if(data.contains("1s0")){
                                        String code = data.replace("1s", "");

                                        String URL_AGENCES = "https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyDnQuzadpPIOWJSUBgzQVKZ71ODTyADChc&ftid="+code;
                                        StringRequest SRequests = new StringRequest(Request.Method.GET, URL_AGENCES,
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
                                                            //Toast.makeText(getActivity(), bankid +" et "+ title +"  et  "+ lat +" et "+ lng, Toast.LENGTH_SHORT).show();

                                                            connection(getActivity(),bankid,title,lat,lng);
                                                            Log.e("nombre de boucle", String.valueOf(bankid) + title +lat +lng );





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
        requestQueuess.add(SRequest);*/




       // requestPermission();


        return v;
    }



    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {

        super.onActivityCreated(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        validate_location(getActivity());


    }

    public   boolean validate_location( Context context){
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
               // Toast.makeText(context, "essai de prendre l'etat ", Toast.LENGTH_LONG).show();
                if(locationMode != Settings.Secure.LOCATION_MODE_OFF){
                   // Toast.makeText(context, "geolocalisation deja activer ", Toast.LENGTH_LONG).show();

                }else{
                   // Toast.makeText(context, "activer la geolocalisation", Toast.LENGTH_LONG).show();
                    active();

                }

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
               // Toast.makeText(context, "essai de prendre l'etat na pas marcher  ", Toast.LENGTH_LONG).show();

                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;



        }else{
            Toast.makeText(context, "version pas ok ", Toast.LENGTH_LONG).show();

            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }

    }






    public void active(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage(R.string.gps_disabled_message)
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            Toast.makeText(getActivity(), "bien recut", Toast.LENGTH_LONG).show();

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                Toast.makeText(getActivity(), "permission accepter", Toast.LENGTH_LONG).show();

            } else {
                // User refused to grant permission. You can add AlertDialog here
                Toast.makeText(getActivity(), "permission non accepter", Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        mMap.animateCamera(CameraUpdateFactory.zoomTo((float) 5.909));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(R.layout.loading_dialog);
        alert.setCancelable(false);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();


         String URL_AGENCE = "http://digitalfinances.innovstech.com/getLatLng.php?id="+bankid;
        //Toast.makeText(ListGab.this, URL_GAB, Toast.LENGTH_LONG).show();
        StringRequest SRequest = new StringRequest(Request.Method.GET, URL_AGENCE,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.length() > 0){
                                JSONArray agence = new JSONArray(response);
                                for (int i = 0; i < agence.length(); i++) {
                                    JSONObject b = agence.getJSONObject(i);
                                    final String title = b.getString("title");
                                    String lng = b.getString("longitude");
                                    String lat = b.getString("latitude");

                                    Double Nlat = Double.parseDouble(lat);
                                    Double Nlng = Double.parseDouble(lng);

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
                                        Toast.makeText(getActivity(), "permission non occorder ", Toast.LENGTH_SHORT).show();

                                    }


                                    // for (String data : location.split("!")){
                                    //   if(data.contains("1s0")){
                                    //    String code = data.replace("1s", "");
                                      /*  String URL_AGENCE = "https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyDnQuzadpPIOWJSUBgzQVKZ71ODTyADChc&ftid="+code;
                                        StringRequest SRequests = new StringRequest(Request.Method.GET, URL_AGENCE,
                                                new Response.Listener<String>() {

                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            if(response.length() > 0){
                                                            }*/
/*
                                                            JSONObject obj = new JSONObject(response);
                                                            JSONObject obj_resl = obj.getJSONObject("result");
                                                            JSONObject obj_geo = obj_resl.getJSONObject("geometry");
                                                            JSONObject obj_location = obj_geo.getJSONObject("location");


                                                            String lat = obj_location.getString("lat");
                                                            String lng = obj_location.getString("lng");*/

                                    //Toast.makeText(getActivity(), title +"  et  "+ lat +" et "+ lng, Toast.LENGTH_SHORT).show();





                                     /*                   } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }*/


                                    //   }
                                    //     },
                        /*                        new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        //Toast.makeText(ListGab.this, "Connection Error... "+error.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });*/

/*
                                        RequestQueue requestQueues = Volley.newRequestQueue(getContext());
                                        requestQueues.add(SRequests);
*/

/*
                                    }else if (data.contains("1s0?")) {
                                        Toast.makeText(getActivity(), "veillez revoir le liens de geocalisation de l'agence", Toast.LENGTH_SHORT).show();

                                    }*/
                                    // }
                                }

                                alertDialog.dismiss();
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

    public static void connection(final Context context, final int id,final String title, final String lat, final String lng){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,"https://digitalfinances.innovstech.com/insertLatLng.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //  Toast.makeText(context,"connection bd login effectuer "+response,Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"verifier la connection internet ",Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("bank_id",String.valueOf(id));
                params.put("title",title);
                params.put("lat",lat);
                params.put("lng",lng);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("bank_id",String.valueOf(id));
                params.put("title",title);
                params.put("lat",lat);
                params.put("lng",lng);
                return params;
            }
        };
        //   Toast.makeText(context,usermail,Toast.LENGTH_LONG).show();

        queue.add(sr);
    }


}
