package com.example.locationserviceex;

import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBarActivity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;


public class MainActivity extends ActionBarActivity implements OnClickListener {

	String log,lat;
	TextView tvCo;
	Button btnstrt,btnstop,del;
	BroadcastReceiver rcv;
	String dateb;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(rcv==null){
			rcv = new BroadcastReceiver(){

				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO Auto-generated method stub
					//tvCo.append("\n"+intent.getExtras().get("longitude"));
					//String data = intent.getExtras().get(key);
					dateb = String.valueOf(intent.getExtras().get("date"));
					log = String.valueOf(intent.getExtras().get("longitude"));
					lat =  String.valueOf(intent.getExtras().get("latitude"));
					tvCo.append("\n"+log+","+lat);
					
					save();
				}

				private void save() {
					// TODO Auto-generated method stub

					ContentValues values = new ContentValues();
					values.put(Location_Provider.LONGITUDE,log);
					values.put(Location_Provider.LATITUDE,lat);
					values.put(Location_Provider.DATES,dateb);
					Uri uri = getContentResolver().insert(Location_Provider.Content_uri,values);
					
					Toast.makeText(getBaseContext(),"Content "+uri.toString()+" added",Toast.LENGTH_LONG).show();
				}
			
		};
		}
		registerReceiver(rcv,new IntentFilter("Location_update"));
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();
    }

    private void init() 
    {
		tvCo = (TextView)findViewById(R.id.tdisCo);
		btnstrt = (Button) findViewById(R.id.btstart);
		btnstop = (Button) findViewById(R.id.btstop);
		btnstrt.setOnClickListener(this);
		btnstop.setOnClickListener(this);
		del = (Button) findViewById(R.id.btdel);
		del.setOnClickListener(this);
		
	}


	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btstart:
				Intent i = new Intent(getApplicationContext(),GPS_Service.class);
				startService(i);
				break;
	
			case R.id.btstop:
				Intent i1 = new Intent(getApplicationContext(),GPS_Service.class);
				stopService(i1);
				show();
				break;
		
			case R.id.btdel :
				  int c = getContentResolver().delete(
			                Uri.parse("content://net.learn2develop.provider.Locations/locations"),null, null);
				  if(c>0)
				  {
					  Toast.makeText(getApplicationContext(),"Deleted Successfully.",Toast.LENGTH_SHORT).show();
				  }else
				  {
					  Toast.makeText(getApplicationContext(),"Deletion Failed.",Toast.LENGTH_SHORT).show();
				  }
				  break;
		}
		
	}
    
    
	private void show() 
	{
		Uri allTitles = Uri.parse("content://net.learn2develop.provider.Locations/locations");
		   CursorLoader cursorLoader = new CursorLoader(getApplicationContext(),allTitles, null, null, null,null);
         Cursor c = cursorLoader.loadInBackground();
         
         if(c.moveToFirst())
         {
         	do{
         		StringBuffer buffer = new StringBuffer();
         		buffer.append("LocationProviders"+" : ");
         		buffer.append(c.getString(c.getColumnIndex(Location_Provider.ID))+"= ");
         		buffer.append(c.getString(c.getColumnIndex(Location_Provider.LONGITUDE))+", ");
         		buffer.append(c.getString(c.getColumnIndex(Location_Provider.LATITUDE))+"\n");
         		//Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
         		
         	}while(c.moveToNext());
         }
		
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
