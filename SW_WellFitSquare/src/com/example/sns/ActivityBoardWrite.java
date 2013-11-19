package com.example.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.R;
import com.example.sw_wellfitsquare.AsyncUseJson;
import com.example.sw_wellfitsquare.ReadData;

public class ActivityBoardWrite extends Activity implements ReadData {

	EditText edit_board_write;
	
	Intent intent;
	String r_info;
	String m_info;
	String type;
	String board_text_uid;
	
	Button btn_board_write;
	Button btn_board_write_cancel;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_board_write);
	    btn_board_write = (Button)findViewById(R.id.btn_board_write);
	    edit_board_write = (EditText)findViewById(R.id.edit_board_write);
		btn_board_write_cancel = (Button)findViewById(R.id.btn_board_write_cancel);
		btn_board_write_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	    intent = getIntent();
	    r_info = intent.getStringExtra("r_info");
	    type = intent.getStringExtra("type");
	    board_text_uid = intent.getStringExtra("board_text_uid");
	    
	    m_info = intent.getStringExtra("m_info");
	    //m_info = "4";
	    
	    
	    
	    if(type.equals("insert"))
	    {
		    setTitle("글 쓰기");
	    	
	    }else if(type.equals("update"))
	    {
		    setTitle("글 수정");
	    	HashMap<String, String> ab = new HashMap<String, String>();
			
			List<String> wantGap = new ArrayList<String>();

			ab.put("table", "room_board_text");
			ab.put("field", "contents,r_info,m_info");
			ab.put("condition", "uid="+"\""+board_text_uid+"\"");
			wantGap.add("contents");
			wantGap.add("r_info");
			wantGap.add("m_info");

			AsyncUseJson a = new AsyncUseJson(this, ReadData.Select, wantGap);
			a.execute(ab);
			
			btn_board_write.setText("수  정");
	    }
		
	    
	    btn_board_write.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				String temp_write = edit_board_write.getEditableText().toString();
				 if(type.equals("insert"))
				    {
					 HashMap<String,String> ab = new HashMap<String,String>();
						List<String> wantGap = new ArrayList<String>();
						ab.put("table","room_board_text");
						ab.put("field","m_info,contents,r_info");
						ab.put("data", m_info+",\""+edit_board_write.getText().toString()+"\","+r_info);
						
//						ab.put("data", "\""+getintent_main_to_vitalinfo.getStringExtra("member_uid")+"\",\""+edit_weight.getText().toString()+"\",\""+edit_pressure.getText().toString()+"\",\""+temp_day+"\"");
						
						/*ab.put("field","trainer_uid,member_uid,date,start_date,end_date");
						ab.put("data", "1,1,\""+t_d+"\""+",\""+s_d+"\",\""+e_d+"\"");*/
						AsyncUseJson a = new AsyncUseJson(ActivityBoardWrite.this,ReadData.Insert,wantGap);
						a.execute(ab);

						setResult(RESULT_OK, new Intent());
						finish();
				    }else if(type.equals("update"))
				    {
				    	HashMap<String,String> ab = new HashMap<String,String>();
						List<String> wantGap = new ArrayList<String>();
						ab.put("table","room_board_text");
						//ab.put("field","member_uid="+"\""+getintent_main_to_vitalinfo.getStringExtra("member_uid") + "\",weight=\""+edit_weight.getText().toString() +"\",pressure="+"\""+ edit_pressure.getText().toString() +"\", date =\""+temp_day+"\"");
						//ab.put("data", "\""+getintent_main_to_vitalinfo.getStringExtra("member_uid")+"\",\""+edit_weight.getText().toString()+"\",\""+edit_pressure.getText().toString()+"\",\""+temp_day+"\"");
						ab.put("field","contents");
						ab.put("data", "\""+edit_board_write.getText().toString()+"\"");
						ab.put("condition","uid=\""+board_text_uid+"\"");
						ab.put("overlap","0");   // 중복체크는 1
						
						/*ab.put("field","trainer_uid,member_uid,date,start_date,end_date");
						ab.put("data", "1,1,\""+t_d+"\""+",\""+s_d+"\",\""+e_d+"\"");*/

						AsyncUseJson a = new AsyncUseJson(ActivityBoardWrite.this,ReadData.Update,wantGap);
						a.execute(ab);
						
						setResult(RESULT_OK, new Intent());
						finish();
				    }
			}
		});
	    
	    
	}

	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		if(HashList.size() != 0){
			edit_board_write.setText(HashList.get(0).get("contents"));
		}
	}

	@Override
	public void NullData() {
		// TODO Auto-generated method stub
		
	}
}
