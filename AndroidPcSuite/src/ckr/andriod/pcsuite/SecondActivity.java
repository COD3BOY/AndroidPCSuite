package ckr.andriod.pcsuite;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class SecondActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		//Get a Notification manager
		NotificationManager nm= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		//Create a Notification object
		Notification notif = new Notification(R.drawable.ic_launcher,"Reminder",System.currentTimeMillis());
		//Create an Intent 
		Intent i = new Intent();
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
	
		
		Bundle bund = getIntent().getExtras();
		String mess = bund.getString("message");
		Log.d("Second", mess);
		TextView tx = (TextView)findViewById(R.id.textView1);
		tx.setText(mess);
		//Now we add a few more details to the notification object using setLatestEventInfo() method
		notif.setLatestEventInfo(this, "Reminder", mess, pendingIntent);
		//Finally now we send the notification using notify() method
		nm.notify(1,notif);
		MediaPlayer mp = MediaPlayer.create(this,R.raw.newf);
		mp.start();
	}
	
}
