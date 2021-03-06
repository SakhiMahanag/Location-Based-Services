package com.example.locationprovider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.R.anim;
import android.support.v7.app.ActionBarActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;


public class MyListActivity extends ActionBarActivity {

	ListView lisv;
	ArrayList<String> arr;
	ArrayAdapter<String> adapter;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);
        
        init();
    }

    private void init() 
    {
	  arr = new ArrayList<String>();
	  arr = getIntent().getStringArrayListExtra("Mydata");
	  lisv = (ListView)findViewById(R.id.list1);
	  adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.text_main, arr);
	  lisv.setAdapter(adapter);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
