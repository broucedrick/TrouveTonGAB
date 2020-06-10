package com.example.trouvetongab;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
    private List<String> latLongs;

    private List<Gab> gabs = new ArrayList<>();

    LoadingDialog loadingDialog;
    Context context;

    View v;
    int bankid;


    public FragmentGab(int bankID, Context context) {
        this.bankid = bankID;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.gab_frag, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.gab_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        StaggeredGridLayoutManager mlayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mlayoutManager);

        int bank_id = bankid;

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(R.layout.loading_dialog);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();

        String URL_GAB = "https://digitalfinances.innovstech.com/getGab.php?id="+bank_id;
        //Toast.makeText(ListGab.this, URL_GAB, Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GAB,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(ListGab.this, "Connecté", Toast.LENGTH_LONG).show();
                        try {
                            if(response.length() > 0){

                                alertDialog.dismiss();
                                JSONArray gab = new JSONArray(response);
                                for (int i = 0; i < gab.length(); i++) {
                                    JSONObject b = gab.getJSONObject(i);

                                    String title = b.getString("title");
                                    String location = b.getString("location");
                                    String commune = b.getString("commune");
                                    int posted = b.getInt("posted");
                                    // Toast.makeText(getActivity(), title, Toast.LENGTH_SHORT).show();

                                    gabs.add(new Gab(title, location, posted, commune));

                                }

                                mAdapter = new GabListAdapter(getContext(), gabs);
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
                        //Toast.makeText(ListGab.this, "Connection Error... "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        searchBar = (EditText) v.findViewById(R.id.searchbar_gab);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
