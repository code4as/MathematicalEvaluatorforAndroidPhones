package com.fyp.ocr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;



public class Database {
	public static final String KEY_ROWID="_id";
	public static final String KEY_CHARACTER="character";
	public static final String KEY_IMAGE="img";
	public static final String TAG="Application";
	
	private static final String DATABASE_NAME="imgdb1";
	private static final String DATABASE_TABLE="imgtable";
	//private static final String DATABASE_TABLE1="imgtable1";
	private static final int DATABASE_VERSION=1;
	
	private static DBhelper ourHelper;
	private static Context ourContext;
	private static SQLiteDatabase ourdb;
	Bitmap bmp1;
	
	private static class DBhelper extends SQLiteOpenHelper{

		public DBhelper(Context context) {
			super(context,DATABASE_NAME,null,DATABASE_VERSION);
			// TODO Auto-generated constructor stub
			Log.v(TAG, "DB Helper called");
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			Log.v(TAG, "Creating DB");
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " ( " +
					KEY_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_CHARACTER +" TEXT NOT NULL, "+
					KEY_IMAGE +" BLOB);"
					);
			Log.v(TAG, "Database Created");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.v(TAG,"Deleting");
			db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE);
			onCreate(db);
		}
		
	}
	public Database (Context c){
		ourContext=c;
	}
	public Database open() throws SQLException{
       Log.v(TAG,"In Open");
		
		ourHelper= new DBhelper(ourContext);
		
		Log.v("App","Before get writable db");
		
		ourdb=ourHelper.getWritableDatabase();
	    
		return this;
	}
   public void close(){
	   ourHelper.close();
	   
   }
public long createEntry(String s, Bitmap bmp) {
	// TODO Auto-generated method stub
	Log.v(TAG,"In Create Entry");
	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	
    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
    byte[] byteArray = stream.toByteArray();
   
    Log.v(TAG,"Taking Values");
	ContentValues cv=new ContentValues();
	
	cv.put(KEY_CHARACTER,s);
	cv.put(KEY_IMAGE, byteArray);
	Log.v(TAG,"After Taking values");
	
	return ourdb.insert(DATABASE_TABLE, null,cv);
}
public String getdata() {
	// TODO Auto-generated method stub
	Log.v(TAG,"Getting Data");
	String[] columns = new String[]{KEY_ROWID,KEY_CHARACTER};
	Cursor c=ourdb.query(DATABASE_TABLE, columns, null,null,null,null, null);
	String result="";
	
	int iname=c.getColumnIndex(KEY_CHARACTER);
			
	for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
		result= result+c.getString(iname)+"\n";
	}
	Log.v(TAG,"After Getting Data");
	return result;
	
}
@SuppressWarnings("null")
public Bitmap[] getimage() {
	// TODO Auto-generated method stub
	Log.v(TAG, "Getting Image");
	
	byte [] img = null;
	int i=0;
	Bitmap[] res = null;
	Bitmap r;
	
	Log.v(TAG, "Creating Cursor");
	Cursor c=ourdb.query(DATABASE_TABLE, null, null,null, null, null,null);

	Log.v(TAG, "Query Returned");
	int iimg=c.getColumnIndex(KEY_IMAGE);
	for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
		img=c.getBlob(iimg);
		ByteArrayInputStream istream=new ByteArrayInputStream(img);
		Log.v(TAG, "Recieved Byte Array");
		r=BitmapFactory.decodeStream(istream);
		res[i]=r;
		Log.v(TAG, "Converted to Bmp");
		i++;
		
	}
	
	 bmp1 = res[i];
	
	Log.v(TAG, "Returning Image");
	return res;
	
   
}
public String compare(Bitmap bmp) {
	Boolean ans=imagesAreEqual(bmp,bmp1);
	String s = null;
	if (ans)
	{
		Cursor c=ourdb.query(DATABASE_TABLE, null, null,null, null, null,null);
        int n=c.getColumnIndex(KEY_IMAGE);
        s=c.getString(n);	}
	
	return s;
	// TODO Auto-generated method stub
	
}
Boolean imagesAreEqual(Bitmap i1, Bitmap i2)
{
    if (i1.getHeight() != i2.getHeight()) return false;
    if (i1.getWidth() != i2.getWidth()) return false;

    for (int y = 0; y < i1.getHeight(); ++y)
       for (int x = 0; x < i1.getWidth(); ++x)
            if (i1.getPixel(x, y) != i2.getPixel(x, y)) return false;

    return true;
}
}