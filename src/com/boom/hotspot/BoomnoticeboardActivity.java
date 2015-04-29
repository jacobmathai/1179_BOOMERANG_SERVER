package com.boom.hotspot;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.boom.hotspot.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.Toast;

public class BoomnoticeboardActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        new BoomRouter().start();
        String ip=getLocalIpAddress();
        Toast.makeText(getApplicationContext(), ip, Toast.LENGTH_LONG).show();
//        if(BoomRouter.c==1) {
//        System.out.println(ip);
//      	Toast.makeText(getApplicationContext(), ip, Toast.LENGTH_LONG).show();
        
        }
    public String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    String ip = Formatter.formatIpAddress(inetAddress.hashCode());
	                   // Log.i(TAG, "***** IP="+ ip);
	                    return ip;
	                }
	            }
	        }
	    } catch (SocketException ex) {
	      //  Log.e(TAG, ex.toString());
	    }
	    return null;
	}
    }
//}