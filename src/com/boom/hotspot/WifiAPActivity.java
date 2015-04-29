package com.boom.hotspot;

import com.boom.hotspot.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;



public class WifiAPActivity extends Activity {
    //private String TAG = "WifiAPActivity";
    boolean wasAPEnabled = false;
    static WifiAP wifiAp;
    private WifiManager wifi;
    static Button btnWifiToggle;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi);
        
        BoomRouter.context=this;
        
        btnWifiToggle = (Button) findViewById(R.id.btnWifiToggle);
        tv=(TextView)findViewById(R.id.server_txt);
        wifiAp = new WifiAP();
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        btnWifiToggle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                wifiAp.toggleWiFiAP(wifi, WifiAPActivity.this);
                String abc=btnWifiToggle.getText().toString();
                if(abc.equalsIgnoreCase("Turn on"))
                {
                	BoomRouter.isrunning=true;
                	new BoomRouter().start();
                    try {
                    	Thread.sleep(1000);
    				} catch (Exception e) {
    					// TODO: handle exception
    					e.printStackTrace();
    				}
                    tv.setText("Server Starting");
                    try {
                    	Thread.sleep(5000);
    				} catch (Exception e) {
    					// TODO: handle exception
    					e.printStackTrace();
    				}
    				tv.setText("Server Started");
                }
                else{
                	BoomRouter.isrunning=false;
                	tv.setText("Server stopped");
                }
                
            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        
    }

    @Override
    public void onResume() {
        super.onResume();
        if (wasAPEnabled) {
            if (wifiAp.getWifiAPState()!=wifiAp.WIFI_AP_STATE_ENABLED && wifiAp.getWifiAPState()!=wifiAp.WIFI_AP_STATE_ENABLING){
                wifiAp.toggleWiFiAP(wifi, WifiAPActivity.this);
            }
        }
        updateStatusDisplay();
    }

    @Override
    public void onPause() {
        super.onPause();
        boolean wifiApIsOn = wifiAp.getWifiAPState()==wifiAp.WIFI_AP_STATE_ENABLED || wifiAp.getWifiAPState()==wifiAp.WIFI_AP_STATE_ENABLING;
        if (wifiApIsOn) {
            wasAPEnabled = true;
            wifiAp.toggleWiFiAP(wifi, WifiAPActivity.this);
        } else {
            wasAPEnabled = false;
        }
        updateStatusDisplay();
    }

    public static void updateStatusDisplay() {
        if (wifiAp.getWifiAPState()==wifiAp.WIFI_AP_STATE_ENABLED || wifiAp.getWifiAPState()==wifiAp.WIFI_AP_STATE_ENABLING) {
            btnWifiToggle.setText("Turn off");
            //findViewById(R.id.bg).setBackgroundResource(R.drawable.bg_wifi_on);
        } else {
            btnWifiToggle.setText("Turn on");
            //findViewById(R.id.bg).setBackgroundResource(R.drawable.bg_wifi_off);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	menu.clear();
    	menu.add(0, 0, 0, "View Users");
    	menu.add(0, 1, 0, "Logout");
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	int id = item.getItemId();
    	switch (id) {
		case 0:
			startActivity(new Intent(WifiAPActivity.this,ViewUsers.class));
			break;
		case 1:
			startActivity(new Intent(WifiAPActivity.this,Login.class));
			finish();
			break;

		default:
			break;
		}
    	return super.onOptionsItemSelected(item);
    }
    
}