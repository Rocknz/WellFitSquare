package com.example.pts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.R;
import com.example.sw_wellfitsquare.AsyncUseJson;
import com.example.sw_wellfitsquare.ReadData;

public class PtTime extends Activity implements ReadData{
	String url = new String();
	Calendar send_date = null;
	Calendar now_date = null;
	TextView tb = null;
	String member_uid;
	String trainer_uid;
	int type = -1;
	
	
	DatePickerDialog.OnDateSetListener DateDialogListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			send_date = Calendar.getInstance();
			send_date.set(year, monthOfYear, dayOfMonth);	
			new TimePickerDialog(PtTime.this, TimeDialogListener, now_date.get(Calendar.HOUR), now_date.get(Calendar.MINUTE),true).show();		
		}
	}; 
	TimePickerDialog.OnTimeSetListener TimeDialogListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			send_date.set(send_date.get(Calendar.YEAR), send_date.get(Calendar.MONTH), send_date.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
			setsText();
		}
	};

	public void setsText(){
		if(send_date != null){
			String t = null;
			t = String.valueOf(send_date.get(Calendar.YEAR));
			t += "년 ";
			t += String.valueOf(send_date.get(Calendar.MONTH)+1);
			t += "월 ";
			t += String.valueOf(send_date.get(Calendar.DAY_OF_MONTH));
			t += "일 ";
			t += String.valueOf(send_date.get(Calendar.HOUR));
			t += "시 ";
			t += String.valueOf(send_date.get(Calendar.MINUTE));
			t += "분 ";
			tb.setText(t);
		}
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pt_time);
		
		getData();
		
		
		Button bt = (Button)findViewById(R.id.PT_time_Button);
		tb = (TextView)findViewById(R.id.PT_time_dateText);
		LinearLayout timelayout = (LinearLayout)findViewById(R.id.PT_time_layout);
		timelayout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				now_date = Calendar.getInstance();
				new DatePickerDialog(PtTime.this, DateDialogListener, now_date.get(Calendar.YEAR), now_date.get(Calendar.MONTH), now_date.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		bt.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(send_date != null && type == -1){
					//send Data
					Calendar start_date = send_date;
					Calendar end_date = Calendar.getInstance();
					Calendar today = Calendar.getInstance();
					end_date.set(start_date.get(Calendar.YEAR),start_date.get(Calendar.MONTH), start_date.get(Calendar.DAY_OF_MONTH), start_date.get(Calendar.HOUR)+1, start_date.get(Calendar.MINUTE));
					String s_d = stringCalendar(start_date);
					String e_d = stringCalendar(end_date);
					String t_d = stringCalendar(today); 
					HashMap<String,String> ab = new HashMap<String,String>();
					List<String> wantGap = new ArrayList<String>();
					ab.put("table","reserve");
					ab.put("field","trainer_uid,member_uid,date,start_date,end_date");
					ab.put("data", "1,1,\""+t_d+"\""+",\""+s_d+"\",\""+e_d+"\"");
					AsyncUseJson a = new AsyncUseJson(PtTime.this,ReadData.Insert,wantGap);
					type = 1;
					a.execute(ab);
				}
				else if(send_date == null){
					Toast.makeText(getBaseContext(), "날짜를 입력해주세요.", Toast.LENGTH_SHORT).show();
				}
				else if(type == 0){
					Toast.makeText(getBaseContext(), "데이터 로드중입니다.", Toast.LENGTH_SHORT).show();
				}
				else if(type == 1){
					Toast.makeText(getBaseContext(), "데이터 전송중입니다.", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public void getData(){
		Intent intent = this.getIntent();
		member_uid=intent.getStringExtra("member_uid");
		trainer_uid=intent.getStringExtra("trainer_uid");
		
		// trainer calendarId 를 받음
		HashMap<String,String> a = new HashMap<String,String>();
		List<String> Wantgap = new ArrayList<String>();
		a.put("table", "trainer");
		a.put("field", "calendar_id");
		a.put("condition", "uid="+trainer_uid);
		Wantgap.add("calendar_id");
		AsyncUseJson ab = new AsyncUseJson(this,ReadData.Select,Wantgap);
		type = 0;
		ab.execute(a);
	}
	public static String stringCalendar(Calendar c){
		String str;
		str = String.valueOf(c.get(Calendar.YEAR)) + "-" + String.valueOf(c.get(Calendar.MONTH)+1) + "-" + String.valueOf(c.get(Calendar.DAY_OF_MONTH))+" "+String.valueOf(c.get(Calendar.HOUR))+":"+String.valueOf(c.get(Calendar.MINUTE))+":00";
		return str;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return this.getParent().onKeyDown(keyCode, event);
		}
		return false;
	}
	
	@Override
	public void setData(List<HashMap<String,String>> HashList) {
		if(type == 0){
			
			WebView webview = (WebView)findViewById(R.id.PT_time_webview);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.setWebViewClient(new WebViewClient());
			String urlF="https://www.google.com/calendar/embed?src=";
			String urlS="&ctz=Asia/Seoul";
//			String calendarId = "erg2lks4j14nm95h8sq28vocag@group.calendar.google.com";
			String calendarId = HashList.get(0).get("calendar_id"); 
			url = urlF + calendarId + urlS;
			webview.loadUrl(url); 
		}
		if(type == 1){
			Toast.makeText(getBaseContext(), "신청되었습니다.", Toast.LENGTH_SHORT).show();
			send_date = null;
			tb.setText("클릭하여 날짜를 선택해주세요");
		}
		type = -1;
	}

	@Override
	public void NullData() {
		// TODO Auto-generated method stub
		
	}
}
/* 
 * https://www.google.com/calendar/embed?mode=WEEK&amp;height=400&amp;wkst=1&amp;bgcolor=%23FFFFFF&amp;src=erg2lks4j14nm95h8sq28vocag%40group.calendar.google.com&amp;color=%232952A3&amp;ctz=Asia%2FSeoul
 * google Calendar Url
 * 
 * 
 * <iframe src="https://www.google.com/calendar/embed?src=erg2lks4j14nm95h8sq28vocag%40group.calendar.google.com&ctz=Asia/Seoul" style="border: 0" width="800" height="600" frameborder="0" scrolling="no"></iframe>
 * 
 * https://www.google.com/calendar/embed?mode=WEEK&amp;height=600&amp;wkst=1&amp;bgcolor=%23FFFFFF&amp;src=erg2lks4j14nm95h8sq28vocag%40group.calendar.google.com&amp;color=%232952A3&amp;ctz=Asia%2FSeoul
 * https://www.google.com/calendar/embed?src=erg2lks4j14nm95h8sq28vocag%40group.calendar.google.com&ctz=Asia/Seoul
 * 
		
		
		
		AsyncUseJson 쓰는법
		
		
//	HashMap<String,String>ab = new HashMap<String,String>();
//	List<String> wantGap = new ArrayList<String>();
//	ab.put("table","member");
//	ab.put("field", "name");
//	ab.put("condition", "id=\"testid\"");
//	wantGap.add("name");
//	AsyncUseJson a = new AsyncUseJson(this,ReadData.Select,wantGap);
//	a.execute(ab);
 */

