package com.boom.hotspot;

import com.boom.db.DBConnection;
import com.boom.hotspot.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	
	EditText et_uname,et_upass;
	String username,password;
	Button login,cancel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		DBConnection dbConnection = new DBConnection(getApplicationContext());
		dbConnection.init();
//		String sql = "insert into users values ('cerin','192.168.1.37')";
//		dbConnection.insertData(sql);
		System.out.println("This worked fine ************  ");
		et_uname = (EditText)findViewById(R.id.log_uname);
		et_upass = (EditText)findViewById(R.id.log_password);
		login = (Button)findViewById(R.id.login_bt);
		cancel = (Button)findViewById(R.id.cancel_bt);
		
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				username = et_uname.getText().toString();
				password = et_upass.getText().toString();
				if("".equals(username) || "".equals(password))
					Toast.makeText(getApplicationContext(), "Enter All Fields !", Toast.LENGTH_LONG).show();
				
				if("admin".equals(username)&& "admin".equals(password)){
					startActivity(new Intent(Login.this,WifiAPActivity.class));
				}else{
					Toast.makeText(getApplicationContext(), "Invalid Username or password", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
	}

}
