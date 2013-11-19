package com.example.exercise;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.R;
import com.example.pts.PtTime;
import com.example.sw_wellfitsquare.AsyncUseJson;
import com.example.sw_wellfitsquare.ReadData;

public class Exercise extends Activity implements ReadData{

	ListView lv;
	String member_uid;
	List<today_exercise> today = new ArrayList<today_exercise>();
	L_Adapter adapter = new L_Adapter();
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercise);
		
		Intent intent = getIntent();
		member_uid = intent.getStringExtra("member_uid");
		
		lv = (ListView)findViewById(R.id.Exercise_List);
		lv.setAdapter(adapter);
		
		Button add_bt = (Button)findViewById(R.id.Exercise_Add);
		add_bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 측정하기
				Intent intent = new Intent(getBaseContext(),NFC_MainActivity.class);
				intent.putExtra("member_uid",member_uid);
				startActivityForResult(intent,0);
			}
		});
		Button sum_bt = (Button)findViewById(R.id.Exercise_Sum);
		sum_bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),Exercise_Sum.class);
				intent.putExtra("member_uid", member_uid);
				startActivity(intent);
			}
		});
		SetList();
	}
	public void SetList(){

		Calendar c = Calendar.getInstance();
		String date = String.valueOf(c.get(Calendar.YEAR))+"-"+String.valueOf(c.get(Calendar.MONTH)+1)+"-"+String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		
		HashMap<String,String>ab = new HashMap<String,String>();
		List<String> wantGap = new ArrayList<String>();
		ab.put("table","exercise");
		ab.put("field", "kind,num,time");
		ab.put("condition", "member_uid="+member_uid+" && date=\""+date+"\"");
		wantGap.add("kind");
		wantGap.add("num");
		wantGap.add("time");
		AsyncUseJson a = new AsyncUseJson(Exercise.this,ReadData.Select,wantGap);
		a.execute(ab);
		
	}
	public void onActivityResult(int requestCode,int resultCode, Intent Data){
		// 측정 끝나고 다시 SetList();
		if(requestCode == 0){
			SetList();
		}
	}
	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		today = new ArrayList<today_exercise>();
		for(HashMap<String,String> i : HashList){
			today_exercise in = new today_exercise();
			in.kind = i.get("kind");
			in.num = Integer.parseInt(i.get("num"));
			in.time = i.get("time");
			today.add(in);
		}
		resetAdapter();
	}

	@Override
	public void NullData() {
		today = new ArrayList<today_exercise>();
		today_exercise in = new today_exercise();
		in.kind = "오늘 하신 운동이 없습니다.";
		today.add(in);
		resetAdapter();
	}
	class today_exercise{
		String kind;
		int num;
		String time;
	}
	public void resetAdapter(){
		Exercise.this.runOnUiThread(new Runnable() {
	        public void run() {
	             adapter.notifyDataSetChanged();
	        }
		});
	}
	public class L_Adapter extends BaseAdapter{
		@Override
		public int getCount() {
			return today.size();
		}

		@Override
		public today_exercise getItem(int position) {
			return today.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.exercise_list, parent, false);
			
			TextView tv_kind = (TextView)convertView.findViewById(R.id.exercise_kind);
			TextView tv_num = (TextView)convertView.findViewById(R.id.exercise_num);
			TextView tv_time = (TextView)convertView.findViewById(R.id.exercise_time);
			tv_kind.setText(today.get(position).kind);
			tv_num.setText(String.valueOf(today.get(position).num));
			tv_time.setText(today.get(position).time);
			
			return convertView;
		}
		
	}

	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return this.getParent().onKeyDown(keyCode, event);
		}
		return false;
	}
}
