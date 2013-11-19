package com.example.sns;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.R;
import com.example.sw_wellfitsquare.AsyncUseJson;
import com.example.sw_wellfitsquare.ReadData;

public class ActivityBoardContent extends Activity implements ReadData {
	List<HashMap<String, String>> Boardcontent_HashList;
	ArrayList<Member> arMemberList;
	ArrayList<Member> temp_MemberList;
	
	
	ListView list_board_content;
	
	int flag=0;
	String uids;
	String room_uid;
	String member_uid;

	HashMap<String, String> main_has_login; // 멤버 uid 가져옴

	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.activity_board_content);
	    
	    Intent intent = getIntent();
	    
	    sharedPref= getSharedPreferences("sns_room_uid",Activity.MODE_PRIVATE);//uid key 값, 어떤 방을 클릭했는지 알 수 있음
		editor = sharedPref.edit();
		
	    // board_comments 테이블 의 c_info 는 board_text 테이블의 uid 가져옴
	    // room_board_text 테이블 의 r_info , m_info 가져옴  나중에 글쓰기나 수정등을 위해 필요
	    room_uid = sharedPref.getString("room_uid", "-1");  // 게시판 방에서 클릭해서 들어올 때 받는 r_info 
	    member_uid = intent.getStringExtra("member_uid");
//	    member_uid = "1";
	    
		Boardcontent_HashList = new ArrayList<HashMap<String, String>>();

		readBoardData(); // 게시글 읽어옴 
		Button btn_board_room_choice = (Button)findViewById(R.id.btn_board_content_room_choice);
		btn_board_room_choice.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent;
				intent = new Intent(getBaseContext(), Sns_room.class);
				
			   	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			   	
			   	intent.putExtra("member_uid", member_uid);
			   	
			   	startActivityForResult(intent,1);
			}
		});
		Button btn_board_content_write = (Button)findViewById(R.id.btn_board_content_write);
		btn_board_content_write.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),ActivityBoardWrite.class);
				intent.putExtra("r_info", room_uid);
				intent.putExtra("m_info", member_uid);
				intent.putExtra("type", "insert");
				startActivityForResult(intent,1);
			}
		});
	}
	
	
	private void readBoardData() {
		try {
		    Thread.sleep(300);
		} catch (InterruptedException ignore) {}
		
		
		HashMap<String, String> ab = new HashMap<String, String>();
		
		List<String> wantGap = new ArrayList<String>();
		list_board_content = (ListView) findViewById(R.id.list_board_content);

		ab.put("table", "room_board_text");
		ab.put("field", "contents,m_info,uid");
		ab.put("condition", "r_info="+"\""+room_uid+"\"");
		wantGap.add("contents");
		wantGap.add("m_info");
		wantGap.add("uid"); // room_board_text 

		AsyncUseJson a = new AsyncUseJson(this, ReadData.Select, wantGap);
		a.execute(ab);
	}


	void getMember_uid() {
		flag = 1;
		// 막힌부분  쓴사람 아이디 알아내려면 m_info 를 이용해서 계쏙 AsyncUseJson 을 돌려주어야 하는데... 

		 list_board_content = (ListView) findViewById(R.id.list_board_content);
		 //		board_comments 테이블 의 c_info 는 board_text 테이블의 uid 가져옴
		HashMap<String, String> ab = new HashMap<String, String>();
			
		List<String> wantGap = new ArrayList<String>();
		ab.put("table", "member");
		ab.put("field", "name,uid");
		ab.put("condition", "uid>0");
		wantGap.add("name");
		wantGap.add("uid"); // 게시판 uid 리플 테이블에서 입력

		AsyncUseJson a = new AsyncUseJson(this, ReadData.Select, wantGap);
		a.execute(ab);
	}
	
	void getReply_count() {
		flag = 2;
		
		HashMap<String, String> ab = new HashMap<String, String>();
		
		List<String> wantGap = new ArrayList<String>();
		ab.put("table", "room_board_comments");
		ab.put("field", "c_info");
		ab.put("condition", "uid>0");
		wantGap.add("c_info"); // 게시판 uid 리플 테이블에서 입력

		AsyncUseJson a = new AsyncUseJson(this, ReadData.Select, wantGap);
		a.execute(ab);
		
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
			int res = R.layout.board_list;
			convertView = mInflater.inflate(res, parent,false);
			
			Button btn_reply = (Button)convertView.findViewById(R.id.btn_reply);
			Button btn_remove = (Button)convertView.findViewById(R.id.btn_remove);
			Button btn_update = (Button)convertView.findViewById(R.id.btn_update);
			ImageView imageView = (ImageView)convertView.findViewById(R.id.board_image);
			
			
			TextView txt1 = (TextView)convertView.findViewById(R.id.text1);
			txt1.setText(arSrc.get(position).name);
			
			TextView board_writer = (TextView)convertView.findViewById(R.id.board_writer);
			board_writer.setText(arSrc.get(position).board_writer);
			
			
			if(arSrc.get(position).m_info.equals(member_uid))
			{
				btn_remove.setVisibility(View.VISIBLE);
				btn_update.setVisibility(View.VISIBLE);
			}
			
			
			if(Integer.parseInt(arSrc.get(position).replyCount)!=0)
			{
				btn_reply.setText("댓글("+ arSrc.get(position).replyCount +")");
			}
			btn_reply.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), ActivityReply.class);
					intent.putExtra("board_text_uid", arSrc.get(position).uid);
					intent.putExtra("r_info", room_uid);
					intent.putExtra("m_info", member_uid);
					startActivityForResult(intent,1);
				}
			});
			
			
			btn_update.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), ActivityBoardWrite.class);
					intent.putExtra("board_text_uid", arSrc.get(position).uid);
					intent.putExtra("type", "update");
					startActivityForResult(intent,1);
				}
			});
			// Add by Rock
			btn_remove.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					flag = 3;
					uids = arSrc.get(position).uid;
					//댓글 선삭제!
					
					HashMap<String, String> ab = new HashMap<String, String>();
					List<String> wantGap = new ArrayList<String>();
					ab.put("table", "room_board_comments");
					ab.put("condition", "c_info="+ arSrc.get(position).uid); // 게시판 uid 리플 테이블에서 입력
					AsyncUseJson a = new AsyncUseJson(ActivityBoardContent.this, ReadData.Delete, wantGap);
					a.execute(ab);
				}
			});
			
			(new GetMemberImage(imageView, arSrc.get(position).m_info)).execute();
			
			return convertView;
		}
	}

    public class GetMemberImage extends AsyncTask<Void, Void, Boolean>{
    	public Bitmap bm;
    	ImageView memberImage;
    	String uid;
    	public GetMemberImage(ImageView imageView,String uid){
    		memberImage = imageView;
    		this.uid = uid;
    	}
        public int dpToPx(int dp) {
            DisplayMetrics displayMetrics = getBaseContext().getResources().getDisplayMetrics();
            int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
            return px;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                URL url = new URL("http://61.43.139.69/member_image/" + uid + ".jpg");
                bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return true;
            }catch (IOException ex){
                    return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            int x = dpToPx(70);
            if(result == true) memberImage.setImageBitmap(Bitmap.createScaledBitmap(bm, x, x, true));
            else {
            	bm = BitmapFactory.decodeResource(getResources(), R.drawable.rock2);
            	memberImage.setImageBitmap(Bitmap.createScaledBitmap(bm, x, x, true));
            }
        }
    }
	class Member{
		String name;
		int image;
		String board_writer;
		String uid;
		String replyCount;
		String m_info;
		public Member(int _image, String _name,String _board_writer)
		{
			name = _name;
			image = _image;
			board_writer = _board_writer;
		}
		
		
		public Member(int _image, String _name,String _board_writer, String _uid)
		{
			name = _name;
			image = _image;
			board_writer = _board_writer;
			uid = _uid;
		}
		
		public Member(int _image, String _name,String _board_writer, String _uid, String _replyCount, String _m_info)
		{
			name = _name;
			image = _image;
			board_writer = _board_writer;
			uid = _uid;
			replyCount = _replyCount;
			m_info = _m_info;
		}
	}



	@Override
	public void setData(List<HashMap<String, String>> HashList) {
		if (HashList.size() != 0) {
			if(flag == 0)
			{
				Boardcontent_HashList = HashList;
				arMemberList = new ArrayList<Member>();
	
				for (int i =  Boardcontent_HashList.size()-1; i > -1; i--) {
					arMemberList.add(new Member(R.drawable.ic_launcher,Boardcontent_HashList.get(i).get("contents"), Boardcontent_HashList.get(i).get("m_info"), Boardcontent_HashList.get(i).get("uid")));
				}
				
				getMember_uid();
			}else if(flag ==1)
			{
				Boardcontent_HashList = HashList;
				temp_MemberList = new ArrayList<Member>();
				
				for (int i = 0; i< arMemberList.size(); i++) {
					for(int j=0;j<Boardcontent_HashList.size();j++)
					{
						String t = arMemberList.get(i).board_writer;
						if(arMemberList.get(i).board_writer.equals(Boardcontent_HashList.get(j).get("uid")))
						{
							temp_MemberList.add(new Member(R.drawable.ic_launcher, arMemberList.get(i).name,Boardcontent_HashList.get(j).get("name"), arMemberList.get(i).uid));
						}
					}
				}

				getReply_count();
			}else if(flag ==2)
			{
				Boardcontent_HashList = HashList;
				ArrayList<Member> temp_ReplyCount = new ArrayList<Member>();
				
				for(int i=0;i< temp_MemberList.size();i++)
				{
					int temp_reply_count =0;
					
					for(int j=0;j<Boardcontent_HashList.size();j++)
					{
						if(temp_MemberList.get(i).uid.equals(Boardcontent_HashList.get(j).get("c_info")))
						{
							temp_reply_count+=1;
						}
						
					}
					temp_ReplyCount.add(new Member(R.drawable.ic_launcher, temp_MemberList.get(i).name, temp_MemberList.get(i).board_writer, temp_MemberList.get(i).uid,String.valueOf(temp_reply_count),arMemberList.get(i).board_writer));
				}
				MemberAdapter myMemberAdapter = new MemberAdapter(this,	temp_ReplyCount);
				list_board_content.setAdapter(myMemberAdapter);
				
				flag = 0;
			}
		} 
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if(resultCode == RESULT_OK)
			{
				sharedPref= getSharedPreferences("sns_room_uid",Activity.MODE_PRIVATE);
				editor = sharedPref.edit();
				room_uid = sharedPref.getString("room_uid", "-1");  
				readBoardData();
			}
			break;
		}
	}


	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return this.getParent().onKeyDown(keyCode, event);
		}
		return false;
	}
	public class delete_text extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			HashMap<String, String> ab = new HashMap<String, String>();
			List<String> wantGap = new ArrayList<String>();
			ab.put("table", "room_board_text");
			ab.put("condition", "uid="+uids); // 게시판 uid 리플 테이블에서 입력
			AsyncUseJson a = new AsyncUseJson(ActivityBoardContent.this, ReadData.Delete, wantGap);
			a.execute(ab);
			
			return null;
		}
		
	}
	@Override
	public void NullData() {
		if(flag == 3){
			flag = 4;
			delete_text at = new delete_text();
			at.execute();
		}
		else if(flag == 4){
			flag = 0;
			readBoardData();
		}
		else{
			ArrayList<Member> temp_ReplyCount = new ArrayList<Member>();
			MemberAdapter myMemberAdapter = new MemberAdapter(this,	temp_ReplyCount);
			list_board_content.setAdapter(myMemberAdapter);
		}
	}
}