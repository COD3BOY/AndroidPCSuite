package ckr.andriod.pcsuite; 


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AndroidSuiteActivity extends Activity {

	/** Called when the activity is first created. */

	BluetoothDevice device;
	BluetoothAdapter mBluetoothAdapter;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	
		Button initial = (Button) findViewById(R.id.button1);
		OnClickListener initialListener = new OnClickListener() {
			public void onClick(View v) {
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); vib.vibrate(50);
				
				Intent serverIntent = new Intent(getApplicationContext(), DeviceListActivity.class);
	            startActivity(serverIntent);
	            
			}
		};

		initial.setOnClickListener(initialListener);


	}
}