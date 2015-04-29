package com.boom.hotspot;

import java.util.List;

import com.boom.db.DBConnection;
import com.boom.hotspot.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class ViewUsers extends Activity implements OnItemClickListener {

	ListView lv;
	Context context;
	String ips[] = null;
	List<Users> users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewusers);
		lv = (ListView) findViewById(R.id.users_list);
		context = this;
		DBConnection dbConnection = new DBConnection(context);
		users = dbConnection.display();
		String user[] = new String[users.size()];
		for (int i = 0; i < users.size(); i++) {
			user[i] = users.get(i).getName();
		}
		
		ArrayAdapter<String> ard = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, user);
		lv.setAdapter(ard);
		lv.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Users list = users.get(arg2);		
		String ip = list.getIp();
		String files = new DBConnection(getApplicationContext()).getAllFIles(ip);
		showDialogue(files);
	}

	public void showDialogue(String files) {
		
		final Dialog dialog = new Dialog(ViewUsers.this);
		// Include dialog.xml file
		dialog.setContentView(R.layout.dialog);
		dialog.setTitle("Files");
		// set values for custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.textDialog);
		text.setText(files);
		dialog.show();
		Button declineButton = (Button) dialog.findViewById(R.id.declineButton);

		declineButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Close dialog
				dialog.dismiss();
			}
		});

	}
}
