package com.example.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.R;
import com.example.sw_wellfitsquare.AsyncUseJson_nonmember;
import com.example.sw_wellfitsquare.ReadData;

public class Evaluate extends Activity implements ReadData{
	Spinner point_spin;
	String fitness_uid = "1";
	String member_uid;
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.evaluate);
	    setTitle("피트니스 평가하기");
	    Intent intent = getIntent();
	    member_uid = intent.getStringExtra("member_uid");
	    point_spin = (Spinner)findViewById(R.id.evaluate_spin);
	    point_spin.setPrompt("점수");
	    ArrayAdapter<String> p = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
	    p.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    for(int i = 0;i<=5;i++){
	    	p.add(String.valueOf(i));
	    }
	    point_spin.setAdapter(p);
	    
	    Button cancel = (Button)findViewById(R.id.evaluate_cancel);
	    cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	    
	    Button submit = (Button)findViewById(R.id.evaluate_submit);
	    submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendEvaluate();
			}
		});
	    
	}
	public void sendEvaluate(){
		int point = point_spin.getSelectedItemPosition();
		EditText et = (EditText)findViewById(R.id.evaluate_comment);
		String text = et.getText().toString();
		HashMap<String,String> a = new HashMap<String,String>();
		List<String> Wantgap = new ArrayList<String>();
		a.put("table", "fitness_evaluation");
		a.put("field", "u_evaluation,u_contents,f_uid,u_uid");
		a.put("data", String.valueOf(point)+","+"\""+ text +"\""+","+fitness_uid+","+member_uid);
		a.put("condition","u_uid="+ member_uid +"&& f_uid=" + fitness_uid);
		a.put("overlap", "1");
		AsyncUseJson_nonmember ab = new AsyncUseJson_nonmember(this,ReadData.Update,Wantgap);
		ab.execute(a);
		Toast.makeText(getBaseContext(), "저장 완료", Toast.LENGTH_SHORT).show();
		finish();
	}
	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		
	}
	@Override
	public void NullData() {
		
	}
}
