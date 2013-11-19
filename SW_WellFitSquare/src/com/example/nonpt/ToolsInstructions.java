package com.example.nonpt;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.R;

public class ToolsInstructions extends Activity implements OnItemClickListener{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_instructions);
		
		//(제목) 헬스기구 이름, 운동 이름   - (내용) 멀티미디어 자료,  헬스기구 사용법, 운동 내용
		ArrayList<Member> arMemberList = new ArrayList<Member>();
	    

	    arMemberList.add(new Member(R.drawable.tool_shulug,"스미스 머신","슈러그","상체운동 기구","두손을 들었다놨다 들었다놨다 들었다 놨다해","http://www.youtube.com/watch?v=wEBsPQkZbAo&list=PL-Uib6-8Ps_2xfNP3AmnUTbyaijzSX4p8"));
	    
	    arMemberList.add(new Member(R.drawable.tool_law,"시티드 로우 머신","로우","상체운동 기구","양손으로 팔을 당겼다놨다 당겼다놨다 당겼다 놨다해","http://www.youtube.com/watch?v=9eUXe36D2_0&list=PL-Uib6-8Ps_2xfNP3AmnUTbyaijzSX4p8"));
	    
	    arMemberList.add(new Member(R.drawable.tool_dips,"딥스 머신","딥스","상체운동 기구","몸을 들었다놨다 들었다놨다 들었다 놨다해","http://www.youtube.com/watch?v=dKGt0DlM3Vk&list=PL-Uib6-8Ps_2xfNP3AmnUTbyaijzSX4p8"));
	    
		
	    MemberAdapter myMemberAdapter = new MemberAdapter(this, arMemberList);
	    ListView paperlistview = (ListView) findViewById(R.id.paperlistview);
	    paperlistview.setAdapter(myMemberAdapter);
	    
	    paperlistview.setOnItemClickListener(this);
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return this.getParent().onKeyDown(keyCode, event);
		}
		return false;
	}
	
	
	class Member{
		int image;
		String tools_name;
		String sports_name;
		String tools_way;
		String sports_content;
		String sports_data;

		public Member(int _image, String _name)
		{
			tools_name = _name;
			image = _image;
		}
		
		public Member(int _image, String tools_name, String sports_name, String tools_way, String sports_content, String sports_data)
		{
			this.image = _image;
			this.tools_name = tools_name;
			this.sports_name = sports_name;
			this.tools_way = tools_way;
			this.sports_content = sports_content;
			this.sports_data = sports_data;
		}
		
		
	}
	
	int flag;
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

		public View getView(int position, View convertView, ViewGroup parent) {

			int res = R.layout.tools_instructions_list;
			convertView = mInflater.inflate(res, parent,false);
			
			ImageView image = (ImageView)convertView.findViewById(R.id.image);
			image.setImageResource(arSrc.get(position).image);
			
			TextView tools_name = (TextView)convertView.findViewById(R.id.tools_name);
			tools_name.setText(arSrc.get(position).tools_name);
			
			TextView sports_name = (TextView)convertView.findViewById(R.id.sports_name);
			sports_name.setText(arSrc.get(position).sports_name);
			
			TextView tools_way = (TextView)convertView.findViewById(R.id.tools_way);
			tools_way.setText(arSrc.get(position).tools_way);
			
			TextView sports_content = (TextView)convertView.findViewById(R.id.sports_content);
			sports_content.setText(arSrc.get(position).sports_content);
			
			TextView sports_data = (TextView)convertView.findViewById(R.id.sports_data);
			sports_data.setText(arSrc.get(position).sports_data);
			
			final String uri = arSrc.get(position).sports_data;
			
			sports_data.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW,
					          Uri.parse(uri));
					 startActivity(intent);
				}
			});

			final LinearLayout linear_content = (LinearLayout)convertView.findViewById(R.id.linear_content); 
			
			image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(linear_content.getVisibility()==View.GONE)
					{
						flag=0;
					}else
					{
						flag=1;
					}
					
					if(flag==0)
					{
						linear_content.setVisibility(View.VISIBLE);
					}else
					{
						linear_content.setVisibility(View.GONE);
					}
				}
			});
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}
}
