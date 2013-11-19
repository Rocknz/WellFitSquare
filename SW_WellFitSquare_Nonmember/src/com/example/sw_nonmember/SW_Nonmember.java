package com.example.sw_nonmember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.example.common.AsyncUseJson_nonmember;
import com.example.common.ReadData;
import com.example.sw_wellfitsquare_nonmember.R;

public class SW_Nonmember extends Activity implements ReadData{
	String number_phone;
	String fitness_uid;
	String trainer_uid;
	int letsgo = 0;
	String member_uid;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sw_nonmember);
		
		//Button user = (Button)findViewById(R.id.start_User);
		letsgo = 0;
		Intent intent = getIntent();
		trainer_uid = intent.getStringExtra("trainer_uid");
		fitness_uid = intent.getStringExtra("fitness_uid");
		TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
    	number_phone = tMgr.getLine1Number();
    	char[] s = new char[13];
    	s = number_phone.toCharArray();
    	number_phone = new String();
    	for(int i=s.length-1;i>=s.length-8;i--){
    		number_phone = String.valueOf(s[i]) + number_phone;
    	}
    	number_phone = "010"+ number_phone;
		(new getUser_uid()).execute();
	}
	
	@Override
	public void setData(List<HashMap<String, String>> HashList, int requestCode) {
		if(requestCode == 0){
			// uid 가 존재
			letsgo = 1;
			member_uid = HashList.get(0).get("uid"); 
			finish();
		}
	}
	@Override
	public void NullData(int requestCode) {
		if(requestCode == 0){
			//uid 가 없음 
			(new joining()).execute();
		}
		else if(requestCode == 1){
			(new getUser_uid()).execute();
		}
	}
	public class getUser_uid extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			HashMap<String,String> a = new HashMap<String,String>();
			List<String> Wantgap = new ArrayList<String>();
			a.put("table", "member");
			a.put("field", "uid");
			a.put("condition","phone=\""+number_phone+"\"");
			Wantgap.add("uid");
			AsyncUseJson_nonmember ab = new AsyncUseJson_nonmember(SW_Nonmember.this,ReadData.Select,Wantgap,0);
			ab.execute(a);
			
			return null;
		}
	}
	
	public class joining extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			HashMap<String,String> a = new HashMap<String,String>();
			List<String> Wantgap = new ArrayList<String>();
			a.put("table", "member");
			a.put("field", "phone");
			a.put("data","\""+number_phone+"\"");
			AsyncUseJson_nonmember ab = new AsyncUseJson_nonmember(SW_Nonmember.this,ReadData.Insert,Wantgap,1);
			ab.execute(a);
			
			return null;
		}
	}
	public void onDestroy(){
		super.onDestroy();
		if(letsgo == 1){
			Intent intent = new Intent(getBaseContext(),Chatting_UT.class);
			intent.putExtra("member_uid",member_uid);
			intent.putExtra("trainer_uid",trainer_uid);
			intent.putExtra("fitness_uid",fitness_uid);
			intent.putExtra("User", "User");
			startActivity(intent);
		}
	}
	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void NullData() {
		// TODO Auto-generated method stub
		
	}
}
