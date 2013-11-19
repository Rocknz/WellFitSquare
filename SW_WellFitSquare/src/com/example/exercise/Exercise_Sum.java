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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.R;
import com.example.sw_wellfitsquare.AsyncUseJson;
import com.example.sw_wellfitsquare.ReadData;

public class Exercise_Sum extends Activity implements ReadData{
	
	Calendar before_date;
	Calendar after_date;
	String member_uid;
	TextView tv;
	int icango = 0;
	List<String> kindList = new ArrayList<String>();
	List<ExerciseS> datas = new ArrayList<ExerciseS>();
	List<ExerciseS> today = new ArrayList<ExerciseS>();
	L_Adapter adapter = new L_Adapter();
	
	
	DatePickerDialog.OnDateSetListener beforeDateDialogListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			before_date = Calendar.getInstance();
			before_date.set(year, monthOfYear+1, dayOfMonth);
			DatePickerDialog bt = new DatePickerDialog(Exercise_Sum.this, afterDateDialogListener, after_date.get(Calendar.YEAR), after_date.get(Calendar.MONTH), after_date.get(Calendar.DAY_OF_MONTH));
			bt.setTitle("Select end date");
			bt.show();
		}
	}; 
	DatePickerDialog.OnDateSetListener afterDateDialogListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			after_date = Calendar.getInstance();
			after_date.set(year, monthOfYear+1, dayOfMonth);	
			Search();
		}
	}; 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercise_sum);
		Intent intent = getIntent();
		member_uid = intent.getStringExtra("member_uid");
		tv = (TextView)findViewById(R.id.exercise_sum_day_text);
		LinearLayout bt = (LinearLayout)findViewById(R.id.exercise_sum_day_layout);
		bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				before_date = Calendar.getInstance();
				after_date = Calendar.getInstance();
				DatePickerDialog at = new DatePickerDialog(Exercise_Sum.this, beforeDateDialogListener, before_date.get(Calendar.YEAR), before_date.get(Calendar.MONTH), before_date.get(Calendar.DAY_OF_MONTH));
				at.setTitle("Select start date");
				at.show();
			}
		});
		Button choose = (Button)findViewById(R.id.exercise_sum_day_choose);
		choose.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				GoChoose();
			}
		});
		ListView lv = (ListView)findViewById(R.id.exercise_sum_list);
		lv.setAdapter(adapter);
		
		today = new ArrayList<ExerciseS>();
		ExerciseS in = new ExerciseS();
		in.kind = "운동 종류";
		in.date = "날짜";
		in.num = "횟수";
		in.time = "시간";
		today.add(in);
		resetAdapter();
	}
	public void GoChoose(){
		if(icango == 0){
			//List Setting
			today = new ArrayList<ExerciseS>();
			ExerciseS in = new ExerciseS();
			in.kind = "운동 종류";
			in.date = "날짜";
			in.num = "횟수";
			in.time = "시간";
			today.add(in);
			
			if(kindList.size() != 0){
				if(kindList.get(kindList.size()-1) != "모두 보기"){
					kindList.add("모두 보기");
				}
				Intent intent = new Intent(getBaseContext(),Exercise_Sum_Select.class);
				String[] list = new String[kindList.size()];
				int lc = 0;
				for(String i: kindList){
					list[lc] = new String();
					list[lc] = i;
					lc++;
				}
				intent.putExtra("list",list);
				startActivityForResult(intent,0);
			}
			else{
				Toast.makeText(getBaseContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(getBaseContext(), "로딩중입니다. 잠시후에 시도해주세요.", Toast.LENGTH_SHORT).show();
		}
	}
	public void onActivityResult(int requestCode,int resultCode, Intent Data){
		if(requestCode == 0){
			String kinds = kindList.get(resultCode);
			for(ExerciseS i : datas){
				if(i.kind.compareTo(kinds) == 0){
					today.add(i);
				}
				else if(kinds.compareTo("모두 보기") == 0){
					today.add(i);
				}
			}
		}
		resetAdapter();
	}
	public void Search(){
		icango = 1;
		HashMap<String,String>ab = new HashMap<String,String>();
		List<String> wantGap = new ArrayList<String>();
		ab.put("table","exercise");
		ab.put("field", "kind,num,time,date");
		String beforedate = String.valueOf(before_date.get(Calendar.YEAR))+"-"+String.valueOf(before_date.get(Calendar.MONTH))+"-"+String.valueOf(before_date.get(Calendar.DAY_OF_MONTH));
		String afterdate = String.valueOf(after_date.get(Calendar.YEAR))+"-"+String.valueOf(after_date.get(Calendar.MONTH))+"-"+String.valueOf(after_date.get(Calendar.DAY_OF_MONTH));
		tv.setText(beforedate +" ~ "+ afterdate);
		ab.put("condition", "member_uid="+member_uid+" && date >= \""+beforedate+"\" && date <= \"" + afterdate+"\"");
		wantGap.add("kind");
		wantGap.add("num");
		wantGap.add("time");
		wantGap.add("date");
		kindList = new ArrayList<String>();
		datas = new ArrayList<ExerciseS>();
		
		//List Setting
		today = new ArrayList<ExerciseS>();
		ExerciseS in = new ExerciseS();
		in.kind = "운동 종류";
		in.date = "날짜";
		in.num = "횟수";
		in.time = "시간";
		today.add(in);
		
		resetAdapter();
		AsyncUseJson a = new AsyncUseJson(Exercise_Sum.this,ReadData.Select,wantGap);
		a.execute(ab);
	}
	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		int check = 0;
		for(HashMap<String,String> i : HashList){
			ExerciseS addE = new ExerciseS();
			addE.date = i.get("date");
			addE.num = i.get("num");
			addE.time = i.get("time");
			addE.kind = i.get("kind");
			check = 0;
			for(String a : kindList){
				if(a.compareTo(addE.kind) == 0){
					check = 1;
					break;
				}
			}
			if(check == 0){
				kindList.add(addE.kind);
			}
			datas.add(addE);
		}
		icango = 0;
	}
	
	@Override
	public void NullData(){
		ExerciseS addE = new ExerciseS();
		addE.kind = "측정 데이터가 없습니다.";
		today.add(addE);
		icango = 0;
	}

	class ExerciseS{
		String date;
		String kind;
		String time;
		String num;
	}
	public void resetAdapter(){
		Exercise_Sum.this.runOnUiThread(new Runnable() {
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
		public ExerciseS getItem(int position) {
			return today.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.exercise_list_2, parent, false);
			
			TextView tv_kind = (TextView)convertView.findViewById(R.id.exercise_kind);
			TextView tv_num = (TextView)convertView.findViewById(R.id.exercise_num);
			TextView tv_time = (TextView)convertView.findViewById(R.id.exercise_time);
			TextView tv_date = (TextView)convertView.findViewById(R.id.exercise_date);
			
			tv_kind.setText(today.get(position).kind);
			tv_num.setText(today.get(position).num);
			tv_time.setText(today.get(position).time);
			tv_date.setText(today.get(position).date);
			
			return convertView;
		}
	}
}
