package ckr.andriod.pcsuite;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;



public class AlarmTask implements Runnable{
	// The date selected for the alarm
	private final Calendar date;
	// The android system alarm manager
	private final AlarmManager am;
	// Your context to retrieve the alarm manager from
	private final Context context;
	String msg;

	public AlarmTask(Context context, Calendar date,String message) {
		this.context = context;
		this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		this.date = date;
		this.msg = message;
		Log.d("AlarmTask", msg);
	}
	
	

	public void run() {
		
		Intent inte = new Intent(context, SecondActivity.class);
		Bundle bun = new Bundle();
		bun.putString("message", msg);
		Log.d("RUN", msg);
		int num = msg.length();
		inte.putExtras(bun);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, num, inte, 0); //Second argument is the flag
		//If you keep the second argument a constant value like 0, then there will be some problem. So we are changing
		//the flag argument values depending the size of the reminder message. So each pending intent will be different from the
		//previous one
		
		
		// Sets an alarm - note this alarm will be lost if the phone is turned off and on again
		 am.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
		 
			
		
	}
}
