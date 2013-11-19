package com.example.sw_wellfitsquare_nonmember;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Dialog_Info extends Activity {

	String name;
	String number;
	String locate;
	int uid;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_info);
		
		Intent intent = getIntent();
		
		name = intent.getStringExtra("name");
		number = intent.getStringExtra("number");
		locate = intent.getStringExtra("locate");
		uid = intent.getIntExtra("uid",-1);
		
		
		TextView info_name = (TextView)findViewById(R.id.info_name);
		TextView info_number = (TextView)findViewById(R.id.info_number);
		TextView info_locate = (TextView)findViewById(R.id.info_locate);
		
		info_name.setText(info_name.getText() + name);
		info_number.setText(info_number.getText() + number);
		info_locate.setText(info_locate.getText() + locate);
		
		Button info_btn = (Button)findViewById(R.id.info_btn);
		info_btn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Activity_Info.class);
				intent.putExtra("name",name);
				intent.putExtra("number",number);
				intent.putExtra("locate",locate);
				intent.putExtra("uid",uid);
				
				startActivity(intent);
				finish();
			}
		});
	}
}
