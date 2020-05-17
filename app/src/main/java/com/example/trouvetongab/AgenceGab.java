package com.example.trouvetongab;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class AgenceGab extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerFragAdapter adapter;
    DialogFragment progressDialogs = new DialogFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agence_gab);

        Bundle bundle = getIntent().getExtras();
        String bank_name = bundle.getString("bank_id");
        setTitle(bank_name);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        setTitle(bank_name);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new ViewPagerFragAdapter(getSupportFragmentManager(), bundle);

       // loadingDialog.startLoadingDialog();
       // progressDialog.show();
        adapter.AddFragment(new FragmentGab(bundle.getInt("bank_id")), "GAB/DAB");
        adapter.AddFragment(new FragmentAgence(bundle.getInt("bank_id")) , "Agences");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}