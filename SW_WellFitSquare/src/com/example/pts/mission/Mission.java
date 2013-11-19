package com.example.pts.mission;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.R;
import com.example.sw_wellfitsquare.AsyncUseJson;
import com.example.sw_wellfitsquare.ReadData;

public class Mission extends Activity implements ReadData{
	ListView MissionList;
	HashMap<String,String> add;
	static MissionAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.mission);
	    MissionList = (ListView)findViewById(R.id.MissionList);
	    adapter = new MissionAdapter(this);
	    getData();
	    
	}
	
	@SuppressWarnings({ "unchecked" })
	public void getData(){
		Intent intent = this.getIntent();
		String uid = intent.getStringExtra("member_uid");
		Calendar now = Calendar.getInstance();
		String today;
		today = String.valueOf(now.get(Calendar.YEAR))+"-"+String.valueOf(now.get(Calendar.MONTH)+1)+"-"+String.valueOf(now.get(Calendar.DAY_OF_MONTH));
		HashMap<String,String>ab = new HashMap<String,String>();
		List<String> wantGap = new ArrayList<String>();
		ab.put("table","mission_comms");
		//ab.put("field", "exercise_kind");
		ab.put("field", "train_uid,member_uid,exercise_kind,exercise_tool,exercise_number,exercise_set,exercise_des,point,complete");
		//ab.put("condition", "member_uid=\""+uid+"\"");
		ab.put("condition", "member_uid=\""+uid+"\"&&date=\""+today+"\"");
		wantGap.add("train_uid");
		wantGap.add("member_uid");
		wantGap.add("exercise_kind");
		wantGap.add("exercise_tool");
		wantGap.add("exercise_number");
		wantGap.add("exercise_set");
		wantGap.add("exercise_des");
		wantGap.add("point");
		wantGap.add("complete");
		AsyncUseJson a = new AsyncUseJson(this,ReadData.Select,wantGap);
		a.execute(ab);
	}
	
	@Override
	public void setData(List<HashMap<String, String>> HashList){
		for(HashMap<String,String> i : HashList){
			add = new HashMap<String,String>();
			add.put("type", "1");
			add.put("name", i.get("exercise_kind"));
			add.put("set", i.get("exercise_set"));
			add.put("text", i.get("exercise_des"));
			add.put("tools", i.get("exercise_tool"));
			add.put("point", i.get("point"));
			add.put("number", i.get("exercise_number"));
			adapter.addItem(add);
		}
		MissionList.setAdapter(adapter);
		MissionList.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				HashMap<String,String> gets = adapter.getItem(arg2);
				int types = Integer.parseInt(gets.get("type"));
				types = 1 - types;
				gets.put("type", String.valueOf(types));
				adapter.Item.set(arg2, gets);
				adapter.notifyDataSetChanged();
			}
		});
		
	}

	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return this.getParent().onKeyDown(keyCode, event);
		}
		return false;
	}

	@Override
	public void NullData() {
		// TODO Auto-generated method stub
		
	}
}
