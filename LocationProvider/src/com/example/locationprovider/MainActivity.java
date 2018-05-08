package com.example.locationprovider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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


public class MainActivity extends ActionBarActivity {

	
	String pre[];
	ArrayList<String> arr ;
	int i=0;
	Button b,find;
	TextView tv;
	ListView vlist;
	//List<Address> addr=null;
	static final Uri CONTENT_URI = Uri.parse("content://net.learn2develop.provider.Locations/locations");
	ContentResolver resolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        
        resolver = getContentResolver();
        b = (Button)findViewById(R.id.bt);
        find = (Button) findViewById(R.id.btfind);
        arr = new ArrayList<String>();
        b.setOnClickListener(new View.OnClickListener() {
	     
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String[] projection = new String[]{"id","longitude","latitude","dates"};
				Cursor c = resolver.query(CONTENT_URI, projection, null, null, null);
				
				String data = "";
				if(c.moveToFirst())
				{
					do{
						String id = c.getString(c.getColumnIndex("id"));
						String log = c.getString(c.getColumnIndex("longitude"));
						String lat = c.getString(c.getColumnIndex("latitude"));
						String da = c.getString(c.getColumnIndex("dates"));
						double lo = Double.parseDouble(log);
						double la = Double.parseDouble(lat);
						String saddress = getCompleteAddressString(la, lo);
												
						data = "ID : "+id+"\n"+"\tLongitude : "+log +"\n"+"\tLatitude : "+lat+"\n"+"\tDates : "+da+"\n"+"\tAddress : "+saddress+"\n";
						arr.add(data);
					}while(c.moveToNext());
				}
				//Toast.makeText(getApplicationContext(),data.toString(), Toast.LENGTH_LONG).show();
				
				Intent  i = new Intent(MainActivity.this,MyListActivity.class);
				i.putStringArrayListExtra("Mydata",arr);
				startActivity(i);
				arr.clear();
			//	tv.append("\n"+data);
				//ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,arr);
		      //  vlist.setAdapter(adapter);
			}

		});
        
        find.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent dos = new Intent(MainActivity.this,SecondActivity.class);
				startActivity(dos);
			}
		});
	}

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Toast.makeText(getApplicationContext(),strAdd, Toast.LENGTH_LONG).show();
                Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
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
