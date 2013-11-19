package com.example.exercise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.R;

public class Exercise_Sum_Select extends Activity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setTitle("운동 선택");
		setContentView(R.layout.exercise_sum_select);
		Intent intent = getIntent();
		String[] list = intent.getStringArrayExtra("list");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
		ListView lv = (ListView)findViewById(R.id.exercise_sum_kind);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				setResult(arg2);
				finish();
			}
		});
		
	}

}
