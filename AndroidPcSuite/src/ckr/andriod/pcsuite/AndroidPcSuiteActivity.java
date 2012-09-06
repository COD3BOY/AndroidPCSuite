package ckr.andriod.pcsuite;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothSocket;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;
import android.util.Log;



public class AndroidPcSuiteActivity extends Activity {
	
	 @Override
	    protected void onStop() {
	    	super.onStop();
	    }

	private BluetoothSocket socket;
	protected static int HELLO_ID = 1;
	class ForThread extends Thread {

		BluetoothSocket mSocket;

		ForThread(BluetoothSocket mSocket) {
			this.mSocket = mSocket;
		}

		public void receiveFile(File file,byte[] testarr) throws IOException {
			 try
		     {
		      FileOutputStream fos = new FileOutputStream(file);
		     
		       fos.write(testarr);
		       fos.close();
		     
		     }
		   catch(IOException ioe)
		     {
		      System.out.println("IOException : " + ioe);
		     }
		  }
		
		
		public void setAlarmNotification(int day, int month, int year,
				int hourOfTheDay, int minute, String message) {
			Calendar cal = Calendar.getInstance();

			cal.set(Calendar.MONTH, month - 1);
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.DAY_OF_MONTH, day);
			cal.set(Calendar.HOUR_OF_DAY, hourOfTheDay);
			cal.set(Calendar.MINUTE, minute);

			Intent alarmintent = new Intent(getApplicationContext(),
					AlarmReceiver.class);
			alarmintent.putExtra("title", "Remainder");
			alarmintent.putExtra("note", message);

			PendingIntent sender = PendingIntent.getBroadcast(
					getApplicationContext(), HELLO_ID, alarmintent,
					PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA);

			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);

			Log.i("ALARM SET!", "Alarm Set!");
		}
		
		public void run() {
			while (true) {

				try {
					byte[] testarray;
					int k;	
					if((k=socket.getInputStream().available())>0){
						testarray = new byte[k];
						
						mSocket.getInputStream().read(testarray);
						String mStr = new String(testarray);
						if (mStr.equalsIgnoreCase("contacts")) {

							ContentResolver cr = getContentResolver();
							Cursor cur = cr.query(
									ContactsContract.Contacts.CONTENT_URI,
									null, null, null, null);

							int count = cur.getCount();
							String[] mNames = new String[count];
							String[] mNumbers = new String[count];
							int ctr = 0;
							if (cur.getCount() > 0) {
								while (cur.moveToNext()) {
									String id = cur
											.getString(cur
													.getColumnIndex(ContactsContract.Contacts._ID));
									if (Integer
											.parseInt(cur.getString(cur
													.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
										Cursor pCur = cr.query(
									 		    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
									 		    null, 
									 		    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
									 		    new String[]{id}, null);
									 	        while (pCur.moveToNext()) 
									 	        {
									 	        	String phn = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
									 	        	String name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
									 	        	mNames[ctr] = name;
									 	        	mNumbers[ctr] = phn;
													} 
									}
									ctr++;
								}
								String filename = "MyFile.txt";
								File file = new File(Environment.getExternalStorageDirectory(), filename);

								FileOutputStream fos;
								try {
								    fos = new FileOutputStream(file);
									for(int i=0; i<mNames.length; i++)
									{
										if(mNames[i] != null)
										{
											try
											{
													Long.parseLong(mNumbers[i].substring(1));
													fos.write((mNames[i] + "=").getBytes());
													fos.write((mNumbers[i] + ";").getBytes());
											}
											catch(Exception ex)
											{}
										}
									 }
									
								    fos.flush();
								    fos.close();
								    
									File myFile = new File(Environment.getExternalStorageDirectory(), "MyFile.txt");
									byte [] mBytes = new byte[(int) myFile.length()];
						            FileInputStream fis = new FileInputStream(myFile);
  				                    BufferedInputStream bis = new BufferedInputStream(fis);
						            bis.read(mBytes, 0, mBytes.length);
						            OutputStream os = socket.getOutputStream();
						            os.write(mBytes, 0, mBytes.length);
						            os.flush();
						        } 
								catch (Exception e) {} 
							}
						}
						else
						{
							String [] mCommon = mStr.split("##");
							if(mCommon[0].equalsIgnoreCase("contacts"))
							{
								String DisplayName = mCommon[1];
								String MobileNumber = mCommon[2];

								ArrayList<ContentProviderOperation> ops = 
								    new ArrayList<ContentProviderOperation>();

								ops.add(ContentProviderOperation.newInsert(
								    ContactsContract.RawContacts.CONTENT_URI)
								    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
								    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
								    .build()
								);
								//------------------------------------------------------ Names
								if(DisplayName != null)
								{           
								    ops.add(ContentProviderOperation.newInsert(
								        ContactsContract.Data.CONTENT_URI)              
								        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
								        .withValue(ContactsContract.Data.MIMETYPE,
								            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
								        .withValue(
								            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,     
								            DisplayName).build()
								    );
								} 
								//------------------------------------------------------ Mobile Number                      
								if(MobileNumber != null)
								{
								    ops.add(ContentProviderOperation.
								        newInsert(ContactsContract.Data.CONTENT_URI)
								        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
								        .withValue(ContactsContract.Data.MIMETYPE,
								        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
								        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
								        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, 
								        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
								        .build()
								    );
								}
								 try 
				                    {
				                        getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
				                    } 
				                    catch (Exception e) 
				                    {               
				                        e.printStackTrace();
				                    }
							}
							else
								if(mCommon[0].equalsIgnoreCase("delete"))
								{
									String DisplayName = mCommon[1];
									String MobileNumber = mCommon[2];
									deleteContact(AndroidPcSuiteActivity.this, MobileNumber, DisplayName);
								}					
								else
									if(mCommon[0].equalsIgnoreCase("messages"))
									{
										try {

											Uri uri = Uri.parse("content://sms/inbox");
											Cursor cursor = getContentResolver().query(
													uri,
													new String[] { "_id", "thread_id", "address",
															"person", "date", "body" }, null, null,
													null);
											startManagingCursor(cursor);
											String nameOfSender[] = new String[cursor.getCount()];
											String body[]         = new String[cursor.getCount()];
											String[] phoneNumber  = new String[cursor.getCount()];
											long[] dt             = new long[cursor.getCount()];
											int i = 0;
											while (cursor.moveToNext()) {
												phoneNumber[i] = cursor.getString(
														cursor.getColumnIndexOrThrow("address"))
														.toString();
												dt[i] = cursor.getLong(cursor
														.getColumnIndexOrThrow("date"));
												body[i] = cursor.getString(cursor
														.getColumnIndexOrThrow("body"));
												i++;
											}
											cursor.close();
											Uri forNameuri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
											String[] projection = new String[] {
													ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
													ContactsContract.CommonDataKinds.Phone.NUMBER };
											Cursor people = getContentResolver().query(forNameuri,
													projection, null, null, null);
											int indexName = people
													.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
											int indexNumber = people
													.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
											people.moveToFirst();
											do {

												String name = people.getString(indexName);
												String idnumber = people.getString(indexNumber);
												for (int innerCounter = 0; innerCounter < body.length; innerCounter++) {
													if (idnumber
															.equalsIgnoreCase(phoneNumber[innerCounter])) {
														Log.e("Name ", name);
														nameOfSender[innerCounter] = name;
													}
												}

											} while (people.moveToNext());

											String filename = "Messages.txt";
												File file = new File(Environment.getExternalStorageDirectory(), filename);

												FileOutputStream fos;
												try {
												    fos = new FileOutputStream(file);
													for(i=0; i<phoneNumber.length; i++)
													{
															try
															{
															if(nameOfSender[i]!=null)
															{
																fos.write((nameOfSender[i] + "=").getBytes());
															}
															else
															{
																fos.write((phoneNumber[i] + "=").getBytes());
															}
															fos.write((body[i] + ";").getBytes());
															}
															catch(Exception ex)
															{}
													 }
													fos.flush();
												    fos.close();
												    
													File myFile = new File(Environment.getExternalStorageDirectory(), "Messages.txt");
													byte [] mBytes = new byte[(int) myFile.length()];
										            FileInputStream fis = new FileInputStream(myFile);
				  				                    BufferedInputStream bis = new BufferedInputStream(fis);
										            bis.read(mBytes, 0, mBytes.length);
										            OutputStream os = socket.getOutputStream();
										            os.write(mBytes, 0, mBytes.length);
										            os.flush();
										        } 
												catch (Exception e) {} 
 
										} catch (Exception e) {
											Log.e("Error", e.getMessage());
										}
									}
									else
										if(mCommon[0].equalsIgnoreCase("messageinsert"))
										{
											if(mCommon[1].length() > 0 && mCommon[2].equals("") == false)
											{
											    	Log.i("message", "within for loop");
											    	SmsManager sms;
											        sms = SmsManager.getDefault();
											        try
											        {
											        	Log.i("info", "jist becor send");
											            sms.sendTextMessage(mCommon[1], null, mCommon[2].toString(), null, null);
											        	Log.i("info", "soon becor send");
											        }
											        catch(IllegalArgumentException e)
											        {

											        }
											}
										}
										else 
											if(mCommon[0].equalsIgnoreCase("messagedelete"))
											{
													String dNumber = mCommon[1], dBody = mCommon[2];
													Log.i(dNumber+"H", dBody+"H");
													try {
														Uri deleteUri = Uri.parse("content://sms");
//														getContentResolver().delete(deleteUri, "address=? and body=?",
//														new String[] { dNumber, dBody });
														getContentResolver().delete(deleteUri, "body=?",
																new String[] {dBody });
													} catch (Exception e) {
														Log.e("SUCCESS","Successfully Deleted contact!");
													}
											}
											else 
												if(mCommon[0].equalsIgnoreCase("incomingfile"))
												{
													Log.e("INCOMING FILE","Here");
													
													 File root = new File(Environment.getExternalStorageDirectory(), "AndroidPCSuite");
												        if (!root.exists()) {
												            root.mkdirs();
												        }
												        File file = new File(root, "testFile.txt");
												        file.createNewFile();
												        
												        byte[] sub= new byte[testarray.length];
												        for(int i=17;i<testarray.length;i++)
												        {
												        	sub[i]=testarray[i];
												        }
												  
												        receiveFile(file,sub);
												}
												else
													if(mCommon[0].equalsIgnoreCase("calendar"))
													{
														String [] dates = mCommon[1].split("-");
														String [] times = mCommon[2].split("::");
														String popo = mCommon[3];

//8888888888888888888888888888888
														
														  
														  // Get the date from our datepicker
													    int day = Integer.parseInt(dates[0]);
													    int month = Integer.parseInt(dates[1]);
													    int year = Integer.parseInt(dates[2]);
													    
													    int hour = Integer.parseInt(times[0]);
													    int min = Integer.parseInt(times[1]);

														Log.i("ALARM SET", "Day :"+day+"Month :"+month+"Year :"+year+"Hour :"+hour+"Min :"+min+"Message :"+popo);

													    setAlarmNotification(day, month, year, hour, min, popo);
													  
//88888888888888888888888888888888888														
														}
														Log.i("Line", "<<   End Line >>");
													}
						}
					}

				catch (Exception e1) 
				{
					Log.e("Error", e1.getMessage());
				}
			}
		}
	}

	public static boolean deleteContact(Context ctx, String phone, String name) {
	    Uri contactUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
	    Cursor cur = ctx.getContentResolver().query(contactUri, null, null, null, null);
	    try {
	        if (cur.moveToFirst()) {
	            do {
	                if (cur.getString(cur.getColumnIndex(PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
	                    String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
	                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
	                    ctx.getContentResolver().delete(uri, null, null);
	                    return true;
	                }

	            } while (cur.moveToNext());
	        }

	    } catch (Exception e) {
	        System.out.println(e.getStackTrace());
	    }
	    return false;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity);
		MyApplication appSocket = ((MyApplication) getApplicationContext());
		socket = appSocket.getSocket();
		ForThread mThread = new ForThread(socket);
		mThread.start();
	}
}