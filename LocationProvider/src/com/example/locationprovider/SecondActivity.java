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


public class SecondActivity extends ActionBarActivity {
	
	static final Uri CONTENT_URI = Uri.parse("content://net.learn2develop.provider.Locations/locations");
	ContentResolver resolver;
	Button b;
	EditText tvb;
	String d;
	ArrayList<String> uparr;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_main); 
        init();
    }
    private void init() 
    {
      uparr = new ArrayList<String>();
      resolver = getContentResolver();
	  b = (Button)findViewById(R.id.fin);
	  tvb = (EditText)findViewById(R.id.fintxt);
	  b.setOnClickListener(new View.OnClickListener() {
	  
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			d = tvb.getText().toString();
			
			String[] projection = new String[]{"id","longitude","latitude","dates"};
	    	
		    Cursor c = resolver.query(CONTENT_URI, projection,"dates = ? ",new String[]{d}, null);
			if(c!=null){
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
					uparr.add(data);
				}while(c.moveToNext());
			}
			//Toast.makeText(getApplicationContext(),data.toString(), Toast.LENGTH_LONG).show();
			Intent  i = new Intent(SecondActivity.this,MyListActivity.class);
			i.putStringArrayListExtra("Mydata",uparr);
			startActivity(i);
			}else
			{
				Toast.makeText(getApplicationContext(),"No Serach Results Found",Toast.LENGTH_SHORT).show();
			}
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
