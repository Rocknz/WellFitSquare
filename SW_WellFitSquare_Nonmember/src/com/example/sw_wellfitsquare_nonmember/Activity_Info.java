package com.example.sw_wellfitsquare_nonmember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.common.AsyncUseJson_nonmember;
import com.example.common.ReadData;
import com.example.sw_nonmember.SW_Nonmember;

public class Activity_Info extends Activity implements ReadData{

	String name;
	String number;
	String locate;
	int uid;
	float ave;
	int count;
	RatingBar rat;
	
	List<HashMap<String, String>> temp_hash;
	
	    Spinner sp;
	    
	    ArrayList<String> tranner;
	    String tranner_uid;
	    
	    ArrayList<Member> MapList;
	    
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_info);
	    
	    Intent intent = getIntent();
	    temp_hash = new ArrayList<HashMap<String, String>>();
	    
	    name = intent.getStringExtra("name");
		number = intent.getStringExtra("number");
		locate = intent.getStringExtra("locate");
		uid = intent.getIntExtra("uid", -1);
		
		rat = (RatingBar)findViewById(R.id.rat);
	    
		TextView health_title = (TextView)findViewById(R.id.health_title);
	    TextView health_name = (TextView)findViewById(R.id.health_name);
	    TextView health_number = (TextView)findViewById(R.id.health_number);
	    TextView health_locate = (TextView)findViewById(R.id.health_locate);
	    
	    health_title.setText(name);
	    health_name.setText(health_name.getText() + name);
	    health_number.setText(health_number.getText() + number);
	    health_locate.setText(health_locate.getText() + locate);
	    
	    Button connect_chat = (Button)findViewById(R.id.connect_chat);
	    connect_chat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SW_Nonmember.class);
				intent.putExtra("trainer_uid", tranner_uid);
				intent.putExtra("fitness_uid",String.valueOf(uid));
				startActivity(intent);
			}
		});

	    HashMap<String, String> ab = new HashMap<String, String>();
		
		List<String> wantGap = new ArrayList<String>();
		ab.put("table", "trainer");
		ab.put("field", "name,uid");
		ab.put("condition", "f_uid="+uid);
		wantGap.add("name");
		wantGap.add("uid"); // 게시판 uid 리플 테이블에서 입력

		AsyncUseJson_nonmember a = new AsyncUseJson_nonmember(this, ReadData.Select, wantGap,0);
		a.execute(ab);		
	}
	
	class Member {
		int image;
		String name;
		Double let;
		Double len;
		
		String comment;
		int point;
		String u_uid;
		

		public Member(int _image, String _name, Double _let, Double _len) {
			image = _image;
			name = _name;
			let = _let;
			len = _len;
		}
		
		public Member(String _comment, int _point) {
			comment = _comment;
			point = _point;
		}
	}
	
	class MemberAdapter extends BaseAdapter{
		LayoutInflater mInflater;
		ArrayList<Member> arSrc;
		Context _context;
		
		public MemberAdapter(Context context, ArrayList<Member> personListItem)	{
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arSrc = personListItem;
			_context = context;
		}
		
		public int getCount() {
			return arSrc.size();
		}

		public Object getItem(int position) {
			return arSrc.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			int res = R.layout.item_activity_info;
			convertView = mInflater.inflate(res, parent,false);
			
			TextView comment = (TextView)convertView.findViewById(R.id.comment);
			RatingBar bar = (RatingBar)convertView.findViewById(R.id.bar);
			comment.setText(arSrc.get(position).comment);
			
			bar.setRating((float) arSrc.get(position).point);
			bar.setIsIndicator(true);
			
			return convertView;
		}
	}
	
	

	
	
	
	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		
	}

	@Override
	public void NullData() {
		
	}

	@Override
	public void setData(final List<HashMap<String, String>> hashList, int requestCode) {
		if(requestCode==0)
		{
		     tranner = new ArrayList<String>();
		    for(int i=0;i<hashList.size();i++)
		    {
		    	tranner.add(hashList.get(i).get("name"));
		    }
   
		    sp = (Spinner)findViewById(R.id.Spinner01);
		    ArrayAdapter<String> adapter = 
		            new ArrayAdapter<String> (this, 
		            		android.R.layout.simple_spinner_item,tranner);
		    
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		        sp.setAdapter(adapter);

		        sp.setOnItemSelectedListener(new OnItemSelectedListener() {

		            public void onItemSelected(AdapterView<?> arg0, View arg1,
		                    int arg2, long arg3) {
		                int item = sp.getSelectedItemPosition();
		                
		                tranner_uid = hashList.get(arg2).get("uid"); 
		            }
		            public void onNothingSelected(AdapterView<?> arg0) {
		            }
		        });
 
		        // u_uid 를 f_uid 로 포레인키 때문엔 u_uid 로 임시 이용
		        HashMap<String, String> ab = new HashMap<String, String>();
				
				List<String> wantGap = new ArrayList<String>();
				ab.put("table", "fitness_evaluation");
				ab.put("field", "u_contents, u_evaluation");  // f_uid 추가로 가져와야함
				ab.put("condition", "f_uid=\"" + uid + "\""); // 여기를 f_uid로 
				wantGap.add("u_contents"); 
				wantGap.add("u_evaluation"); 
				// f_uid 추가
				
				AsyncUseJson_nonmember a = new AsyncUseJson_nonmember(this, ReadData.Select, wantGap,2);
				a.execute(ab);
			    
		}else if(requestCode==2)
		{
			MapList = new ArrayList<Member>();
			for(int i=0;i<hashList.size();i++)  
			{
				MapList.add(new Member(hashList.get(i).get("u_contents"),Integer.parseInt(hashList.get(i).get("u_evaluation"))));
				count+=1;
				ave+=Integer.parseInt(hashList.get(i).get("u_evaluation"));
			}
			
			rat.setRating((float) ave/(float)count);
			rat.setIsIndicator(true);
			
			ListView list_preference = (ListView)findViewById(R.id.list_preference);
		    MemberAdapter myMapAdapter = new MemberAdapter(this, MapList);
		    list_preference.setAdapter(myMapAdapter);
		}
	}

	@Override
	public void NullData(int requestCode) {
	}

}
