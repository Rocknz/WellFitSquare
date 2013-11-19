package com.example.sw_wellfitsquare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.R;


public class WellFitSquare extends Activity implements OnClickListener{
	EditText id_input;
	EditText password_input;
	CheckBox check;
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	Button enter;
	Button btn_new_member;
	HashMap<String, String> main_has_login;
	String gap = null;
	HashMap<String,String> main_has_select ;
	
	static final String IP_JOIN = "http://61.43.139.69/";
			 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_well_fit_square);
		
		main_has_login = new HashMap<String,String>();
		
		initId();
		initSetting();
		if(id_input.getText().toString().getBytes().length >= 1 && gap != null){
//			enter.performClick(); imsi
//			Intent i = new Intent(getApplicationContext(), Main.class);
//			i.putExtra("member_uid", gap);
//			startActivity(i);
		}
	}
	
	private void initSetting() {
		sharedPref= getSharedPreferences("shared_login",Activity.MODE_PRIVATE);
		editor = sharedPref.edit();
		id_input.setText(sharedPref.getString(getString(R.string.saved_id), ""));
		password_input.setText(sharedPref.getString(getString(R.string.saved_password), ""));
		gap = sharedPref.getString("uid", null);
		enter.setOnClickListener(this);
		btn_new_member.setOnClickListener(this);
		
		if(id_input.getText().toString().getBytes().length >= 1){
			check.setChecked(true);
		}
	}

	private void initId() {
		id_input = (EditText)findViewById(R.id.id_input);
		password_input = (EditText)findViewById(R.id.password_input);
		check = (CheckBox)findViewById(R.id.check);
		enter = (Button)findViewById(R.id.enter);
		btn_new_member = (Button)findViewById(R.id.btn_new_member);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void editNullText(){
		id_input.setText("");
		password_input.setText("");
	}
	
	private void idSave(){
		if(check.isChecked())
		{
			editor.putString(getString(R.string.saved_id), id_input.getText().toString());
			editor.putString(getString(R.string.saved_password),password_input.getText().toString());
			editor.putString("uid", gap);
			editor.commit();
		}else{
			editNullText();
			editor.putString(getString(R.string.saved_id), id_input.getText().toString());
			editor.putString(getString(R.string.saved_password), password_input.getText().toString());
			editor.commit();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.enter:
			
			main_has_login.put("id", id_input.getText().toString());
			main_has_login.put("pw", password_input.getText().toString());
			main_has_login.put("table", "member");
			
			AsyncCustom ac = new AsyncCustom();
			ac.execute(main_has_login);

			break;
		case R.id.btn_new_member:
			Intent i = new Intent(getApplicationContext(), Joining.class);
			startActivity(i);
			break;
		}
	}
	public class AsyncCustom extends AsyncTask<HashMap<String, String>,  Void, String>
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		List<HashMap<String, String>> result_hash;
		@Override
		protected String doInBackground(HashMap<String, String>... params) {
		
			HashMap<String, String> send = new HashMap<String,String>();
			
			send = params[0];
			final JsonRequestPost eee =new JsonRequestPost(send,IP_JOIN+"login.php");
			eee.requestPost();
			
			
			List<String> name = new ArrayList<String>();
			name.add("id");
			
			return eee.getStr();
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
		
		Intent i_to_Main = new Intent(getApplicationContext(), Main.class);
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			System.out.println(result);
//			Log.i();
			Log.i("resulttttt",""+result);
			if(result.equals("error") || result.equals("fail"))
			{
				Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인해 주세요.", Toast.LENGTH_LONG).show();
			} else
			{
				gap = result;				
				idSave();  
				i_to_Main.putExtra("member_uid", gap);
				startActivity(i_to_Main);	
				finish();	
			}
		}
	}
}