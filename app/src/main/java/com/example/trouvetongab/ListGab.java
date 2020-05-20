package com.example.trouvetongab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.SearchView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListGab extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView recyclerView;
    private ListGabAdapter mAdapter;
    private EditText searchBar;

    private List<Gab> gb = new ArrayList<>();

    DrawerLayout drawerLayout;
    NavigationView drawerNavView;
    ActionBarDrawerToggle toggle;

    LoadingDialog loadingDialog;
    GoogleSignInClient mGoogleSignInClient;
    private String nom;
    private String email;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_gab);

        SharedPreferences mSharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);
        nom = mSharedPreferences.getString("nom","");
        email = mSharedPreferences.getString("email", "");

        loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();

        Bundle bundle = getIntent().getExtras();
        String bank_name = bundle.getString("bank_name");
        setTitle(bank_name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        drawerNavView = findViewById(R.id.drawerNavView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

      //  View headerView = drawerNavView.getHeaderView(0);

/*        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            //String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            //Toast.makeText(this, personPhoto.toString(), Toast.LENGTH_LONG).show();

            ImageView avatar = (ImageView) headerView.findViewById(R.id.avatar);
            TextView avatar_name = (TextView) headerView.findViewById(R.id.avatar_name);


            avatar_name.setText(personEmail);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.ic_launcher_background);
            requestOptions.error(R.drawable.ic_launcher_background);

            Glide.with(this).load(personPhoto)
                    .apply(requestOptions.circleCrop()).thumbnail(0.5f).into(avatar);
        }*/

        toggle = new ActionBarDrawerToggle(ListGab.this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        drawerNavView.setNavigationItemSelectedListener(this);

        searchBar = (EditText) findViewById(R.id.custom_search);
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

        recyclerView = (RecyclerView) findViewById(R.id.recyclerGab);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        StaggeredGridLayoutManager mlayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mlayoutManager);

        bundle = getIntent().getExtras();
        int bank_id = bundle.getInt("bank_id");

        String URL_GAB = "https://digitalfinances.innovstech.com/getGab.php?id="+bank_id;
        //Toast.makeText(ListGab.this, URL_GAB, Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GAB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(ListGab.this, "Connecté", Toast.LENGTH_LONG).show();
                        try {
                            if(response.length() > 0){
                                loadingDialog.dismissDialog();
                            }
                            JSONArray gab = new JSONArray(response);
                            //Toast.makeText(ListGab.this, gab.toString(), Toast.LENGTH_LONG).show();
                            for (int i = 0; i < gab.length(); i++) {
                                JSONObject b = gab.getJSONObject(i);

                                String title = b.getString("title");
                                String location = b.getString("location");
                                int posted = b.getInt("posted");

                                gb.add(new Gab(title, location, posted));

                            }
                            mAdapter = new ListGabAdapter(ListGab.this, gb);
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }
*/
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
                startActivity(new Intent(this, InfosActivity.class));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
    private void clearPrefData(){
        SharedPreferences mSharedPreferences = getSharedPreferences("User Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear().apply();
    }


}
