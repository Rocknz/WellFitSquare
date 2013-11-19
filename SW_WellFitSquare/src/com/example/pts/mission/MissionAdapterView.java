package com.example.pts.mission;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.R;

@SuppressLint("ViewConstructor")
public class MissionAdapterView extends LinearLayout{
	String type;
	TextView name,set,text,tools,number,point;
	Context mContext;
	public MissionAdapterView(Context context , HashMap<String,String> input) {
		super(context);
		type = input.get("type");
		mContext = context;
		if(Integer.parseInt(type) == 0){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.missionview, this, true);	
			
			name = (TextView)findViewById(R.id.mission_name);
			name.setText(input.get("name")+"("+input.get("point")+"점)");
			number =(TextView)findViewById(R.id.mission_number);
			number.setText(input.get("number")+"회");
			set = (TextView)findViewById(R.id.mission_set);	
			set.setText(input.get("set")+"세트");	
			text = (TextView)findViewById(R.id.mission_text);
			text.setText(input.get("text"));		
			tools = (TextView)findViewById(R.id.mission_tools);
			tools.setText(input.get("tools"));
		}
		else{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.missionview2, this, true);	
			
			name = (TextView)findViewById(R.id.mission2_name);
			name.setText(input.get("name")+"("+input.get("point")+"점)");
			number =(TextView)findViewById(R.id.mission2_number);
			number.setText(input.get("number")+"회");
			set = (TextView)findViewById(R.id.mission2_set);	
			set.setText(input.get("set")+"세트");	
			
		}
	}
}
