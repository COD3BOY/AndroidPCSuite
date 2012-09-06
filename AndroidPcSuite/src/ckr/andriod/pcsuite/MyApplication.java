package ckr.andriod.pcsuite;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

public class MyApplication extends Application {
	
	public MyApplication() {
		// TODO Auto-generated constructor stub
	}

	  private BluetoothSocket testsocket;
	  public BluetoothSocket getSocket(){
	    return testsocket;
	  }
	  public void setSocket(BluetoothSocket s){
		  testsocket=s;
	    
	  }
	}
