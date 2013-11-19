package com.example.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.R;
import com.example.sw_wellfitsquare.AsyncUseJson;
import com.example.sw_wellfitsquare.ReadData;

public class DialogReplyUpdate extends Activity implements ReadData{
	
	Intent intent;
	String reply_uid;
	
	EditText edit_reply_update;
	int flag = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.dialog_reply_update);
	    
	    intent = getIntent();
	    reply_uid = intent.getStringExtra("reply_uid");
	    
	    edit_reply_update = (EditText)findViewById(R.id.edit_reply_update);
	    
	    Button reply_cancle = (Button)findViewById(R.id.reply_cancle);
	    Button reply_update = (Button)findViewById(R.id.reply_update);
	    
	    HashMap<String, String> ab = new HashMap<String, String>();
		
		List<String> wantGap = new ArrayList<String>();

		ab.put("table", "room_board_comments");
		ab.put("field", "contents");
		ab.put("condition", "uid="+"\""+reply_uid+"\"");
		wantGap.add("contents");


		AsyncUseJson a = new AsyncUseJson(this, ReadData.Select, wantGap);
		a.execute(ab);
	    
	    reply_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	    
	    reply_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<String,String> ab = new HashMap<String,String>();
				List<String> wantGap = new ArrayList<String>();
				ab.put("table","room_board_comments");
				ab.put("field","contents");
				ab.put("data", "\""+edit_reply_update.getText().toString()+"\"");
				ab.put("condition","uid=\""+reply_uid+"\"");
				ab.put("overlap","1");   // 중복체크는 1
				
				AsyncUseJson a = new AsyncUseJson(DialogReplyUpdate.this,ReadData.Update,wantGap);
				a.execute(ab);
				setResult(RESULT_OK, new Intent());
				finish();
			}
		});
	}

	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		if(flag == 0){
			edit_reply_update.setText(HashList.get(0).get("contents"));
			flag = 1;
		}
	}

	@Override
	public void NullData() {
		
	}
}
