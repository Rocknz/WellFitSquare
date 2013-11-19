package com.example.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.R;
import com.example.sw_wellfitsquare.AsyncUseJson;
import com.example.sw_wellfitsquare.ReadData;

public class ActivityReply extends Activity implements ReadData{
	List<HashMap<String, String>> Boardcontent_HashList;
	HashMap<String, String> main_has_login; // 멤버 uid 가져옴
	ArrayList<Member> arMemberList;
	ListView list_board_reply;
	
	EditText reply_edit;
	
	String m_info;
	String board_text_uid;
	int icango=0;
	int isDelete = 0;
	boolean flag = false;
	
	MemberAdapter myMemberAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_reply);

	    Intent intent = getIntent();
	    board_text_uid = intent.getStringExtra("board_text_uid");
	    m_info = intent.getStringExtra("m_info");

		arMemberList = new ArrayList<Member>();
	
		list_board_reply = (ListView) findViewById(R.id.reply_list);
		

		Boardcontent_HashList = new ArrayList<HashMap<String, String>>();
		reloadReply();
		
		reply_edit = (EditText)findViewById(R.id.reply_edit);
		Button reply_send = (Button)findViewById(R.id.reply_send);
		reply_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(reply_edit.getText().toString() != ""){
					if(icango == 0){
						HashMap<String,String> ab = new HashMap<String,String>();
						List<String> wantGap = new ArrayList<String>();
						ab.put("table","room_board_comments");
						ab.put("field","m_info,contents,c_info");
						ab.put("data", "\""+m_info+"\",\""+reply_edit.getText().toString()+"\",\""+board_text_uid + "\"");
						isDelete = 1;
						AsyncUseJson a = new AsyncUseJson(ActivityReply.this,ReadData.Insert,wantGap);
						icango = 1;
						a.execute(ab);
						reply_edit.setText("");
					}
				}
			}
		});
	}
	
	void getMember_uid() {
		if(icango == 0){
			flag = true;
	//		board_comments 테이블 의 c_info 는 board_text 테이블의 uid 가져옴
			HashMap<String, String> ab = new HashMap<String, String>();
				
			List<String> wantGap = new ArrayList<String>();
			ab.put("table", "member");
			ab.put("field", "name,uid");
			ab.put("condition", "uid>0");
			wantGap.add("name");
			wantGap.add("uid"); // 게시판 uid 리플 테이블에서 입력
	
			AsyncUseJson a = new AsyncUseJson(this, ReadData.Select, wantGap);
	
			icango = 1;
			a.execute(ab);	
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
			int res = R.layout.reply_list;
			convertView = mInflater.inflate(res, parent,false);
			
			Button reply_remove = (Button)convertView.findViewById(R.id.reply_remove);
			Button reply_update = (Button)convertView.findViewById(R.id.reply_update);
			
			TextView txt1 = (TextView)convertView.findViewById(R.id.reply_name);
			txt1.setText(arSrc.get(position).m_info);
			TextView txt2 = (TextView)convertView.findViewById(R.id.reply_content);
			txt2.setText(arSrc.get(position).contents);

			String t = arSrc.get(position).reply_uid;
			String t1 = m_info;
			
			if(arSrc.get(position).uid.equals(m_info))
			{
				reply_remove.setVisibility(View.VISIBLE);
				reply_update.setVisibility(View.VISIBLE);
			}
			
			reply_remove.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(icango == 0){
						HashMap<String, String> ab = new HashMap<String, String>();
						List<String> wantGap = new ArrayList<String>();
						ab.put("table", "room_board_comments");
						ab.put("condition", "uid="+arSrc.get(position).reply_uid); 
						AsyncUseJson a = new AsyncUseJson(ActivityReply.this, ReadData.Delete, wantGap);
						isDelete = 1;
						icango = 1;
						a.execute(ab);
					}
				}
			});
			
			reply_update.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), DialogReplyUpdate.class);
					intent.putExtra("reply_uid", arSrc.get(position).reply_uid);
					startActivityForResult(intent,1);
				}
			});
			
			
			return convertView;
		}
	}

		class Member {
			String contents;
			String m_info;
			String uid;
			String reply_uid;

			public Member(String _contents, String _m_info) {
				contents = _contents;
				m_info = _m_info;
			}
			
			public Member(int _image, String _contents, String _m_info) {
				contents = _contents;
				m_info = _m_info;
			}
			
			public Member(String _contents, String _m_info, String _reply_uid ) {
				contents = _contents;
				m_info = _m_info;
				reply_uid = _reply_uid;			
			}
			
			
			public Member(int _image, String _contents, String _m_info, String _uid, String _reply_uid) {
				contents = _contents;
				m_info = _m_info;
				uid = _uid;
				reply_uid = _reply_uid;				
			}
		}



		@Override
		public void setData(List<HashMap<String, String>> HashList) {
			icango = 0;
			if(isDelete == 1){
				reloadReply();
			}
			isDelete = 0;
			if (HashList.size() != 0) {
				if(flag == false)
				{
					Boardcontent_HashList = HashList;
					arMemberList = new ArrayList<Member>();
		
					for (int i =  Boardcontent_HashList.size()-1; i > -1; i--)
					{
						arMemberList.add(new Member(Boardcontent_HashList.get(i).get("contents"),Boardcontent_HashList.get(i).get("m_info"),Boardcontent_HashList.get(i).get("uid")));
					}
					getMember_uid();
				}else if(flag == true)
				{
					Boardcontent_HashList = HashList;
					ArrayList<Member> temp_MemberList = new ArrayList<Member>();
					
					for (int i = 0; i< arMemberList.size(); i++) {
						for(int j=0;j<Boardcontent_HashList.size();j++)
						{
							if(arMemberList.get(i).m_info.equals(Boardcontent_HashList.get(j).get("uid")))
							{
								temp_MemberList.add(new Member(R.drawable.ic_launcher, arMemberList.get(i).contents, Boardcontent_HashList.get(j).get("name"),arMemberList.get(i).m_info,arMemberList.get(i).reply_uid));
							}
						}
					}
					myMemberAdapter = new MemberAdapter(this,	temp_MemberList);
					list_board_reply.setAdapter(myMemberAdapter);
					
					flag = false;
					
//					myMemberAdapter.notifyDataSetChanged();
				}
			}
		}
		

		private void reloadReply() {
			
			try {
			    Thread.sleep(300);
			} catch (InterruptedException ignore) {}
			if(icango == 0){
				HashMap<String, String> ab = new HashMap<String, String>();
				List<String> wantGap = new ArrayList<String>();
				
				ab.put("table", "room_board_comments");
				ab.put("field", "contents,m_info,uid");
				ab.put("condition", "c_info=\"" + board_text_uid +"\"");
				wantGap.add("contents");
				wantGap.add("m_info");
				wantGap.add("uid");
				
				AsyncUseJson a = new AsyncUseJson(this, ReadData.Select, wantGap);
	
				icango = 1;
				a.execute(ab);
			}
		}
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			
			if(keyCode == KeyEvent.KEYCODE_BACK){
				setResult(RESULT_OK, new Intent());
				finish();
			}
			return super.onKeyDown(keyCode, event);
		}
		
		
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			
			switch (requestCode) {
			case 1:
				if(resultCode == RESULT_OK)
				{
					reloadReply();
				}
				break;
			}
		}
		@Override
		public void NullData() {
			icango = 0;
			if(isDelete == 1){
				isDelete = 0;
				reloadReply();
			}
		}
}
