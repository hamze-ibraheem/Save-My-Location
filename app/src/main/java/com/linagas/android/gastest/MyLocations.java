package com.linagas.android.gastest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hamze sarsour local on 12/2/2016.
 */

public class MyLocations extends AppCompatActivity {

    private TextView textData;
    private ListView mainListView;
    private ArrayAdapter<String> listAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        Bundle p = getIntent().getExtras();
        mainListView = (ListView) findViewById( R.id.dataListView );

        textData = (TextView) findViewById( R.id.rowTextView );

        ArrayList<String> List = new ArrayList<String>();

        listAdapter = new ArrayAdapter<String>(this, R.layout.data_list,R.id.rowTextView ,List);

        for (int i =0 ; i < p.size() ; i++) {
            listAdapter.add(p.getString("jsondata" + i));

        }
        mainListView.setAdapter( listAdapter );
    }
}
