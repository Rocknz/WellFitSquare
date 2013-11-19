package com.example.sw_wellfitsquare;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;

public class Joining extends Activity implements ReadData{
	
	Button send_btn,cancel_btn;
	EditText send_id;
	EditText send_pw;
	TextView send_phone;
	HashMap<String, String> main_has_join;
	HashMap<String,String> main_has_select ;
	int type = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.joining);
	    initId();
	    cancel_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		send_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				//find number;
				HashMap<String,String> ab = new HashMap<String,String>();
				ab.put("table", "member");
				ab.put("field","uid,id");
				ab.put("condition", "phone=\""+send_phone.getText().toString()+"\"");
				List<String> a = new ArrayList<String>();
				a.add("uid");
				a.add("id");
				AsyncUseJson c = new AsyncUseJson(Joining.this,ReadData.Select,a);
				type = 0;
				c.execute(ab);
			}
		});
	}
	private void initId() {
		send_btn = (Button)findViewById(R.id.join_add);
		cancel_btn = (Button)findViewById(R.id.join_cancel);
		send_id = (EditText)findViewById(R.id.join_id);
		send_pw= (EditText)findViewById(R.id.join_pw);
		send_phone= (TextView)findViewById(R.id.join_phone);
		TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		String number_phone;
    	number_phone = tMgr.getLine1Number();
    	char[] s = new char[13];
    	s = number_phone.toCharArray();
    	number_phone = new String();
    	for(int i=s.length-1;i>=s.length-8;i--){
    		number_phone = String.valueOf(s[i]) + number_phone;
    	}
    	number_phone = "010"+ number_phone;
		send_phone.setText(number_phone);
	}
	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		if(type == 0){
			//통과했으니 핸드폰 번호가 존재함
			String s = HashList.get(0).get("id");
			if(s != null){
				Toast.makeText(getBaseContext(), "이미 가입하셨습니다", Toast.LENGTH_SHORT).show();
			}
			else{
				//회원 update;
				Update a = new Update();
				a.execute();
			}
			type = 1;
		}
		else{
			finish();
		}
	}
	public class Update extends AsyncTask<Void,Void,Void>{

		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
				String id,pw,phone;
				id = send_id.getText().toString();
				pw = send_pw.getText().toString();
				String date;
				Calendar c = Calendar.getInstance();
				date = String.valueOf(c.get(Calendar.YEAR))+"-"+String.valueOf(c.get(Calendar.MONTH))+"-"+String.valueOf(c.get(Calendar.DAY_OF_MONTH));
				phone = send_phone.getText().toString();
				
				HashMap<String,String> ab = new HashMap<String,String>();
				ab.put("table", "member");
				ab.put("field", "id,pw,date");
				ab.put("data", "\""+id+"\",\""+pw+"\",\""+date+"\"");
				ab.put("condition", "phone=\""+phone+"\"");
				ab.put("overlap", "1");
				List<String> a = new ArrayList<String>();
				AsyncUseJson ct = new AsyncUseJson(Joining.this,ReadData.Update,a);
				ct.execute(ab);
				
				//db변경해야함.
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
	@Override
	public void NullData() {
		// TODO Auto-generated method stub
		
	}
}
