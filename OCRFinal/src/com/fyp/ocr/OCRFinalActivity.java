package com.fyp.ocr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.math.*;


import com.googlecode.tesseract.android.TessBaseAPI;
import com.javacalculus.core.CalculusEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;


import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class OCRFinalActivity extends Activity {
    /** Called when the activity is first created. */
	public static final String DATA_PATH = Environment
 	.getExternalStorageDirectory().toString() + "/OCR/";
	//String _path = DATA_PATH + "/ocr.jpg";
	String path = DATA_PATH + "/mcos.png";
	Double d;
	int m;
	Boolean signal=true;
	
	final static int cameradata=0;
	Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	String rt,str1,stri,strf,s,s1,strr;
	 public static final String lang = "eng";
	 private static final String TAG ="OCR.java";
	 CalculusEngine cal=new CalculusEngine();	
	
    @Override
    
    public void onCreate(Bundle savedInstanceState) {
    	String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
					return;
				} else {
					Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}

		}
		

		
		
		// lang.traineddata file with the app (in assets folder)
		// You can get them at:
		// http://code.google.com/p/tesseract-ocr/downloads/list
		// This area needs work and optimization
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
			try {

				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/eng.traineddata");
				//GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(DATA_PATH
						+ "tessdata/eng.traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				//while ((lenf = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				//gin.close();
				out.close();
				
				Log.v(TAG, "Copied " + lang + " traineddata");
			} catch (IOException e) {
				Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
			}
		}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button b1,b2;
        
       
	
        b1=(Button) findViewById(R.id.camera);
        b2=(Button) findViewById(R.id.gallery);
        
       
       
        b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				startCameraActivity();
			}
		});
        b2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
        
    }
    public void startCameraActivity(){
    	
    	File file = new File(path);
		Uri outputFileUri = Uri.fromFile(file);

		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		
		startActivityForResult(intent, 0);
		
		
        
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		
		if(resultCode==RESULT_OK)
		{
		setContentView(R.layout.postcam);
		initialize();
		}
		
		
	}
	public void initialize(){
		 Button pocr,pcal,ret;
		 
		 ImageView iv;
		 final TextView display;
		 pocr=(Button) findViewById(R.id.button1);
		 pcal=(Button) findViewById(R.id.cal);
		 ret=(Button) findViewById(R.id.retry);
		 display=(TextView) findViewById(R.id.textView1);
		 iv=(ImageView) findViewById(R.id.imageView1);
		
		 BitmapFactory.Options options = new BitmapFactory.Options();
		 options.inSampleSize = 4;
		 //final Bitmap bmp=BitmapFactory.decodeFile(path,options);
		 final Bitmap bmp1=BitmapFactory.decodeResource(getResources(), R.drawable.mcos);
		 
		 iv.setImageBitmap(bmp1);
		 
		
		 pocr.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v("Tag","After OCR");
				rt=ocr(bmp1);
				display.setText(rt);
			}
		});
		 
		 pcal.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				Calculate(rt);
				
				
			}
		});
		 ret.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startCameraActivity();
			}
		});
		 
		 
		 
	}
	public void Calculate(final String str)
	{
		setContentView(R.layout.calculate);
		
		
			
		
	
		
		
		
		Button np,b,in,diff;
		final TextView r;
		np=(Button) findViewById (R.id.newpic);
		b=(Button) findViewById (R.id.basic);
		in=(Button) findViewById (R.id.in);
		diff=(Button) findViewById (R.id.diff);
		r=(TextView) findViewById (R.id.result);
		Log.v("Tag","Calculate");
		
		
		
		
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String value;
				String recognizedText =str;
				Log.v("Tag","Calculate2");
				String s2=recognizedText.substring(0, 3);
				Log.v("Tag","Calculate3");
				
				
				
				 
				if(s2.equalsIgnoreCase("sin"))
				{
					value=recognizedText.substring(3);
				  d = Double.valueOf(value.trim()).doubleValue();
				double ans= Math.sin(d);
				 String s=Double.toString(ans);
				 r.setText(s);
				 Log.v(TAG, "O1: " + s);
				 signal=false;
			
				}
			
				else if(s2.equalsIgnoreCase("cos"))
				{
					value=recognizedText.substring(3);
				  d = Double.valueOf(value.trim()).doubleValue();
				double ans= Math.cos(d);
				 String s=Double.toString(ans);
				 r.setText(s);
				 Log.v(TAG, "O1: " + s);
				 signal=false;
			
				}
				else if(s2.equalsIgnoreCase("log"))
				{
					value=recognizedText.substring(3);
				  d = Double.valueOf(value.trim()).doubleValue();
				double ans= Math.log(d);
				 String s=Double.toString(ans);
				 r.setText(s);
				 Log.v(TAG, "O1: " + s);
				 signal=false;
			
				}
				else if(s2.equalsIgnoreCase("abs"))
				{
					value=recognizedText.substring(3);
				  d = Double.valueOf(value.trim()).doubleValue();
				double ans= Math.abs(d);
				 String s=Double.toString(ans);
				 r.setText(s);
				 Log.v(TAG, "O1: " + s);
				 signal=false;
			
				}
				else if(s2.equalsIgnoreCase("sqr"))
				{
					value=recognizedText.substring(3);
				  d = Double.valueOf(value.trim()).doubleValue();
				double ans= Math.sqrt(d);
				 String s=Double.toString(ans);
				 r.setText(s);
				 Log.v(TAG, "O1: " + s);
				 signal=false;
			
				}
				else if(s2.equalsIgnoreCase("exp"))
				{
					value=recognizedText.substring(3);
				  d = Double.valueOf(value.trim()).doubleValue();
				double ans= Math.exp(d);
				 String s=Double.toString(ans);
				 r.setText(s);
				 Log.v(TAG, "O1: " + s);
				 signal=false;
			
				}
				else if(signal==true) {
					 Log.v("Tag","Calculate1");
			 Expression e=new Expression();
			 Log.v("Tag","Calculate2");
			  int res=e.calc(recognizedText);
			  Log.v(TAG, "Ot: " + r);
			  String s=Integer.toString(res);
			  r.setText(s);
			 
			}
				
			}
		});
		
		in.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(str.length()>1){
					Log.v(TAG, "Inside if loop");
					if(str.contains("COS") || str.contains("SIN")|| str.contains("TAN"))
					{
						if(str.contains("COS")){
							 m=str.indexOf("S");
						}
						else{
							 m=str.indexOf("N");
						}
						
						str1=str.substring(0,m+1);
						Log.v(TAG, str1);
						stri=str.substring(m+1);
						stri=stri.toLowerCase();
						Log.v(TAG, stri);
						 
						  strf=""+str1+"("+stri+")";
						  Log.v(TAG, strf);
						  s1="INT("+strf+",x)";
						  Log.v(TAG, s1);
						  s=cal.execute(s1);
							
							r.setText(s);
						Log.v(TAG, "Done");
						
					}
				
			    }
				else
				{
				Log.v(TAG, "No Trig");
				strf=str.toLowerCase();
				s1="INT("+strf+",x)";
				s=cal.execute(s1);
				r.setText(s);
				Log.v(TAG, "Done 1");
				}
				
			
				
		 }
		});
		
		
		diff.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(str.length()>1){
					Log.v(TAG, "Inside if loop");
					if(str.contains("COS") || str.contains("SIN")|| str.contains("TAN"))
					{
						if(str.contains("COS")){
							 m=str.indexOf("S");
						}
						else{
							 m=str.indexOf("N");
						}
						
						str1=str.substring(0,m+1);
						Log.v(TAG, str1);
						stri=str.substring(m+1);
						stri=stri.toLowerCase();
						Log.v(TAG, stri);
						 
						  strf=""+str1+"("+stri+")";
						  Log.v(TAG, strf);
						  s1="DIFF("+strf+",x)";
						  Log.v(TAG, s1);
						  s=cal.execute(s1);
							
							r.setText(s);
						Log.v(TAG, "Done");
						
					}
				
			    }
				else
				{
				Log.v(TAG, "No Trig");
				strf=str.toLowerCase();
				s1="DIFF("+strf+",x)";
				s=cal.execute(s1);
				r.setText(s);
				Log.v(TAG, "Done 1");
				}
				
			}
		});
		
	
		
	
		
		
		
	
		np.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startCameraActivity();
			}
		});
		
		
		
		
	}
	public String ocr(Bitmap bitmap)
	{
		try {
			ExifInterface exif = new ExifInterface(path);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

		

			int rotate = 0;

			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}

			

			if (rotate != 0) {

				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();

				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);

				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);

				// Convert to ARGB_8888, required by tess
				bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
			}

		} catch (IOException e) {
			
		}

		// _image.setImageBitmap( bitmap );
		
		Log.v(TAG, "Before baseApi");

		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();

		// You now have the text in recognizedText var, you can do anything with it.
		// We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
		// so that garbage doesn't make it to the display.

		

		
		Log.v(TAG, "OCRED TEXT: " + recognizedText);
		//if ( lang.equalsIgnoreCase("eng") ) {
			//recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9+*/]", " ");
		//}
		
		
		
		return recognizedText;
		

		
		}
	}
    
	
	
   
    
