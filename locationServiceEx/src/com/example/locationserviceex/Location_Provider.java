package com.example.locationserviceex;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class Location_Provider extends ContentProvider
{
	public static final String Provider_name = "net.learn2develop.provider.Locations";
	public static final Uri Content_uri = Uri.parse("content://"+ Provider_name + "/locations");
	
	public static final String ID = "id";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String DATES = "dates";
	
	private static final int LOCATIONS = 1;
	private static final int LOCATIONS_ID = 2;
	
	private static final UriMatcher uriMatcher;
	   static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Provider_name, "locations", LOCATIONS);
		uriMatcher.addURI(Provider_name, "locations/#", LOCATIONS_ID);
		}
	
	   private SQLiteDatabase uDB;
	   
	   private static final String DATABASE_NAME = "LocationsData1";
	   private static final String DATABASE_TABLE = "LocationData";
	   private static final int DATABASE_VERSION = 1;
	   
	   private static final String DATABASE_CREATE =
				  "create table " + DATABASE_TABLE +
				   " (id integer primary key autoincrement," 
				   + "longitude text not null, latitude text not null,dates text not null);";
	   
	   private static class DatabaseHelper extends SQLiteOpenHelper
	   {

		public DatabaseHelper(Context context) {
			super(context,DATABASE_NAME,null,DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w("Content provider database",
					"Upgrading database from version " +
					oldVersion + " to " + newVersion +
					", which will destroy all old data");
					db.execSQL("DROP TABLE IF EXISTS titles");
					onCreate(db);
		}
		   
	   }
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		Context context = getContext();
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		uDB = dbHelper.getWritableDatabase();
		return (uDB == null)? false:true;
	//	return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(DATABASE_TABLE);
		if (uriMatcher.match(uri) == LOCATIONS_ID)
			sqlBuilder.appendWhere(ID + " = " + uri.getPathSegments().get(1));
		
		/*if (sortOrder==null || sortOrder=="")
			sortOrder = LONGITUDE;*/
		
		Cursor c = sqlBuilder.query(uDB,projection,selection,selectionArgs,null,null,null);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)){
		case LOCATIONS:
				return "vnd.android.cursor.dir/vnd.learn2develop.locations ";
		case LOCATIONS_ID:
				return "vnd.android.cursor.item/vnd.learn2develop.locations ";
		default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
        long rowID = uDB.insert(DATABASE_TABLE,"",values);
		
		if (rowID>0)
		{
		Uri _uri = ContentUris.withAppendedId(Content_uri, rowID);
		getContext().getContentResolver().notifyChange(_uri, null);
		return _uri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		int count=0;
		switch (uriMatcher.match(uri))
		{
			case LOCATIONS:
				count = uDB.delete(DATABASE_TABLE,arg1,arg2);
				break;
			case LOCATIONS_ID :
				String id = uri.getPathSegments().get(1);
				count = uDB.delete(DATABASE_TABLE,ID + " = " + id +(!TextUtils.isEmpty(arg1) ? " AND (" +arg1 + ')' : ""),arg2);
			    break;
			default: throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count = 0;
		switch (uriMatcher.match(uri)){
		case LOCATIONS:
				count = uDB.update(DATABASE_TABLE,values,selection,selectionArgs);
				break;
		case LOCATIONS_ID:
			count = uDB.update(DATABASE_TABLE,values,ID + " = " + uri.getPathSegments().get(1) +(!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""),selectionArgs);
			break;
			default: throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
