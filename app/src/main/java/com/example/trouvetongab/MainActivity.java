package com.example.trouvetongab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<String> myDataset = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerBank);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        StaggeredGridLayoutManager mlayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mlayoutManager);

        myDataset.add("Banque 1");
        myDataset.add("Banque 2");
        myDataset.add("Banque 3");
        myDataset.add("Banque 4");
        myDataset.add("Banque 5");
        myDataset.add("Banque 6");
        myDataset.add("Banque 7");
        myDataset.add("Banque 8");
        myDataset.add("Banque 9");
        myDataset.add("Banque 10");

        // specify an adapter (see also next example)
        mAdapter = new ListAdapter(this, myDataset);
        recyclerView.setAdapter(mAdapter);


    }
}
